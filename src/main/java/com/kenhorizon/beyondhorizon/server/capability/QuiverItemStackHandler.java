package com.kenhorizon.beyondhorizon.server.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.items.ItemStackHandler;

public class QuiverItemStackHandler extends ItemStackHandler {
    protected int selectedSlot = 0;
    public static final String NBT_SELECTED_SLOT = "selected_slot";

    public QuiverItemStackHandler(int containerSize) {
        super(containerSize);
    }

    public int getSelectedSlot() {
        return this.selectedSlot;
    }

    public void setSelectedSlot(int selectedSlot) {
        this.selectedSlot = selectedSlot;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = super.serializeNBT();
        nbt.putInt(NBT_SELECTED_SLOT, this.getSelectedSlot());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.setSelectedSlot(nbt.getInt(NBT_SELECTED_SLOT));
        super.deserializeNBT(nbt);
    }
}
