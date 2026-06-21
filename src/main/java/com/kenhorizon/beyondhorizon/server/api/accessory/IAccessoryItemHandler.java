package com.kenhorizon.beyondhorizon.server.api.accessory;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandlerModifiable;

public interface IAccessoryItemHandler extends IItemHandlerModifiable, INBTSerializable<CompoundTag> {

    ItemStack getPreviousItemStack(int slot);

    void setPreviousItemStack(int slot, ItemStack itemStack);

    @Override
    void deserializeNBT(CompoundTag nbt);

    boolean contains(TagKey<Item> tagKey);

    boolean contains(ItemStack itemStack);

    public int whatSlots(ItemStack itemStack);

    @Override
    CompoundTag serializeNBT();
}