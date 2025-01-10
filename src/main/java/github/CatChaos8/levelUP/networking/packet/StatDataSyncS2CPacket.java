package github.catchaos8.levelup.networking.packet;

import github.catchaos8.levelup.client.ClientStatData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class StatDataSyncS2CPacket {

    private int[] stat;
    private int[] limitedStat;

    public StatDataSyncS2CPacket(int[] stat, int[] limitedStat) {
        this.stat = stat;
        this.limitedStat = limitedStat;
    }

    public StatDataSyncS2CPacket(FriendlyByteBuf buf) {
        this.stat = buf.readVarIntArray();

        this.limitedStat = buf.readVarIntArray();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeVarIntArray(stat);

        buf.writeVarIntArray(limitedStat);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> ClientStatData.set(stat, limitedStat));
        return true;
    }
}
