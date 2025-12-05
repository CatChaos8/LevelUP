package github.catchaos8.levelup.compat;

import net.minecraftforge.fml.ModList;

public class CompatManager {

    public static void init() {
        if (ModList.get().isLoaded("ars_nouveau")) {
            ArsCompat.init();
        }
        if(ModList.get().isLoaded("irons_spellbooks")) {
            IronsCompat.init();
        }
        if(ModList.get().isLoaded("curios_api")) {
            CuriosCompat.init();
        }
    }
}
