package com.kenhorizon.beyondhorizon.server.item.tooltips;

import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public class QuiverTooltip implements TooltipComponent {
    private final NonNullList<ItemStack> items;
    public QuiverTooltip(NonNullList<ItemStack> stacks) {
        this.items = stacks;
    }

    public NonNullList<ItemStack> getItems() {
        return this.items;
    }
}
