package com.kenhorizon.beyondhorizon.server.api.accessory;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IAccessoryEvent {

    default boolean onKeybindPressed(Player player, ItemStack itemStack, int slot) {
        return false;
    }

    default void onChangePrevAccessorySlot(Player player, ItemStack itemStack) {}

    default void onChangePostAccessorySlot(Player player, ItemStack itemStack) {}
}
