package com.kenhorizon.beyondhorizon.server.api.data;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public interface IItemProperties {
    default InteractionResult useOn(UseOnContext contextIn) {
        return InteractionResult.PASS;
    }

    default UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.NONE;
    }
//
//    default WeaponArmPose getWeaponArmPose() {
//        return WeaponArmPose.EMPTY;
//    }
//
//    default WeaponAnimations getWeaponAnimations() {
//        return WeaponAnimations.EMPTY;
//    }

    default InteractionResultHolder<ItemStack> use(ItemStack itemStack, Level level, Player player, InteractionHand hand) {
        return InteractionResultHolder.pass(itemStack);
    }

    default void onUsingTick(Level level, LivingEntity entity, ItemStack itemStack, int remainingUseDuration, float attackDamage) {}

    default void releaseUsing(ItemStack itemStack, Level level, LivingEntity entity, int chargedDuration, float attackDamage) {}

}
