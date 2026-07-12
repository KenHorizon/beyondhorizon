package com.kenhorizon.beyondhorizon.client.keybinds;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

public class Keybinds {
    public static final Map<String, String> KEYBINDING = new HashMap<>();
    public static final String KEY_CATEGORY = keyBind("category");
    public static final String KEY_DASH = keyBind("dash");
    public static final String KEY_SKILL_SELECT = keyBind("skill_select");
    public static final String KEY_LEVEL_SYSTEM = keyBind("level_system");
    public static final String KEY_QUIVER_INVENTORY = keyBind("quiver_inventory");
    public static final String KEY_ACCESSORY_SKILL_SLOTS = keyBind("accessory_skill_slots");

    static  {
        KEYBINDING.put(KEY_CATEGORY, "Beyond Horizon");
        KEYBINDING.put(KEY_LEVEL_SYSTEM, "Level System");
        KEYBINDING.put(KEY_QUIVER_INVENTORY, "Quiver Inventory");
        KEYBINDING.put(KEY_ACCESSORY_SKILL_SLOTS, "Accessory skill slots");
        KEYBINDING.put(KEY_SKILL_SELECT, "Skill Select");
    }

    public static final KeyMapping LEVEL_SYSTEM = new KeyMapping(KEY_LEVEL_SYSTEM, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_O, KEY_CATEGORY);
    public static final KeyMapping QUIVER_INVENTORY = new KeyMapping(KEY_QUIVER_INVENTORY, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_X, KEY_CATEGORY);
    public static final KeyMapping ACCESSORY_SLOTS = new KeyMapping(KEY_ACCESSORY_SKILL_SLOTS, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_LEFT_CONTROL, KEY_CATEGORY);
    public static final KeyMapping SKILL_SELECT = new KeyMapping(KEY_SKILL_SELECT, KeyConflictContext.IN_GAME, KeyModifier.CONTROL, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_C, KEY_CATEGORY);

    public static String keyBind(String name) {
        return "key." + BeyondHorizon.ID + "." + name;
    }
}
