package github.fixinggithub.levelup.networking.packet;

import github.fixinggithub.levelup.client.ClientStatData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class StatDataSyncS2CPacket {

    private int[] stat;

    public StatDataSyncS2CPacket(int[] stat) {
        this.stat = stat;
    }

    public StatDataSyncS2CPacket(FriendlyByteBuf buf) {
        this.stat = buf.readVarIntArray();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeVarIntArray(stat);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> ClientStatData.set(stat));
        return true;
    }
}
