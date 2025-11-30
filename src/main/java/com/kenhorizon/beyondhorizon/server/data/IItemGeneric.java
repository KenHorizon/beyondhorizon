package com.kenhorizon.beyondhorizon.server.data;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface IItemGeneric {

    default void onItemUpdate(ItemStack itemStack, Level level, LivingEntity entity, int itemSlot, boolean isSelected) {}

}
