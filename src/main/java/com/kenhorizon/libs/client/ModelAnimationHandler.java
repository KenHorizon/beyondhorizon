package com.kenhorizon.libs.client;

import com.kenhorizon.beyondhorizon.server.item.IArmPose;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public enum ModelAnimationHandler {
    INSTANCE;
    public <T extends LivingEntity> WeaponArmPose getWeaponArmPose(T entity, InteractionHand hand) {
        ItemStack itemStack = entity.getItemInHand(hand);
        if (!itemStack.isEmpty()) {
            if (itemStack.getItem() instanceof IArmPose armPose) {
                if (entity instanceof Player player) {
                    WeaponAnimations weaponAnimations = armPose.getWeaponAnimations(player, itemStack);
                    if (entity.getUsedItemHand() == hand && entity.getUseItemRemainingTicks() > 0) {
                        if (weaponAnimations == WeaponAnimations.HOLDING && hand == entity.getUsedItemHand()) {
                            return WeaponArmPose.HOLDING;
                        }
                        if (weaponAnimations == WeaponAnimations.GUARDIAN_SWORD && hand == entity.getUsedItemHand()) {
                            return WeaponArmPose.GUARDIAN_SWORD;
                        }
                        if (weaponAnimations == WeaponAnimations.HOLDING_ALT && hand == entity.getUsedItemHand()) {
                            return WeaponArmPose.HOLDING_1;
                        }
                    }
                }
            }
        }
        return WeaponArmPose.EMPTY;
    }
}
