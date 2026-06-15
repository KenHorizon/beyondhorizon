package com.kenhorizon.beyondhorizon.server.entity.ability;

import com.kenhorizon.beyondhorizon.server.init.BHEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class BlazingInfernoRayAbility extends AbstractDeathRayAbility {
    public BlazingInfernoRayAbility(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.setColor(0, 148, 255);
    }

    public BlazingInfernoRayAbility(Level world, LivingEntity caster, double x, double y, double z, float yaw, float pitch, int duration) {
        super(BHEntity.BLAZING_INFERNO_RAY.get(), world, caster, x, y, z, yaw, pitch, duration);
        this.setColor(0, 148, 255);
    }

    public BlazingInfernoRayAbility(Level world, LivingEntity caster, double x, double y, double z, float yaw, float pitch, int duration, float range) {
        super(BHEntity.BLAZING_INFERNO_RAY.get(), world, caster, x, y, z, yaw, pitch, duration, range);
        this.setColor(0, 148, 255);
    }
}
