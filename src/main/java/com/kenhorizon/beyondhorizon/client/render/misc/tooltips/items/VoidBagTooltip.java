package com.kenhorizon.beyondhorizon.client.render.misc.tooltips.items;

import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public class VoidBagTooltip implements TooltipComponent {
    private final NonNullList<ItemStack> items;
    public VoidBagTooltip(NonNullList<ItemStack> pItems) {
        this.items = pItems;
    }

    public NonNullList<ItemStack> getItems() {
        return this.items;
    }
}
