package github.catchaos8.levelup.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class LevelUPCommonConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;


    public static final ForgeConfigSpec.ConfigValue<Float> CONSTITUTION_FALL_DAMAGE_REDUCTION;
    public static final ForgeConfigSpec.ConfigValue<Float> CONSTITUTION_HP;

    public static final ForgeConfigSpec.ConfigValue<Float> DEXTERITY_SPEED;
    public static final ForgeConfigSpec.ConfigValue<Float> DEXTERITY_SWIM_SPEED;

    public static final ForgeConfigSpec.ConfigValue<Float> STRENGTH_DAMAGE;
    public static final ForgeConfigSpec.ConfigValue<Float> STRENGTH_KNOCKBACK;

    public static final ForgeConfigSpec.ConfigValue<Float> VITALITY_ARMOR;
    public static final ForgeConfigSpec.ConfigValue<Float> VITALITY_HP_REGEN;

    public static final ForgeConfigSpec.ConfigValue<Float> ENDURANCE_ARMOR_TOUGHNESS;
    public static final ForgeConfigSpec.ConfigValue<Float> ENDURANCE_KNOCKBACK_RESISTANCE;


    public static final ForgeConfigSpec.ConfigValue<Integer> FREEPOINTS_PER_LEVEL;
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

        CONSTITUTION_HP = BUILDER.comment("HP Increase per stat").define("HP Increase", (float) 0.025);

        DEXTERITY_SPEED = BUILDER.comment("Speed Increase per stat").define("Speed Increase", (float) 0.005);
        DEXTERITY_SWIM_SPEED = BUILDER.comment("Swim Speed Increase per stat").define("Swim speed Increase", (float) 0.005);

        STRENGTH_DAMAGE = BUILDER.comment("Damage Increase per stat").define("Dmg Increase", (float) 0.01);
        STRENGTH_KNOCKBACK = BUILDER.comment("Knockback Increase per stat").define("Kb Increase", (float) 0.01);

        VITALITY_ARMOR = BUILDER.comment("Armor Increase per stat").define("Armor Increase", (float) 0.01);

        ENDURANCE_ARMOR_TOUGHNESS = BUILDER.comment("Armor Toughness Increase per stat").define("Armor Toughness Increase", (float) 0.1);
        ENDURANCE_KNOCKBACK_RESISTANCE = BUILDER.comment("Knockback Resistance per stat").define("KB Res Increase", (float) 0.01);

        LEVEL_CAP = BUILDER.comment("Max Level a player can reach without commands")
                .define("Max Level", 999999);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

}
