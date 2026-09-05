package com.kenhorizon.beyondhorizon.server.enchantment;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.kenhorizon.beyondhorizon.server.api.accessory.IAccessory;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.UUID;

public interface IAttributeEnchantment {
    /**
     * Get or Calculate given attribute/s amount
     * @param level Enchantment Level
     * @param modifier Attribute being applied
     * */
    default double getAttributeModifierValue(int level, LevelValue levelValue) {
        return levelValue.levelBased(level);
    }

    default Multimap<Attribute, AttributeModifier> getAttributeModifiers(UUID uuid, ItemStack stack) {
        return HashMultimap.create();
    }
}
