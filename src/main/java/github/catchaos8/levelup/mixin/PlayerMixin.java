package github.catchaos8.levelup.mixin;

import github.catchaos8.levelup.config.LevelUPCommonConfig;
import github.catchaos8.levelup.stats.PlayerStatsProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin {

    @Inject(method = "causeFoodExhaustion", at = @At("HEAD"), cancellable = true)
    private void modifyExhaustion(float exhaustion, CallbackInfo ci) {
        Player player = (Player) (Object) this;
        FoodData foodData = player.getFoodData();
        if(LevelUPCommonConfig.DO_HUNGER.get()) {
            player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
                float end = stats.getStat(12);
                if (end != 0) {
                    if (player.isSprinting()) {
                        double hungerDecrease = LevelUPCommonConfig.ENDURANCE_HUNGER.get();
                        //Exponential or whatever
                        float totalDecrease = (float) Math.pow(1.0-hungerDecrease, end);
                        foodData.addExhaustion((exhaustion*totalDecrease));
                        ci.cancel();
                    }
                }
            });
        }
    }
}

