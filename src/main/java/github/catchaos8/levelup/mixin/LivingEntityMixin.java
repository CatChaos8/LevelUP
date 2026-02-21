package github.catchaos8.levelup.mixin;


import github.catchaos8.levelup.registries.ModAttributes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Unique
    private boolean levelUPNeoforge_1_21_1$effectReentryGuard = false;

    @Inject(method="addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z", at = @At("HEAD"), cancellable = true)
    private void modifyPotionDuration(MobEffectInstance effect, @Nullable Entity source, CallbackInfoReturnable<Boolean> cir) {
        if(this.levelUPNeoforge_1_21_1$effectReentryGuard) return;
        if(!((Object) this instanceof ServerPlayer player)) return;

        if(effect.getEffect().value().getCategory() == MobEffectCategory.BENEFICIAL) {
            double multiplier = player.getAttributeValue(ModAttributes.POTION_DURATION_MULTI);
            int baseDuration = effect.getDuration();

            int newDuration = (int) (baseDuration*multiplier);

            MobEffectInstance newEffectInstance = new MobEffectInstance(effect.getEffect(),
                    newDuration,
                    effect.getAmplifier(),
                    effect.isAmbient(),
                    effect.isVisible(),
                    effect.showIcon());

            try {
                this.levelUPNeoforge_1_21_1$effectReentryGuard = true;

                ((LivingEntity) (Object) this).addEffect(newEffectInstance, source);

                cir.setReturnValue(true);
                cir.cancel();
            } finally {
                this.levelUPNeoforge_1_21_1$effectReentryGuard = false;
            }
        }

    }

}
