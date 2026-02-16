package github.catchaos8.levelup.events;

import github.catchaos8.levelup.Config;
import github.catchaos8.levelup.LevelUP;
import github.catchaos8.levelup.registries.ModAttachments;
import github.catchaos8.levelup.util.FormulaParser;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = LevelUP.MOD_ID)
public class ModEvents {

    // Cached block XP map, rebuilt when config loads
    private static Map<String, Double> blockXpCache = null;

    // -------------------------------------------------------------------------
    // Block XP Cache
    // -------------------------------------------------------------------------

    private static Map<String, Double> getBlockXpMap() {
        if (blockXpCache == null) {
            blockXpCache = new HashMap<>();
            for (String entry : Config.BLOCK_XP_LIST.get()) {
                String[] parts = entry.split(",");
                if (parts.length == 2) {
                    try {
                        blockXpCache.put(parts[0].trim(), Double.parseDouble(parts[1].trim()));
                    } catch (NumberFormatException e) {
                        LevelUP.LOGGER.warn("Invalid block XP entry in config: {}", entry);
                    }
                }
            }
        }
        return blockXpCache;
    }

    // -------------------------------------------------------------------------
    // XP & Leveling Helpers
    // -------------------------------------------------------------------------

    private static double round2dp(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    private static double getXpRequired(int level) {
        try {
            return Math.round(FormulaParser.evaluate(Config.XP_FORMULA.get(), level));
        } catch (Exception e) {
            LevelUP.LOGGER.error("Invalid XP formula in config: '{}'. Falling back to default.", Config.XP_FORMULA.get());
            // Fallback formula
            return Math.round(0.5 * Math.pow(2, level) + 24);
        }
    }

    private static void awardXp(ServerPlayer player, double amount) {
        if (amount <= 0) return;
        ModAttachments.ProgressData progress = player.getData(ModAttachments.PROGRESS);

        double newXp = round2dp(progress.xp() + amount);
        int currentLevel = progress.level();
        double freePoints = progress.freePoints();

        // Handle leveling up — loop in case of multiple level ups at once
        while (newXp >= getXpRequired(currentLevel)) {
            newXp = round2dp(newXp - getXpRequired(currentLevel));
            currentLevel++;
            freePoints = round2dp(freePoints + Config.FREE_POINTS_PER_LEVEL.get());
            LevelUP.LOGGER.debug("Player {} leveled up to level {}!", player.getName().getString(), currentLevel);
        }

        player.setData(ModAttachments.PROGRESS, new ModAttachments.ProgressData(currentLevel, newXp, freePoints));
    }

    // -------------------------------------------------------------------------
    // Events
    // -------------------------------------------------------------------------

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (!(event.getPlayer() instanceof ServerPlayer player)) return;

        Block block = event.getState().getBlock();
        ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(block);

        String blockKey = blockId.toString(); // e.g. "minecraft:diamond_ore"
        Map<String, Double> blockXpMap = getBlockXpMap();

        double xp;
        if (blockXpMap.containsKey(blockKey)) {
            xp = blockXpMap.get(blockKey);
        } else {
            xp = Config.DEFAULT_BLOCK_XP.get();
        }

        awardXp(player, xp);
    }

    @SubscribeEvent
    public static void onMobKill(LivingDeathEvent event) {
        // Only killed by a player
        if (!(event.getSource().getEntity() instanceof ServerPlayer player)) return;

        LivingEntity mob = event.getEntity();

        double healthXp = 0;
        double damageXp = 0;
        double speedXp = 0;

        // Health contribution
        var maxHealthAttr = mob.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealthAttr != null) {
            healthXp = maxHealthAttr.getValue() / Config.MOB_HEALTH_DIVISOR.get();
        }

        // Attack damage contribution
        var attackAttr = mob.getAttribute(Attributes.ATTACK_DAMAGE);
        if (attackAttr != null) {
            damageXp = attackAttr.getValue();
        }

        // Movement speed contribution
        var speedAttr = mob.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speedAttr != null) {
            speedXp = speedAttr.getValue() / Config.MOB_SPEED_DIVISOR.get();
        }

        double totalXp = round2dp(healthXp + damageXp + speedXp);
        if(mob instanceof ServerPlayer) totalXp*=10;

        awardXp(player, totalXp);
    }
}