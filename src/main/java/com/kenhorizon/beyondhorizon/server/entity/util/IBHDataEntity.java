package com.kenhorizon.beyondhorizon.server.entity.util;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;

public interface IBHDataEntity {
    CompoundTag getEntityData();

    void setEntityData(CompoundTag nbt);

    void setBHSharedFlags(int flag, boolean set);

    boolean getBHSharedFlags(int flag);
}
