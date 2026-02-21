package github.catchaos8.levelup.mixin;


import github.catchaos8.levelup.registries.ModAttributes;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class HungerMixin {

    @Inject(method = "causeFoodExhaustion", at = @At("HEAD"), cancellable = true)
    private void modifyExhaustion(float exhaustion, CallbackInfo ci) {
        Player player = (Player) (Object) this;
        FoodData foodData = player.getFoodData();
        double x = player.getAttributeValue(ModAttributes.HUNGER_COST_REDUCTION);
        if(x > 0) {
            double reduct = Math.pow(0.99, x);
            float newHunger = (float) (reduct*exhaustion);
            foodData.addExhaustion(newHunger);
            ci.cancel();
        }

    }



}
