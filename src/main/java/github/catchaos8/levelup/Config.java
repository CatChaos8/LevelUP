package github.catchaos8.levelup;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public class Config {
    public static final ModConfigSpec SPEC;

    // XP Formula
    public static final ModConfigSpec.ConfigValue<String> XP_FORMULA;

    // EXP ORB XP
    public static final ModConfigSpec.BooleanValue ENABLE_EXPERIENCE_ORB_XP;

    // Mob XP
    public static final ModConfigSpec.DoubleValue MOB_HEALTH_DIVISOR;
    public static final ModConfigSpec.DoubleValue MOB_SPEED_DIVISOR;

    // Block XP
    public static final ModConfigSpec.DoubleValue DEFAULT_BLOCK_XP;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> BLOCK_XP_LIST;

    // Stats
    public static final ModConfigSpec.IntValue STAT_CAP;

    // Leveling
    public static final ModConfigSpec.DoubleValue FREE_POINTS_PER_LEVEL;

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

        builder.comment("Stats Settings").push("stats");
        STAT_CAP = builder
                .comment("Maximum value a single stat can reach")
                .defineInRange("stat_cap", 99999, 1, Integer.MAX_VALUE);
        builder.pop();

        builder.comment("Leveling Settings").push("leveling");
        FREE_POINTS_PER_LEVEL = builder
                .comment("How many free points the player receives per level up")
                .defineInRange("free_points_per_level", 3, 0.1, Double.MAX_VALUE);
        builder.pop();

        SPEC = builder.build();
    }
}