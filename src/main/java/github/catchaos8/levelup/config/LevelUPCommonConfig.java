package github.catchaos8.levelup.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class LevelUPCommonConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;


    public static final ForgeConfigSpec.ConfigValue<Boolean> RESET_POINTS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> LOSE_LEVELS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> LOSE_XP;
    public static final ForgeConfigSpec.ConfigValue<Double> XP_LOSS_PERCENT;


    public static final ForgeConfigSpec.ConfigValue<Double> CONSTITUTION_FALL_DAMAGE_REDUCTION;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DO_FALL_DAMAGE_REDUCTION;
    public static final ForgeConfigSpec.ConfigValue<Double> CONSTITUTION_HP;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DO_HP;

    public static final ForgeConfigSpec.ConfigValue<Double> DEXTERITY_SPEED;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DO_SPEED;
    public static final ForgeConfigSpec.ConfigValue<Double> DEXTERITY_SWIM_SPEED;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DO_SWIM_SPEED;
    public static final ForgeConfigSpec.ConfigValue<Double> DEXTERITY_ATTACK_SPEED;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DO_ATTACK_SPEED;
    public static final ForgeConfigSpec.ConfigValue<Double> DEXTERITY_JUMP_BOOST;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DO_JUMP_BOOST;

    public static final ForgeConfigSpec.ConfigValue<Double> STRENGTH_DAMAGE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DO_DAMAGE;
    public static final ForgeConfigSpec.ConfigValue<Double> STRENGTH_KNOCKBACK;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DO_KNOCKBACK;

//    public static final ForgeConfigSpec.ConfigValue<Double> VITALITY_ARMOR; //Switched To endurance
//    public static final ForgeConfigSpec.ConfigValue<Boolean> DO_ARMOR;
    public static final ForgeConfigSpec.ConfigValue<Double> VITALITY_HP_REGEN;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DO_HEALING;
    public static final ForgeConfigSpec.ConfigValue<Double> VITALITY_HEALING;
    public static final ForgeConfigSpec.ConfigValue<Integer> VITALITY_TICKS_PER_REGEN;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DO_HP_REGEN;
    public static final ForgeConfigSpec.ConfigValue<Double> VITALITY_HARDCORE_NERF;

    public static final ForgeConfigSpec.ConfigValue<Double> ENDURANCE_ARMOR_TOUGHNESS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DO_ARMOR_TOUGHNESS;
    public static final ForgeConfigSpec.ConfigValue<Double> ENDURANCE_KNOCKBACK_RESISTANCE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DO_KB_RES;
    public static final ForgeConfigSpec.ConfigValue<Double> ENDURANCE_HUNGER;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DO_HUNGER;
    public static final ForgeConfigSpec.ConfigValue<Double> ENDURANCE_ARMOR;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DO_ARMOR;

    public static final ForgeConfigSpec.ConfigValue<Double> WISDOM_XP_MULTIPLIER;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DO_WISDOM_XP;
    public static final ForgeConfigSpec.ConfigValue<Double> WISDOM_LEVELING_SPEED;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DO_WISDOM_LEVELING_SPEED;
    public static final ForgeConfigSpec.ConfigValue<Double> WISDOM_ARS_MANA_REGEN_BOOST;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DO_ARS_MANA_REGEN_BOOST;
    public static final ForgeConfigSpec.ConfigValue<Double> WISDOM_IRONS_MANA_REGEN_BOOST;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DO_IRONS_MANA_REGEN_BOOST;


    public static final ForgeConfigSpec.ConfigValue<Double> INTELLIGENCE_DURABILITY_DAMAGE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DO_DURABILITY_REDUCTION;
    public static final ForgeConfigSpec.ConfigValue<Double> INTELLIGENCE_POTION_DURATION_BOOST;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DO_POTION_DURATION_BOOST;
    public static final ForgeConfigSpec.ConfigValue<Double> INTELLIGENCE_ARS_MAX_MANA_BOOST;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DO_ARS_MAX_MANA_BOOST;
    public static final ForgeConfigSpec.ConfigValue<Double> INTELLIGENCE_IRONS_MAX_MANA_BOOST;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DO_IRONS_MAX_MANA_BOOST;


    public static final ForgeConfigSpec.ConfigValue<Double> FREEPOINTS_PER_LEVEL;
    public static final ForgeConfigSpec.ConfigValue<Integer> LEVEL_CAP;
    public static final ForgeConfigSpec.ConfigValue<Integer> STAT_CAP;


    public static final ForgeConfigSpec.ConfigValue<Double> A_VALUE;
    public static final ForgeConfigSpec.ConfigValue<Double> B_VALUE;
    public static final ForgeConfigSpec.ConfigValue<Double> C_VALUE;
    public static final ForgeConfigSpec.ConfigValue<Double> D_VALUE;

    public static final ForgeConfigSpec.ConfigValue<Boolean> DISPLAY_LEVEL_UNDER_NAME;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DISPLAY_LEVEL_BESIDE_NAME_IN_PLAYER_LIST;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DISPLAY_LEVEL_UNDER_NAME_IN_SIDEBAR;

    public static final ForgeConfigSpec.ConfigValue<Integer> STAT_INCREASE_PER_ENCHANT_LVL;


    static {
        BUILDER.push("Client Configs for LevelUP");

        //Config
        FREEPOINTS_PER_LEVEL = BUILDER.comment("How many stat points per level!")
                        .define("Points Per Level", 3.0);
        RESET_POINTS = BUILDER.comment("Reset player stats after death(and level) - Overrides lose levels")
                .define("Reset", false);
        LOSE_LEVELS = BUILDER.comment("Make player lose stats and levels after death. When using try and use non-decimal points per level")
                .define("Lose levels", false);

        LOSE_XP = BUILDER.comment("Make player lose xp after death. Makes you lose levels if you have lose levels set to true. ")
                .define("Lose XP", true);

        XP_LOSS_PERCENT = BUILDER.comment("XP % loss after death if lose xp is enabled. 1 = 1%")
                .define("Death XP Loss", 25.0);

        CONSTITUTION_FALL_DAMAGE_REDUCTION = BUILDER.comment("Height before fall damage per stat")
                .define("Fall dmg mitigation", 0.25);
        DO_FALL_DAMAGE_REDUCTION = BUILDER.comment("Enable constitution fall damage")
                .define("Do fall damage reduction", true);

        CONSTITUTION_HP = BUILDER.comment("HP increase per stat").define("HP increase",  0.01);
        DO_HP = BUILDER.comment("Enable constitution hp increase")
                .define("Do hp", true);


        DEXTERITY_SPEED = BUILDER.comment("Speed Increase per stat").define("Speed increase",  0.01);
        DO_SPEED = BUILDER.comment("Enable dexterity speed")
                .define("Do speed", true);
        DEXTERITY_ATTACK_SPEED = BUILDER.comment("Attack speed Increase per stat").define("Attack Speed increase", 0.01);
        DO_ATTACK_SPEED = BUILDER.comment("Enable dexterity attack speed")
                .define("Do attack speed", true);
        DEXTERITY_SWIM_SPEED = BUILDER.comment("Swim Speed Increase per stat").define("Swim speed increase", 0.01);
        DO_SWIM_SPEED = BUILDER.comment("Enable dexterity swim speed")
                .define("Do swim speed", false);
        DEXTERITY_JUMP_BOOST = BUILDER.comment("Jump boost level per x stat points").define("Jump height increase", 50.0);
        DO_JUMP_BOOST = BUILDER.comment("Enable dexterity jump boost")
                .define("Do jump boost", true);

        STRENGTH_DAMAGE = BUILDER.comment("Damage Increase per stat").define("Dmg Increase", 0.01);
        DO_DAMAGE = BUILDER.comment("Enable strength damage")
                .define("Do damage", true);
        STRENGTH_KNOCKBACK = BUILDER.comment("Knockback Increase per stat").define("Kb Increase", 0.01);
        DO_KNOCKBACK = BUILDER.comment("Enable strength knockback")
                .define("Do Knockback", true);

        VITALITY_HP_REGEN = BUILDER.comment("HP Regen/tick per stat lvl")
                .define("HP Regen/tick per stat lvl", 0.00025);
        DO_HP_REGEN = BUILDER.comment("Enable vitality regen")
                .define("Do regen", true);
        VITALITY_TICKS_PER_REGEN = BUILDER.comment("Ticks per Regen").define("Ticks Per Regen", 1);
        VITALITY_HEALING = BUILDER.comment("Vitality Healing Buff to all healing").define("Healing Buff",  0.005);
        DO_HEALING = BUILDER.comment("Enable vitality armor")
                .define("Do armor", true);
        VITALITY_HARDCORE_NERF = BUILDER.comment("The amount regen is divided by. Divide by 1 to disable, use a number lower than one to buff vitality in hardcore. DO NOT USE 0.")
                .define("Vitality regen nerf in hardcore", 1.5);

        ENDURANCE_ARMOR_TOUGHNESS = BUILDER.comment("Armor Toughness Increase per stat").define("Armor Toughness Increase", 0.1);
        DO_ARMOR_TOUGHNESS = BUILDER.comment("Enable endurance armor toughness")
                .define("Do armor toughness", false);
        ENDURANCE_KNOCKBACK_RESISTANCE = BUILDER.comment("Knockback Resistance per stat").define("KB Res Increase", 0.01);
        DO_KB_RES = BUILDER.comment("Enable endurance kb res")
                .define("Do kb res", false);
        ENDURANCE_HUNGER = BUILDER.comment("Hunger cost decrease of sprinting per stat").define("Hunger Cost Decrease", 0.01);
        DO_HUNGER = BUILDER.comment("Enable endurance hunger decrease")
                .define("do hunger decrease", true);
        ENDURANCE_ARMOR = BUILDER.comment("Armor % increase for each point in endurance")
                .define("Endurance armor percent", 0.01);
        DO_ARMOR = BUILDER.comment("Enable Endurance Armor")
                .define("endurance armor", true);


        WISDOM_XP_MULTIPLIER = BUILDER
                .comment("XP gain multiplier per stat ")
                .define("Wisdom XP Multiplier", 0.01);

        DO_WISDOM_XP = BUILDER
                .comment("Enable wisdom XP gain bonus")
                .define("Do wisdom xp", true);

        WISDOM_LEVELING_SPEED = BUILDER
                .comment("LevelUP xp multiplier ")
                .define("Wisdom leveling speed multiplier", 0.01);

        DO_WISDOM_LEVELING_SPEED = BUILDER.comment("Enable Wisdom Leveling Speed")
                .define("Do wisdom leveling speed", true);

        WISDOM_ARS_MANA_REGEN_BOOST = BUILDER
                .comment("Ars Nouveau Mana Regen Boost ")
                .define("Ars mana regen boost", 0.01);

        DO_ARS_MANA_REGEN_BOOST = BUILDER.comment("Enable Ars Nouveau mana regen boost")
                .define("Do ars mana regen", true);

        WISDOM_IRONS_MANA_REGEN_BOOST = BUILDER
                .comment("Irons Mana Regen Boost ")
                .define("Irons regen boost", 0.01);

        DO_IRONS_MANA_REGEN_BOOST = BUILDER.comment("Enable Irons mana regen boost")
                .define("Do Irons regen", true);

        INTELLIGENCE_DURABILITY_DAMAGE = BUILDER
                .comment("Chance to not take durability damage (Exponential, not linear, like the endurance hunger)")
                .define("Durability damage reduction", 0.01);

        DO_DURABILITY_REDUCTION = BUILDER
                .comment("Enable intelligence durability reduction")
                .define("Do durability reduction", true);
        INTELLIGENCE_POTION_DURATION_BOOST = BUILDER
                .comment("Chance to not take durability damage (Exponential, not linear, like the endurance hunger)")
                .define("Durability damage reduction", 0.01);

        INTELLIGENCE_ARS_MAX_MANA_BOOST = BUILDER
                .comment("Ars Nouveau Mana Regen Boost ")
                .define("Ars max mana boost", 0.01);

        DO_ARS_MAX_MANA_BOOST = BUILDER.comment("Enable Irons Spells max regen boost")
                .define("Do ars max mana", true);
        INTELLIGENCE_IRONS_MAX_MANA_BOOST = BUILDER
                .comment("Ars Nouveau Mana Regen Boost ")
                .define("Iron's max mana boost", 0.01);

        DO_IRONS_MAX_MANA_BOOST = BUILDER.comment("Enable Iron's Spells max regen boost")
                .define("Do iron's max mana", true);


        DO_POTION_DURATION_BOOST = BUILDER
                .comment("Enable intelligence durability reduction")
                .define("Do durability reduction", true);


        LEVEL_CAP = BUILDER.comment("Max Level a player can reach without commands")
                .define("Max Level", 999999);
        STAT_CAP = BUILDER.comment("Max stat a player can reach without commands")
                .define("Max Stat", 1073741824);



        A_VALUE = BUILDER.comment("The a value in the equation for calculating xp for next level: ax^d+bx+c     a increases how fast the xp needed for the next level increases")
                .define("A value", 0.2);
        B_VALUE = BUILDER.comment("The b value in the equation for calculating xp for next level: ax^d+bx+c     b increases how fast the xp needed for the next level increases")
                .define("B value",  0.25);
        C_VALUE = BUILDER.comment("The c value in the equation for calculating xp for next level: ax^d+bx+c     c is the base xp needed for level 1")
                .define("C value",  10.0);
        D_VALUE = BUILDER.comment("The d value in the equation for calculating xp for next level: ax^d+bx+c     d increases how fast the xp needed for the next level increases")
                .define("D value",  2.0);

        DISPLAY_LEVEL_UNDER_NAME = BUILDER.comment("Display the level under your nametag")
                        .define("Display under name", false);
        DISPLAY_LEVEL_BESIDE_NAME_IN_PLAYER_LIST = BUILDER.comment("Display the level beside your name in playerlist")
                .define("Display in list", true);
        DISPLAY_LEVEL_UNDER_NAME_IN_SIDEBAR = BUILDER.comment("Display the level on the side of your screen")
                .define("Display in sidebar", false);


        STAT_INCREASE_PER_ENCHANT_LVL = BUILDER.comment("Stat increase per enchantment level")
                .define("Ench Stat Increase", 5);




        BUILDER.pop();
        SPEC = BUILDER.build();
    }

}
