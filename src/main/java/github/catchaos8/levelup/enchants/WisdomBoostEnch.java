package github.catchaos8.levelup.enchants;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class WisdomBoostEnch extends Enchantment {
    public WisdomBoostEnch(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @Override
    public int getMaxLevel() {
        return 10;
    }

    @Override
    public boolean checkCompatibility(Enchantment pOther) {
        return super.checkCompatibility(pOther) && !(pOther instanceof StrengthBoostEnch
                || pOther instanceof VitalityBoostEnch
                || pOther instanceof ConstitutionBoostEnch
                || pOther instanceof DexterityBoostEnch
                || pOther instanceof EnduranceBoostEnch);
    }
}
