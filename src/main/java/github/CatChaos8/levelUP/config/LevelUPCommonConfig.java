package github.catchaos8.levelup.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class LevelUPCommonConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> FREEPOINTS_PER_LEVEL;

    public static final ForgeConfigSpec.ConfigValue<Float> VITALITY_HP_REGEN;

    public static final ForgeConfigSpec.ConfigValue<Float> CONSTITUTION_FALL_DAMAGE_REDUCTION;

    public static final ForgeConfigSpec.ConfigValue<Float> STRENGTH_MINING_SPEED;

    public static final ForgeConfigSpec.ConfigValue<Float> CONSTITUTION_HP;

    public static final ForgeConfigSpec.ConfigValue<Integer> LEVEL_CAP;

    static {
        BUILDER.push("Client Configs for LevelUP");

        //Config
        FREEPOINTS_PER_LEVEL = BUILDER.comment("How many stat points per level!")
                        .define("Points Per Level", 3);

        VITALITY_HP_REGEN = BUILDER.comment("HP Regen/tick per stat lvl")
                .define("HP Regen/tick per stat lvl", (float) 0.00025);

        CONSTITUTION_FALL_DAMAGE_REDUCTION = BUILDER.comment("height before fall damage per stat")
                .define("Fall dmg mitigation", (float) 0.25);

        STRENGTH_MINING_SPEED = BUILDER.comment("Tick increase for maximum air a player can have")
                .define("ticks/stat", (float) 0.01);

        CONSTITUTION_HP = BUILDER.comment("HP Increase per stat").define("HP Increase", (float) 0.025);

        LEVEL_CAP = BUILDER.comment("Max Level a player can reach without commands")
                .define("Max Level", 999999);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

}
