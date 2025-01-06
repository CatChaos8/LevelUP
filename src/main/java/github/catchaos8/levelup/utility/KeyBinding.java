package github.catchaos8.levelup.utility;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBinding {
    public static final String KEY_CATEGORY_LEVELUP = "key.category.levelup";
    public static final String KEY_OPEN_GUI = "key.levelup.open_gui";


    public static final String KEY_CON = "key.levelup.upgrade_con";
    public static final String KEY_STR = "key.levelup.upgrade_str";
    public static final String KEY_DEX = "key.levelup.upgrade_dex";
    public static final String KEY_VIT = "key.levelup.upgrade_int";
    public static final String KEY_END = "key.levelup.upgrade_end";

    public static final KeyMapping OPEN_KEY = new KeyMapping(KEY_OPEN_GUI, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_BACKSLASH, KEY_CATEGORY_LEVELUP);

    public static final KeyMapping UPGRADE_CON = new KeyMapping(KEY_CON, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_Z, KEY_CATEGORY_LEVELUP);
    public static final KeyMapping UPGRADE_DEX = new KeyMapping(KEY_DEX, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_X, KEY_CATEGORY_LEVELUP);
    public static final KeyMapping UPGRADE_STR = new KeyMapping(KEY_STR, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_C, KEY_CATEGORY_LEVELUP);
    public static final KeyMapping UPGRADE_VIT = new KeyMapping(KEY_VIT, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, KEY_CATEGORY_LEVELUP);
    public static final KeyMapping UPGRADE_END = new KeyMapping(KEY_END, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_B, KEY_CATEGORY_LEVELUP);
}
