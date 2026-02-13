package github.catchaos8.levelup.compat;

import com.hollingsworth.arsnouveau.api.perk.PerkAttributes;
import github.catchaos8.levelup.config.LevelUPCommonConfig;
import github.catchaos8.levelup.stats.PlayerStatsProvider;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.UUID;

public class ArsCompat {

    public static void init() {

    }
    public static void makeArsAttributeMods(ServerPlayer player, int type) {
        final UUID STATS_MOD_UUID = UUID.fromString("d7663cf7-09d3-48a9-9e22-bc0f495a96b8");

        //wisdom
        player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
            if(type == 5 || type == 99) {
                if(LevelUPCommonConfig.DO_ARS_MANA_REGEN_BOOST.get()) {
                    makeAttributeMod(5,
                            LevelUPCommonConfig.WISDOM_ARS_MANA_REGEN_BOOST.get(),
                            AttributeModifier.Operation.MULTIPLY_TOTAL,
                            player, STATS_MOD_UUID,
                            getManaRegenAttribute());
                }
            }
            if(type == 6 || type == 99) {
                if(LevelUPCommonConfig.DO_ARS_MAX_MANA_BOOST.get()) {
                    makeAttributeMod(6,
                            LevelUPCommonConfig.INTELLIGENCE_ARS_MAX_MANA_BOOST.get(),
                            AttributeModifier.Operation.MULTIPLY_TOTAL,
                            player, STATS_MOD_UUID,
                            getManaAttribute());
                }
            }

        });
    }

    private static Attribute getManaAttribute() {
        try {
            return PerkAttributes.MAX_MANA.get();
        } catch (Throwable t) {
            return null;
        }
    }
    private static Attribute getManaRegenAttribute() {
        try {
            return PerkAttributes.MANA_REGEN_BONUS.get();
        } catch (Throwable t) {
            return null;
        }
    }
    private static Attribute getArsSpellDamage() {
        try {
            return PerkAttributes.SPELL_DAMAGE_BONUS.get();
        } catch(Throwable t) {
            return null;
        }
    }

    public static void makeAttributeMod(int baseStat,
                                        double modifierPerStat, AttributeModifier.Operation attributeModOperation,
                                        ServerPlayer player, UUID uuid,
                                        net.minecraft.world.entity.ai.attributes.Attribute attributeName) {
        player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {


            AttributeModifier modifier = new AttributeModifier(
                    uuid,
                    "boost from player stats",
                    modifierPerStat * stats.getLimitedStat(baseStat),
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
