package github.CatChaos8.levelUP.utility;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBinding {
    public static final String KEY_CATEGORY_LEVELUP = "key.category.levelup";
    public static final String KEY_OPEN_GUI = "key.levelup.open_gui";

    public static final KeyMapping OPEN_KEY = new KeyMapping(KEY_OPEN_GUI, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_BACKSLASH, KEY_CATEGORY_LEVELUP);
}
