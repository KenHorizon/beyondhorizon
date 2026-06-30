package com.kenhorizon.beyondhorizon.server.inventory;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.kenhorizon.beyondhorizon.server.api.inventory.IStackHandler;
import com.kenhorizon.beyondhorizon.server.api.accessory.IAccessoryStackHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.*;

public class AccessoryStackHandler implements IAccessoryStackHandler {
    private IStackHandler stackHandler;
    private int baseSize;

    public static final String NBT_BASE_SIZE = "default_size";
    public static final String NBT_STACKS = "stacks";


    public AccessoryStackHandler(int size) {
        this.stackHandler = new DynamicStackHandler(size);
        this.baseSize = size;
    }

    @Override
    public IStackHandler getStacks() {
        return this.stackHandler;
    }

    @Override
    public int getSlots() {
        return this.stackHandler.getSlots();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt(NBT_BASE_SIZE, this.baseSize);
        nbt.put(NBT_STACKS, this.getStacks().serializeNBT());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (nbt.contains(NBT_BASE_SIZE)) {
            this.baseSize = nbt.getInt(NBT_BASE_SIZE);
        }
        if (nbt.contains(NBT_STACKS)) {
            this.stackHandler.deserializeNBT(nbt);
        }
    }
}