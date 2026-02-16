package github.catchaos8.levelup.networking;

import github.catchaos8.levelup.LevelUP;
import github.catchaos8.levelup.networking.packets.SyncS2CPacket;
import github.catchaos8.levelup.registries.ModAttachments;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

public class PacketFunctions {

    public static void syncToPlayer(ServerPlayer player) {
        ModAttachments.ProgressData progress = player.getData(ModAttachments.PROGRESS);
        int[] stats = player.getData(ModAttachments.STATS);
        int[] limitedStats = player.getData(ModAttachments.LIMITED_STATS);

        PacketDistributor.sendToPlayer(player, new SyncS2CPacket(progress.level(),
                progress.xp(),
                progress.freePoints(),
                stats,
                limitedStats,
                player.getName().getString()));

    }
}
