package com.kenhorizon.beyondhorizon.client.render.misc.tooltips;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.util.AttributePercentage;
import com.kenhorizon.beyondhorizon.configs.BHConfigs;
import com.kenhorizon.beyondhorizon.server.api.bonus_set.ArmorBonusSet;
import com.kenhorizon.beyondhorizon.server.api.bonus_set.ArmorSetRegistry;
import com.kenhorizon.beyondhorizon.server.enchantment.IAttributeEnchantment;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.ForgeMod;

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
    public void makeAttributeTooltip(Player player, List<Component> tooltip, ItemStack itemStack, int startLine) {
        int indexAppend = 0;
        for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
            Multimap<Attribute, AttributeModifier> multimap = itemStack.getAttributeModifiers(equipmentSlot);
            for (Map.Entry<Attribute, AttributeModifier> entry : multimap.entries()) {
                AttributeModifier attributeModifier = entry.getValue();
                Attribute attribute = entry.getKey();
                double attributeAmount = getAttributeAmount(player, itemStack, attribute, attributeModifier.getAmount());
                makeTooltips(tooltip, attribute, attributeModifier, attributeAmount, startLine + indexAppend);
                indexAppend++;
            }
        }
    }
    public void makeEnchantmentAttributeTooltip(Player player, List<Component> tooltip, ItemStack itemStack) {
        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(itemStack);
        for (Map.Entry<Enchantment, Integer> enchantmentEntry : map.entrySet()) {
            int level = enchantmentEntry.getValue();
            if (enchantmentEntry.getKey() instanceof IAttributeEnchantment attributeEnchantment) {
                for (Map.Entry<Attribute, AttributeModifier> entry : attributeEnchantment.getAttributeModifiers().entries()) {
                    AttributeModifier attributeModifier = entry.getValue();
                    Attribute attribute = entry.getKey();
                    double amount = attributeEnchantment.getAttributeModifierValue(level, attributeModifier);
                    double attributeAmount = this.getAttributeAmount(player, itemStack, attribute, amount);
                    this.makeTooltips(tooltip, attribute, attributeModifier, attributeAmount, ChatFormatting.GOLD);
                }
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
    private void makeTooltips(List<Component> tooltip, Attribute attribute, AttributeModifier attributeModifier, double attributeAmount, ChatFormatting colors, int startLine) {
        try {
            double amount = formatAttributeValues(attribute, attributeModifier, attributeAmount);
            ChatFormatting color = colors == null ? Tooltips.attributeColorFormat(attributeAmount) : colors;
            Component displayName = Component.translatable(attribute.getDescriptionId());
            String isPositive = amount > 0.0D ? "plus" : "take";
            amount *= attributeAmount > 0.0D ? 1.0D : -1.0D;
            if (amount == 0.0D) return;
            if (checkIfPercentage(attribute)) {
                tooltip.set(startLine, CommonComponents.space().append(Component.translatable(String.format("%s.attributes.%s.percent", BeyondHorizon.ID, isPositive), Maths.format(amount), displayName).withStyle(color)));
            } else {
                tooltip.set(startLine, CommonComponents.space().append(Component.translatable(String.format("%s.attributes.%s.%s", BeyondHorizon.ID, isPositive, attributeModifier.getOperation().toValue()), Maths.format(amount), displayName).withStyle(color)));
            }
        } catch (Exception e) {
            BeyondHorizon.LOGGER.debug("Out of bounds: {} : {}", startLine, tooltip.size());
        }
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
    private void makeTooltips(List<Component> tooltip, Attribute attribute, AttributeModifier attributeModifier, double attributeAmount, int startLine) {
         makeTooltips(tooltip, attribute, attributeModifier, attributeAmount, null, startLine);
    }
    private void makeTooltips(List<Component> tooltip, Attribute attribute, AttributeModifier attributeModifier, double attributeAmount) {
        makeTooltips(tooltip, attribute, attributeModifier, attributeAmount, null);
    }

    public void addTooltips(ItemStack itemStack, Player player, TooltipFlag context, List<Component> tooltip) {
        int lastAttributeLine = 0;
        String prefix = "attribute.modifier";

        if (BHConfigs.ATTRIBUTE_TOOLTIP_OVERHAUl) {
            for (int i = 0; i < tooltip.size(); i++) {
                var component = tooltip.get(i);
//                BeyondHorizon.LOGGER.debug("Tooltip lines: {}", this.removeTooltip(tooltip, prefix));
                lastAttributeLine = this.removeTooltip(tooltip, prefix);
            }
            this.makeAttributeTooltip(player, tooltip, itemStack, lastAttributeLine);
            this.makePotionTooltip(itemStack, tooltip, lastAttributeLine);
        }
        this.makeEnchantmentAttributeTooltip(player, tooltip, itemStack);

        for (ArmorBonusSet set : ArmorSetRegistry.getAll()) {
            if (set.contains(itemStack)) {
                set.addTooltips(tooltip, itemStack, player);
            }
        }
    }

    public void makePotionTooltip(ItemStack itemStack, List<Component> tooltips, int lastAttribueLine) {
        int remaining = tooltips.size() - lastAttribueLine;
        List<Component> lists = new ArrayList<>();
        for (int i = 0; i < remaining; i++) {
            lists.add(tooltips.get(lastAttribueLine + i));
        }
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
                tooltips.addAll(lists);
            }
        }
    }

    private int removeTooltip(List<Component> tooltip, String startWith) {
        for (EquipmentSlot slots : EquipmentSlot.values()) {
            for (int i = 0; i < tooltip.size(); i++) {
                Component component = tooltip.get(i);
                if (component instanceof MutableComponent mutableComponents) {
                    for (Component vanillaAttribute : mutableComponents.getSiblings()) {
                        if (vanillaAttribute.getContents() instanceof TranslatableContents translatableContents) {
                            if (translatableContents.getKey().startsWith(startWith)) {
//                                tooltip.remove(i);
                                return i;
                            }
                        }
                    }
                    if (mutableComponents.getContents() instanceof TranslatableContents translatableContents) {
                        if (translatableContents.getKey().startsWith(startWith)) {
//                            tooltip.remove(i);
                            return i;
                        }
                    }
                }
            }
        }
        return 0;
    }

    private boolean checkIfPercentage(Attribute attribute) {
        return AttributePercentage.isMatch(attribute);
    }

    private double getAttributeAmount(LivingEntity entity, ItemStack itemStack, Attribute attribute, double attributeAmount) {
        if (entity != null) {
            if (attribute.equals(Attributes.ATTACK_DAMAGE)) {
                attributeAmount += entity.getAttributeBaseValue(Attributes.ATTACK_DAMAGE);
                attributeAmount += EnchantmentHelper.getDamageBonus(itemStack, MobType.UNDEFINED);
            }
            if (attribute.equals(Attributes.ATTACK_SPEED)) {
                attributeAmount += entity.getAttributeBaseValue(Attributes.ATTACK_SPEED);
            }
            if (attribute.equals(ForgeMod.ENTITY_REACH.get())) {
                attributeAmount += entity.getAttributeBaseValue(ForgeMod.ENTITY_REACH.get());
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
