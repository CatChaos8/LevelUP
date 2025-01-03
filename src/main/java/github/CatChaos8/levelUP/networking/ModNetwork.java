package github.catchaos8.levelup.networking;

import github.catchaos8.levelup.LevelUP;
import github.catchaos8.levelup.networking.packet.IncreaseConstitutionC2SPacket;
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

        net.messageBuilder(IncreaseConstitutionC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(IncreaseConstitutionC2SPacket::new)
                .encoder(IncreaseConstitutionC2SPacket::toBytes)
                .consumerMainThread(IncreaseConstitutionC2SPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
