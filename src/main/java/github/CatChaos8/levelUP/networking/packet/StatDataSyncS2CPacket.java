package github.catchaos8.levelup.networking.packet;

import github.catchaos8.levelup.client.ClientStatData;
import github.catchaos8.levelup.stats.PlayerStatsProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
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
