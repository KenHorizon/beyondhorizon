package com.kenhorizon.beyondhorizon.client.keybinds;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;

public class Keybinds {

    public static final String KEY_CATEGORY = keyBind("category");

    public static String keyBind(String name) {
        return "key." + BeyondHorizon.ID + "." + name;
    }
}
