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

public record SpendPointsC2SPacket(int index, int amount)
        implements CustomPacketPayload {


    public static final CustomPacketPayload.Type<SpendPointsC2SPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(LevelUP.MOD_ID, "spend_free_points"));


    public static final StreamCodec<FriendlyByteBuf, SpendPointsC2SPacket> STREAM_CODEC =
            StreamCodec.of((buf, packet) -> {
                        buf.writeInt(packet.index);
                        buf.writeInt(packet.amount);
                    }, friendlyByteBuf ->
                    new SpendPointsC2SPacket(friendlyByteBuf.readInt(), friendlyByteBuf.readInt())
            );


    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SpendPointsC2SPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if(!(context.player() instanceof ServerPlayer player)) return;

            int[] baseStats = player.getData(ModAttachments.STATS).clone();
            int[] limitedStats = player.getData(ModAttachments.LIMITED_STATS).clone();
            ModAttachments.ProgressData progress = player.getData(ModAttachments.PROGRESS);
            double freePoints = progress.freePoints();

            int index = packet.index;
            int increase = packet.amount;

            double newFreePoints = freePoints;

            if(freePoints >= increase) {
                if(baseStats[index] == limitedStats[index]) {
                    limitedStats[index] += increase;
                }
                baseStats[index] += increase;

                newFreePoints = (double) Math.round(100 * (freePoints - increase)) /100;
            }


            player.setData(ModAttachments.PROGRESS,
                    new ModAttachments.ProgressData(progress.level(), progress.xp(), newFreePoints));
            player.setData(ModAttachments.STATS,
                    baseStats);
            player.setData(ModAttachments.LIMITED_STATS,
                    limitedStats);

            syncToPlayer(player);
        });
    }
}
