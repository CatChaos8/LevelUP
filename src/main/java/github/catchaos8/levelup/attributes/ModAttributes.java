package github.catchaos8.levelup.attributes;

import github.catchaos8.levelup.LevelUP;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES =
            DeferredRegister.create(ForgeRegistries.ATTRIBUTES, LevelUP.MOD_ID);

    public static final RegistryObject<Attribute> CONSTITUTION =
            ATTRIBUTES.register("constitution", () -> new RangedAttribute("attribute.levelup.con", 0, 0, 1073741824)
                    .setSyncable(true));

    public static final RegistryObject<Attribute> DEXTERITY =
            ATTRIBUTES.register("dexterity", () -> new RangedAttribute("attribute.levelup.dex", 0, 0,1073741824)
                    .setSyncable(true));

    public static final RegistryObject<Attribute> STRENGTH =
            ATTRIBUTES.register("strength", () -> new RangedAttribute("attribute.levelup.str", 0, 0,1073741824)
                    .setSyncable(true));


    public static final RegistryObject<Attribute> VITALITY =
            ATTRIBUTES.register("vitality", () -> new RangedAttribute("attribute.levelup.vit", 0, 0,1073741824)
                    .setSyncable(true));

    public static final RegistryObject<Attribute> ENDURANCE =
            ATTRIBUTES.register("endurance", () -> new RangedAttribute("attribute.levelup.end", 0, 0,1073741824)
                    .setSyncable(true));

    public static void register(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }

}
