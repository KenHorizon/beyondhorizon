package com.kenhorizon.beyondhorizon.server.world.item.util;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;

public class ItemStackUtils {

    public static void displayItemActivation(ItemStack stack) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.gameRenderer.displayItemActivation(stack);
    }

}
