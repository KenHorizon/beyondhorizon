package com.kenhorizon.beyondhorizon.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

public class ResourceUtils {

    public static boolean getImage(ResourceLocation rl) {
        Minecraft mc = Minecraft.getInstance();
        var options = mc.getResourceManager().getResource(rl);
        return options.isPresent();
    }
}
