package com.kenhorizon.beyondhorizon.client.render.misc.tooltips;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.configs.BHConfigs;
import com.kenhorizon.beyondhorizon.configs.client.ModClientConfig;
import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.item.util.ItemStackUtil;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.ForgeMod;

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

    public void makeAttributeTooltip(ItemStack stack, List<Component> tooltip, Multimap<Attribute, AttributeModifier> modifierMap, ChatFormatting chatFormatting) {
        if (modifierMap.isEmpty()) return;
        for (Map.Entry<Attribute, AttributeModifier> entry : modifierMap.entries()) {
            AttributeModifier attributeModifier = entry.getValue();
            Attribute attribute = entry.getKey();
            double attributeAmount = attributeModifier.getAmount();
            this.makeTooltips(tooltip, attribute, attributeModifier, attributeAmount, chatFormatting);
        }
    }
    public void makeAttributeTooltip(ItemStack stack, List<Component> tooltip, Multimap<Attribute, AttributeModifier> modifierMap) {
        makeAttributeTooltip(stack, tooltip, modifierMap, null);
    }
    private void makeTooltips(List<Component> tooltip, Attribute attribute, AttributeModifier attributeModifier, double attributeAmount, ChatFormatting colors) {
        double amount = formatAttributeValues(attribute, attributeModifier, attributeAmount);
        ChatFormatting color = colors == null ? Tooltips.attributeColorFormat(attributeAmount) : colors;
        Component displayName = Component.translatable(attribute.getDescriptionId());
        String isPositive = amount > 0.0D ? "plus" : "take";
        amount *= attributeAmount > 0.0D ? 1.0D : -1.0D;
        if (amount == 0.0D) return;
        if (checkIfPercentage(attribute)) {
            tooltip.add(CommonComponents.space().append(Component.translatable(String.format("%s.attributes.%s.percent", BeyondHorizon.ID, isPositive), Maths.format(amount), displayName).withStyle(color)));
        } else {
            tooltip.add(CommonComponents.space().append(Component.translatable(String.format("%s.attributes.%s.%s", BeyondHorizon.ID, isPositive, attributeModifier.getOperation().toValue()), Maths.format(amount), displayName).withStyle(color)));
        }
    }
    private void makeTooltips(List<Component> tooltip, Attribute attribute, AttributeModifier attributeModifier, double attributeAmount) {
         makeTooltips(tooltip, attribute, attributeModifier, attributeAmount, null);
    }


    public void addTooltips(ItemStack itemStack, Player player, TooltipFlag context, List<Component> tooltip) {
        int lastAttributeLine = 0;
        int firstHandLine = 0;
        Integer lastGreenAttributeIndex = null;
        String prefix = "attribute.modifier";
        String attributeEqualsPrefix = "attribute.modifier.equals.0";
        String handPrefix = "item.modifiers";
        if (BHConfigs.ATTRIBUTE_TOOLTIP_OVERHAUl) {
            for (int i = 0; i < tooltip.size(); i++) {
                var component = tooltip.get(i);
                var content = component.getContents();
                this.removeTooltip(tooltip, prefix);
            }
            this.makeAttributeTooltip(player, tooltip, itemStack);
            this.makePotionTooltip(itemStack, tooltip);
        }
    }

    public void makePotionTooltip(ItemStack itemStack, List<Component> tooltips) {
        if (itemStack.getItem() instanceof PotionItem || itemStack.getItem() instanceof LingeringPotionItem || itemStack.getItem() instanceof TippedArrowItem) {
            List<Pair<Attribute, AttributeModifier>> list = Lists.newArrayList();
            for (MobEffectInstance instance : PotionUtils.getMobEffects(itemStack)) {
                MobEffect effect = instance.getEffect();
                Map<Attribute, AttributeModifier> map = effect.getAttributeModifiers();
                if (!map.isEmpty()) {
                    for (Map.Entry<Attribute, AttributeModifier> entry : map.entrySet()) {
                        AttributeModifier attributemodifier = entry.getValue();
                        AttributeModifier modifier = new AttributeModifier(attributemodifier.getName(), effect.getAttributeModifierValue(instance.getAmplifier(), attributemodifier), attributemodifier.getOperation());
                        list.add(new Pair<>(entry.getKey(), modifier));
                    }
                }
            }
            if (!list.isEmpty()) {
                this.makePotionTooltip(tooltips, list);
            }
        }
    }

    private void removeTooltip(List<Component> tooltip, String startWith) {
        for (EquipmentSlot slots : EquipmentSlot.values()) {
            for (int i = 0; i < tooltip.size(); i++) {
                Component component = tooltip.get(i);
                if (component instanceof MutableComponent mutableComponents) {
                    for (Component vanillaAttribute : mutableComponents.getSiblings()) {
                        if (vanillaAttribute.getContents() instanceof TranslatableContents translatableContents) {
                            if (translatableContents.getKey().startsWith(startWith)) {
                                tooltip.remove(i);
                                break;
                            }
                        }
                    }
                    if (mutableComponents.getContents() instanceof TranslatableContents translatableContents) {
                        if (translatableContents.getKey().startsWith(startWith)) {
                            tooltip.remove(i);
                            break;
                        }
                    }
                }
            }
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
