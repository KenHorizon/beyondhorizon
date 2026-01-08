package com.kenhorizon.beyondhorizon.server.enchantment;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;

public interface IAttributeEnchantment {
    private Enchantment self() {
        return (Enchantment) this;
    }
    /**
     * Apply given attributes to entity to corressponded enchantment using or in slots
     * @param entity Entity who have enchantments
     * @param attributeMap Entity's Attribute Map
     * @param level Enchantment level
     * @param multiplier Multiply base amount of attribute
     *
     * @apiNote Only use on armors
     * */
    void addAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int level, double multiplier);

    /**
     * Remove stored attributes to entity
     * @param entity Entity who have enchantments
     * @param attributeMap Entity's Attribute Map
     * */
    void removeAttributeModifiers(LivingEntity entity, AttributeMap attributeMap);

    /**
     * Get or Calculate given attribute/s amount
     * @param level Enchantment Level
     * @param modifier Attribute being applied
     * */
    double getAttributeModifierValue(int level, AttributeModifier modifier);

    /**
     * Calculate how the attribute modifier amount per level <br>
     * If set zero will be multiplied by level to the amount given in enchanment <br>
     * */
    default void perLevel(double perLevel) {}

    /**
     * Get Attribute and Modifiers Map
     * */
     Multimap<Attribute, AttributeModifier> getAttributeModifiers();

}
