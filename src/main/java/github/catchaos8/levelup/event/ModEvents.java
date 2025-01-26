package github.catchaos8.levelup.event;

import github.catchaos8.levelup.LevelUP;
import github.catchaos8.levelup.commands.get.*;
import github.catchaos8.levelup.commands.set.*;
import github.catchaos8.levelup.config.LevelUPCommonConfig;
import github.catchaos8.levelup.networking.ModNetwork;
import github.catchaos8.levelup.networking.DisplayLevelScoreboard;
import github.catchaos8.levelup.networking.packet.StatDataSyncS2CPacket;
import github.catchaos8.levelup.stats.PlayerStats;
import github.catchaos8.levelup.stats.PlayerStatsProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

import java.util.UUID;


@Mod.EventBusSubscriber(modid = LevelUP.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void commandsRegister(RegisterCommandsEvent event) {
        new GetStatsCommand(event.getDispatcher());
        new GetConCommand(event.getDispatcher());
        new GetDexCommand(event.getDispatcher());
        new GetStrCommand(event.getDispatcher());
        new GetVitCommand(event.getDispatcher());
        new GetEndCommand(event.getDispatcher());

        new GetAllStatsCommand(event.getDispatcher());

        new GetFreePointsCommand(event.getDispatcher());
        new GetClassXPCommand(event.getDispatcher());
        new GetClassLevelCommand(event.getDispatcher());

        new SetStatsCommand(event.getDispatcher());
        new SetConCommand(event.getDispatcher());
        new SetDexCommand(event.getDispatcher());
        new SetStrCommand(event.getDispatcher());
        new SetVitCommand(event.getDispatcher());
        new SetEndCommand(event.getDispatcher());

        new SetAllStatsCommand(event.getDispatcher());


        new SetFreePointsCommand(event.getDispatcher());
        new SetClassXPCommand(event.getDispatcher());
        new SetClassLevelCommand(event.getDispatcher());


        ConfigCommand.register(event.getDispatcher());
    }


    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            if(!event.getObject().getCapability(PlayerStatsProvider.PLAYER_STATS).isPresent()) {
                event.addCapability(new ResourceLocation(LevelUP.MOD_ID, "levelup"), new PlayerStatsProvider());
            }

        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {

        Player original = event.getOriginal();
        original.revive();
        Player player = event.getEntity();

        final UUID STATS_MOD_UUID = UUID.fromString("d7663cf7-09d3-48a9-9e22-bc0f495a96b8");

        event.getOriginal().getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(oldStore -> {
                event.getEntity().getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(newStore -> {

                    //Reset XP if it was death,

                    newStore.copyFrom(oldStore);if(event.isWasDeath()) {
                        newStore.setStat(6, 0);
                    }

                });
         });

        if(event.getEntity() instanceof ServerPlayer playera) {
            player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
                //Set Modifier to attributes based on stats
                //Constitution
                //HP Increase
                makeAttributeMod(8, "Health",
                        LevelUPCommonConfig.CONSTITUTION_HP.get(),
                        AttributeModifier.Operation.MULTIPLY_BASE, playera,
                        STATS_MOD_UUID, Attributes.MAX_HEALTH);
                //Max fall before fall dmg is in mod events

                //Dexterity

                makeAttributeMod(9, "Speed",
                        LevelUPCommonConfig.DEXTERITY_SPEED.get(),
                        AttributeModifier.Operation.MULTIPLY_BASE, playera,
                        STATS_MOD_UUID, Attributes.MOVEMENT_SPEED);

                makeAttributeMod(9, "Swim Speed",
                        LevelUPCommonConfig.DEXTERITY_SWIM_SPEED.get(),
                        AttributeModifier.Operation.MULTIPLY_BASE, playera,
                        STATS_MOD_UUID, ForgeMod.SWIM_SPEED.get());


                //Strength

                //Damage
                makeAttributeMod(10, "Damage",
                        LevelUPCommonConfig.STRENGTH_DAMAGE.get(),
                        AttributeModifier.Operation.MULTIPLY_BASE, playera,
                        STATS_MOD_UUID, Attributes.ATTACK_DAMAGE);

                //Knockback
                makeAttributeMod(10, "Knockback",
                        LevelUPCommonConfig.STRENGTH_KNOCKBACK.get(),
                        AttributeModifier.Operation.MULTIPLY_BASE, playera,
                        STATS_MOD_UUID, Attributes.ATTACK_KNOCKBACK);


                //Vitality

                //Regen somewhere else

                //Armor
                makeAttributeMod(11, "Armour",
                        LevelUPCommonConfig.VITALITY_ARMOR.get(),
                        AttributeModifier.Operation.MULTIPLY_BASE, playera,
                        STATS_MOD_UUID, Attributes.ARMOR);


                //Endurance

                //Armour toughness
                makeAttributeMod(12, "Armour Toughness",
                        LevelUPCommonConfig.ENDURANCE_ARMOR_TOUGHNESS.get(),
                        AttributeModifier.Operation.ADDITION, playera,
                        STATS_MOD_UUID, Attributes.ARMOR_TOUGHNESS);

                //Knockback resistance
                makeAttributeMod(12, "Knockback Resistance",
                        LevelUPCommonConfig.ENDURANCE_KNOCKBACK_RESISTANCE.get(),
                        AttributeModifier.Operation.MULTIPLY_BASE, playera,
                        STATS_MOD_UUID, Attributes.KNOCKBACK_RESISTANCE);

                ModNetwork.sendToPlayer(new StatDataSyncS2CPacket(stats.getStatArr()), playera);
                playera.heal(99999);
            });
        }
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PlayerStats.class);
    }


    @SubscribeEvent
    public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
        if(!event.getLevel().isClientSide()) {
            if(event.getEntity() instanceof ServerPlayer player) {
                player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {

                    int level = stats.getStat(7);
                    DisplayLevelScoreboard.updateLevel(player, level);
                    ModNetwork.sendToPlayer(new StatDataSyncS2CPacket(stats.getStatArr()), player);

                });
            }
        }
    }


    @SubscribeEvent
    public static void onPlayerPickXP(PlayerXpEvent event) {
        if (event instanceof PlayerXpEvent.PickupXp pickupXpEvent) {
            Player player = pickupXpEvent.getEntity();
            ExperienceOrb orb = pickupXpEvent.getOrb();
            int xpAmount = orb.getValue();
            // server-side execution
            if (!player.level().isClientSide && player instanceof ServerPlayer serverPlayer) {
                player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
                    stats.addStat(6, xpAmount);

                    int level = stats.getStat(7);
                    int xp = stats.getStat(6);

                    int xpNeeded = (int) (0.2 * (level * level) + 0.25 * level + 10);
                    int freepointsGiven = LevelUPCommonConfig.FREEPOINTS_PER_LEVEL.get();

                    int maxLevel = LevelUPCommonConfig.LEVEL_CAP.get();

                    if (xp >= xpNeeded && level < maxLevel) {
                        //Level
                        stats.addStat(7, 1);
                        //XP
                        stats.subStat(6, xpNeeded);
                        //FreePoints
                        stats.addStat(5, freepointsGiven);

                        player.sendSystemMessage(Component.literal("LevelUP! You are now level " + stats.getStat(7) + "!"));

                    }

                    //Sync
                    ModNetwork.sendToPlayer(new StatDataSyncS2CPacket(stats.getStatArr()), serverPlayer);
                });
            }
        }
    }

    //Vitality
    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingTickEvent event) {
        if (event.getEntity() instanceof Player player) {
            player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
                int vitality = stats.getStat(11);
                //Regen
                float regenMulti = LevelUPCommonConfig.VITALITY_HP_REGEN.get();
                if (vitality > 0) {
                    if(!player.level().getLevelData().isHardcore()) {
                        player.heal(vitality * regenMulti);
                    } else {
                        player.heal(vitality*regenMulti/3);
                    }
                }
            });
        }
    }

    //Constitution max height before fall
    @SubscribeEvent
    public static void onLivingFall(LivingFallEvent event) {
        if (event.getEntity() instanceof Player player) {
            player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
                if(event.getDistance() > 0) {
                    int constitution = stats.getStat(8);
                    float fallDMGReduction = LevelUPCommonConfig.CONSTITUTION_FALL_DAMAGE_REDUCTION.get();

                    float currentDamage = event.getDamageMultiplier() * event.getDistance();

                    // Apply flat reduction
                    float newDamage = Math.max(0, currentDamage - fallDMGReduction * constitution);
                    if (!player.level().isClientSide) {
                        if (constitution > 0) {
                            event.setDamageMultiplier(newDamage / event.getDistance());
                        }
                    }
                }
            });
        }
    }


    @SubscribeEvent
    public static void onServerStart(ServerStartingEvent event) {
        DisplayLevelScoreboard.initializeScoreboard(event.getServer().getScoreboard());
    }

    public static void makeAttributeMod(int baseStat, String attributeItModifies,
                                 float modifierPerStat, AttributeModifier.Operation attributeModOperation,
                                 ServerPlayer player, UUID uuid,
                                 Attribute attributeName) {
        player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {


            AttributeModifier modifier = new AttributeModifier(
                    uuid,
                    attributeItModifies + " boost",
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
