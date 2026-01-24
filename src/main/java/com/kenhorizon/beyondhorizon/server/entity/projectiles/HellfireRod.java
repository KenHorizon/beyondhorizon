package com.kenhorizon.beyondhorizon.server.entity.projectiles;

import com.kenhorizon.beyondhorizon.client.particle.RingParticles;
import com.kenhorizon.beyondhorizon.client.particle.world.RingParticleOptions;
import com.kenhorizon.beyondhorizon.client.render.util.ColorUtil;
import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import com.kenhorizon.beyondhorizon.server.init.BHEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

public class HellfireRod extends ExtendedProjectile {
    public HellfireRod(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
        this.setIgniteAttack(true);
    }

    public HellfireRod(EntityType<? extends Projectile> entityType, double x, double y, double z, Level level) {
        this(entityType, level);
        this.setPos(x, y, z);
    }

    public HellfireRod(Level level, double x, double y, double z, LivingEntity shooter) {
        this(BHEntity.HELLFIRE_ROD.get(), level);
        this.setOwner(shooter);
        this.setPos(x, y, z);
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (this.level().isClientSide()) {
            this.discard();
        }
    }

    @Override
    public void tick() {
        Entity entity = this.getOwner();
        if (this.level().isClientSide() || (entity == null || !entity.isRemoved()) && this.level().hasChunkAt(this.blockPosition())) {
            super.tick();
            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (hitresult.getType() != HitResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, hitresult)) {
                this.onHit(hitresult);
            }
            this.checkInsideBlocks();
            Vec3 vec3 = this.getDeltaMovement();
            double k0 = this.getX() + vec3.x;
            double k1 = this.getY() + vec3.y;
            double k2 = this.getZ() + vec3.z;
            if (this.isInWater()) {
                for(int i = 0; i < 4; ++i) {
                    float f1 = 0.25F;
                    this.level().addParticle(ParticleTypes.BUBBLE, k0 - vec3.x * 0.25D, k1 - vec3.y * 0.25D, k2 - vec3.z * 0.25D, vec3.x, vec3.y, vec3.z);
                }
            }
            if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
                double d0 = vec3.horizontalDistance();
                this.setYRot((float) (Mth.atan2(vec3.x, vec3.z) * (double) (180F / (float) Math.PI)));
                this.setXRot((float) (Mth.atan2(vec3.y, d0) * (double) (180F / (float) Math.PI)));
                this.yRotO = this.getYRot();
                this.xRotO = this.getXRot();
            }
            vec3 = this.getDeltaMovement();
            double d5 = vec3.x;
            double d6 = vec3.y;
            double d1 = vec3.z;
            double d7 = this.getX() + d5;
            double d2 = this.getY() + d6;
            double d3 = this.getZ() + d1;
            double d4 = vec3.horizontalDistance();
            this.setYRot((float) (Mth.atan2(d5, d1) * (double) (180F / (float) Math.PI)));
            this.setXRot((float) (Mth.atan2(d6, d4) * (double) (180F / (float) Math.PI)));
            this.setXRot(lerpRotation(this.xRotO, this.getXRot()));
            this.setYRot(lerpRotation(this.yRotO, this.getYRot()));

            float yaw = (float) Math.toRadians(-this.getYRot());
            float yaw1 = (float) Math.toRadians(-this.getYRot() + 180.0F);
            float pitch = (float) Math.toRadians(-this.getXRot());

            float r = ColorUtil.getFARGB(0xFFFFFF)[0];
            float g = ColorUtil.getFARGB(0xFFFFFF)[1];
            float b = ColorUtil.getFARGB(0xFFFFFF)[2];
            this.level().addAlwaysVisibleParticle(new RingParticleOptions(yaw, pitch, 15, r, g, b, 1.0F, 4.0F, false, RingParticles.Behavior.SHRINK), this.getX(), this.getY() + this.getBbHeight() / 2 - 0.15F, this.getZ(), 0, 0, 0);
            this.level().addAlwaysVisibleParticle(new RingParticleOptions(yaw1, pitch, 15, r, g, b, 1.0F, 4.0F, false, RingParticles.Behavior.SHRINK), this.getX(), this.getY() + this.getBbHeight() / 2 - 0.15F, this.getZ(), 0, 0, 0);

            float f = 0.99F;
            float f1 = 0.05F;
            float sqrt = (float)this.getDeltaMovement().length();
            if (sqrt < 0.1F) {
                this.discard();
            }
            this.setDeltaMovement(vec3.scale((double) f));
            this.setPos(d7, d2, d3);
        } else {
            this.discard();
        }
    }

    @Override
    public DamageSource setDamageSource() {
        return BHDamageTypes.blazingRod(this, this.getOwner());
    }
}
