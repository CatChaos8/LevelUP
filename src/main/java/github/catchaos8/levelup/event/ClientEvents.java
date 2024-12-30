package github.catchaos8.levelup.event;
import github.catchaos8.levelup.LevelUP;
import github.catchaos8.levelup.networking.ModNetwork;
import github.catchaos8.levelup.networking.packet.IncreaseConstitutionC2SPacket;
import github.catchaos8.levelup.utility.KeyBinding;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ClientEvents {

    @Mod.EventBusSubscriber(modid = LevelUP.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {

        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event){

            if(KeyBinding.OPEN_KEY.consumeClick()) {
                Minecraft.getInstance().player.sendSystemMessage(Component.literal("Pressed open GUI key"));
                //To send a packet to the serv:
                ModNetwork.sendToServer(new IncreaseConstitutionC2SPacket());
            }

            if(KeyBinding.UPGRADE_CON.consumeClick()) {
                ModNetwork.sendToServer(new IncreaseConstitutionC2SPacket());
            }
            if(KeyBinding.UPGRADE_DEX.consumeClick()) {
                Minecraft.getInstance().player.sendSystemMessage(Component.literal("Pressed Upgrade Dex key"));
            }
            if(KeyBinding.UPGRADE_END.consumeClick()) {
                Minecraft.getInstance().player.sendSystemMessage(Component.literal("Pressed Upgrade End key"));
            }
            if(KeyBinding.UPGRADE_STR.consumeClick()) {
                Minecraft.getInstance().player.sendSystemMessage(Component.literal("Pressed Upgrade Str key"));
            }
            if(KeyBinding.UPGRADE_INT.consumeClick()) {
                Minecraft.getInstance().player.sendSystemMessage(Component.literal("Pressed Upgrade Int key"));
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
            event.register(KeyBinding.UPGRADE_INT);
            event.register(KeyBinding.UPGRADE_STR);
            event.register(KeyBinding.UPGRADE_END);
        }
    }
}
