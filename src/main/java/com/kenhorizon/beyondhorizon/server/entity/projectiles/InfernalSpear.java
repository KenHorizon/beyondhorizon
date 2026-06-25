package com.kenhorizon.beyondhorizon.server.entity.projectiles;

import com.kenhorizon.beyondhorizon.server.init.BHEntity;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.*;

public class InfernalSpear extends BaseSpearProjectile {
    public InfernalSpear(EntityType<? extends BaseSpearProjectile> entityType, Level level) {
        super(entityType, level);
        this.setRadius(2.5F);
    }

    public InfernalSpear(EntityType<? extends BaseSpearProjectile> entityType, Level level,
                         double x, double y, double z, double dx, double dy, double dz) {
        this(entityType, level);
        this.moveTo(x, y, z, this.getYRot(), this.getXRot());
        this.reapplyPosition();
        double d0 = Math.sqrt(dx * dx + dy * dy + dz * dz);
        this.setPos(x, y, z);
        if (d0 != 0.0D) {
            this.xPower = dx / d0 * 0.1D;
            this.yPower = dy / d0 * 0.1D;
            this.zPower = dz / d0 * 0.1D;
        }
        this.setDuration(80);
        this.setRadius(2.5F);
    }
    public InfernalSpear(Level level, DamageType damageType, LivingEntity owner, float damage,
                         double dx, double dy, double dz, boolean crit) {
        this(BHEntity.INFERNAL_SPEAR.get(), level, owner.getX(), owner.getY(), owner.getZ(), dx, dy, dz);
        this.setOwner(owner);
        this.setRot(owner.getYRot(), owner.getXRot());
        this.setBaseDamage(damage);
        this.setDamageType(damageType);
        this.setCrit(crit);
    }

    public static void spawn(Level level, LivingEntity owner, float damage, DamageType damageType, double dx, double dy, double dz, boolean crit) {
        InfernalSpear ability = new InfernalSpear(level, damageType, owner, damage, dx, dy, dz, crit);
        double spawnX = ability.getX();
        double spawnY = owner.getY(0.5D) + 0.5D;
        double spawnZ = ability.getZ();
        ability.setPos(spawnX, spawnY, spawnZ);
        level.addFreshEntity(ability);
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        var entity = entityHitResult.getEntity();
        this.getDamageType().dealDamage((LivingEntity) entity, (LivingEntity) this.getOwner(), this.getBaseDamage());
    }

    @Override
    public void onDuration() {
        HitResult raytraceresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (raytraceresult.getType() != HitResult.Type.MISS) {
            this.onHit(raytraceresult);
        }
        this.checkInsideBlocks();
        Vec3 vec3 = this.getDeltaMovement();
        double d0 = this.getX() + vec3.x;
        double d1 = this.getY() + vec3.y;
        double d2 = this.getZ() + vec3.z;
        float f = this.getInertia();
        this.setDeltaMovement(vec3.add(this.xPower, this.yPower, this.zPower).scale((double)f));
        this.setPos(d0, d1, d2);

    }
    protected void onHit(HitResult hitResult) {
        HitResult.Type hitresult$type = hitResult.getType();
        if (hitresult$type == HitResult.Type.ENTITY) {
            this.onHitEntity((EntityHitResult)hitResult);
            this.level().gameEvent(GameEvent.PROJECTILE_LAND, hitResult.getLocation(), GameEvent.Context.of(this, (BlockState)null));
        } else if (hitresult$type == HitResult.Type.BLOCK) {
            BlockHitResult blockhitresult = (BlockHitResult)hitResult;
            this.onHitBlock(blockhitresult);
            BlockPos blockpos = blockhitresult.getBlockPos();
            this.level().gameEvent(GameEvent.PROJECTILE_LAND, blockpos, GameEvent.Context.of(this, this.level().getBlockState(blockpos)));
        }

    }

    protected void onHitBlock(BlockHitResult result) {

    }

    protected boolean canHitEntity(Entity entity) {
        return this.canHit(entity) && !entity.noPhysics;
    }


    protected boolean canHit(Entity entityHit) {
        if (!entityHit.canBeHitByProjectile()) {
            return false;
        } else {
            Entity entity = this.getOwner();
            return entity == null || !entity.isPassengerOfSameVehicle(entityHit);
        }
    }
}
