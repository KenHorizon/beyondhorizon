package com.kenhorizon.beyondhorizon.server.api.accessory;

import com.kenhorizon.beyondhorizon.server.api.inventory.IStackHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface IAccessoryStackHandler {

    IStackHandler getStacks();

    int getSlots();

    CompoundTag serializeNBT();

    void deserializeNBT(CompoundTag nbt);

}