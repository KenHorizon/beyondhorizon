package com.kenhorizon.beyondhorizon.client.level.tooltips;

import com.google.common.collect.Multimap;
import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import com.kenhorizon.beyondhorizon.server.util.Tooltips;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import net.minecraftforge.registries.tags.ITag;
import net.minecraftforge.registries.tags.ITagManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AttributeTooltips {
    public void makeAttributeTooltip(Player player, List<Component> tooltip, ItemStack itemStack) {
        for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
            Multimap<Attribute, AttributeModifier> multimap = itemStack.getAttributeModifiers(equipmentSlot);
            for (Map.Entry<Attribute, AttributeModifier> entry : multimap.entries()) {
                AttributeModifier attributeModifier = entry.getValue();
                Attribute attribute = entry.getKey();
                double attributeAmount = getAttributeAmount(player, itemStack, attribute, attributeModifier.getAmount());
                makeTooltips(tooltip, attribute, attributeModifier, attributeAmount);
            }
        }
    }

    public void makePotionTooltip(List<Component> tooltip, List<Pair<Attribute, AttributeModifier>> list) {
        for (Pair<Attribute, AttributeModifier> pair : list) {
            Attribute attribute = pair.getFirst();
            AttributeModifier attributeModifier = pair.getSecond();
            double attributeAmount = attributeModifier.getAmount();
            this.makeTooltips(tooltip, attribute, attributeModifier, attributeAmount);
        }
    }

    public void makeAttributeTooltip(ItemStack stack, List<Component> tooltip, Map<Attribute, AttributeModifier> modifierMap) {
        if (modifierMap.isEmpty()) return;
        for (Map.Entry<Attribute, AttributeModifier> entry : modifierMap.entrySet()) {
            AttributeModifier attributeModifier = entry.getValue();
            Attribute attribute = entry.getKey();
            double attributeAmount = attributeModifier.getAmount();
            this.makeTooltips(tooltip, attribute, attributeModifier, attributeAmount);
        }
    }

    public void makeAttributeTooltip(ItemStack stack, List<Component> tooltip, Multimap<Attribute, AttributeModifier> modifierMap) {
        if (modifierMap.isEmpty()) return;
        for (Map.Entry<Attribute, AttributeModifier> entry : modifierMap.entries()) {
            AttributeModifier attributeModifier = entry.getValue();
            Attribute attribute = entry.getKey();
            double attributeAmount = attributeModifier.getAmount();
            this.makeTooltips(tooltip, attribute, attributeModifier, attributeAmount);
        }
    }

    private void makeTooltips(List<Component> tooltip, Attribute attribute, AttributeModifier attributeModifier, double attributeAmount) {
        double amount = formatAttributeValues(attribute, attributeModifier, attributeAmount);
        ChatFormatting color = Tooltips.attributeColorFormat(attributeAmount);
        Component displayName = Component.translatable(attribute.getDescriptionId());
        String isPositive = amount > 0.0D ? "plus" : "take";
        amount *= attributeAmount > 0.0D ? 1.0D : -1.0D;
        if (amount == 0.0D) return;
        if (checkIfPercentage(attribute)) {
            tooltip.add(Component.translatable(String.format("%s.attributes.%s.percent", BeyondHorizon.ID, isPositive), Maths.FORMAT.format(amount), displayName).withStyle(color));
        } else {
            tooltip.add(Component.translatable(String.format("%s.attributes.%s.%s", BeyondHorizon.ID, isPositive, attributeModifier.getOperation().toValue()), Maths.FORMAT.format(amount), displayName).withStyle(color));
        }
    }

    private boolean checkIfPercentage(Attribute attribute) {
        return AttributeReader.INSTANCE.getAttributePercentages(attribute);
    }

    private double getAttributeAmount(Player player, ItemStack itemStack, Attribute attribute, double attributeAmount) {
        if (player != null) {
            if (attribute.equals(Attributes.ATTACK_DAMAGE)) {
                attributeAmount += player.getAttributeBaseValue(Attributes.ATTACK_DAMAGE);
                attributeAmount += EnchantmentHelper.getDamageBonus(itemStack, MobType.UNDEFINED);
            }
            if (attribute.equals(Attributes.ATTACK_SPEED)) {
                attributeAmount += player.getAttributeBaseValue(Attributes.ATTACK_SPEED);
            }
            if (attribute.equals(ForgeMod.ENTITY_REACH.get())) {
                attributeAmount += player.getAttributeBaseValue(ForgeMod.ENTITY_REACH.get());
            }
        }
        return attributeAmount;
    }

    private double formatAttributeValues(Attribute attribute, AttributeModifier modifier, double amount) {
        if (modifier.getOperation() != AttributeModifier.Operation.MULTIPLY_BASE && modifier.getOperation() != AttributeModifier.Operation.MULTIPLY_TOTAL) {
            if (checkIfPercentage(attribute)) {
                return amount * 100.0D;
            } else {
                return amount;
            }
        } else {
            return amount * 100.0D;
        }
    }
}
