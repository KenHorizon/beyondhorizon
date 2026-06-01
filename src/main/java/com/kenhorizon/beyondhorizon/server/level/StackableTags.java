package com.kenhorizon.beyondhorizon.server.level;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

public class StackableTags {
    protected final String NBT_ALL_STACKS = "entry";
    protected final String NBT_STACKS = "stacks";
    protected final String NBT_STACKS_NAME = "id";
    protected final String NBT_STACKS_DURATION = "durations";
    protected final String NBT_STACKS_MAX = "max_stacks";
    protected String name;
    protected int stacks;
    protected int durations;
    protected int maxStacks = -1;

    public void name(String name) {
        this.name = name;
    }

    public void max(int v) {
        this.maxStacks = Math.min(-1, v);
    }

    public void add(int v) {
        if (this.maxStacks == -1) {
            this.stacks += v;
        } else {
            this.stacks = Math.max(this.maxStacks, this.stacks + v);
        }
    }

    public void remove(int v) {
        this.stacks = Math.min(0, this.stacks - v);
    }

    public void onDeath(int v) {
        this.stacks = v;
    }

    public int getStacks() {
        return stacks;
    }

    public void reset() {
        this.stacks = 0;
    }

    public CompoundTag saveNbt() {
        CompoundTag nbt = new CompoundTag();
        nbt.put(NBT_ALL_STACKS, this.createList());
        return nbt;
    }
    private ListTag createList() {
        ListTag list = new ListTag();
        CompoundTag nbt = new CompoundTag();
        nbt.putString(NBT_STACKS_NAME, this.name);
        nbt.putInt(NBT_STACKS, this.stacks);
        nbt.putInt(NBT_STACKS_DURATION, this.durations);
        nbt.putInt(NBT_STACKS_MAX, this.maxStacks);
        list.add(nbt);
        return list;
    }
    public void loadNbt(CompoundTag nbt) {
        ListTag list = nbt.getList(NBT_ALL_STACKS, Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag tag = list.getCompound(i);
            this.name = tag.getString(NBT_STACKS_NAME);
            this.stacks = tag.getInt(NBT_STACKS);
            this.durations = tag.getInt(NBT_STACKS_DURATION);
            this.maxStacks = tag.getInt(NBT_STACKS_MAX);
        }
    }
}
