package com.kenhorizon.beyondhorizon.server.capability;

import com.google.common.collect.Multimap;
import com.kenhorizon.beyondhorizon.server.api.accessory.IAccessory;
import com.kenhorizon.beyondhorizon.server.api.accessory.IAccessoryItem;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import java.util.UUID;

public class ItemizedAccessoryCap implements IAccessory {
    private final ItemStack itemStack;
    private final IAccessoryItem accessoryItem;

    public ItemizedAccessoryCap(IAccessoryItem accessoryItem, ItemStack itemStack) {
        this.accessoryItem = accessoryItem;
        this.itemStack = itemStack;
    }

    @Override
    public ItemStack getStack() {
        return this.itemStack;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(UUID uuid, ItemStack stack) {
        return this.accessoryItem.getAttributeModifiers(uuid, stack);
    }

    @Override
    public boolean makePiglinsNeutral() {
        return this.accessoryItem.makePiglinsNeutral();
    }
    @Override
    public boolean canWalkOnPoweredSnow() {
        return this.accessoryItem.canWalkOnPoweredSnow();
    }
}
