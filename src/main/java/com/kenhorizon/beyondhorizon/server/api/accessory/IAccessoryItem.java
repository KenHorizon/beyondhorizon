package com.kenhorizon.beyondhorizon.server.api.accessory;

import com.google.common.collect.Multimap;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.UUID;

public interface IAccessoryItem {

    IAccessory defaultInstance = () -> ItemStack.EMPTY;

    boolean has(Accessory skill);

    List<Accessory> getAccessories();

    Multimap<Attribute, AttributeModifier> getAttributeModifiers();

    boolean isCompatible(ItemStack inSlot, ItemStack outside);
    //

    default boolean hasCapability(ItemStack stack) {
        return true;
    }
    default Multimap<Attribute, AttributeModifier> getAttributeModifiers(UUID uuid, ItemStack stack) {
        return this.getAttributeModifiers(stack);
    }

    default Multimap<Attribute, AttributeModifier> getAttributeModifiers(ItemStack stack) {
        return defaultInstance.getAttributeModifiers(stack);
    }
}