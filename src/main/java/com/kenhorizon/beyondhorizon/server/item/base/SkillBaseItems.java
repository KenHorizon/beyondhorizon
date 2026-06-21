package com.kenhorizon.beyondhorizon.server.item.base;

import com.kenhorizon.beyondhorizon.server.Utils;
import com.kenhorizon.beyondhorizon.server.api.data.IItemProperties;
import com.kenhorizon.beyondhorizon.server.api.skills.Skill;
import com.kenhorizon.beyondhorizon.server.api.skills.SkillBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.MendingEnchantment;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

public class SkillBaseItems {
    private final Item item;
    public SkillBaseItems(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public void appendHoverText(ItemStack itemStack, List<Component> tooltip, List<Skill> skills) {
        int size = skills.size();
        for (int i = 0; i < skills.size(); i++) {
            Skill skill = skills.get(i);
            if (!skill.getAttributeModifiers().isEmpty()) {
                size--;
                skill.addTooltipAttributes(itemStack, tooltip);
            }
            skill.addTooltip(itemStack, tooltip, size, Utils.isShiftPressed(), i == 0);
        }
    }

    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int slot, boolean isSelected, List<Skill> skills) {
        if (entity instanceof LivingEntity living) {
            if (skills != null) {
                skills.forEach((skill) -> {
                    skill.entityProperties().ifPresent(callback -> {
                        callback.onItemUpdate(itemStack, level, living, slot, isSelected);
                    });
                });
            }
        }
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment, List<Skill> skills, Tier tier) {
        for (Skill skill : skills) {
            if (skill.isEnchantmentCompatible(enchantment)) {
                return true;
            } else if (skill.isEnchantmentIncompatible(enchantment)) {
                return false;
            }
        }
        if (enchantment instanceof MendingEnchantment && tier.getUses() < 0) {
            return false;
        }
        return false;
    }
    public void onUseTick(Level level, LivingEntity entity, ItemStack itemStack, int remainingUseDuration, SkillBuilder builder) {
        if (entity instanceof Player player) {
//            if (skills != null) {
//                skills.forEach((skill) -> {
//                    skill.itemProperties().ifPresent(callback -> {
//                        callback.onUsingTick(level, player, itemStack, remainingUseDuration);
//                    });
//                });
//            }
            Optional<Skill> actionTrait = builder.getActionTrait();
            if (actionTrait.isPresent()) {
                Skill trait = actionTrait.get();
                if (trait.itemProperties().isPresent()) {
                    trait.itemProperties().get().onUsingTick(level, player, itemStack, remainingUseDuration);
                }
            }
        }
    }

    public void releaseUsing(Level level, LivingEntity entity, ItemStack itemStack, int remainingUseDuration, SkillBuilder builder) {
        if (entity instanceof Player player) {
            Optional<Skill> actionTrait = builder.getActionTrait();
            if (actionTrait.isPresent()) {
                Skill trait = actionTrait.get();
                if (trait.itemProperties().isPresent()) {
                    trait.itemProperties().get().releaseUsing(itemStack, level, player, remainingUseDuration);
                }
            }
        }
    }

    public int getUseDuration(ItemStack itemStack, int original, SkillBuilder builder) {
        Optional<Skill> actionTrait = builder.getActionTrait();
        if (actionTrait.isPresent()) {
            Skill trait = actionTrait.get();
            if(trait.itemProperties().isPresent())
                return trait.itemProperties().get().getUseDuration(itemStack);
        }
        return original;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, ItemStack itemStack, InteractionHand hand, SkillBuilder builder) {
        Optional<Skill> actionTrait = builder.getActionTrait();
        if (actionTrait.isPresent()) {
            Skill trait = actionTrait.get();
            if(trait.itemProperties().isPresent())
                return trait.itemProperties().get().use(itemStack, level, player, hand);
        }
        return null;
    }

    public void finishUsingItem(Level level, Player player, ItemStack itemStack, SkillBuilder builder) {
        Optional<Skill> actionTrait = builder.getActionTrait();
        if (actionTrait.isPresent()) {
            Skill trait = actionTrait.get();
            if(trait.itemProperties().isPresent())
                trait.itemProperties().get().finishedUsingItem(itemStack, level, player);
        }
    }
}
