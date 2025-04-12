package github.catchaos8.levelup.enchants;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class EnchantCategories {
    public static final EnchantmentCategory ARMOR_AND_WEAPONS = EnchantmentCategory.create("armor_and_weapons", item ->
            item instanceof ArmorItem || item instanceof SwordItem || item instanceof AxeItem
    );
}
