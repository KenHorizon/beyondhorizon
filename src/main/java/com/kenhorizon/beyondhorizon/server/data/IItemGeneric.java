package com.kenhorizon.beyondhorizon.server.data;

import com.google.common.collect.ImmutableMultimap;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface IItemGeneric {

    default void onItemUpdate(ItemStack itemStack, Level level, LivingEntity entity, int itemSlot, boolean isSelected) {}

    default void addAttributes(ImmutableMultimap.Builder<Attribute, AttributeModifier> builder) {}

}
