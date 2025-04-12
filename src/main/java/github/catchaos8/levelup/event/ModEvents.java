package github.catchaos8.levelup.event;

import github.catchaos8.levelup.LevelUP;
import github.catchaos8.levelup.attributes.ModAttributes;
import github.catchaos8.levelup.commands.get.*;
import github.catchaos8.levelup.commands.set.*;
import github.catchaos8.levelup.config.LevelUPCommonConfig;
import github.catchaos8.levelup.lib.DisplayLevelScoreboard;
import github.catchaos8.levelup.networking.ModNetwork;
import github.catchaos8.levelup.networking.packet.StatDataSyncS2CPacket;
import github.catchaos8.levelup.stats.PlayerStats;
import github.catchaos8.levelup.stats.PlayerStatsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

import java.util.List;

import static github.catchaos8.levelup.lib.DisplayLevelScoreboard.setName;
import static github.catchaos8.levelup.lib.SetStats.*;

public class ModEvents {

    @Mod.EventBusSubscriber(modid = LevelUP.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ForgeEvents {
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
                if (!event.getObject().getCapability(PlayerStatsProvider.PLAYER_STATS).isPresent()) {
                    event.addCapability(new ResourceLocation(LevelUP.MOD_ID, "levelup"), new PlayerStatsProvider());
                }

            }
        }

        @SubscribeEvent
        public static void onPlayerCloned(PlayerEvent.Clone event) {

            Player original = event.getOriginal();
            original.revive();
            Player player = event.getEntity();


            event.getOriginal().getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(oldStore -> {
                //>:(
                event.getEntity().getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(newStore -> {

                    //Reset XP if it was death,

                    newStore.copyFrom(oldStore);
                    if (event.isWasDeath()) {
                        newStore.setStat(6, 0);
                    }

                });
            });

            if (event.getEntity() instanceof ServerPlayer serverPlayer) {
                player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
                    //Set Modifier to attributes based on stats
                    setAttributeStat(stats.getStat(0), 0,
                            serverPlayer);

                    setAttributeStat( stats.getStat(1), 1,
                            serverPlayer);

                    setAttributeStat(stats.getStat(2), 2,
                            serverPlayer);

                    setAttributeStat( stats.getStat(3), 3,
                            serverPlayer);

                    setAttributeStat(stats.getStat(4), 4,
                            serverPlayer);

                    //Checking to make sure that limited stat isn't above max due to items before death
                    if(serverPlayer.getAttributeValue(ModAttributes.CONSTITUTION.get()) < stats.getStat(8)) {
                        stats.setStat(8,(int) serverPlayer.getAttributeValue(ModAttributes.CONSTITUTION.get()));
                    }
                    if(serverPlayer.getAttributeValue(ModAttributes.DEXTERITY.get()) < stats.getStat(9)) {
                        stats.setStat(9,(int) serverPlayer.getAttributeValue(ModAttributes.DEXTERITY.get()));
                    }
                    if(serverPlayer.getAttributeValue(ModAttributes.STRENGTH.get()) < stats.getStat(10)) {
                        stats.setStat(10,(int) serverPlayer.getAttributeValue(ModAttributes.STRENGTH.get()));
                    }
                    if(serverPlayer.getAttributeValue(ModAttributes.VITALITY.get()) < stats.getStat(11)) {
                        stats.setStat(11,(int) serverPlayer.getAttributeValue(ModAttributes.VITALITY.get()));
                    }
                    if(serverPlayer.getAttributeValue(ModAttributes.ENDURANCE.get()) < stats.getStat(12)) {
                        stats.setStat(12,(int) serverPlayer.getAttributeValue(ModAttributes.ENDURANCE.get()));
                    }

                    makeAttributeMods(serverPlayer, 99);

                    ModNetwork.sendToPlayer(new StatDataSyncS2CPacket(stats.getStatArr()), serverPlayer);

                    //Heal
                    serverPlayer.heal((float) serverPlayer.getAttributeValue(Attributes.MAX_HEALTH));
                });
            }
        }

        @SubscribeEvent
        public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
            event.register(PlayerStats.class);
        }


        @SubscribeEvent
        public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
            if (!event.getLevel().isClientSide()) {
                if (event.getEntity() instanceof ServerPlayer player) {
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

                        //see if player can levelup
                        increaseLevel(serverPlayer);

                        //Sync
                        ModNetwork.sendToPlayer(new StatDataSyncS2CPacket(stats.getStatArr()), serverPlayer);
                    });
                }
            }
        }

        //Vitality/update on attribute stuff
        @SubscribeEvent
        public static void onLivingUpdate(LivingEvent.LivingTickEvent event) {
            if (event.getEntity() instanceof Player player) {
                player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
                    if(LevelUPCommonConfig.DO_HP_REGEN.get()) {
                        //Do the vit stuff
                        int vitality = stats.getStat(11);
                        //Regen
                        double regenMulti = LevelUPCommonConfig.VITALITY_HP_REGEN.get();
                        if (vitality > 0) {
                            if (!player.level().getLevelData().isHardcore()) {
                                player.heal((float) (vitality * regenMulti));
                            } else {
                                player.heal((float) (vitality * regenMulti / LevelUPCommonConfig.VITALITY_HARDCORE_NERF.get()));
                            }
                        }
                    }
                });
            }
        }

        //Constitution max height before fall
        @SubscribeEvent
        public static void onLivingFall(LivingFallEvent event) {
            if(LevelUPCommonConfig.DO_FALL_DAMAGE_REDUCTION.get()) {
                if (event.getEntity() instanceof Player player) {
                    player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
                        if (event.getDistance() > 0) {
                            int constitution = stats.getStat(8);

                            double fallDMGReductionDouble = LevelUPCommonConfig.CONSTITUTION_FALL_DAMAGE_REDUCTION.get();
                            float fallDMGReduction = (float) fallDMGReductionDouble;

                            float currentDamage = event.getDamageMultiplier() * event.getDistance();

                            // Apply flat reduction
                            float newDamage = Math.max(0, currentDamage - fallDMGReduction * constitution - 3);
                            if (!player.level().isClientSide) {
                                if (constitution > 0) {
                                    event.setDamageMultiplier(newDamage / event.getDistance());
                                }
                            }
                        }
                    });
                }
            }
        }


        @SubscribeEvent
        public static void onServerStart(ServerStartingEvent event) {
            DisplayLevelScoreboard.initializeScoreboard(event.getServer().getScoreboard());
        }

        @SubscribeEvent
        public static void setTab(PlayerEvent.TabListNameFormat event) {

            if( event.getEntity() instanceof ServerPlayer player) {
                player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
                    if(LevelUPCommonConfig.DISPLAY_LEVEL_BESIDE_NAME_IN_PLAYER_LIST.get()) {
                        setName(event);
                    }
                });
            }


        }

        //Checking if player equips anything that changes their attributes so that it changes automatically
        @SubscribeEvent
        public static void onEquipmentChange(LivingEquipmentChangeEvent event) {

            if (event.getEntity() instanceof ServerPlayer player) {
                player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
                    EquipmentSlot slot = event.getSlot();
                    ItemStack newItem = event.getFrom();
                    ItemStack oldItem = event.getTo();

                    // Make the thing to show the limited stat, the attribute, and the base stat for each stat type
                    record StatRecord(int index, Attribute attribute, int modIndex) {}

                    List<StatRecord> statRecordList = List.of(
                            new StatRecord(8, ModAttributes.CONSTITUTION.get(), 0),
                            new StatRecord(9, ModAttributes.DEXTERITY.get(), 1),
                            new StatRecord(10, ModAttributes.STRENGTH.get(), 2),
                            new StatRecord(11, ModAttributes.VITALITY.get(), 3),
                            new StatRecord(12, ModAttributes.ENDURANCE.get(), 4)
                    );

                    for (StatRecord info : statRecordList) {
                        AttributeInstance attrInstance = player.getAttribute(info.attribute());
                        if (attrInstance == null) continue; //For eff so that it checks if u have a 0 in a stat

                        int current = (int) attrInstance.getValue(); //Gets total stats(base + items)
                        int limited = stats.getStat(info.index());   //Gets limited stats

                        double[] newVals = getAttributeValues(oldItem, info.attribute(), slot); //Gets attributes the items give in the old items and new ones
                        double[] oldVals = getAttributeValues(newItem, info.attribute(), slot);

                        if (oldVals[1] == 0 && newVals[1] == 0) { //If the attribute increase is additive
                            int increase = (int) (newVals[0] - oldVals[0]);
                            //If it increases
                            if (increase != 0) {
                                if (current == limited || (increase < 0 && current + increase < limited)) { //To check if it increases or if the increase is less than 0 and u have less than the increase or whatever
                                    stats.setStat(info.index(), current + increase);
                                    makeAttributeMods(player, info.modIndex());
                                }
                            }
                        }

                        //Sync w/ client
                        ModNetwork.sendToPlayer(new StatDataSyncS2CPacket(stats.getStatArr()), player);
                    }
                });

            }
        }


    }

    @Mod.EventBusSubscriber(modid = LevelUP.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEventBus {

        @SubscribeEvent
        public static void addAttributes(EntityAttributeModificationEvent event) {
                //Con flat
            for (var type : event.getTypes()) {
                if (type.getCategory() == MobCategory.MONSTER) {
                    event.add(type, ModAttributes.CONSTITUTION.get(), 0.0);
                    event.add(type, ModAttributes.DEXTERITY.get(), 0.0);
                    event.add(type, ModAttributes.STRENGTH.get(), 0.0);
                    event.add(type, ModAttributes.VITALITY.get(), 0.0);
                    event.add(type, ModAttributes.ENDURANCE.get(), 0.0);
                }
            }
            event.add(EntityType.PLAYER, ModAttributes.CONSTITUTION.get(), 0.0);
            event.add(EntityType.PLAYER, ModAttributes.DEXTERITY.get(), 0.0);
            event.add(EntityType.PLAYER, ModAttributes.STRENGTH.get(), 0.0);
            event.add(EntityType.PLAYER, ModAttributes.VITALITY.get(), 0.0);
            event.add(EntityType.PLAYER, ModAttributes.ENDURANCE.get(), 0.0);
        }
    }
}
