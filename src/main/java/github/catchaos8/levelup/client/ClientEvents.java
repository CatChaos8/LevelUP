package github.catchaos8.levelup.client;


import github.catchaos8.levelup.LevelUP;
import github.catchaos8.levelup.networking.packets.RequestSyncC2SPacket;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = LevelUP.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(ModKeybindings.OPEN_STATS_SCREEN);
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen != null) return;

        if (ModKeybindings.OPEN_STATS_SCREEN.consumeClick()) {
            // Open screen first, then request data from server
            mc.setScreen(new StatsScreen());
            PacketDistributor.sendToServer(new RequestSyncC2SPacket());
        }
    }
}
