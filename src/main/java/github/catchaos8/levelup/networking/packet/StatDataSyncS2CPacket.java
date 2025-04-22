package github.catchaos8.levelup.networking.packet;

import github.catchaos8.levelup.client.ClientStatData;
import github.catchaos8.levelup.lib.StatType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class StatDataSyncS2CPacket {

    private float[] info;
    private StatType[] statTypes;

    public StatDataSyncS2CPacket(float[] info, StatType[] statTypes) {
        this.info = info;
        this.statTypes = statTypes;
    }

    public StatDataSyncS2CPacket(FriendlyByteBuf buf) {
        // Read info array
        int infoLength = buf.readVarInt();
        this.info = new float[infoLength];
        for (int i = 0; i < infoLength; i++) {
            info[i] = buf.readFloat();
        }

        // Read statTypes array
        int statCount = buf.readVarInt();
        this.statTypes = new StatType[statCount];
        for (int i = 0; i < statCount; i++) {
            float base = buf.readFloat();
            float limited = buf.readFloat();
            String name = buf.readUtf();
            statTypes[i] = new StatType(base, limited, name);
        }
    }

    public void toBytes(FriendlyByteBuf buf) {
        // Write info array
        buf.writeVarInt(info.length);
        for (float f : info) {
            buf.writeFloat(f);
        }

        // Write statTypes array
        buf.writeVarInt(statTypes.length);
        for (StatType stat : statTypes) {
            buf.writeFloat(stat.getBase());
            buf.writeFloat(stat.getLimited());
            buf.writeUtf(stat.getName());
        }
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientStatData.setPlayerInfo(info);        // Set the info array
            ClientStatData.setStatTypes(statTypes);        // Set the statTypes array
        });
        context.setPacketHandled(true);
    }
}
