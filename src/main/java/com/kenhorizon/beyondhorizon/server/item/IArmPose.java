package com.kenhorizon.beyondhorizon.server.item;

import com.kenhorizon.libs.client.WeaponAnimations;
import com.kenhorizon.libs.client.WeaponArmPose;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IArmPose {
    default WeaponArmPose getWeaponArmPose(Player player, InteractionHand hand) {
        return WeaponArmPose.EMPTY;
    }

    default WeaponAnimations getWeaponAnimations(Player player, ItemStack itemStack) {
        return WeaponAnimations.EMPTY;
    }
}
