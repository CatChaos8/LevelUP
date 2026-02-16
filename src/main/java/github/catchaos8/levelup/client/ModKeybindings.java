package github.catchaos8.levelup.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class ModKeybindings {
    public static final KeyMapping OPEN_STATS_SCREEN = new KeyMapping(
            "key.levelup.opengui",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_BACKSLASH,
            "key.categories.levelup"
    );
}
