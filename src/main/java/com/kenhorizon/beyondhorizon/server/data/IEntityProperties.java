package com.kenhorizon.beyondhorizon.server.data;

import com.google.common.collect.ImmutableMultimap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface IEntityProperties {

    default void onItemUpdate(ItemStack itemStack, Level level, LivingEntity entity, int itemSlot, boolean isSelected) {}

    default void addAttributes(ImmutableMultimap.Builder<Attribute, AttributeModifier> builder) {}

    default void onEntityUpdate(LivingEntity entity, ItemStack itemStack) {}

    default void onEntityJump(LivingEntity entity, ItemStack itemStack) {}

    default void onChangeEquipment(LivingEntity entity, ItemStack itemStack, boolean hasChanged) {}

    default double onModifyMiningSpeed(Player player, BlockState blockState, BlockPos blockPos, double originalSpeed) {
        return 0.0D;
    }

    default int modifyExprienceDrop(int dropExperience, LivingEntity target, Player player) {
        return dropExperience;
    }
}
