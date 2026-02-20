package github.catchaos8.levelup.events;

import github.catchaos8.levelup.Config;
import github.catchaos8.levelup.LevelUP;
import github.catchaos8.levelup.registries.ModAttachments;
import github.catchaos8.levelup.registries.ModAttributes;
import github.catchaos8.levelup.util.FormulaParser;
import github.catchaos8.levelup.util.MakeAttributeModifiers;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerXpEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static github.catchaos8.levelup.networking.PacketFunctions.syncToPlayer;

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

        amount*=player.getAttributeValue(ModAttributes.LEVELING_SPEED);

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

    @SubscribeEvent
    public static void onPickupXP(PlayerXpEvent.PickupXp event) {
        if(!Config.ENABLE_EXPERIENCE_ORB_XP.get()) return;
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        double amount = event.getOrb().value;
        awardXp(player, amount);
    }

    @SubscribeEvent
    public static void onLivingTick(EntityTickEvent.Post event) {
        if(!(event.getEntity() instanceof ServerPlayer player)) return;
        if (player.getAttributeValue(ModAttributes.PASSIVE_REGEN) <= 0) return;
        if(player.tickCount % (Config.TICKS_BETWEEN_REGEN.get() + 1) == 0) player.heal((float) (player.getAttributeValue(ModAttributes.PASSIVE_REGEN)/20
                /player.getAttributeValue(ModAttributes.HEALING_MULTIPLIER)*(Config.TICKS_BETWEEN_REGEN.get() + 1)));

    }

    @SubscribeEvent
    public static void onLivingHeal(LivingHealEvent event) {
        if(!(event.getEntity() instanceof ServerPlayer player)) return;

        double multi = player.getAttributeValue(ModAttributes.HEALING_MULTIPLIER);
        event.setAmount((float) (event.getAmount()*multi));
    }

    @SubscribeEvent
    public static void onAttributeRegistration(EntityAttributeModificationEvent event) { //Register attributes
        event.add(EntityType.PLAYER, ModAttributes.CONSTITUTION);
        event.add(EntityType.PLAYER, ModAttributes.DEXTERITY);
        event.add(EntityType.PLAYER, ModAttributes.STRENGTH);
        event.add(EntityType.PLAYER, ModAttributes.VITALITY);
        event.add(EntityType.PLAYER, ModAttributes.WISDOM);
        event.add(EntityType.PLAYER, ModAttributes.INTELLIGENCE);
        event.add(EntityType.PLAYER, ModAttributes.PASSIVE_REGEN);
        event.add(EntityType.PLAYER, ModAttributes.HEALING_MULTIPLIER);
        event.add(EntityType.PLAYER, ModAttributes.HUNGER_COST_REDUCTION);
        event.add(EntityType.PLAYER, ModAttributes.LEVELING_SPEED);
        event.add(EntityType.PLAYER, ModAttributes.ITEM_DURABILITY_DAMAGE_REDUCTION);
        event.add(EntityType.PLAYER, ModAttributes.POTION_DURATION_MULTI);
        event.add(EntityType.PLAYER, ModAttributes.PROJECTILE_DAMAGE);
    }

    @SubscribeEvent
    public static void death(PlayerEvent.Clone event) {
        if(!(event.getEntity() instanceof ServerPlayer player)) return;
        if(event.isWasDeath()) player.server.execute(() -> {
            for (int i = 0; i < Config.LOST_LEVELS_COUNT.get(); i++) {
                loseLevel(player);
                MakeAttributeModifiers.makeModifiers(player);
            }
        }); //Run after 1 tick
    }

    @SubscribeEvent
    public static void onPlayerJoin(EntityJoinLevelEvent event) {
        if(!(event.getEntity() instanceof ServerPlayer player)) return;
        syncToPlayer(player);
    }


    private static final Holder<Attribute>[] attributes = new Holder[]{
            ModAttributes.CONSTITUTION,
            ModAttributes.DEXTERITY,
            ModAttributes.STRENGTH,
            ModAttributes.VITALITY,
            ModAttributes.WISDOM,
            ModAttributes.INTELLIGENCE
    };

    @SubscribeEvent
    public static void onEquipmentChange(LivingEquipmentChangeEvent event) {
        if(!(event.getEntity() instanceof ServerPlayer player)) return;

        int[] oldAttributeValues = new int[attributes.length];
        for(int i = 0; i < attributes.length; i++) {
            AttributeInstance instance = player.getAttribute(attributes[i]);
            if(instance != null) {
                oldAttributeValues[i] = (int) instance.getValue();
            }
        }

        int[] oldLimited = player.getData(ModAttachments.LIMITED_STATS).clone();

        Objects.requireNonNull(player.getServer()).tell(new TickTask(1, () -> {
            int[] limited = oldLimited.clone();

            boolean changed = false;

            for (int i = 0; i < attributes.length; i++) {
                AttributeInstance newInstance = player.getAttribute(attributes[i]);
                if(newInstance == null) continue;

                int newValue = (int) newInstance.getValue();
                int oldValue = oldAttributeValues[i];
                int change = newValue - oldValue;

                if(change != 0) {
                    if(oldLimited[i] == oldValue) {
                        limited[i] = newValue;
                        changed = true;
                    } else if (newValue < oldLimited[i]) {
                        limited[i] = newValue;
                        changed = true;
                    }
                }

            }

            if(changed) {
                player.setData(ModAttachments.LIMITED_STATS, limited);
                MakeAttributeModifiers.makeModifiers(player);
                syncToPlayer(player);
            }
        }));
    }

    private static void loseLevel(ServerPlayer player) {
        if(Config.LOSE_LEVELS.get()) {
            int[] stats = player.getData(ModAttachments.STATS).clone();
            int[] limited = player.getData(ModAttachments.LIMITED_STATS).clone();
            ModAttachments.ProgressData progress = player.getData(ModAttachments.PROGRESS);

            //Count total spent points
            double totalPoints = 0;
            for (int stat : stats) {
                totalPoints += stat;
            }
            //Add freepoints
            totalPoints += progress.freePoints();

            //Points per level
            double pointsPerLvl = Config.FREE_POINTS_PER_LEVEL.get();



            //If the level is > 0
            if (progress.level() > 0 && totalPoints >= pointsPerLvl) { //Lose points

                double freePointLoss = Math.min(progress.freePoints(), pointsPerLvl); //Takes freepoints first

                double newFreepoints = progress.freePoints() - freePointLoss;

                double lostPoints = freePointLoss;

                RandomSource random = player.getRandom();

                while (lostPoints < pointsPerLvl) {

                    int lostStat = random.nextInt(stats.length);
                    if(stats[lostStat] > 0) {
                        stats[lostStat] -= 1;
                        if(limited[lostStat] > stats[lostStat])
                            limited[lostStat] -= 1;
                        lostPoints +=1;
                    }
                }
                //Decrease level
                int newLevel = progress.level() - 1;

                player.setData(ModAttachments.STATS, stats);
                player.setData(ModAttachments.PROGRESS, new ModAttachments.ProgressData(newLevel, 0, newFreepoints));
                player.setData(ModAttachments.LIMITED_STATS, limited);
            }

        }
    }

}