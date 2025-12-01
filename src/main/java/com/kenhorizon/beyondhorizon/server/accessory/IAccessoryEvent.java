package com.kenhorizon.beyondhorizon.server.accessory;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IAccessoryEvent {

    default void onChangePrevAccessorySlot(Player player, ItemStack itemStack) {}

    default void onChangePostAccessorySlot(Player player, ItemStack itemStack) {}
}
