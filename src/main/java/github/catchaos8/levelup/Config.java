package github.catchaos8.levelup;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public class Config {
    public static final ModConfigSpec SPEC;

    // XP Formula
    public static final ModConfigSpec.ConfigValue<String> XP_FORMULA;

    // EXP ORB XP
    public static final ModConfigSpec.BooleanValue ENABLE_EXPERIENCE_ORB_XP;

    //Enable lose levels
    public static final ModConfigSpec.BooleanValue LOSE_LEVELS;
    //Amount of lost levels
    public static final ModConfigSpec.IntValue LOST_LEVELS_COUNT;

    // Mob XP
    public static final ModConfigSpec.DoubleValue MOB_HEALTH_DIVISOR;
    public static final ModConfigSpec.DoubleValue MOB_SPEED_DIVISOR;

    // Block XP
    public static final ModConfigSpec.DoubleValue DEFAULT_BLOCK_XP;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> BLOCK_XP_LIST;

    // Stats
    public static final ModConfigSpec.IntValue STAT_CAP;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> CONSTITUTION_ATTRIBUTES;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> DEXTERITY_ATTRIBUTES;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> STRENGTH_ATTRIBUTES;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> VITALITY_ATTRIBUTES;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> WISDOM_ATTRIBUTES;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> INTELLIGENCE_ATTRIBUTES;

    // Leveling
    public static final ModConfigSpec.DoubleValue FREE_POINTS_PER_LEVEL;

    //Attributes
    public static final ModConfigSpec.DoubleValue TICKS_BETWEEN_REGEN;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.comment("LevelUP Configuration").push("xp");

        builder.comment("Formula for XP required to level up(Rounded to the nearest whole number). Use 'x' as the current level variable.",
                        "Example: '0.5*(2^(x/10)) + 25'")
                .push("formula");
        XP_FORMULA = builder.define("xp_formula", "0.5*(2^(x/10)) + 100");
        builder.pop();

        builder.comment("Experience Orb XP").push("xp_orbs");
        ENABLE_EXPERIENCE_ORB_XP = builder.comment("Gain xp when picking up experience orbs")
                .define("xp_orbs_enabled", false);
        builder.pop();

        builder.comment("Lose Levels on Death").push("death");
        LOSE_LEVELS = builder.comment("Enable losing levels on death")
                .define("lose_levels_on_death", true);

        LOST_LEVELS_COUNT = builder.comment("Amount of levels lost per death if lose_levels is enabled")
                        .defineInRange("lost_levels_amount", 1, 1, Integer.MAX_VALUE);

        builder.pop();



        builder.comment("Mob XP Settings").push("mobs");
        MOB_HEALTH_DIVISOR = builder
                .comment("XP gained = maxHealth / this value (default: 5, so 20 health = 4 XP)")
                .defineInRange("health_divisor", 5.0, 0.1, Double.MAX_VALUE);
        MOB_SPEED_DIVISOR = builder
                .comment("XP gained = movementSpeed / this value (default: 0.1, so 0.3 speed = 3 XP)")
                .defineInRange("speed_divisor", 0.1, 0.001, Double.MAX_VALUE);
        builder.pop();

        builder.comment("Block XP Settings").push("blocks");
        DEFAULT_BLOCK_XP = builder
                .comment("Default XP given for breaking any block not in the list below")
                .defineInRange("default_block_xp", 0.1, 0.0, Double.MAX_VALUE);

        BLOCK_XP_LIST = builder
                .comment(
                        "Custom XP values for specific blocks. Format: 'modid:blockname,xp'",
                        "Example: 'minecraft:diamond_ore,125.0'",
                        "This completely replaces the default XP for that block.",
                        "Copy and add new lines to add more blocks (including modded ones)."
                )
                .defineListAllowEmpty("block_xp_list", List.of(
                        // Coal & Copper tier
                        "minecraft:coal_ore,1.0",
                        "minecraft:deepslate_coal_ore,1.0",
                        "minecraft:copper_ore,1.0",
                        "minecraft:deepslate_copper_ore,1.0",
                        // Iron tier
                        "minecraft:iron_ore,5.0",
                        "minecraft:deepslate_iron_ore,5.0",
                        // Gold tier
                        "minecraft:gold_ore,10.0",
                        "minecraft:deepslate_gold_ore,10.0",
                        "minecraft:nether_gold_ore,10.0",
                        // Lapis tier
                        "minecraft:lapis_ore,100.0",
                        "minecraft:deepslate_lapis_ore,100.0",
                        // Diamond tier
                        "minecraft:diamond_ore,125.0",
                        "minecraft:deepslate_diamond_ore,125.0",
                        // Emerald tier
                        "minecraft:emerald_ore,150.0",
                        "minecraft:deepslate_emerald_ore,150.0",
                        // Ancient Debris
                        "minecraft:ancient_debris,500.0"
                ), entry -> {
                    if (!(entry instanceof String s)) return false;
                    String[] parts = s.split(",");
                    if (parts.length != 2) return false;
                    try {
                        Double.parseDouble(parts[1].trim());
                        return parts[0].trim().contains(":");
                    } catch (NumberFormatException e) {
                        return false;
                    }
                });
        builder.pop();

        builder.pop();

        builder.comment("Stats settings").push("stats");

        builder.comment("Max value").push("max");
        STAT_CAP = builder
                .comment("Maximum value a single stat can reach")
                .defineInRange("stat_cap", 99999, 1, Integer.MAX_VALUE);
        builder.pop();


        builder.comment("Leveling Settings").push("leveling");
        FREE_POINTS_PER_LEVEL = builder
                .comment("How many free points the player receives per level up")
                .defineInRange("free_points_per_level", 3, 0.1, Double.MAX_VALUE);
        builder.pop();

        builder.comment("Attributes affected by stats").push("attributes");
        builder.comment("Constitution Attributes").push("constitution");
        CONSTITUTION_ATTRIBUTES = builder.comment(
                "Attributes that get increased by Constitution. Format: modid:attribute,signum,amount",
                "Example: 'minecraft:generic.max_health,multiplication,0.01'",
                "Possible signums: multiplication, addition",
                "No spaces"
        ).defineListAllowEmpty("constitution_attribute_list",
                List.of("minecraft:generic.max_health,multiplication,0.01",
                "minecraft:generic.safe_fall_distance,addition,0.25"), Config::validateAttributeEntry);
        builder.pop();



        builder.comment("Dexterity Attributes").push("dexterity");
        DEXTERITY_ATTRIBUTES = builder.comment(
                "Attributes that get increased by Dexterity. Format: modid:attribute,signum,amount",
                "Example: 'minecraft:generic.max_health,multiplication,0.01'",
                "Possible signums: multiplication, addition",
                "No spaces"
        ).defineListAllowEmpty("dexterity_attribute_list",
                List.of("minecraft:generic.movement_speed,multiplication,0.01",
                        "minecraft:generic.attack_speed,multiplication,0.01",
                        "minecraft:generic.jump_strength,addition,0.0001"), Config::validateAttributeEntry);
        builder.pop();



        builder.comment("Strength Attributes").push("strength");
        STRENGTH_ATTRIBUTES = builder.comment(
                "Attributes that get increased by Strength. Format: modid:attribute,signum,amount",
                "Example: 'minecraft:generic.max_health,multiplication,0.01'",
                "Possible signums: multiplication, addition",
                "No spaces"
        ).defineListAllowEmpty("strength_attribute_list",
                List.of("minecraft:generic.attack_damage,multiplication,0.01",
                        "minecraft:generic.attack_knockback,multiplication,0.01"), Config::validateAttributeEntry);
        builder.pop();

        builder.comment("Vitality Attributes").push("vitality");
        VITALITY_ATTRIBUTES = builder.comment(
                "Attributes that get increased by Vitality. Format: modid:attribute,signum,amount",
                "Example: 'minecraft:generic.max_health,multiplication,0.01'",
                "Possible signums: multiplication, addition",
                "No spaces"
        ).defineListAllowEmpty("vitality_attribute_list",
                List.of("levelup:passive_regen,addition,0.01",
                        "levelup:healing_multi,addition,0.01"), Config::validateAttributeEntry);
        builder.pop();

        builder.comment("Wisdom Attributes").push("wisdom");
        WISDOM_ATTRIBUTES = builder.comment(
                "Attributes that get increased by Wisdom. Format: modid:attribute,signum,amount",
                "Example: 'minecraft:generic.max_health,multiplication,0.01'",
                "Possible signums: multiplication, addition",
                "No spaces"
        ).defineListAllowEmpty("wisdom_attribute_list",
                List.of("levelup:leveling_speed,addition,0.01"), Config::validateAttributeEntry);
        builder.pop();

        builder.comment("Intelligence Attributes").push("intelligence");
        INTELLIGENCE_ATTRIBUTES = builder.comment(
                "Attributes that get increased by Intelligence. Format: modid:attribute,signum,amount",
                "Example: 'minecraft:generic.max_health,multiplication,0.01'",
                "Possible signums: multiplication, addition",
                "No spaces"
        ).defineListAllowEmpty("intelligence_attribute_list",
                List.of("levelup:item_durability_damage_reduction,addition,0.01",
                        "levelup:potion_duration_multi,addition,0.01"), Config::validateAttributeEntry);
        builder.pop();

        builder.pop();
        builder.pop();


        builder.comment("Attribute Settings").push("attributes");
        builder.comment("Healing Tick attributes").push("healing_tick");
        TICKS_BETWEEN_REGEN = builder
                .comment("How many ticks between healing ticks")
                .defineInRange("healing_tick_delay", 0, 0, Double.MAX_VALUE);
        builder.pop();

        SPEC = builder.build();
    }



    private static boolean validateAttributeEntry(Object entry) {
        if (!(entry instanceof String s)) return false;
        String[] parts = s.split(",");
        if (parts.length != 3) return false; // Changed from 2 to 3
        try {
            Double.parseDouble(parts[2].trim()); // Changed from parts[1] to parts[2]
            String operation = parts[1].trim().toLowerCase();
            // Validate operation is either "addition" or "multiplication"
            if (!operation.equals("addition") && !operation.equals("multiplication")) {
                return false;
            }
            return parts[0].trim().contains(":");
        } catch (NumberFormatException e) {
            return false;
        }
    }
}