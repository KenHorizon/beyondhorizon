package com.kenhorizon.beyondhorizon.server.world.item.enchantment;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public interface IAttributeEnchantment {

    default Multimap<Attribute, AttributeModifier> getAttributeModifiers(UUID uuid, ItemStack stack, int level) {
        return HashMultimap.create();
    }
}
