package com.kenhorizon.beyondhorizon.server.entity.projectiles;

import com.kenhorizon.beyondhorizon.server.init.*;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class HellfireOrb extends MortalProjectile {
    public enum EffectOnHit {
        STUN,
        SLOW
    }
    public double prevDeltaMovementX;
    public double prevDeltaMovementY;
    public double prevDeltaMovementZ;

    private EffectOnHit effect = EffectOnHit.SLOW;
    public HellfireOrb(EntityType<? extends MortalProjectile> entityType, Level level) {
        super(entityType, level);
        this.setBaseDamage(4);
        this.setRadius(0.5F);
    }

    public HellfireOrb(Level level, EffectOnHit effect, LivingEntity shooter) {
        super(BHEntity.HELLFIRE_ORB.get(), level, shooter);
        this.effect = effect;
        this.setRadius(0.5F);
    }

    @Override
    public void afterGotHit(LivingEntity entity) {
        if (this.effect == EffectOnHit.SLOW) {
            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, Maths.sec(1), 1));
        }
        if (this.effect == EffectOnHit.STUN) {
            entity.addEffect(new MobEffectInstance(BHEffects.STUN.get(), Maths.sec(1), 1));
        }
    }

    protected void makeTrail() {
        if (this.level().isClientSide()) {
            for (int i = 0; i < 5; i++) {
                double dx = this.getX() + 0.5F * (this.random.nextFloat() - 0.5F);
                double dy = this.getY() + 0.5F * (this.random.nextFloat() - 0.5F);
                double dz = this.getZ() + 0.5F * (this.random.nextFloat() - 0.5F);
                this.level().addParticle(BHParticle.HELLFIRE_ORB_TRAIL.get(), dx, dy, dz, 0, 0, 0);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.makeTrail();
        this.prevDeltaMovementX = this.getDeltaMovement().x;
        this.prevDeltaMovementY = this.getDeltaMovement().y;
        this.prevDeltaMovementZ = this.getDeltaMovement().z;
        Vec3 vec3 = this.getDeltaMovement();
        this.setYRot(-((float) Mth.atan2(vec3.x, vec3.z)) * (180F / (float)Math.PI)) ;

    }

    @Override
    public ParticleOptions getExplosionParticles() {
        return BHParticle.HELLFIRE_ORB_EXPLOSION.get();
    }

    @Override
    public DamageSource setDamageSource(LivingEntity entity) {
        return BHDamageTypes.magicDamage(this, entity);
    }
}
