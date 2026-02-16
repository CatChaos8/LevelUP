package github.catchaos8.levelup.networking.packets;

import github.catchaos8.levelup.LevelUP;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static github.catchaos8.levelup.networking.PacketFunctions.syncToPlayer;

public record RequestSyncC2SPacket() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<RequestSyncC2SPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(LevelUP.MOD_ID, "request_stats"));

    public static final StreamCodec<FriendlyByteBuf, RequestSyncC2SPacket> STREAM_CODEC =
            StreamCodec.of((buf, packet) -> {}, buf -> new RequestSyncC2SPacket());

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    // Called on server side — player is asking for their data
    public static void handle(RequestSyncC2SPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!(context.player() instanceof ServerPlayer player)) return;
            syncToPlayer(player);
        });
    }
}
