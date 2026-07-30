package com.kenhorizon.beyondhorizon.server.api.stackable_tags;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Collection;
import java.util.Map;
import java.util.SortedMap;

public interface IStackableInstance extends INBTSerializable<CompoundTag> {

    void tick(LivingEntity entity);

    void instance(LivingEntity entity);

    Map<String, StackableTags> getAllRegistry();

    StackableTags[] getAllRegistryOnEntity(LivingEntity entity);

    StackableTags makeInstance(StackableTags instance);

    Collection<StackableTags> getStackableTags();

}
