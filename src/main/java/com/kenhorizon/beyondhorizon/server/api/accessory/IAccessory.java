package com.kenhorizon.beyondhorizon.server.api.accessory;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface IAccessory {

    ItemStack getStack();

    default Multimap<Attribute, AttributeModifier> getAttributeModifiers(UUID uuid, ItemStack stack) {
        return getAttributeModifiers(stack);
    }

    default Multimap<Attribute, AttributeModifier> getAttributeModifiers(ItemStack stack) {
        return HashMultimap.create();
    }

    default UUID getSlotId() {
        return UUID.randomUUID();
    }

    default boolean makePiglinsNeutral() {
        return false;
    }
}
