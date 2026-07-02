package com.kenhorizon.beyondhorizon.server.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class QuiverItemStackHandler extends ItemStackHandler {
    protected int selectedSlot = 0;
    protected boolean ammoCollect = true;
    public static final String NBT_SELECTED_SLOT = "selected_index";
    public static final String NBT_AMMO_COLLECT = "ammo_collect";

    public QuiverItemStackHandler(int containerSize) {
        super(containerSize);
    }

    public int getSelectedSlot() {
        return this.selectedSlot;
    }

    public void setSelectedSlot(int selectedSlot) {
        this.selectedSlot = selectedSlot;
    }

    public void setAmmoCollect(boolean v) {
        this.ammoCollect = v;
    }

    public boolean isAmmoCollect() {
        return ammoCollect;
    }

    public int getTotalOccupiedSlots() {
        int occupiedSlots = 0;
        for (int i = 0; i < this.getSlots(); i++) {
            ItemStack stack = this.getStackInSlot(i);
            if (!stack.isEmpty()) {
                occupiedSlots += 1;
            }
        }
        return occupiedSlots;
    }
    public int getTotalEmptySlots() {
        int occupiedSlots = 0;
        for (int i = 0; i < this.getSlots(); i++) {
            ItemStack stack = this.getStackInSlot(i);
            if (stack.isEmpty()) {
                occupiedSlots += 1;
            }
        }
        return this.getSlots() - occupiedSlots;
    }
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = super.serializeNBT();
        nbt.putInt(NBT_SELECTED_SLOT, this.getSelectedSlot());
        nbt.putBoolean(NBT_AMMO_COLLECT, this.isAmmoCollect());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.setSelectedSlot(nbt.getInt(NBT_SELECTED_SLOT));
        this.setAmmoCollect(nbt.getBoolean(NBT_AMMO_COLLECT));
        super.deserializeNBT(nbt);
    }
}
