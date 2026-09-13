package com.kenhorizon.beyondhorizon.server.entity.ability;

import com.kenhorizon.beyondhorizon.server.entity.ability.beam.AbstractDeathRayAbility;
import com.kenhorizon.beyondhorizon.server.entity.mobs.FayeWildfire;
import com.kenhorizon.beyondhorizon.server.init.BHEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class TwilightRayAbility extends AbstractDeathRayAbility {
    public TwilightRayAbility(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.setColor(255, 116, 0);
        this.setScale(0.5F);
    }

    public TwilightRayAbility(Level world, LivingEntity caster, double x, double y, double z, float yaw, float pitch, int duration) {
        super(BHEntity.TWILIGHT_RAY.get(), world, caster, x, y, z, yaw, pitch, duration);
        this.setColor(255, 116, 0);
        this.setScale(0.5F);
    }

    public TwilightRayAbility(Level world, LivingEntity caster, double x, double y, double z, float yaw, float pitch, int duration, float range) {
        super(BHEntity.TWILIGHT_RAY.get(), world, caster, x, y, z, yaw, pitch, duration, range);
        this.setColor(255, 116, 0);
        this.setScale(0.5F);
    }

    @Override
    protected void updateWithMob() {
        if (this.caster instanceof FayeWildfire) {
            this.setYaw((float) ((caster.yHeadRot + 90) * Math.PI / 180.0D));
            this.setPitch((float) (-caster.getXRot() * Math.PI / 180.0D));
            Vec3 vecOffset1 = new Vec3(0, 0, 0.6).yRot((float) Math.toRadians(-caster.getYRot()));
            Vec3 vecOffset2 = new Vec3(1.2, 0, 0).yRot(-getYaw()).xRot(getPitch());
            this.setPos(caster.getX() + vecOffset1.x() + vecOffset2.x(), caster.getY() + (caster.getBbHeight() / 2) + vecOffset1.y() + vecOffset2.y(), caster.getZ() + vecOffset1.z() + vecOffset2.z());

        } else {
            super.updateWithMob();
        }
    }
    @Override
    protected void spawnExplosionParticles(int amount) {
        for (int i = 0; i < amount; i++) {
            final float velocity = 0.1F;
            float yaw = (float) (random.nextFloat() * 2 * Math.PI);
            float motionY = random.nextFloat() * 0.08F;
            float motionX = velocity * Mth.cos(yaw);
            float motionZ = velocity * Mth.sin(yaw);
            level().addParticle(ParticleTypes.WHITE_ASH, collidePosX, collidePosY + 0.1, collidePosZ, motionX, motionY, motionZ);
        }
        for (int i = 0; i < amount / 2; i++) {
            level().addParticle(ParticleTypes.CLOUD, collidePosX, collidePosY + 0.1, collidePosZ, 0, 0, 0);
        }
    }
}
