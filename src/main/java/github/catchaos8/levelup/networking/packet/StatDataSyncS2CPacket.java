package github.catchaos8.levelup.networking.packet;

import github.catchaos8.levelup.client.ClientStatData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class StatDataSyncS2CPacket {

    private float[] stat;

    public StatDataSyncS2CPacket(float[] stat) {
        this.stat = stat;
    }

    public StatDataSyncS2CPacket(FriendlyByteBuf buf) {
        int length = buf.readVarInt(); // First read the length
        stat = new float[length];
        for (int i = 0; i < length; i++) {
            stat[i] = buf.readFloat(); // Read each float individually cause u cant do arrays :(
        }
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeVarInt(stat.length); // Write the length first
        for (float f : stat) {
            buf.writeFloat(f); // Write each float in the array :(
        }
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> ClientStatData.set(stat));
        return true;
    }
}
