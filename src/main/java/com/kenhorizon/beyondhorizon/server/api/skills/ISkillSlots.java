package com.kenhorizon.beyondhorizon.server.api.skills;

import net.minecraft.nbt.CompoundTag;

public interface ISkillSlots {

    int getSelectedSlot();

    void select(int slot);

    void setMaxSlot(int maxSlot);

    void setTotal(int total);

    int getTotal();

    void loadNbt(CompoundTag nbt);

    CompoundTag writeNbt();
}
