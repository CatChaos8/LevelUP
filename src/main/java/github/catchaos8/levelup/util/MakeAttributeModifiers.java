package github.catchaos8.levelup.util;

import github.catchaos8.levelup.Config;
import github.catchaos8.levelup.LevelUP;
import github.catchaos8.levelup.registries.ModAttachments;
import github.catchaos8.levelup.registries.ModAttributes;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.List;

public class MakeAttributeModifiers {

    private static final Holder<Attribute>[] attributes = new Holder[]{
            ModAttributes.CONSTITUTION,
            ModAttributes.DEXTERITY,
            ModAttributes.STRENGTH,
            ModAttributes.VITALITY,
            ModAttributes.WISDOM,
            ModAttributes.INTELLIGENCE
    };

    public static void makeModifiers(ServerPlayer player) {
        int[] stats = player.getData(ModAttachments.LIMITED_STATS);

        applyStatBonuses(player, Config.CONSTITUTION_ATTRIBUTES.get(), stats[0], "constitution");
        applyStatBonuses(player, Config.DEXTERITY_ATTRIBUTES.get(), stats[1], "dexterity");
        applyStatBonuses(player, Config.STRENGTH_ATTRIBUTES.get(), stats[2], "strength");
        applyStatBonuses(player, Config.VITALITY_ATTRIBUTES.get(), stats[3], "vitality");
        applyStatBonuses(player, Config.WISDOM_ATTRIBUTES.get(), stats[4], "wisdom");
        applyStatBonuses(player, Config.INTELLIGENCE_ATTRIBUTES.get(), stats[5], "intelligence");

        for(int i = 0; i < attributes.length; i++) {
            String[] statNames = {"constitution", "dexterity", "strength", "vitality", "wisdom", "intelligence"};
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(LevelUP.MOD_ID, "base_" + statNames[i]);
            AttributeInstance instance = player.getAttribute(attributes[i]); // No Holder.direct() needed

            if (instance == null) {
                LevelUP.LOGGER.warn("Player doesn't have attribute: {}", statNames[i]);
                continue;
            }

            AttributeModifier modifier = new AttributeModifier(id, player.getData(ModAttachments.STATS)[i], AttributeModifier.Operation.ADD_VALUE);
            instance.removeModifier(id);
            instance.addPermanentModifier(modifier);
        }

    }


    private static void applyStatBonuses(ServerPlayer player, List<? extends String> bonusList, int statValue, String statName) {
        for (String entry : bonusList) {
            if (!validateAttributeEntry(entry)) continue;

            String[] parts = entry.split(",");
            String attributeId = parts[0].trim();
            String operation = parts[1].trim().toLowerCase();
            double amountPerPoint = Double.parseDouble(parts[2].trim());

            // Get the attribute from registry
            ResourceLocation attrLocation = ResourceLocation.parse(attributeId);
            Holder<Attribute> attribute = BuiltInRegistries.ATTRIBUTE.getHolder(attrLocation).orElse(null);

            if (attribute == null) {
                LevelUP.LOGGER.warn("Unknown attribute: {}", attributeId);
                continue;
            }


            AttributeInstance instance = player.getAttribute(attribute);
            if (instance == null) {
                LevelUP.LOGGER.warn("Player doesn't have attribute: {}", attributeId);
                continue;
            }

            // Remove old modifier
            ResourceLocation modifierId = ResourceLocation.fromNamespaceAndPath(LevelUP.MOD_ID, statName + "_" + attributeId.replace(":", "_"));
            instance.removeModifier(modifierId);

            // Add new modifier with correct operation
            double totalBonus = amountPerPoint * statValue;
            AttributeModifier.Operation op = operation.equals("multiplication")
                    ? AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                    : AttributeModifier.Operation.ADD_VALUE;

            AttributeModifier modifier = new AttributeModifier(modifierId, totalBonus, op);
            instance.addPermanentModifier(modifier);
        }
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

