package github.catchaos8.levelup.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class LevelUPClientConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> DISPLAY_DISABLED_STAT_INCREASE;


    static {
        BUILDER.push("Client Configs for LevelUP");

        //Config

        DISPLAY_DISABLED_STAT_INCREASE = BUILDER.comment("Show disabled stat increases in red")
                .define("Disabled Red Stat Increases", false);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

}
