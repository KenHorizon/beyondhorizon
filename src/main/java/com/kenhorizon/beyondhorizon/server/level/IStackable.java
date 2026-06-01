package com.kenhorizon.beyondhorizon.server.level;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface IStackable extends INBTSerializable<CompoundTag> {

    String tagName();

    void addStack(int amount);

    void removeStack(int amount);

    void onDeath(int amount);

    int getStacks();

    void reset();

}
