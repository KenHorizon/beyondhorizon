package com.kenhorizon.beyondhorizon.server.item.util;

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemStackUtils {

    public static void displayItemActivation(ItemStack stack) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.gameRenderer.displayItemActivation(stack);
    }

}
