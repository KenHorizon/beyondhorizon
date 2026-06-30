package com.kenhorizon.beyondhorizon.mixins.common;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.enchantment.AdvancedEnchantment;
import com.kenhorizon.beyondhorizon.server.enchantment.IAdditionalEnchantment;
import com.kenhorizon.beyondhorizon.server.enchantment.IAttributeEnchantment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixins implements IAttributeEnchantment, IAdditionalEnchantment {
    @Unique
    private final Multimap<Attribute, AttributeModifier> enchantmentAttributeModifiers = HashMultimap.create();
    @Unique
    private double perLevel = 0.0D;

    @Unique
    @Override
    public Optional<IAdditionalEnchantment> enchantmentCallback() {
        return Optional.of(this);
    }

    @Unique
    @Override
    public void addAttributeModifiers(LivingEntity entity, EquipmentSlot slot,  int level) {
//        BeyondHorizon.LOGGER.debug("Attribute Added {} {}", level, multiplier);
        AttributeMap attributeMap = entity.getAttributes();
        for (Map.Entry<Attribute, AttributeModifier> entry : this.enchantmentAttributeModifiers.entries()) {
            AttributeInstance attributeInstance = attributeMap.getInstance(entry.getKey());
            if (attributeInstance != null) {
                UUID id = AdvancedEnchantment.ARMOR_MODIFIER_UUID_PER_TYPE.get(slot);
                AttributeModifier modifier = attributeInstance.getModifier(id);
                attributeInstance.removeModifier(modifier);
                double amount = this.getAttributeModifierValue(level, modifier);
                attributeInstance.addPermanentModifier(new AttributeModifier(id, "Enchantment Attribute Modifiers", amount, modifier.getOperation()));
            }
        }
    }

    @Unique
    @Override
    public void removeAttributeModifiers(LivingEntity entity, EquipmentSlot slot) {
        AttributeMap attributeMap = entity.getAttributes();
        for (Map.Entry<Attribute, AttributeModifier> entry : this.enchantmentAttributeModifiers.entries()) {
            AttributeInstance attributeInstance = attributeMap.getInstance(entry.getKey());
            if (attributeInstance != null) {
                UUID id = AdvancedEnchantment.ARMOR_MODIFIER_UUID_PER_TYPE.get(slot);
                AttributeModifier modifier = attributeInstance.getModifier(id);
                attributeInstance.removeModifier(modifier);
            }
        }
    }
    @Unique
    @Override
    public double getAttributeModifierValue(int level, AttributeModifier modifier) {
        return this.perLevel > 0.0D ? modifier.getAmount() + (this.perLevel * level) : modifier.getAmount() * level;
    }
    @Unique
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers() {
        return this.enchantmentAttributeModifiers;
    }

    @Unique
    @Override
    public void perLevel(double perLevel) {
        this.perLevel = perLevel;
    }
}
