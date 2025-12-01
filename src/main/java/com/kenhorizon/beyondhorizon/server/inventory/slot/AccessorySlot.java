package com.kenhorizon.beyondhorizon.server.inventory.slot;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.accessory.AccessoryHelper;
import com.kenhorizon.beyondhorizon.server.accessory.IAccessoryItemHandler;
import com.kenhorizon.beyondhorizon.server.tags.BHItemTags;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.Nullable;

public class AccessorySlot extends SlotItemHandler {
    public static final ResourceLocation BLOCK_ATLAS = ResourceLocation.parse("textures/atlas/blocks.png");
    public static final ResourceLocation EMPTY_ACCESSORY = BeyondHorizon.resource("item/empty_accessory_slot");
    public final IAccessoryItemHandler handler;
    public AccessorySlot(IAccessoryItemHandler handler, int index, int xPosition, int yPosition) {
        super(handler, index, xPosition, yPosition);
        this.handler = handler;
    }

    public void initialize(ItemStack stack) {
        super.initialize(stack);
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        return itemStack.is(BHItemTags.ONLY_ACCESSORY) && AccessoryHelper.checkAccessoryIsPresentInSlot(itemStack, handler);
    }

    @Override
    public boolean isHighlightable() {
        return false;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Nullable
    @Override
    public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
        return Pair.of(BLOCK_ATLAS, EMPTY_ACCESSORY);
    }
}