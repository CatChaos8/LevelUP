package github.catchaos8.levelup.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class LevelUPCommonConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> FREEPOINTS_PER_LEVEL;

    public static final ForgeConfigSpec.ConfigValue<Integer> LEVEL_CAP;

    static {
        BUILDER.push("Client Configs for LevelUP");

        //Config
        FREEPOINTS_PER_LEVEL = BUILDER.comment("How many stat points per level!")
                        .define("Points Per Level", 3);

        LEVEL_CAP = BUILDER.comment("Max Level a player can reach without commands")
                .define("Max Level", 999999);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

}
