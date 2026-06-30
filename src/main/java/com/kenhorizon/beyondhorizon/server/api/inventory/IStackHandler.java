package com.kenhorizon.beyondhorizon.server.api.inventory;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;

public interface IStackHandler extends IItemHandlerModifiable {

    void setStackInSlot(int slot, @Nonnull ItemStack stack);

    @Nonnull
    ItemStack getStackInSlot(int slot);

    void setPreviousItemStack(int slot, @Nonnull ItemStack stack);

    ItemStack getPreviousItemStack(int slot);

    int getSlots();

    CompoundTag serializeNBT();

    void deserializeNBT(CompoundTag nbt);

    boolean contains(TagKey<Item> tagKey);

    boolean contains(ItemStack itemStack);

    int whatSlots(ItemStack itemStack);
}
