package github.catchaos8.levelup.networking.packet;

import github.catchaos8.levelup.attributes.ModAttributes;
import github.catchaos8.levelup.config.LevelUPCommonConfig;
import github.catchaos8.levelup.lib.SetStats;
import github.catchaos8.levelup.networking.ModNetwork;
import github.catchaos8.levelup.stats.PlayerStatsProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static github.catchaos8.levelup.lib.SetStats.setAttributeStat;

public class IncreaseBaseStatC2SPacket {

    private final int type;
    private final float amount;


    public IncreaseBaseStatC2SPacket(int type, float amount) {
        this.type = Math.max(0, type);

        this.amount = Math.max(0, amount);
    }

    public IncreaseBaseStatC2SPacket(FriendlyByteBuf buf) {
        this.type = buf.readInt();

        this.amount = buf.readFloat();

    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(type);

        buf.writeFloat(amount);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            //This is the server side part I think
            ServerPlayer player = context.getSender();
            assert player != null;
            player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {

                //Check if freepoints is > 0 and if the stat u r upgrading is below stat cap
                if (stats.getInfo(0) >= amount && stats.getBaseStat(type) < LevelUPCommonConfig.STAT_CAP.get()) {
                    //Increase Stats
                    stats.addBaseStat(type, amount);

                    //Increase limited stats
                    Attribute attribute = switch (type) {
                        case 0 -> ModAttributes.CONSTITUTION.get();
                        case 1 -> ModAttributes.DEXTERITY.get();
                        case 2 -> ModAttributes.STRENGTH.get();
                        case 3 -> ModAttributes.VITALITY.get();
                        case 4 -> ModAttributes.ENDURANCE.get();
                        case 5 -> ModAttributes.WISDOM.get();
                        case 6 -> ModAttributes.INTELLIGENCE.get();
                        default -> null;
                    };

                    if (attribute != null && player.getAttributeValue(attribute) == stats.getLimitedStat(type)) {
                        stats.addLimitedStat(type, amount);
                    }
                    //Sub freepoints
                    stats.subInfo(0, amount);

                    SetStats.makeAttributeSingleMod(player, type);
                    setAttributeStat(stats.getBaseStat(type), type, player);

                }

                ModNetwork.sendToPlayer(new StatDataSyncS2CPacket(stats.getInfoArr(), stats.getStatsTypeArr()), player);
            });
        });

    }
}
