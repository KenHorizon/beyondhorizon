package com.kenhorizon.beyondhorizon.server.skills.accessory;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface IAccessoryItems<T extends Item> {

    boolean has(Accessory accessory);

    List<Accessory> getAllAccessory();

    T getItem();

    boolean isCompatible(ItemStack inSlot, ItemStack outside);
}