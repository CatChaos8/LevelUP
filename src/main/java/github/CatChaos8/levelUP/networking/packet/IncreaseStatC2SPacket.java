package github.catchaos8.levelup.networking.packet;

import github.catchaos8.levelup.LevelUP;
import github.catchaos8.levelup.config.LevelUPCommonConfig;
import github.catchaos8.levelup.networking.ModNetwork;
import github.catchaos8.levelup.stats.PlayerStatsProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class IncreaseStatC2SPacket {

    private int type;
    private int amount;


    public IncreaseStatC2SPacket(int type, int amount) {
        this.type = Math.max(0, Math.min(7, type));

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

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {


            //This is the server side part I think
            ServerPlayer player = context.getSender();

            if(type <= 4) {
                //Check if freestats is > 0
                player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {

                    if (stats.getStat(5) > 0) {
                        //Increase Stats

                        stats.addStat(type, amount);

                        stats.subStat(5, 1);
                        //Sync
                        ModNetwork.sendToPlayer(new StatDataSyncS2CPacket(stats.getStatArr()), player);

                        //Set Modifier to attributes based on stats

                    } else {
                        //say not enough points

                        //Sync
                        ModNetwork.sendToPlayer(new StatDataSyncS2CPacket(stats.getStatArr()), player);
                    }
                });
            } else {
                //Get the equation from the config here

                player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {

                    if (type == 6) {
                        stats.addStat(type, amount);

                        int level = stats.getStat(7);
                        int xp = stats.getStat(6);

                        int xpNeeded = (int) (0.2*(level*level) + 0.25*level + 10);
                        int freepointsGiven = LevelUPCommonConfig.FREEPOINTS_PER_LEVEL.get();

                        int maxLevel = LevelUPCommonConfig.LEVEL_CAP.get();

                        if (xp >= xpNeeded && level < maxLevel) {
                            //Level
                            stats.addStat(7, 1);
                            player.sendSystemMessage(Component.literal("" + xpNeeded));

                            //XP
                            stats.subStat(6, xpNeeded);
                            //FreePoints
                            stats.addStat(5, freepointsGiven);

                            player.sendSystemMessage(Component.literal("LevelUP! You are now level " + stats.getStat(7) + "!"));

                        }

                        //Sync
                        ModNetwork.sendToPlayer(new StatDataSyncS2CPacket(stats.getStatArr()), player);
                    } else {
                        stats.addStat(type, amount);

                        //Sync
                        ModNetwork.sendToPlayer(new StatDataSyncS2CPacket(stats.getStatArr()), player);
                    }
                });

            }

        });
        return true;
    }
}
