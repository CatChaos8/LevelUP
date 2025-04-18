package github.catchaos8.levelup.lib;

import com.google.common.collect.Multimap;
import github.catchaos8.levelup.attributes.ModAttributes;
import github.catchaos8.levelup.config.LevelUPCommonConfig;
import github.catchaos8.levelup.enchants.ModEnchants;
import github.catchaos8.levelup.stats.PlayerStatsProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.ForgeMod;

import java.util.Map;
import java.util.UUID;

public class SetStats {
    public static void setAttributeStat(float amount, int type, ServerPlayer player){

        UUID uuid = UUID.fromString("d7663cf7-09d3-48a9-9e22-bc0f495a96b8");

        player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
            AttributeModifier modifier = new AttributeModifier(
                    uuid,
                    "stat base",
                    amount,
                    AttributeModifier.Operation.ADDITION
            );

            AttributeInstance attribute = null;
            AttributeInstance attributeInstance = null;

            if(type == 0){
                attribute = player.getAttribute(ModAttributes.CONSTITUTION.get());
                attributeInstance = player.getAttribute(ModAttributes.CONSTITUTION.get());
            }else if(type == 1) {
                attribute = player.getAttribute(ModAttributes.DEXTERITY.get());
                attributeInstance = player.getAttribute(ModAttributes.DEXTERITY.get());
            } else if (type == 2) {
                attribute = player.getAttribute(ModAttributes.STRENGTH.get());
                attributeInstance = player.getAttribute(ModAttributes.STRENGTH.get());
            } else if (type == 3) {
                attribute = player.getAttribute(ModAttributes.VITALITY.get());
                attributeInstance = player.getAttribute(ModAttributes.VITALITY.get());
            } else if (type == 4) {
                attribute = player.getAttribute(ModAttributes.ENDURANCE.get());
                attributeInstance = player.getAttribute(ModAttributes.ENDURANCE.get());
            }

            assert attributeInstance != null;
            attributeInstance.setBaseValue(amount);


            if (attribute != null) {
                // Remove any existing modifier with the same UUID to avoid stacking
                attribute.removeModifier(uuid);
            }
        });
    }



    public static void makeAttributeMod(int baseStat,
                                 double modifierPerStat, AttributeModifier.Operation attributeModOperation,
                                 ServerPlayer player, UUID uuid,
                                 Attribute attributeName) {
        player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {


            AttributeModifier modifier = new AttributeModifier(
                    uuid,
                    "boost from player stats",
                    modifierPerStat * stats.getStat(baseStat),
                    attributeModOperation
            );
            var attribute = player.getAttribute(attributeName);
            if (attribute != null) {
                // Remove any existing modifier with the same UUID to avoid stacking
                attribute.removeModifier(uuid);
                // Add the new modifier
                attribute.addPermanentModifier(modifier);
            }

        });
    }

    public static void increaseLevel(ServerPlayer player) {
        player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {

            float xp = stats.getStat(6);
            float level = stats.getStat(7);

            int xpNeeded = (int) (LevelUPCommonConfig.A_VALUE.get()*(level*level) + LevelUPCommonConfig.B_VALUE.get()*level + LevelUPCommonConfig.C_VALUE.get());
            float freepointsGiven = LevelUPCommonConfig.FREEPOINTS_PER_LEVEL.get();

            int maxLevel = LevelUPCommonConfig.LEVEL_CAP.get();

            if (xp >= xpNeeded && level < maxLevel) {
                //Level
                stats.addStat(7, 1);
                //XP
                stats.subStat(6, xpNeeded);
                //FreePoints
                stats.addStat(5, freepointsGiven);

                player.sendSystemMessage(Component.literal("LevelUP! You are now level " + ((int) stats.getStat(7)) + "!"));

                //If there is enough xp for another level
                xp = stats.getStat(6);
                level = stats.getStat(7);
                xpNeeded = (int) (0.2*(level*level) + 0.25*level + 10);

                //Update the scoreboard
                DisplayLevelScoreboard.updateLevel(player, level);

                if(xp >= xpNeeded) {
                    increaseLevel(player);
                }

            }
        });
    }

    public static void makeAttributeMods(ServerPlayer player, int type) {

        final UUID STATS_MOD_UUID = UUID.fromString("d7663cf7-09d3-48a9-9e22-bc0f495a96b8");

        //Constitution
        if(type == 0 || type == 99) {
            //HP Increase
            if(LevelUPCommonConfig.DO_HP.get()) {
                makeAttributeMod(8,
                        LevelUPCommonConfig.CONSTITUTION_HP.get(),
                        AttributeModifier.Operation.MULTIPLY_BASE, player,
                        STATS_MOD_UUID, Attributes.MAX_HEALTH);
            }
            //Max fall before fall dmg is in mod events
        }

        //Dexterity
        if(type == 1 || type == 99) {
            //Speed
            if(LevelUPCommonConfig.DO_SPEED.get()) {
                makeAttributeMod(9,
                        LevelUPCommonConfig.DEXTERITY_SPEED.get(),
                        AttributeModifier.Operation.MULTIPLY_BASE, player,
                        STATS_MOD_UUID, Attributes.MOVEMENT_SPEED);
            }
            //Attack Speed
            if(LevelUPCommonConfig.DO_ATTACK_SPEED.get()) {
                makeAttributeMod(9,
                        LevelUPCommonConfig.DEXTERITY_ATTACK_SPEED.get(),
                        AttributeModifier.Operation.MULTIPLY_BASE, player,
                        STATS_MOD_UUID, Attributes.ATTACK_SPEED);
            }
            //Swim Speed
            if(LevelUPCommonConfig.DO_SWIM_SPEED.get()) {
                makeAttributeMod(9,
                        LevelUPCommonConfig.DEXTERITY_SWIM_SPEED.get(),
                        AttributeModifier.Operation.MULTIPLY_BASE, player,
                        STATS_MOD_UUID, ForgeMod.SWIM_SPEED.get());
            }
        }


        //Strength
        if(type == 2 || type == 99) {

            //Damage
            if(LevelUPCommonConfig.DO_DAMAGE.get()) {
                makeAttributeMod(10,
                        LevelUPCommonConfig.STRENGTH_DAMAGE.get(),
                        AttributeModifier.Operation.MULTIPLY_BASE, player,
                        STATS_MOD_UUID, Attributes.ATTACK_DAMAGE);
            }
            //Knockback
            if(LevelUPCommonConfig.DO_KNOCKBACK.get()) {
                makeAttributeMod(10,
                        LevelUPCommonConfig.STRENGTH_KNOCKBACK.get(),
                        AttributeModifier.Operation.MULTIPLY_BASE, player,
                        STATS_MOD_UUID, Attributes.ATTACK_KNOCKBACK);
            }
        }

        //Vitality
        if(type == 3 || type == 99) {
            //Regen in events

            //Armor
            if(LevelUPCommonConfig.DO_ARMOR.get()) {
                makeAttributeMod(11,
                        LevelUPCommonConfig.VITALITY_ARMOR.get(),
                        AttributeModifier.Operation.MULTIPLY_BASE, player,
                        STATS_MOD_UUID, Attributes.ARMOR);
            }
        }

        //Endurance
        if(type == 4 || type == 99) {

            //Armour toughness
            if(LevelUPCommonConfig.DO_ARMOR_TOUGHNESS.get()) {
                makeAttributeMod(12,
                        LevelUPCommonConfig.ENDURANCE_ARMOR_TOUGHNESS.get(),
                        AttributeModifier.Operation.ADDITION, player,
                        STATS_MOD_UUID, Attributes.ARMOR_TOUGHNESS);
            }
            //Knockback resistance
            if(LevelUPCommonConfig.DO_KB_RES.get()) {
                makeAttributeMod(12,
                        LevelUPCommonConfig.ENDURANCE_KNOCKBACK_RESISTANCE.get(),
                        AttributeModifier.Operation.MULTIPLY_BASE, player,
                        STATS_MOD_UUID, Attributes.KNOCKBACK_RESISTANCE);
            }
        }
    }

    public static double[] getAttributeValues(ItemStack itemStack, Attribute attribute, EquipmentSlot slot) {
        Multimap<Attribute, AttributeModifier> attributes = itemStack.getAttributeModifiers(slot);

        for (Map.Entry<Attribute, AttributeModifier> entry : attributes.entries()) {
            if(entry.getKey().equals(attribute)) {
                //Give value of the attribute
                AttributeModifier.Operation operation = entry.getValue().getOperation();
                double op = 0;
                if (operation == AttributeModifier.Operation.ADDITION) {
                    op = 0;
                } else if (operation == AttributeModifier.Operation.MULTIPLY_BASE) {
                    op = 1;
                } else if (operation == AttributeModifier.Operation.MULTIPLY_TOTAL) {
                    op = 2;
                }
                return new double[]{entry.getValue().getAmount(), op};
            }
        }
        //If that attribute not on item, return 0.0
        return new double[]{0.0, 0}; //
    }

    public static void applyEnchantModifiers(Multimap<Attribute, AttributeModifier> modifiers, ItemStack item, int uuidSuffix) {
        Map<Attribute, Integer> attributeLevels = Map.of(
                ModAttributes.CONSTITUTION.get(), EnchantmentHelper.getEnchantments(item).getOrDefault(ModEnchants.FORTIFYING.get(), 0),
                ModAttributes.DEXTERITY.get(), EnchantmentHelper.getEnchantments(item).getOrDefault(ModEnchants.REFLEX.get(), 0),
                ModAttributes.STRENGTH.get(), EnchantmentHelper.getEnchantments(item).getOrDefault(ModEnchants.STRENGTHENING.get(), 0),
                ModAttributes.VITALITY.get(), EnchantmentHelper.getEnchantments(item).getOrDefault(ModEnchants.INNER_STRENGTH.get(), 0),
                ModAttributes.ENDURANCE.get(), EnchantmentHelper.getEnchantments(item).getOrDefault(ModEnchants.UNYIELDING.get(), 0)
        );

        for (Map.Entry<Attribute, Integer> entry : attributeLevels.entrySet()) {
            if (entry.getValue() > 0) {
                AttributeModifier modifier = new AttributeModifier(
                        UUID.fromString("2a1f767a-e617-44e9-9922-0000000000" + uuidSuffix),
                        "enchant " + entry.getKey().getDescriptionId() + " modifier",
                        entry.getValue() * LevelUPCommonConfig.STAT_INCREASE_PER_ENCHANT_LVL.get(),
                        AttributeModifier.Operation.ADDITION
                );
                modifiers.put(entry.getKey(), modifier);
            }
        }
    }
}
