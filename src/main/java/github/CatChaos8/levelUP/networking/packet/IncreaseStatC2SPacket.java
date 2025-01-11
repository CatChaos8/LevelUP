package github.catchaos8.levelup.networking.packet;

import github.catchaos8.levelup.config.LevelUPCommonConfig;
import github.catchaos8.levelup.networking.ModNetwork;
import github.catchaos8.levelup.stats.PlayerStatsProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class IncreaseStatC2SPacket {


    private static final UUID STATS_MOD_UUID = UUID.fromString("d7663cf7-09d3-48a9-9e22-bc0f495a96b8");

    private final int type;
    private final int amount;


    public IncreaseStatC2SPacket(int type, int amount) {
        this.type = Math.max(0, Math.min(12, type));

        this.amount = Math.max(0, amount);
    }

    public IncreaseStatC2SPacket(FriendlyByteBuf buf) {
        this.type = buf.readInt();

        this.amount = buf.readInt();

    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(type);

        buf.writeInt(amount);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {


            //This is the server side part I think
            ServerPlayer player = context.getSender();

            if(type <= 4) {
                //Check if freestats is > 0
                assert player != null;
                player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {

                    if (stats.getStat(5) >= amount) {
                        //Increase Stats
                        if(stats.getStat(type) == stats.getStat(type + 8)) {
                            stats.addStat(type + 8, amount);
                        }

                        stats.addStat(type, amount);

                        //Sub freepoints
                        stats.subStat(5, amount);
                        //Sync
                        ModNetwork.sendToPlayer(new StatDataSyncS2CPacket(stats.getStatArr()), player);
                        //Set Modifier to attributes based on stats
                        if(type == 0) { //Constitution
                            //HP Increase
                            makeAttributeMod(8,"Health",
                                    LevelUPCommonConfig.CONSTITUTION_HP.get(),
                                    AttributeModifier.Operation.MULTIPLY_BASE, player,
                                    STATS_MOD_UUID, Attributes.MAX_HEALTH);
                            //Max fall before fall dmg is in mod events

                        } else if(type == 1) { //Dexterity

                            makeAttributeMod(9, "Speed",
                                    LevelUPCommonConfig.DEXTERITY_SPEED.get(),
                                    AttributeModifier.Operation.MULTIPLY_BASE, player,
                                    STATS_MOD_UUID, Attributes.MOVEMENT_SPEED);

                            makeAttributeMod(9, "Swim Speed",
                                    LevelUPCommonConfig.DEXTERITY_SWIM_SPEED.get(),
                                    AttributeModifier.Operation.MULTIPLY_BASE, player,
                                    STATS_MOD_UUID, ForgeMod.SWIM_SPEED.get());


                        } else if(type == 2) { //Strength

                            //Damage
                            makeAttributeMod(10, "Damage",
                                    LevelUPCommonConfig.STRENGTH_DAMAGE.get(),
                                    AttributeModifier.Operation.MULTIPLY_BASE, player,
                                    STATS_MOD_UUID, Attributes.ATTACK_DAMAGE);

                            //Knockback
                            makeAttributeMod(10, "Knockback",
                                    LevelUPCommonConfig.STRENGTH_KNOCKBACK.get(),
                                    AttributeModifier.Operation.MULTIPLY_BASE, player,
                                    STATS_MOD_UUID, Attributes.ATTACK_KNOCKBACK);


                        } else if(type == 3) { //Vitality

                            //Regen somewhere else

                            //Armor
                            makeAttributeMod(11, "Armour",
                                    LevelUPCommonConfig.VITALITY_ARMOR.get(),
                                    AttributeModifier.Operation.MULTIPLY_BASE, player,
                                    STATS_MOD_UUID, Attributes.ARMOR);


                        } else if(type == 4) { //Endurance

                            //Armour toughness
                            makeAttributeMod(12, "Armour Toughness",
                                    LevelUPCommonConfig.ENDURANCE_ARMOR_TOUGHNESS.get(),
                                    AttributeModifier.Operation.ADDITION, player,
                                    STATS_MOD_UUID, Attributes.ARMOR_TOUGHNESS);

                            //Knockback resistance
                            makeAttributeMod(12, "Knockback Resistance",
                                    LevelUPCommonConfig.ENDURANCE_KNOCKBACK_RESISTANCE.get(),
                                    AttributeModifier.Operation.MULTIPLY_BASE, player,
                                    STATS_MOD_UUID, Attributes.KNOCKBACK_RESISTANCE);


                        }

                    } else {
                        //say not enough points

                        //Sync
                        ModNetwork.sendToPlayer(new StatDataSyncS2CPacket(stats.getStatArr()), player);
                    }
                });
            } else if (type <= 7) {


                assert player != null;
                player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
                    if (type == 6) {
                        stats.addStat(type, amount);

                        int level = stats.getStat(7);
                        int xp = stats.getStat(6);

                        int xpNeeded = (int) (0.2*(level*level) + 0.25*level + 10);
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
                        ModNetwork.sendToPlayer(new StatDataSyncS2CPacket(stats.getStatArr()), player);
                    } else {
                        stats.addStat(type, amount);

                        //Sync
                        ModNetwork.sendToPlayer(new StatDataSyncS2CPacket(stats.getStatArr()), player);
                    }
                });

            } else { //Limited Stats
                assert player != null;
                player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
                    stats.setStat(type, amount);
                    ModNetwork.sendToPlayer(new StatDataSyncS2CPacket(stats.getStatArr()), player);

                    if(type == 8) { //Constitution
                        //HP Increase
                        makeAttributeMod(8,"Health",
                                LevelUPCommonConfig.CONSTITUTION_HP.get(),
                                AttributeModifier.Operation.MULTIPLY_BASE, player,
                                STATS_MOD_UUID, Attributes.MAX_HEALTH);
                        //Max fall before fall dmg is in mod events

                    } else if(type == 9) { //Dexterity

                        makeAttributeMod(9, "Speed",
                                LevelUPCommonConfig.DEXTERITY_SPEED.get(),
                                AttributeModifier.Operation.MULTIPLY_BASE, player,
                                STATS_MOD_UUID, Attributes.MOVEMENT_SPEED);

                        makeAttributeMod(9, "Swim Speed",
                                LevelUPCommonConfig.DEXTERITY_SWIM_SPEED.get(),
                                AttributeModifier.Operation.MULTIPLY_BASE, player,
                                STATS_MOD_UUID, ForgeMod.SWIM_SPEED.get());


                    } else if(type == 10) { //Strength

                        //Damage
                        makeAttributeMod(10, "Damage",
                                LevelUPCommonConfig.STRENGTH_DAMAGE.get(),
                                AttributeModifier.Operation.MULTIPLY_BASE, player,
                                STATS_MOD_UUID, Attributes.ATTACK_DAMAGE);

                        //Knockback
                        makeAttributeMod(10, "Knockback",
                                LevelUPCommonConfig.STRENGTH_KNOCKBACK.get(),
                                AttributeModifier.Operation.MULTIPLY_BASE, player,
                                STATS_MOD_UUID, Attributes.ATTACK_KNOCKBACK);


                    } else if(type == 11) { //Vitality

                        //Regen somewhere else

                        //Armor
                        makeAttributeMod(11, "Armour",
                                LevelUPCommonConfig.VITALITY_ARMOR.get(),
                                AttributeModifier.Operation.MULTIPLY_BASE, player,
                                STATS_MOD_UUID, Attributes.ARMOR);


                    } else if(type == 12) { //Endurance

                        //Armour toughness
                        makeAttributeMod(12, "Armour Toughness",
                                LevelUPCommonConfig.ENDURANCE_ARMOR_TOUGHNESS.get(),
                                AttributeModifier.Operation.ADDITION, player,
                                STATS_MOD_UUID, Attributes.ARMOR_TOUGHNESS);

                        //Knockback resistance
                        makeAttributeMod(12, "Knockback Resistance",
                                LevelUPCommonConfig.ENDURANCE_KNOCKBACK_RESISTANCE.get(),
                                AttributeModifier.Operation.MULTIPLY_BASE, player,
                                STATS_MOD_UUID, Attributes.KNOCKBACK_RESISTANCE);
                    }
                });
            }

        });
    }


    public void makeAttributeMod(int baseStat, String attributeItModifies,
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
