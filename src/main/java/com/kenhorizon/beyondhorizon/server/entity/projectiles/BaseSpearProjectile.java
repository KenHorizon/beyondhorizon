package com.kenhorizon.beyondhorizon.server.entity.projectiles;

import com.kenhorizon.beyondhorizon.server.init.BHEntity;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

public class BaseSpearProjectile extends ExtendedProjectile {

    protected BaseSpearProjectile(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

    public BaseSpearProjectile(EntityType<? extends Projectile> entityType, Level level,
                                double x, double y, double z, double dx, double dy, double dz) {
        this(entityType, level);
        this.moveTo(x, y, z, this.getYRot(), this.getXRot());
        this.reapplyPosition();
        double d0 = Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (d0 != 0.0D) {
            this.xPower = dx / d0 * 0.1D;
            this.yPower = dy / d0 * 0.1D;
            this.zPower = dz / d0 * 0.1D;
        }
    }
    public BaseSpearProjectile(EntityType<? extends Projectile> entityType, Level level, DamageType damageType, LivingEntity owner, float damage,
                                double dx, double dy, double dz) {
        this(entityType, level, owner.getX(), owner.getY(), owner.getZ(), dx, dy, dz);
        this.setOwner(owner);
        this.setRot(owner.getYRot(), owner.getXRot());
        this.setBaseDamage(damage);
        this.setDamageType(damageType);
    }

    @Override
    public void onDuration() {
        Entity entity = this.getOwner();
        if (this.level().isClientSide() || (entity == null || !entity.isRemoved()) && this.level().hasChunkAt(this.blockPosition())) {
            super.tick();
            HitResult hitresult = this.getHitResultOnMoveVector(this, this::canHitEntity);
            if (hitresult.getType() != HitResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, hitresult)) {
                this.onHit(hitresult);
            }
            this.checkInsideBlocks();
            Vec3 vec3 = this.getDeltaMovement();
            double d0 = this.getX() + vec3.x;
            double d1 = this.getY() + vec3.y;
            double d2 = this.getZ() + vec3.z;
            ProjectileUtil.rotateTowardsMovement(this, 1.0F);
            float f = this.getInertia();
            this.spawnParticle();
            this.setDeltaMovement(vec3.add(vec3.normalize().scale((double)f)));
            this.setPos(d0, d1, d2);
        } else {
            this.discard();
        }
    }

    protected float getInertia() {
        return 0.99F;
    }
}
