package github.catchaos8.levelup.networking.packets;

import github.catchaos8.levelup.LevelUP;
import github.catchaos8.levelup.registries.ModAttachments;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static github.catchaos8.levelup.networking.PacketFunctions.syncToPlayer;

public record SetLimitedStatsC2SPacket(int[] stats)
implements CustomPacketPayload {


    public static final CustomPacketPayload.Type<SetLimitedStatsC2SPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(LevelUP.MOD_ID, "set_limited_stat"));


    public static final StreamCodec<FriendlyByteBuf, SetLimitedStatsC2SPacket> STREAM_CODEC =
            StreamCodec.of((buf, packet) ->
                    buf.writeVarIntArray(packet.stats), friendlyByteBuf ->
                    new SetLimitedStatsC2SPacket(friendlyByteBuf.readVarIntArray())
            );


    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SetLimitedStatsC2SPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if(!(context.player() instanceof ServerPlayer player)) return;

            int[] baseStats = player.getData(ModAttachments.STATS);
            int[] limitedStats = player.getData(ModAttachments.LIMITED_STATS).clone();

            for (int i = 0; i < packet.stats.length; i++) {
                limitedStats[i] = Math.max(Math.min(packet.stats[i], baseStats[i]), 0);
            }

            player.setData(ModAttachments.LIMITED_STATS, limitedStats);
            syncToPlayer(player);
        });
    }
}
