package com.kenhorizon.beyondhorizon.mixins.common;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.enchantment.IAdditionalEnchantment;
import com.kenhorizon.beyondhorizon.server.enchantment.IAttributeEnchantment;
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
    public void addAttributeModifiers(LivingEntity entity, int level, double multiplier) {
//        BeyondHorizon.LOGGER.debug("Attribute Added {} {}", level, multiplier);
        AttributeMap attributeMap = entity.getAttributes();
        for (Map.Entry<Attribute, AttributeModifier> entry : this.enchantmentAttributeModifiers.entries()) {
            AttributeInstance attributeInstance = attributeMap.getInstance(entry.getKey());
            if (attributeInstance != null) {
                AttributeModifier attributeModifier = entry.getValue();
                attributeInstance.removeModifier(attributeModifier);
                double amount = this.getAttributeModifierValue(level, attributeModifier) * (multiplier == 0 ? 1.0D : multiplier);
                attributeInstance.addPermanentModifier(new AttributeModifier(attributeModifier.getId(), "Enchantment Attribute Modifiers", amount, attributeModifier.getOperation()));
            }
        }
    }

    @Unique
    @Override
    public void removeAttributeModifiers(LivingEntity entity) {
        AttributeMap attributeMap = entity.getAttributes();
        for (Map.Entry<Attribute, AttributeModifier> entry : this.enchantmentAttributeModifiers.entries()) {
            AttributeInstance attributeInstance = attributeMap.getInstance(entry.getKey());
            if (attributeInstance != null) {
                attributeInstance.removeModifier(entry.getValue());
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
