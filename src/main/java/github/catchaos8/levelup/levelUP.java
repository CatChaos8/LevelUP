package github.catchaos8.levelup;

import com.mojang.logging.LogUtils;
import github.catchaos8.levelup.networking.ModNetwork;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

@Mod(levelUP.MOD_ID)
public class levelUP {
    public static final String MOD_ID = "levelup";
    public static final Logger LOGGER = LogUtils.getLogger();



    private void commonSetup(final FMLCommonSetupEvent event) {
        ModNetwork.register();
    }

}

