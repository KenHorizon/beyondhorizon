package com.kenhorizon.beyondhorizon.client.keybinds;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

public class Keybinds {
    public static final Map<String, String> KEYBINDING = new HashMap<>();
    public static final String KEY_CATEGORY = keyBind("category");
    public static final String KEY_LEVEL_SYSTEM = keyBind("level_system");

    static  {
        KEYBINDING.put(KEY_CATEGORY, "Beyond Horizon");
        KEYBINDING.put(KEY_LEVEL_SYSTEM, "Level System");
    }

    public static final KeyMapping LEVEL_SYSTEM = new KeyMapping(KEY_LEVEL_SYSTEM, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_O, KEY_CATEGORY);

    public static String keyBind(String name) {
        return "key." + BeyondHorizon.ID + "." + name;
    }
}
