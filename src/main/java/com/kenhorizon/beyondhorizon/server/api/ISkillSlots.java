package com.kenhorizon.beyondhorizon.server.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public interface ISkillSlots {

    int getSelectedSlot();

    void select(int slot);

    void setMaxSlot(int maxSlot);

    void setTotal(int total);

    int getTotal();

    void loadNbt(CompoundTag nbt);

    CompoundTag writeNbt();
}
