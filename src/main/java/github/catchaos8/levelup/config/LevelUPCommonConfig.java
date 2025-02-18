package github.catchaos8.levelup.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class LevelUPCommonConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;


    public static final ForgeConfigSpec.ConfigValue<Double> CONSTITUTION_FALL_DAMAGE_REDUCTION;
    public static final ForgeConfigSpec.ConfigValue<Double> CONSTITUTION_HP;

    public static final ForgeConfigSpec.ConfigValue<Double> DEXTERITY_SPEED;
    public static final ForgeConfigSpec.ConfigValue<Double> DEXTERITY_SWIM_SPEED;

    public static final ForgeConfigSpec.ConfigValue<Double> STRENGTH_DAMAGE;
    public static final ForgeConfigSpec.ConfigValue<Double> STRENGTH_KNOCKBACK;

    public static final ForgeConfigSpec.ConfigValue<Double> VITALITY_ARMOR;
    public static final ForgeConfigSpec.ConfigValue<Double> VITALITY_HP_REGEN;
    public static final ForgeConfigSpec.ConfigValue<Double> VITALITY_HARDCORE_NERF;

    public static final ForgeConfigSpec.ConfigValue<Double> ENDURANCE_ARMOR_TOUGHNESS;
    public static final ForgeConfigSpec.ConfigValue<Double> ENDURANCE_KNOCKBACK_RESISTANCE;


    public static final ForgeConfigSpec.ConfigValue<Integer> FREEPOINTS_PER_LEVEL;
    public static final ForgeConfigSpec.ConfigValue<Integer> LEVEL_CAP;
    public static final ForgeConfigSpec.ConfigValue<Integer> STAT_CAP;


    public static final ForgeConfigSpec.ConfigValue<Double> A_VALUE;
    public static final ForgeConfigSpec.ConfigValue<Double> B_VALUE;
    public static final ForgeConfigSpec.ConfigValue<Double> C_VALUE;

    public static final ForgeConfigSpec.ConfigValue<Boolean> DISPLAY_LEVEL_UNDER_NAME;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DISPLAY_LEVEL_BESIDE_NAME_IN_PLAYER_LIST;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DISPLAY_LEVEL_UNDER_NAME_IN_SIDEBAR;


    static {
        BUILDER.push("Client Configs for LevelUP");

        //Config
        FREEPOINTS_PER_LEVEL = BUILDER.comment("How many stat points per level!")
                        .define("Points Per Level", 3);

        CONSTITUTION_FALL_DAMAGE_REDUCTION = BUILDER.comment("height before fall damage per stat")
                .define("Fall dmg mitigation", 0.25);

        CONSTITUTION_HP = BUILDER.comment("HP Increase per stat").define("HP Increase",  0.025);

        DEXTERITY_SPEED = BUILDER.comment("Speed Increase per stat").define("Speed Increase",  0.01);
        DEXTERITY_SWIM_SPEED = BUILDER.comment("Swim Speed Increase per stat").define("Swim speed Increase", 0.01);

        STRENGTH_DAMAGE = BUILDER.comment("Damage Increase per stat").define("Dmg Increase", 0.01);
        STRENGTH_KNOCKBACK = BUILDER.comment("Knockback Increase per stat").define("Kb Increase", 0.01);

        VITALITY_HP_REGEN = BUILDER.comment("HP Regen/tick per stat lvl")
                .define("HP Regen/tick per stat lvl", 0.00025);
        VITALITY_ARMOR = BUILDER.comment("Armor Increase per stat").define("Armor Increase",  0.01);
        VITALITY_HARDCORE_NERF = BUILDER.comment("The amount regen is divided by. Divide by 1 to disable, use a number lower than one to buff vitality in hardcore. DO NOT USE 0.")
                .define("Vitality regen nerf in hardcore", 1.5);

        ENDURANCE_ARMOR_TOUGHNESS = BUILDER.comment("Armor Toughness Increase per stat").define("Armor Toughness Increase", 0.1);
        ENDURANCE_KNOCKBACK_RESISTANCE = BUILDER.comment("Knockback Resistance per stat").define("KB Res Increase", 0.01);

        LEVEL_CAP = BUILDER.comment("Max Level a player can reach without commands")
                .define("Max Level", 999999);
        STAT_CAP = BUILDER.comment("Max Level a player can reach without commands")
                .define("Max Stat", 1073741824);

        A_VALUE = BUILDER.comment("The a value in the equation for calculating xp for next level: ax^2+bx+c     a increases how fast the xp needed for the next level increases")
                .define("A value", 0.2);
        B_VALUE = BUILDER.comment("The b value in the equation for calculating xp for next level: ax^2+bx+c     a increases how fast the xp needed for the next level increases")
                .define("B value",  0.25);
        C_VALUE = BUILDER.comment("The c value in the equation for calculating xp for next level: ax^2+bx+c     c is the base xp needed for level 1")
                .define("C value",  10.0);

        DISPLAY_LEVEL_UNDER_NAME = BUILDER.comment("Display the level under your nametag")
                        .define("Display under name", false);
        DISPLAY_LEVEL_BESIDE_NAME_IN_PLAYER_LIST = BUILDER.comment("Display the level beside your name in playerlist")
                .define("Display in list", true);
        DISPLAY_LEVEL_UNDER_NAME_IN_SIDEBAR = BUILDER.comment("Display the level on the side of your screen")
                .define("Display in sidebar", false);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

}
