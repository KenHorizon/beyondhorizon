package com.kenhorizon.beyondhorizon.client.util;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.mojang.datafixers.util.Either;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

public class ResourceUtils {

    public static boolean getImage(ResourceLocation rl) {
        Minecraft mc = Minecraft.getInstance();
        var options = mc.getResourceManager().getResource(rl);
        return options.isPresent();
    }
}
