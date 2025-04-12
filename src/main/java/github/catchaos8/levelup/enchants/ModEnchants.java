package github.catchaos8.levelup.enchants;

import github.catchaos8.levelup.LevelUP;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEnchants {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS =
            DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, LevelUP.MOD_ID);

    //Constitution
    public static RegistryObject<Enchantment> FORTIFYING =
            ENCHANTMENTS.register("fortifying",
                    () -> new ConstitutionBoostEnch(Enchantment.Rarity.UNCOMMON,
                            EnchantCategories.ARMOR_AND_WEAPONS, EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.MAINHAND));

    //Dexterity
    public static RegistryObject<Enchantment> REFLEX =
            ENCHANTMENTS.register("reflex",
                    () -> new DexterityBoostEnch(Enchantment.Rarity.UNCOMMON,
                            EnchantCategories.ARMOR_AND_WEAPONS, EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.MAINHAND));

    //Strength
    public static RegistryObject<Enchantment> STRENGTHENING =
            ENCHANTMENTS.register("strengthening",
                    () -> new StrengthBoostEnch(Enchantment.Rarity.UNCOMMON,
                            EnchantCategories.ARMOR_AND_WEAPONS, EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.MAINHAND));

    //Vitality
    public static RegistryObject<Enchantment> INNER_STRENGTH =
            ENCHANTMENTS.register("inner_strength",
                    () -> new VitalityBoostEnch(Enchantment.Rarity.UNCOMMON,
                            EnchantCategories.ARMOR_AND_WEAPONS, EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.MAINHAND));

    //Constitution
    public static RegistryObject<Enchantment> UNYIELDING =
            ENCHANTMENTS.register("unyielding",
                    () -> new EnduranceBoostEnch(Enchantment.Rarity.UNCOMMON,
                            EnchantCategories.ARMOR_AND_WEAPONS, EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.MAINHAND));


    public static void register (IEventBus eventBus) {
        ENCHANTMENTS.register(eventBus);
    }
}
