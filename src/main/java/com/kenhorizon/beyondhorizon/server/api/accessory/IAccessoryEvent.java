package com.kenhorizon.beyondhorizon.server.api.accessory;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public interface IAccessoryEvent {

    default boolean onKeybindPressed(Player player, ItemStack itemStack, int slot) {
        return false;
    }

    default void onUnequip(Player player, ItemStack itemStack, int slot) {}

    default void onEquip(Player player, ItemStack itemStack, int slot) {}

}
