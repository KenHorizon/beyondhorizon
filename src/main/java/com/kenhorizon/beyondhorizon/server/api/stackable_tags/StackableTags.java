package com.kenhorizon.beyondhorizon.server.api.stackable_tags;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.Utils;
import com.kenhorizon.beyondhorizon.server.capability.Capabilities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;

public class StackableTags {

    protected final String NBT_DURATION_TICK = "duration_per_tick";
    protected final String NBT_STACKS = "stack";
    protected final String NBT_STACKS_NAME = "id";
    protected final String NBT_STACKS_DURATION = "duration";
    protected final String NBT_STACKS_MAX = "max_stack";
    protected String name;
    protected int stack;
    protected int durationPerTick;
    protected int duration;
    protected int maxStack = -1;

    public StackableTags() {

    }

    public StackableTags(String name, int maxStack, int duration, int durationPerTick) {
        this.name = name;
        this.maxStack = maxStack;
        this.duration = duration;
        this.durationPerTick = durationPerTick;
    }

    public StackableTags(String name, int duration, int durationPerTick) {
        this(name, -1, duration, durationPerTick);
    }

    public StackableTags(String name, int maxStack) {
        this(name, maxStack, -1, -1);
    }

    public StackableTags(String name) {
        this(name, -1, -1, -1);
    }

    public void name(String name) {
        this.name = name;
    }

    public void add(int v) {
        if (this.maxStack > 0 && this.stack >= this.maxStack) {
            this.stack = this.maxStack;
        }
        this.stack += v;
    }

    public void remove(int v) {
        this.stack = Math.max(0, this.stack - v);
    }

    public int getStack() {
        return stack;
    }

    public void reset() {
        this.stack = 0;
    }

    public boolean hasStacks() {
        return this.getStack() > 0;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }

    public void setStack(int value) {
        this.stack = value;
    }
    
    public void setMaxStack(int value) {
        this.maxStack = value;
    }
    
    public int getMaxStack() {
        return this.maxStack;
    }
    
    public void setDuration(int value) {
        this.duration = value;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDurationPerTick(int value) {
        this.durationPerTick = value;
    }

    public int getDurationPerTick() {
        return this.durationPerTick;
    }

    public boolean isFullyStacked() {
        return this.stack == this.maxStack;
    }

    public void tick(LivingEntity entity) {
        if (this.getDuration() > 0) {
            if (entity.tickCount % this.getDurationPerTick() == 0) {
                this.setDuration(this.getDuration() - 1);
            }
        }
    }
    
    public CompoundTag writeNbt() {
        CompoundTag nbt = new CompoundTag();

        nbt.putString(NBT_STACKS_NAME, this.getName());
        nbt.putInt(NBT_STACKS, this.getStack());
        nbt.putInt(NBT_STACKS_DURATION, this.getDuration());
        nbt.putInt(NBT_STACKS_MAX, this.getMaxStack());
        nbt.putInt(NBT_DURATION_TICK, this.getDurationPerTick());
        return nbt;
    }
    
    public void readNbt(Tag tag) {
        CompoundTag nbt = (CompoundTag) tag;

        this.setName(nbt.getString(NBT_STACKS_NAME));
        this.setStack(nbt.getInt(NBT_STACKS));
        this.setDuration(nbt.getInt(NBT_STACKS_DURATION));
        this.setMaxStack(nbt.getInt(NBT_STACKS_MAX));
        this.setDurationPerTick(nbt.getInt(NBT_DURATION_TICK));
    }

    @Override
    public String toString() {
        return String.format("Name: %s | stacks: %s max: %s| duration: %s - per tick: %s", this.name, this.stack, this.maxStack, this.duration, this.durationPerTick);
    }
}
