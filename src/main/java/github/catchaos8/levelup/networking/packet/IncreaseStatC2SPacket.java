package github.catchaos8.levelup.networking.packet;

import github.catchaos8.levelup.attributes.ModAttributes;
import github.catchaos8.levelup.config.LevelUPCommonConfig;
import github.catchaos8.levelup.lib.DisplayLevelScoreboard;
import github.catchaos8.levelup.networking.ModNetwork;
import github.catchaos8.levelup.stats.PlayerStatsProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

import static github.catchaos8.levelup.lib.SetStats.*;

public class IncreaseStatC2SPacket {

    private final int type;
    private final int amount;


    public IncreaseStatC2SPacket(int type, int amount) {
        this.type = Math.max(0, Math.min(12, type));

        this.amount = Math.max(0, amount);
    }

    public IncreaseStatC2SPacket(FriendlyByteBuf buf) {
        this.type = buf.readInt();

        this.amount = buf.readInt();

    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(type);

        buf.writeInt(amount);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {


            //This is the server side part I think
            ServerPlayer player = context.getSender();

            if(type <= 4) {
                //Check if freestats is > 0
                assert player != null;
                player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {

                    if (stats.getStat(5) >= amount && stats.getStat(type) < LevelUPCommonConfig.STAT_CAP.get()) {
                        //Increase Stats
                        stats.addStat(type, amount);

                        //Increase limited stats
                        if(type == 0) {
                            if(player.getAttributeValue(ModAttributes.CONSTITUTION.get()) == stats.getStat(type + 8)) {
                                stats.addStat(type + 8, amount);
                            }
                        } else if(type == 1) {
                            if(player.getAttributeValue(ModAttributes.DEXTERITY.get()) == stats.getStat(type+8)) {
                                stats.addStat(type+8, amount);
                            }
                        }else if(type == 2) {
                            if(player.getAttributeValue(ModAttributes.STRENGTH.get()) == stats.getStat(type+8)) {
                                stats.addStat(type+8, amount);
                            }
                        }else if(type == 3) {
                            if(player.getAttributeValue(ModAttributes.VITALITY.get()) == stats.getStat(type+8)) {
                                stats.addStat(type+8, amount);
                            }
                        }else if(type == 4) {
                            if(player.getAttributeValue(ModAttributes.ENDURANCE.get()) == stats.getStat(type+8)) {
                                stats.addStat(type+8, amount);
                            }
                        }

                        //Sub freepoints
                        stats.subStat(5, amount);

                        makeAttributeMods(player, type);
                        setAttributeStat(stats.getStat(type), type, player);

                    }


                });
            } else if (type <= 7) {


                assert player != null;
                player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
                    if (type == 6) {
                        stats.addStat(type, amount);

                        //see if player can levelup
                        increaseLevel(player);
                    } else {
                        stats.addStat(type, amount);
                    }
                });

            } else { //Limited Stats
                assert player != null;
                player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
                    stats.setStat(type, amount);
                    makeAttributeMods(player, type-8);
                });
            }

            player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
                //Sync
                ModNetwork.sendToPlayer(new StatDataSyncS2CPacket(stats.getStatArr()), player);
                //Update Level
                int level = stats.getStat(7);
                DisplayLevelScoreboard.updateLevel(player, level);
            });
        });

    }
}
