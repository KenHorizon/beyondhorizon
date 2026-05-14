package com.kenhorizon.beyondhorizon.server.entity.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;

public class EntityData {
    public static CompoundTag getOrCreateTag(LivingEntity entity) {
        CompoundTag tag = getTag(entity);
        return tag == null ? new CompoundTag() : tag;
    }

    public static CompoundTag getTag(LivingEntity entity) {
        return entity instanceof IBHDataEntity ? ((IBHDataEntity) entity).getEntityData() : new CompoundTag();
    }

    public static void setTag(LivingEntity entity, CompoundTag tag) {
        if(entity instanceof IBHDataEntity) {
            ((IBHDataEntity) entity).setEntityData(tag);
        }
    }
    public static void setFlags(LivingEntity entity, int flag, boolean set) {
        if (entity instanceof IBHDataEntity) {
            if (((IBHDataEntity) entity).getBHSharedFlags(flag) != checkFlags(entity, flag, set)) {
                ((IBHDataEntity) entity).setBHSharedFlags(flag, set);
            }
        }
    }
    private static boolean checkFlags(LivingEntity entity, int flag, boolean set) {
        return !entity.level().isClientSide() && set || ((IBHDataEntity) entity).getBHSharedFlags(flag);
    }
}
