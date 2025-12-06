package github.catchaos8.levelup.mixin;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import github.catchaos8.levelup.config.LevelUPCommonConfig;
import github.catchaos8.levelup.stats.PlayerStatsProvider;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

import java.util.Random;

import static github.catchaos8.levelup.lib.SetStats.applyEnchantModifiers;


@Mixin(value = ItemStack.class, priority = 1500)
public class ItemStackMixin {

    //Gets the player and randomsourse to be used
    @Unique private ServerPlayer levelUP$player;
    @Unique private RandomSource levelUP$random;

    @Inject(method = "hurt", at = @At("HEAD"))
    private void levelup$GetVars(
            int amount, RandomSource random, @Nullable ServerPlayer player,
            CallbackInfoReturnable<Boolean> cir
    ) {
        this.levelUP$player = player;
        this.levelUP$random = random;
    }
    //Run the function to get the things
    @ModifyVariable(method = "hurt", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private int levelUP$reduceDurabilityDamage(int value) {
        return levelUP$calculateDurabilityDamage(value, this.levelUP$random, this.levelUP$player);
    }



    @Unique
    private int levelUP$calculateDurabilityDamage(int amount, RandomSource random, @Nullable ServerPlayer player) {
        if (player == null) return amount;
        if (!LevelUPCommonConfig.DO_DURABILITY_REDUCTION.get()) return amount;

        //check if player has int, otherwise return normal amount
        return player.getCapability(PlayerStatsProvider.PLAYER_STATS).map(stats -> {
            //Get int
            int intelligence = (int) stats.getLimitedStat(6);
            double chance = Math.pow(1 - LevelUPCommonConfig.INTELLIGENCE_DURABILITY_DAMAGE.get(), intelligence);

            int newAmount = (int) Math.ceil(amount*chance);

            if(random.nextDouble() > chance && newAmount > 0) {
                newAmount--;
            }

            return Math.max(newAmount, 0);
        }).orElse(amount);
    }

    @Inject(method = "getAttributeModifiers", at = @At("RETURN"), cancellable = true)
    private void addCustomAttributes(EquipmentSlot slot, CallbackInfoReturnable<Multimap<Attribute, AttributeModifier>> cir) {
        //Gets item
        ItemStack item = (ItemStack) (Object) this;
        Multimap<Attribute, AttributeModifier> original = cir.getReturnValue();
        Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create(original);

        //Makes uuid suffix so that each armor piece has a separate uuid
        int uuidSuffix;

        if (item.getItem() instanceof ArmorItem armor) {
            uuidSuffix = switch (armor.getEquipmentSlot()) {
                case HEAD -> 1;     //Heads
                case CHEST -> 2;    //Shoulders
                case LEGS -> 3;     //Knees
                case FEET -> 4;     //Toes
                default -> 0;       //Else default to 0
            };

            if (armor.getEquipmentSlot() == slot) {
                applyEnchantModifiers(modifiers, item, uuidSuffix);
            }

        } else if ((item.getItem() instanceof SwordItem || item.getItem() instanceof AxeItem) && slot == EquipmentSlot.MAINHAND) {
            uuidSuffix = item.getItem() instanceof SwordItem ? 5 : 6;
            applyEnchantModifiers(modifiers, item, uuidSuffix);
        }

        cir.setReturnValue(modifiers);
    }


}
