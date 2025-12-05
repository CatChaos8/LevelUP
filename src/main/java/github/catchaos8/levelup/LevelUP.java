package github.catchaos8.levelup;

import com.mojang.logging.LogUtils;
import github.catchaos8.levelup.attributes.ModAttributes;
import github.catchaos8.levelup.compat.CompatManager;
import github.catchaos8.levelup.config.LevelUPClientConfig;
import github.catchaos8.levelup.config.LevelUPCommonConfig;
import github.catchaos8.levelup.enchants.ModEnchants;
import github.catchaos8.levelup.networking.ModNetwork;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(LevelUP.MOD_ID)
public class LevelUP {
    public static final String MOD_ID = "levelup";
    public static final Logger LOGGER = LogUtils.getLogger();


    public LevelUP() {

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, LevelUPClientConfig.SPEC, "levelup-client.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, LevelUPCommonConfig.SPEC, "levelup-common.toml");

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);

        ModAttributes.register(modEventBus);

        ModEnchants.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);

    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        ModNetwork.register();

        event.enqueueWork(CompatManager::init);
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLCommonSetupEvent event) {

        }
    }
}

