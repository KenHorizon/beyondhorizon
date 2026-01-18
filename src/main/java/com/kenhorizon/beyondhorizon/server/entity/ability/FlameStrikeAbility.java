package com.kenhorizon.beyondhorizon.server.entity.ability;

import com.kenhorizon.beyondhorizon.client.particle.TrailParticles;
import com.kenhorizon.beyondhorizon.client.particle.world.ParticleTrailOptions;
import com.kenhorizon.beyondhorizon.server.entity.projectiles.HellfireOrb;
import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import com.kenhorizon.beyondhorizon.server.init.BHEntity;
import com.kenhorizon.beyondhorizon.server.init.BHParticle;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class FlameStrikeAbility extends AbilityEntity {
    public FlameStrikeAbility(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.setDuration(40);
        this.setRadius(2.5F);
    }

    public static void spawn(Level level, double x, double y, double z, float damage, float radius, int duration, LivingEntity entity) {
        FlameStrikeAbility ability = new FlameStrikeAbility(BHEntity.FLAME_STRIKE.get(), level);
        ability.setBaseDamage(damage);
        ability.setCaster(entity);
        ability.setCasterID(entity.getUUID());
        ability.setRadius(radius);
        ability.setDuration(duration);
        ability.setPos(x, y, z);
        level.addFreshEntity(ability);
    }

    public static void spawn(Level level, double x, double y, double z, float damage, LivingEntity entity) {
        FlameStrikeAbility ability = new FlameStrikeAbility(BHEntity.FLAME_STRIKE.get(), level);
        ability.setBaseDamage(damage);
        ability.setCaster(entity);
        ability.setCasterID(entity.getUUID());
        ability.setPos(x, y, z);
        level.addFreshEntity(ability);
    }

    @Override
    protected void onDuration() {
        if (this.level().isClientSide()) {
            if (this.tickCount % 5 == 0) {
                this.level().addParticle(ParticleTypes.FLAME, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), 0.0D, 0.0D, 0.0D);
                this.particleTrail(32);
            }
        }
    }

    private void particleTrail(int count) {
        int particleCount = count;
        while (particleCount --> 0) {
            double radius = this.getRadius();
            float yaw = (float) (this.random.nextFloat() * 2 * Math.PI);
            float pitch = (float) (this.random.nextFloat() * 2 * Math.PI);
            double ox = (float) (radius * Math.sin(yaw) * Math.sin(pitch));
            double oz = (float) (radius * Math.cos(yaw) * Math.sin(pitch));
            ParticleTrailOptions.add(this.level(), TrailParticles.Behavior.FADE, this.getX() + ox, this.getY(), this.getZ() + oz, 3.0F, 1, 0.0F, 0.0F, 1.0F, 20, new Vec3(this.getX() + ox, this.getY() + 10, this.getZ() + oz));
        }
    }

    @Override
    protected void onEnd() {
        if (this.level().isClientSide()) {
            this.circleParticle(6);
        }
        level().addParticle(BHParticle.HELLFIRE_ORB_EXPLOSION.get(), getX(), getY() + 0.1, getZ(), 0, 0, 0);
        this.dealDamage();
    }

    public void dealDamage() {
        LivingEntity attacker = this.getCaster();
        List<Entity> cleaveRange = this.level().getEntities(this, this.getBoundingBox().inflate(this.getRadius()));
        for (Entity entityOnRange : cleaveRange) {
            if (entityOnRange instanceof LivingEntity targetOnRange) {
                if (targetOnRange == attacker || targetOnRange == this.getTarget()) continue;
                if (targetOnRange.isAlive() && !targetOnRange.isInvulnerable()) {
                    targetOnRange.hurt(BHDamageTypes.magicDamage(this), this.getBaseDamage());
                }
            }
        }
    }

    private void circleParticle(int amount) {
        for (int i = 0; i < amount; i++) {
            final float velocity = 0.1F;
            float yaw = (float) (i * (2 * StrictMath.PI / amount));
            float vy = random.nextFloat() * 0.08F;
            float vx = velocity * Mth.cos(yaw);
            float vz = velocity * Mth.sin(yaw);
            level().addParticle(ParticleTypes.FLAME, getX(), getY() + 0.1, getZ(), vx, vy, vz);
        }
        for (int i = 0; i < amount / 2; i++) {
            level().addParticle(ParticleTypes.LAVA, getX(), getY() + 0.1, getZ(), 0, 0, 0);
        }
    }
}
