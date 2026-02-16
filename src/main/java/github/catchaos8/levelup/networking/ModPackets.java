package github.catchaos8.levelup.networking;

import github.catchaos8.levelup.LevelUP;
import github.catchaos8.levelup.networking.packets.RequestSyncC2SPacket;
import github.catchaos8.levelup.networking.packets.SetLimitedStatsC2SPacket;
import github.catchaos8.levelup.networking.packets.SpendPointsC2SPacket;
import github.catchaos8.levelup.networking.packets.SyncS2CPacket;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class ModPackets {
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(LevelUP.MOD_ID).versioned("1.0.0");

        registrar.playToClient(
                SyncS2CPacket.TYPE,
                SyncS2CPacket.STREAM_CODEC,
                SyncS2CPacket::handle
        );

        registrar.playToServer(
                SpendPointsC2SPacket.TYPE,
                SpendPointsC2SPacket.STREAM_CODEC,
                SpendPointsC2SPacket::handle
        );


        registrar.playToServer(
                SetLimitedStatsC2SPacket.TYPE,
                SetLimitedStatsC2SPacket.STREAM_CODEC,
                SetLimitedStatsC2SPacket::handle
        );


        registrar.playToServer(
                RequestSyncC2SPacket.TYPE,
                RequestSyncC2SPacket.STREAM_CODEC,
                RequestSyncC2SPacket::handle
        );
    }
}
