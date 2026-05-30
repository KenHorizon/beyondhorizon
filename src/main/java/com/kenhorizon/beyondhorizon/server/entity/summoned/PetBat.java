package com.kenhorizon.beyondhorizon.server.entity.summoned;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

public class PetBat extends BHSummonEntity {

    public PetBat(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }
}
