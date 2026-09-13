package com.kenhorizon.beyondhorizon.server.entity.ability;

import com.kenhorizon.beyondhorizon.server.entity.ability.beam.AbstractDeathRayAbility;
import com.kenhorizon.beyondhorizon.server.init.BHEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
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
    @Override
    protected void spawnExplosionParticles(int amount) {
        for (int i = 0; i < amount; i++) {
            final float velocity = 0.1F;
            float yaw = (float) (random.nextFloat() * 2 * Math.PI);
            float motionY = random.nextFloat() * 0.08F;
            float motionX = velocity * Mth.cos(yaw);
            float motionZ = velocity * Mth.sin(yaw);
            level().addParticle(ParticleTypes.SMOKE, collidePosX, collidePosY + 0.1, collidePosZ, motionX, motionY, motionZ);
            level().addParticle(ParticleTypes.ASH, collidePosX, collidePosY + 0.1, collidePosZ, motionX, motionY, motionZ);
            level().addParticle(ParticleTypes.WHITE_ASH, collidePosX, collidePosY + 0.1, collidePosZ, motionX, motionY, motionZ);
            level().addParticle(ParticleTypes.SOUL_FIRE_FLAME, collidePosX, collidePosY + 0.1, collidePosZ, motionX, motionY, motionZ);
        }
        for (int i = 0; i < amount / 2; i++) {
            level().addParticle(ParticleTypes.LAVA, collidePosX, collidePosY + 0.1, collidePosZ, 0, 0, 0);
        }
    }
}
