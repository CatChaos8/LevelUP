package github.catchaos8.levelup.networking.packets;

import github.catchaos8.levelup.LevelUP;
import github.catchaos8.levelup.client.ClientData;
import github.catchaos8.levelup.client.StatsScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SyncS2CPacket (
    int level,
    double xp,
    double freepoints,
    int[] base,
    int[] limited,
    String username
) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SyncS2CPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(LevelUP.MOD_ID, "stats_data"));

    public static final StreamCodec<FriendlyByteBuf, SyncS2CPacket> STREAM_CODEC =
            StreamCodec.of(SyncS2CPacket::encode, SyncS2CPacket::decode);

    private static void encode(FriendlyByteBuf buf, SyncS2CPacket packet) {
        buf.writeInt(packet.level());
        buf.writeDouble(packet.xp());
        buf.writeDouble(packet.freepoints);
        buf.writeVarInt(packet.base.length);
        for (int stat : packet.base) buf.writeInt(stat);
        buf.writeVarInt(packet.limited.length);
        for (int stat : packet.limited) buf.writeInt(stat);
        buf.writeUtf(packet.username);
    }

    private static SyncS2CPacket decode(FriendlyByteBuf buf) {
        int level = buf.readInt();
        double xp = buf.readDouble();
        double freePoints = buf.readDouble();
        int statsLen = buf.readVarInt();
        int[] stats = new int[statsLen];
        for (int i = 0; i < statsLen; i++) stats[i] = buf.readInt();
        int limitedLen = buf.readVarInt();
        int[] limitedStats = new int[limitedLen];
        for (int i = 0; i < limitedLen; i++) limitedStats[i] = buf.readInt();
        String username = buf.readUtf();
        return new SyncS2CPacket(level, xp, freePoints, stats, limitedStats, username);
    }


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SyncS2CPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            ClientData.setBase(packet.base);
            ClientData.setLimited(packet.limited);
            ClientData.setFreepoints(packet.freepoints);
            ClientData.setLevel(packet.level);
            ClientData.setXp(packet.xp);
            ClientData.setUsername(packet.username);

            Minecraft mc = Minecraft.getInstance();
            if (mc.screen instanceof StatsScreen screen) {
                screen.updateData();
            }
        });
    }
}


