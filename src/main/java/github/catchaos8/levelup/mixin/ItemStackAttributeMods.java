package github.catchaos8.levelup.mixin;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static github.catchaos8.levelup.lib.SetStats.applyEnchantModifiers;

@Mixin(ItemStack.class)
public class ItemStackAttributeMods {
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
