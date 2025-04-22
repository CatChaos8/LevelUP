package github.catchaos8.levelup.networking.packet;

import github.catchaos8.levelup.stats.PlayerStatsProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static github.catchaos8.levelup.lib.SetStats.increaseLevel;

public class ChangeInfoC2SPacket {

    private final int type;
    private final float amount;


    public ChangeInfoC2SPacket(int type, float amount) {
        this.type = Math.max(0, type);

        this.amount = Math.max(0, amount);
    }

    public ChangeInfoC2SPacket(FriendlyByteBuf buf) {
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
                if (type == 0) { //Freepoints
                    stats.addInfo(0, amount);

                } else { //XP and Level
                    stats.addInfo(type, amount);
                    //see if player can levelup
                    increaseLevel(player);
                }
            });
        });

    }
}
