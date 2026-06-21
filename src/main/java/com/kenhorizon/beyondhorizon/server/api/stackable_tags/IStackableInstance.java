package com.kenhorizon.beyondhorizon.server.api.stackable_tags;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.List;

public interface IStackableInstance extends INBTSerializable<CompoundTag> {

    void tick(LivingEntity entity);

    void instance(LivingEntity entity);


    List<StackableTags> getInstance();

    StackableTags getInstance(StackableTags instance);
}
