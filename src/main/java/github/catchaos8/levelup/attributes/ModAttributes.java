package github.catchaos8.levelup.attributes;

import github.catchaos8.levelup.LevelUP;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES =
            DeferredRegister.create(Registries.ATTRIBUTE, LevelUP.MOD_ID);

    public static final DeferredHolder<Attribute, Attribute> CONSTITUTION = ATTRIBUTES.register("constitution",
            () -> new RangedAttribute("attribute.levelup.constitution", 0.0, 0.0, Integer.MAX_VALUE));
    public static final DeferredHolder<Attribute, Attribute> DEXTERITY = ATTRIBUTES.register("dexterity",
            () -> new RangedAttribute("attribute.levelup.dexterity", 0.0, 0.0, Integer.MAX_VALUE));
    public static final DeferredHolder<Attribute, Attribute> STRENGTH = ATTRIBUTES.register("strength",
            () -> new RangedAttribute("attribute.levelup.strength", 0.0, 0.0, Integer.MAX_VALUE));
    public static final DeferredHolder<Attribute, Attribute> VITALITY = ATTRIBUTES.register("vitality",
            () -> new RangedAttribute("attribute.levelup.vitality", 0.0, 0.0, Integer.MAX_VALUE));
    public static final DeferredHolder<Attribute, Attribute> WISDOM = ATTRIBUTES.register("wisdom",
            () -> new RangedAttribute("attribute.levelup.wisdom", 0.0, 0.0, Integer.MAX_VALUE));
    public static final DeferredHolder<Attribute, Attribute> INTELLIGENCE = ATTRIBUTES.register("intelligence",
            () -> new RangedAttribute("attribute.levelup.intelligence", 0.0, 0.0, Integer.MAX_VALUE));

    public static final DeferredHolder<Attribute, Attribute> PASSIVE_REGEN = ATTRIBUTES.register("passive_regen",
            () -> new RangedAttribute("attribute.levelup.passive_regen", 0.0, 0.0, Integer.MAX_VALUE));
    public static final DeferredHolder<Attribute, Attribute> HEALING_MULTIPLIER = ATTRIBUTES.register("healing_multi",
            () -> new RangedAttribute("attribute.levelup.healing_multi", 1.0, 0.0, Integer.MAX_VALUE));
    public static final DeferredHolder<Attribute, Attribute> HUNGER_COST_REDUCTION = ATTRIBUTES.register("hunger_cost_reduction",
            () -> new RangedAttribute("attribute.levelup.hunger_cost_reduction", 0.0, 0.0, Integer.MAX_VALUE));
    public static final DeferredHolder<Attribute, Attribute> LEVELING_SPEED = ATTRIBUTES.register("leveling_speed",
            () -> new RangedAttribute("attribute.levelup.leveling_speed", 1.0, 0.0, Integer.MAX_VALUE));
    public static final DeferredHolder<Attribute, Attribute> ITEM_DURABILITY_DAMAGE_REDUCTION = ATTRIBUTES.register("item_durability_damage_reduction",
            () -> new RangedAttribute("attribute.levelup.item_durability_damage_reduction", 0.0, 0.0, Integer.MAX_VALUE));
    public static final DeferredHolder<Attribute, Attribute> POTION_DURATION_MULTI = ATTRIBUTES.register("potion_duration_multi",
            () -> new RangedAttribute("attribute.levelup.potion_duration_multi", 0.0, 0.0, Integer.MAX_VALUE));


}
