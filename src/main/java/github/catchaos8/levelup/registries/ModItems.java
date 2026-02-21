package github.catchaos8.levelup.registries;

import github.catchaos8.levelup.LevelUP;
import github.catchaos8.levelup.items.OrbOfReconstruction;
import github.catchaos8.levelup.items.OrbOfTheVoid;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(LevelUP.MOD_ID);

    public static final DeferredHolder<Item, Item> ORB_OF_THE_VOID = ITEMS.register("orb_of_the_void",
            () -> new OrbOfTheVoid(new Item.Properties()));
    public static final DeferredHolder<Item, Item> ORB_OF_RECONSTRUCTION = ITEMS.register("orb_of_reconstruction",
            () -> new OrbOfReconstruction(new Item.Properties()));
}
