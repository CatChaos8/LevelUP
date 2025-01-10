package github.catchaos8.levelup.networking.packet;

import github.catchaos8.levelup.client.ClientLimitedStatData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class LimitedStatDataSyncS2CPacket {

    private int limitedStatType;

    public LimitedStatDataSyncS2CPacket(int limitedStatType, int amount) {
        this.limitedStatType = limitedStatType;
    }

    public LimitedStatDataSyncS2CPacket(FriendlyByteBuf buf) {
        this.limitedStatType = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(limitedStatType);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> ClientLimitedStatData.set(limitedStatType));
        return true;
    }
}
