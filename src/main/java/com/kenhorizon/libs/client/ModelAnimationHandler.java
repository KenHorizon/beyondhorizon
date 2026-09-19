package com.kenhorizon.libs.client;

import com.kenhorizon.beyondhorizon.server.level.item.classify.IArmPose;
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
                        if (hand == entity.getUsedItemHand()) {
                            if (weaponAnimations == WeaponAnimations.HOLDING) {
                                return WeaponArmPose.HOLDING;
                            }
                            if (weaponAnimations == WeaponAnimations.GUARDIAN_SWORD) {
                                return WeaponArmPose.GUARDIAN_SWORD;
                            }
                            if (weaponAnimations == WeaponAnimations.HOLDING_ALT) {
                                return WeaponArmPose.HOLDING_ALT;
                            }
                            if (weaponAnimations == WeaponAnimations.STAFF) {
                                return WeaponArmPose.STAFF;
                            }
                        }
                    }
                }
            }
        }
        return WeaponArmPose.EMPTY;
    }
}
