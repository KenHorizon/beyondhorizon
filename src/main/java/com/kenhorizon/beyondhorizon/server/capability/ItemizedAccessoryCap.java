package com.kenhorizon.beyondhorizon.server.capability;

import com.google.common.collect.Multimap;
import com.kenhorizon.beyondhorizon.server.api.accessory.IAccessory;
import com.kenhorizon.beyondhorizon.server.api.accessory.IAccessoryItem;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
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
    public boolean makePiglinsNeutral(Player player) {
        return this.accessoryItem.makePiglinsNeutral(player);
    }

    @Override
    public boolean canWalkOnPoweredSnow(Player player) {
        return this.accessoryItem.canWalkOnPoweredSnow(player);
    }

    @Override
    public boolean isEndermanMask(Player player, EnderMan enderMan) {
        return this.accessoryItem.isEndermanMask(player, enderMan);
    }

    @Override
    public boolean isFreezeImmune(Player player) {
        return this.accessoryItem.isFreezeImmune(player);
    }
}
