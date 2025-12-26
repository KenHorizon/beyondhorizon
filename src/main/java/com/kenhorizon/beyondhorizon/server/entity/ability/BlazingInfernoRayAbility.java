package com.kenhorizon.beyondhorizon.server.entity.ability;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class BlazingInfernoRayAbility extends AbstractDeathRayAbility {
    public BlazingInfernoRayAbility(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public BlazingInfernoRayAbility(EntityType<? extends AbstractDeathRayAbility> type, Level world, LivingEntity caster, double x, double y, double z, float yaw, float pitch, int duration) {
        super(type, world, caster, x, y, z, yaw, pitch, duration);
    }

    public BlazingInfernoRayAbility(EntityType<? extends AbstractDeathRayAbility> type, Level world, LivingEntity caster, double x, double y, double z, float yaw, float pitch, int duration, float range) {
        super(type, world, caster, x, y, z, yaw, pitch, duration, range);
    }

    @Override
    protected float getTrailA() {
        return 1.0F;
    }

    @Override
    protected float getTrailR() {
        return 1.0F;
    }

    @Override
    protected float getTrailG() {
        return 0.0F;
    }

    @Override
    protected float getTrailB() {
        return 0.0F;
    }
}
