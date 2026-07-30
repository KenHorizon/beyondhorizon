package com.kenhorizon.beyondhorizon.server.util;

import com.kenhorizon.beyondhorizon.server.entity.util.EntityData;
import net.minecraft.world.entity.LivingEntity;

public final class GlobalTags {
    public static final String SPAWNER = "has_spawner";

    public static void setSpawner(LivingEntity entity, boolean value) {
        EntityData.getOrCreateTag(entity).putBoolean(SPAWNER, value);
    }

    public static boolean hasSpawner(LivingEntity entity) {
        return EntityData.getOrCreateTag(entity).getBoolean(SPAWNER);
    }
}
