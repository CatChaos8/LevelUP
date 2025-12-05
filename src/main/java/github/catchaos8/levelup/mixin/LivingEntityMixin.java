package github.catchaos8.levelup.mixin;


import github.catchaos8.levelup.config.LevelUPCommonConfig;
import github.catchaos8.levelup.stats.PlayerStatsProvider;
import net.minecraft.server.level.ServerPlayer;
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
public abstract class LivingEntityMixin {

    //=====================Potion Effect Duration Buff From Intelligence=====================//
    @Unique
    private boolean levelup_effectReentrancyGuard = false;

    @Inject(
            method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void modifyPotionDuration(
            MobEffectInstance effect,
            @Nullable Entity source,
            CallbackInfoReturnable<Boolean> cir
    ) {
        // If already inside reentrant call, do nothing and let the original method run
        if (this.levelup_effectReentrancyGuard) return;

        if(!LevelUPCommonConfig.DO_POTION_DURATION_BOOST.get()) return;
        // Only proceed for server-side players

        if(!((Object) this instanceof ServerPlayer)) return;

        ServerPlayer player = (ServerPlayer) (Object) this;
        // Optional: only modify beneficial effects
        if (!effect.getEffect().isBeneficial()) return;

        player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
            int intelligence = (int) stats.getLimitedStat(6);

            int baseDuration = effect.getDuration();
            double multiplier = 1.0 + intelligence * LevelUPCommonConfig.INTELLIGENCE_POTION_DURATION_BOOST.get();
            int newDuration = (int) Math.ceil(baseDuration * multiplier);

            // Build modified instance (preserve amplifier, ambient, visibility, icon)
            MobEffectInstance modified = new MobEffectInstance(
                    effect.getEffect(),
                    newDuration,
                    effect.getAmplifier(),
                    effect.isAmbient(),
                    effect.isVisible(),
                    effect.showIcon()
            );

            try {
                // Mark reentrancy so the inner call doesn't go through this same modification path
                this.levelup_effectReentrancyGuard = true;

                // Call addEffect with the modified instance.
                // The second call will re-enter this injected method, but because the guard is true,
                // the injected method returns immediately and the original addEffect logic runs,
                // applying the modified effect.

                ((LivingEntity) (Object) this).addEffect(modified, source);

                // Cancel the outer/original call so the unmodified 'effect' is not also applied.
                cir.setReturnValue(true);
                cir.cancel();
            } finally {
                this.levelup_effectReentrancyGuard = false;
            }
        });
    }


}
