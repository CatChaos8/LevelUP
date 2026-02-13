package github.catchaos8.levelup.event;

import github.catchaos8.levelup.LevelUP;
import github.catchaos8.levelup.client.screen.ClientHooks;
import github.catchaos8.levelup.networking.ModNetwork;
import github.catchaos8.levelup.networking.packet.IncreaseBaseStatC2SPacket;
import github.catchaos8.levelup.utility.KeyBinding;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.client.event.InputEvent;
import net.neoforged.client.event.RegisterKeyMappingsEvent;
import net.neoforged.eventbus.api.SubscribeEvent;
import net.neoforged.fml.DistExecutor;
import net.neoforged.fml.common.Mod;

public class ClientEvents {

    @Mod.EventBusSubscriber(modid = LevelUP.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {

        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event){

            if(KeyBinding.OPEN_KEY.consumeClick()) {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ClientHooks::openLevelUPGui);
            }

            if(KeyBinding.UPGRADE_CON.consumeClick()) {
                ModNetwork.sendToServer(new IncreaseBaseStatC2SPacket(0, 1));
            }
            if(KeyBinding.UPGRADE_DEX.consumeClick()) {
                ModNetwork.sendToServer(new IncreaseBaseStatC2SPacket(1, 1));
            }
            if(KeyBinding.UPGRADE_STR.consumeClick()) {
                ModNetwork.sendToServer(new IncreaseBaseStatC2SPacket(2, 1));
            }
            if(KeyBinding.UPGRADE_VIT.consumeClick()) {
                ModNetwork.sendToServer(new IncreaseBaseStatC2SPacket(3, 1));
            }
            if(KeyBinding.UPGRADE_END.consumeClick()) {
                ModNetwork.sendToServer(new IncreaseBaseStatC2SPacket(4, 1));
            }

        }

    }

    @Mod.EventBusSubscriber(modid = LevelUP.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {

        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(KeyBinding.OPEN_KEY);

            event.register(KeyBinding.UPGRADE_CON);
            event.register(KeyBinding.UPGRADE_DEX);
            event.register(KeyBinding.UPGRADE_VIT);
            event.register(KeyBinding.UPGRADE_STR);
            event.register(KeyBinding.UPGRADE_END);
        }
    }
}
