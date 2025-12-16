package com.kenhorizon.beyondhorizon.server.api.accessory;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface IAccessoryItems<T extends Item> {

    boolean has(Accessory skill);

    List<Accessory> getAccessories();

    T getItem();

    boolean isCompatible(ItemStack inSlot, ItemStack outside);
}