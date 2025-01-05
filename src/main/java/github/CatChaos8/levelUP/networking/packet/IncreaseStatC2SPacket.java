package github.catchaos8.levelup.networking.packet;

import github.catchaos8.levelup.networking.ModNetwork;
import github.catchaos8.levelup.stats.PlayerStatsProvider;
import net.minecraft.network.FriendlyByteBuf;
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

            //Check if freestats is > 0
            if(hasEnoughPoints(player)) {
                //Increase Stats
                player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {

                    stats.addStat(type, amount);

                    //Sync
                    ModNetwork.sendToPlayer(new StatDataSyncS2CPacket(stats.getStatArr()), player);
                });
                //Set Modifier to attributes based on stats

            } else {
                //say not enough points



                //Sync
                player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
                    ModNetwork.sendToPlayer(new StatDataSyncS2CPacket(stats.getStatArr()), player);
                });
            }

        });
        return true;
    }

    private boolean hasEnoughPoints(ServerPlayer player) {
        return true;
    }

}
