package github.catchaos8.levelup.mixin;

import github.catchaos8.levelup.LevelUP;
import github.catchaos8.levelup.registries.ModAttributes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Unique
    private ServerPlayer levelUP$player;
    @Unique
    private RandomSource levelUP$random;

    @Inject(method = "hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)V", at = @At("HEAD"))
    private void levelUP$getVars(int amount, LivingEntity entity, EquipmentSlot slot, CallbackInfo ci) {
        if(entity instanceof ServerPlayer player) {
            this.levelUP$player = player;
            this.levelUP$random = player.getRandom();
        }
    }

    @ModifyVariable(method = "hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)V",
    at = @At("HEAD"),
    ordinal = 0,
    argsOnly = true)
    private int levelUP$reduceDurabilityDamage(int value) {
        return levelUP$calculateDurabilityDamage(value, this.levelUP$random, levelUP$player);
    }


    @Unique
    private int levelUP$calculateDurabilityDamage(int amount, RandomSource random, @Nullable ServerPlayer player) {
        if(player == null) return amount;

        int attributeValue = (int) player.getAttributeValue(ModAttributes.ITEM_DURABILITY_DAMAGE_REDUCTION);
        double chance = Math.pow(0.99, attributeValue);
        int newAmount = (int) Math.ceil(amount*chance);

        if(random.nextDouble() > chance && newAmount > 0) {
            newAmount--;
        }
        return newAmount;
    }
}
