package com.kenhorizon.beyondhorizon.server.world.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public interface ILinkedEntity {
    void link(Entity entity);

    LivingEntity getOwner();
}
