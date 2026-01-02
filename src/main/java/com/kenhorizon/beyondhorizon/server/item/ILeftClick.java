package com.kenhorizon.beyondhorizon.server.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface ILeftClick {

    default boolean preventClickOthers() {
        return false;
    }

    boolean onLeftClick(ItemStack stack, LivingEntity player);
}
