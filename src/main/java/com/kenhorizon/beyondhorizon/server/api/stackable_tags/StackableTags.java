package com.kenhorizon.beyondhorizon.server.api.stackable_tags;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.kenhorizon.beyondhorizon.BeyondHorizon;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.Map;
import java.util.UUID;

public class StackableTags {
    public static final String NBT_RESET_ON_EXPIRED = "reset_on_expired";
    public static final String NBT_STACKS = "stack";
    public static final String NBT_STACKS_NAME = "id";
    public static final String NBT_STACKS_DURATION = "duration";
    public static final String NBT_STACKS_MAX_DURATION = "max_duration";
    public static final String NBT_STACKS_MAX = "max_stack";
    protected String name;
    protected int prevStack;
    protected int stack;
    protected int duration;
    protected int maxDuration;
    protected int maxStack;
    protected boolean resetOnExpired = false;
    private final Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();

    protected StackableTags(String name, int maxStack, int maxDuration) {
        this.name = name;
        this.duration = 0;
        this.maxStack = maxStack;
        this.maxDuration = maxDuration;
    }
    public StackableTags(String name, int maxStack, int stack, int duration, int maxDuration, boolean resetOnExpired) {
        this.name = name;
        this.maxStack = maxStack;
        this.stack = stack;
        this.duration = duration;
        this.maxDuration = maxDuration;
        this.resetOnExpired = resetOnExpired;
    }

    public StackableTags addModifiers(Attribute attribute, double amount, AttributeModifier.Operation operation) {
        this.attributeModifiers.put(attribute, new AttributeModifier(UUID.randomUUID(), "Stacking attributes", amount, operation));
        return this;
    }

    public StackableTags resetOnExpired() {
        this.resetOnExpired = true;
        return this;
    }

    public static StackableTags build(String name, int maxStack, int maxDuration) {
        return new StackableTags(name, maxStack, maxDuration);
    }

    public static StackableTags build(String name, int maxStack) {
        return new StackableTags(name, maxStack, 0);
    }

    public static StackableTags build(String name) {
        return new StackableTags(name, 0, 0);
    }


    public void setResetOnExpired(boolean resetOnExpired) {
        this.resetOnExpired = resetOnExpired;
    }

    public boolean isResetOnExpired() {
        return this.resetOnExpired;
    }

    public void name(String name) {
        this.name = name;
    }

    public void add(int v) {
        if (this.maxStack > 0 && this.stack >= this.maxStack) {
            this.stack = this.maxStack;
        } else {
            this.stack += v;
        }
        if (this.hasStacks()) {
            this.setDuration(0);
        }
    }

    public void remove(int v) {
        this.stack = Math.max(0, this.getStack() - v);
    }

    public int getStack() {
        return stack;
    }

    public void reset() {
        this.setStack(0);
        this.setDuration(0);
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
        this.prevStack = this.stack;
        this.stack = Math.max(0, Math.min(value, this.maxStack));
    }

    public void setMaxStack(int value) {
        this.maxStack = value;
    }

    public int getMaxStack() {
        return this.maxStack;
    }

    public void setMaxDuration(int value) {
        this.maxDuration = value;
    }

    public int getMaxDuration() {
        return this.maxDuration;
    }

    public boolean isFullyStacked() {
        return this.stack >= this.maxStack;
    }

    public void setPrevStack(int prevStack) {
        this.prevStack = prevStack;
    }

    public int getPrevStack() {
        return prevStack;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void tick(LivingEntity entity) {
        if (this.getMaxDuration() > 0) {
            if (this.hasStacks()) {
                this.setDuration(this.getDuration() + 1);
            }
            if (this.getDuration() >= this.getMaxDuration()) {
                if (!this.isResetOnExpired() && this.hasStacks()) {
                    this.remove(1);
                    this.setDuration(0);
                } else {
                    this.reset();
                }
            }
            if (this.getStack() <= 0) {
                this.removeAttributeModifiers(entity, this.getAttributeModifiers());
            }
        }
        if (this.stackHasChanged()) {
            if (this.hasStacks() && this.getDuration() <= 0) {
                this.setDuration(this.getMaxDuration());
            }
            this.addAttributeModifiers(entity, this.getAttributeModifiers());
            this.setPrevStack(this.getStack());
            for (var tags : StackableTagInstance.TAGS) {
                StackableTagInstance.sendPacket(entity, tags);
            }
        }
    }

    private void addAttributeModifiers(LivingEntity entity, Multimap<Attribute, AttributeModifier> modifiers) {
        for (Map.Entry<Attribute, AttributeModifier> entry : modifiers.entries()) {
            AttributeModifier modifier = entry.getValue();
            Attribute attribute = entry.getKey();
            AttributeInstance instance = entity.getAttribute(attribute);
            double amount = modifier.getAmount() + (modifier.getAmount() * this.getStack());
            BeyondHorizon.LOGGER.debug("{}", amount);
            AttributeModifier newModifier = new AttributeModifier(modifier.getId(), modifier.getName(), amount, modifier.getOperation());
            if (instance != null) {
                instance.removeModifier(modifier);
                instance.addTransientModifier(newModifier);
            }
        }
    }

    public boolean stackHasChanged() {
        return this.getPrevStack() != this.getStack();
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers() {
        return attributeModifiers;
    }

    public void removeAttributeModifiers(LivingEntity entity, Multimap<Attribute, AttributeModifier> modifier) {
        AttributeMap attributeMap = entity.getAttributes();
        attributeMap.removeAttributeModifiers(modifier);
    }

    public StackableTags(StackableTags other) {
        this.name = other.name;
        this.prevStack = other.prevStack;
        this.stack = other.stack;
        this.duration = other.duration;
        this.maxDuration = other.maxDuration;
        this.maxStack = other.maxStack;
        this.resetOnExpired = other.resetOnExpired;
        this.attributeModifiers.putAll(other.attributeModifiers);
    }

    public StackableTags copy() {
        StackableTags copy = new StackableTags(this.getName(), this.getMaxStack(), this.getMaxDuration());
        copy.resetOnExpired = this.resetOnExpired;
        for (Map.Entry<Attribute, AttributeModifier> entry : this.attributeModifiers.entries()) {
            AttributeModifier modifier = entry.getValue();
            copy.attributeModifiers.put(entry.getKey(), new AttributeModifier(modifier.getId(), modifier.getName(), modifier.getAmount(), modifier.getOperation()));
        }
        return copy;
    }

    public CompoundTag writeNbt() {
        CompoundTag nbt = new CompoundTag();
        nbt.putString(NBT_STACKS_NAME, this.getName());
        nbt.putInt(NBT_STACKS, this.getStack());
        nbt.putInt(NBT_STACKS_DURATION, this.getDuration());
        nbt.putInt(NBT_STACKS_MAX_DURATION, this.getMaxDuration());
        nbt.putInt(NBT_STACKS_MAX, this.getMaxStack());
        nbt.putBoolean(NBT_RESET_ON_EXPIRED, this.isResetOnExpired());
        return nbt;
    }
    
    public void readNbt(Tag tag) {
        CompoundTag nbt = (CompoundTag) tag;
        this.setName(nbt.getString(NBT_STACKS_NAME));
        this.setStack(nbt.getInt(NBT_STACKS));
        this.setDuration(nbt.getInt(NBT_STACKS_DURATION));
        this.setMaxDuration(nbt.getInt(NBT_STACKS_MAX_DURATION));
        this.setMaxStack(nbt.getInt(NBT_STACKS_MAX));
        this.setResetOnExpired(nbt.getBoolean(NBT_RESET_ON_EXPIRED));
    }

    @Override
    public String toString() {
        return String.format("{Name=%s | stacks=%s max=%s| duration=%s max=%s}", this.name, this.stack, this.maxStack, this.duration, this.maxDuration);
    }
}
