package com.kenhorizon.beyondhorizon.server.api.accessory;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandlerModifiable;

public interface IAccessoryItemHandler extends IItemHandlerModifiable, INBTSerializable<CompoundTag> {

    ItemStack getPreviousItemStack(int slot);

    void setPreviousItemStack(int slot, ItemStack itemStack);

    @Override
    void deserializeNBT(CompoundTag nbt);

    @Override
    CompoundTag serializeNBT();
}