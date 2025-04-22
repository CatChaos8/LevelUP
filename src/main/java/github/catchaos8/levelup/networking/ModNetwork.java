package github.catchaos8.levelup.networking;

import github.catchaos8.levelup.LevelUP;
import github.catchaos8.levelup.networking.packet.ChangeInfoC2SPacket;
import github.catchaos8.levelup.networking.packet.IncreaseBaseStatC2SPacket;
import github.catchaos8.levelup.networking.packet.SetLimitedStatC2SPacket;
import github.catchaos8.levelup.networking.packet.StatDataSyncS2CPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetwork {
    private static SimpleChannel INSTANCE;

    //Makes it so that there r no duplicate ids
    private static int packetID = 0;
    private static int id() {
        return packetID++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(LevelUP.MOD_ID, "messages"))
                .networkProtocolVersion(() ->"1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(IncreaseBaseStatC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(IncreaseBaseStatC2SPacket::new)
                .encoder(IncreaseBaseStatC2SPacket::toBytes)
                .consumerMainThread(IncreaseBaseStatC2SPacket::handle)
                .add();

        net.messageBuilder(SetLimitedStatC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(SetLimitedStatC2SPacket::new)
                .encoder(SetLimitedStatC2SPacket::toBytes)
                .consumerMainThread(SetLimitedStatC2SPacket::handle)
                .add();

        net.messageBuilder(ChangeInfoC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ChangeInfoC2SPacket::new)
                .encoder(ChangeInfoC2SPacket::toBytes)
                .consumerMainThread(ChangeInfoC2SPacket::handle)
                .add();

        net.messageBuilder(StatDataSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(StatDataSyncS2CPacket::new)
                .encoder(StatDataSyncS2CPacket::toBytes)
                .consumerMainThread(StatDataSyncS2CPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
