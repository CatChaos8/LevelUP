package github.catchaos8.levelup.items;

import github.catchaos8.levelup.LevelUP;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, LevelUP.MOD_ID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    public static final RegistryObject<Item> ORB_OF_RECONSTRUCTION = ITEMS.register("orb_of_reconstruction",
            () -> new OrbOfReconstruction(new Item.Properties()));
    public static final RegistryObject<Item> ORB_OF_THE_VOID = ITEMS.register("orb_of_the_void",
            () -> new OrbOfTheVoid(new Item.Properties()));

}
