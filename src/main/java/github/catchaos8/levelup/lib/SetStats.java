package github.catchaos8.levelup.lib;

import github.catchaos8.levelup.attributes.ModAttributes;
import github.catchaos8.levelup.stats.PlayerStatsProvider;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.UUID;

public class SetStats {
    public static void setAttributeStat(int amount, int type, ServerPlayer player){

        UUID uuid = UUID.fromString("d7663cf7-09d3-48a9-9e22-bc0f495a96b8");

        player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
            AttributeModifier modifier = new AttributeModifier(
                    uuid,
                    "stat base",
                    amount,
                    AttributeModifier.Operation.ADDITION
            );

            AttributeInstance attribute = null;

            if(type == 0){
                attribute = player.getAttribute(ModAttributes.CONSTITUTION.get());
            }else if(type == 1) {
                attribute = player.getAttribute(ModAttributes.DEXTERITY.get());
            } else if (type == 2) {
                attribute = player.getAttribute(ModAttributes.STRENGTH.get());
            } else if (type == 3) {
                attribute = player.getAttribute(ModAttributes.VITALITY.get());
            } else if (type == 4) {
                attribute = player.getAttribute(ModAttributes.ENDURANCE.get());
            }

            if (attribute != null) {
                // Remove any existing modifier with the same UUID to avoid stacking
                attribute.removeModifier(uuid);
                // Add the new modifier
                attribute.addPermanentModifier(modifier);
            }
        });
    }



    public static void makeAttributeMod(int baseStat,
                                 float modifierPerStat, AttributeModifier.Operation attributeModOperation,
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
}
