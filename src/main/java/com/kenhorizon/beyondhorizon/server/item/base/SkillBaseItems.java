package com.kenhorizon.beyondhorizon.server.item.base;

import com.kenhorizon.beyondhorizon.server.api.skills.ISkillItems;
import com.kenhorizon.beyondhorizon.server.api.skills.Skill;
import com.kenhorizon.libs.client.WeaponAnimations;
import com.kenhorizon.libs.client.WeaponArmPose;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.MendingEnchantment;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SkillBaseItems {
    private final ISkillItems skillItem;
    private List<Skill> skills = new ArrayList<>();
    private List<Optional<Skill>> activeSkills = new ArrayList<>();
    public SkillBaseItems(ISkillItems skillItems) {
        this.skillItem = skillItems;
    }

    public void setSkills(List<Skill> skills, List<Optional<Skill>> activeSkills) {
        this.skills = skills;
        this.activeSkills = activeSkills;
    }

    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int slot, boolean isSelected) {
        if (entity instanceof LivingEntity living) {
            this.skills.forEach((skill) -> {
                skill.entityProperties().ifPresent(callback -> {
                    callback.onItemUpdate(itemStack, level, living, slot, isSelected);
                });
            });
        }
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment, Tier tier) {
        for (Skill skill : this.skills) {
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

    public void onUseTick(Level level, LivingEntity entity, ItemStack itemStack, int remainingUseDuration) {
        if (entity instanceof Player player) {
            Optional<Skill> actionTrait = this.skillItem.getActiveSkill(itemStack);
            if (actionTrait.isPresent()) {
                Skill trait = actionTrait.get();
                if (trait.itemProperties().isPresent()) {
                    trait.itemProperties().get().onUsingTick(level, player, itemStack, remainingUseDuration);
                }
            }
        }
    }

    public void releaseUsing(Level level, LivingEntity entity, ItemStack itemStack, int remainingUseDuration) {
        if (entity instanceof Player player) {
            Optional<Skill> actionTrait = this.skillItem.getActiveSkill(itemStack);
            if (actionTrait.isPresent()) {
                Skill trait = actionTrait.get();
                if (trait.itemProperties().isPresent()) {
                    trait.itemProperties().get().releaseUsing(itemStack, level, player, remainingUseDuration);
                }
            }
        }
    }

    public int getUseDuration(ItemStack itemStack) {
        Optional<Skill> actionTrait = this.skillItem.getActiveSkill(itemStack);
        if (actionTrait.isPresent()) {
            Skill trait = actionTrait.get();
            if (trait.itemProperties().isPresent())
                return trait.itemProperties().get().getUseDuration(itemStack);
        }
        return 0;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, ItemStack itemStack, InteractionHand hand) {
        Optional<Skill> actionTrait = this.skillItem.getActiveSkill(itemStack);
        if (actionTrait.isPresent()) {
            Skill trait = actionTrait.get();
            if (trait.itemProperties().isPresent())
                return trait.itemProperties().get().use(itemStack, level, player, hand);
        }
        return InteractionResultHolder.pass(itemStack);
    }

    public ItemStack finishUsingItem(Level level, Player player, ItemStack itemStack) {
        Optional<Skill> actionTrait = this.skillItem.getActiveSkill(itemStack);
        if (actionTrait.isPresent()) {
            Skill trait = actionTrait.get();
            if (trait.itemProperties().isPresent())
                trait.itemProperties().get().finishedUsingItem(itemStack, level, player);
        }
        return itemStack;
    }

    public WeaponAnimations getWeaponAnimations(Player player, ItemStack itemStack) {
        Optional<Skill> actionTrait = this.skillItem.getActiveSkill(itemStack);
        if (actionTrait.isPresent()) {
            Skill trait = actionTrait.get();
            if (trait.itemProperties().isPresent())
                return trait.itemProperties().get().getWeaponAnimations(player, itemStack);
        }
        return WeaponAnimations.EMPTY;
    }

    public WeaponArmPose getWeaponPose(Player player, ItemStack itemStack) {
        Optional<Skill> actionTrait = this.skillItem.getActiveSkill(itemStack);
        if (actionTrait.isPresent()) {
            Skill trait = actionTrait.get();
            if (trait.itemProperties().isPresent())
                return trait.itemProperties().get().getWeaponArmPose(player, itemStack);
        }
        return WeaponArmPose.EMPTY;
    }
}
