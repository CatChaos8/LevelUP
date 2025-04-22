package github.catchaos8.levelup.networking.packet;

import github.catchaos8.levelup.attributes.ModAttributes;
import github.catchaos8.levelup.lib.SetStats;
import github.catchaos8.levelup.networking.ModNetwork;
import github.catchaos8.levelup.stats.PlayerStatsProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SetLimitedStatC2SPacket {

    private final int type;
    private final float amount;


    public SetLimitedStatC2SPacket(int type, float amount) {
        this.type = Math.max(0, Math.min(12, type));

        this.amount = Math.max(0, amount);
    }

    public SetLimitedStatC2SPacket(FriendlyByteBuf buf) {
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
             //Check if freestats is > 0
            assert player != null;
            player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
                 //Increase Stats
                Attribute attribute = switch (type) {
                    case 0 -> ModAttributes.CONSTITUTION.get();
                    case 1 -> ModAttributes.DEXTERITY.get();
                    case 2 -> ModAttributes.STRENGTH.get();
                    case 3 -> ModAttributes.VITALITY.get();
                    case 4 -> ModAttributes.ENDURANCE.get();
                    default -> null;
                };

                if (attribute != null && amount <= player.getAttributeValue(attribute)) {
                    stats.setLimitedStat(type, amount);
                }
                SetStats.makeAttributeSingleMod(player, type);
                ModNetwork.sendToPlayer(new StatDataSyncS2CPacket(stats.getInfoArr(), stats.getStatsTypeArr()), player);
            });
        });

    }
}
