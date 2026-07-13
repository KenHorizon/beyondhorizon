package com.kenhorizon.beyondhorizon.server.api;

import com.kenhorizon.beyondhorizon.server.api.skills.ISkillItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class SkillSlots implements ISkillSlots {
    public static final String NBT_SLOTS = "slots";
    public static final String NBT_SIZE = "size";
    private int slots;
    private int size;
    private ISkillItems skillItems;

    public SkillSlots(ISkillItems skillItems) {
        this.skillItems = skillItems;
        this.setTotal(skillItems.getActiveSkills().size());
    }

    @Override
    public int getSelectedSlot() {
        return this.slots;
    }

    @Override
    public void select(int slot) {
        this.slots = slot;
    }

    @Override
    public void setMaxSlot(int maxSlot) {
        this.size = maxSlot;
    }

    @Override
    public void setTotal(int total) {
        this.size = total;
    }

    @Override
    public int getTotal() {
        return this.size;
    }

    @Override
    public void loadNbt(CompoundTag nbt) {
        this.select(nbt.getInt(NBT_SLOTS));
        this.setTotal(nbt.getInt(NBT_SIZE));
    }

    @Override
    public CompoundTag writeNbt() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt(NBT_SLOTS, this.getSelectedSlot());
        nbt.putInt(NBT_SIZE, this.getTotal());
        return nbt;
    }
}
