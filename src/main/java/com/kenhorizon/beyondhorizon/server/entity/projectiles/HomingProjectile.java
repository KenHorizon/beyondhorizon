package com.kenhorizon.beyondhorizon.server.entity.projectiles;


import com.kenhorizon.beyondhorizon.client.particle.TrailParticles;
import com.kenhorizon.beyondhorizon.client.particle.world.TrailParticleOptions;
import com.kenhorizon.beyondhorizon.client.render.util.Colors;
import com.kenhorizon.beyondhorizon.server.entity.boss.pyrolliger.Pyrolliger;
import com.kenhorizon.beyondhorizon.server.init.BHEffects;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageType;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.UUID;

public abstract class HomingProjectile extends ExtendedProjectile {
    @Nullable
    private Entity finalTarget;
    @Nullable
    private UUID targetId;
    protected Vec3 targetPos = Vec3.ZERO;
    public static final String NBT_TARGET_ID = "TargetUUID";

    public HomingProjectile(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
        this.setDelay(Maths.sec(3));
        this.setDuration(160);
        this.setIgniteAttack(true);
    }
    public HomingProjectile(EntityType<? extends Projectile> entityType, Level level,
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
    }
    public HomingProjectile(EntityType<? extends Projectile> entityType, Level level, DamageType damageType, LivingEntity owner, float damage,
                            double dx, double dy, double dz, boolean crit) {
        this(entityType, level, owner.getX(), owner.getY(), owner.getZ(), dx, dy, dz);
        this.setOwner(owner);
        this.setRot(owner.getYRot(), owner.getXRot());
        this.setBaseDamage(damage);
        this.setDamageType(damageType);
        this.setCrit(crit);
    }

    public HomingProjectile(EntityType<? extends Projectile> entityType, Level level, LivingEntity shooter) {
        this(entityType, level);
        this.setOwner(shooter);
        this.setRot(shooter.getYRot(), shooter.getXRot());
        this.moveTo(shooter.getX(), shooter.getY(), shooter.getZ(), shooter.getYRot(), shooter.getXRot());

    }

    public void setTarget(@Nullable LivingEntity targetId) {
        this.finalTarget = targetId;
    }

    @Nullable
    public Entity getTarget() {
        return finalTarget;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (this.getTarget() != null) {
            tag.putUUID(NBT_TARGET_ID, this.getTarget().getUUID());
        }

    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.hasUUID(NBT_TARGET_ID)) {
            this.targetId = tag.getUUID(NBT_TARGET_ID);
        }
    }

    @Override
    public void afterGotHit(LivingEntity entity) {
        var owner = this.getOwner();
        if (owner instanceof LivingEntity owners && owners instanceof Pyrolliger boss) {
            boss.addMana(2);
        }
        entity.addEffect(new MobEffectInstance(BHEffects.BURNING_HEX.get(), Maths.sec(5)));
        this.discard();
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            if (this.finalTarget == null || !this.finalTarget.isAlive() || (this.finalTarget instanceof Player && this.finalTarget.isSpectator())) {
                this.discard();
            } else {
                if (this.distanceTo(this.finalTarget) > 1.5F && this.getLifeSpan() > 3 && this.getLifeSpan() < 40) {
                    Vec3 currentVelocity = this.getDeltaMovement();
                    Vec3 toTarget = this.targetPos.subtract(this.position());
                    Vec3 desiredDirection = toTarget.normalize();
                    double turnFactor = 0.16;
                    Vec3 newDirection = currentVelocity.normalize().scale(1.0 - turnFactor)
                            .add(desiredDirection.scale(turnFactor)).normalize();
                    this.assignDirectionalMovement(newDirection, this.getInertia());
                }
            }
        }
        HitResult raytraceresult = ExtendedProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (raytraceresult.getType() != HitResult.Type.MISS) {
            this.onHit(raytraceresult);
        }
        this.checkInsideBlocks();
        Vec3 vec3 = this.getDeltaMovement();
        double d0 = this.getX() + vec3.x;
        double d1 = this.getY() + vec3.y;
        double d2 = this.getZ() + vec3.z;
        float f = this.getInertia();
        float[] colors = Colors.getFARGB(Colors.GOLD);
        this.level().addParticle(new TrailParticleOptions(40, colors[0], colors[1], colors[2], colors[3], 1.0F, TrailParticles.Behavior.FADE_N_SHRINK, new Vec3(d0, d1, d2)), d0, d1, d2, 0, 0, 0);
        this.setDeltaMovement(vec3.add(this.xPower, this.yPower, this.zPower).scale((double)f));
        this.setPos(d0, d1, d2);
    }
    


    @Override
    public void onStart() {
        if (!this.level().isClientSide()) {
            if (this.finalTarget == null && this.targetId != null) {
                this.finalTarget = ((ServerLevel) this.level()).getEntity(this.targetId);
                if (this.finalTarget == null) {
                    this.targetId = null;
                }
            }
            if (!(this.finalTarget == null || !this.finalTarget.isAlive() || (this.finalTarget instanceof Player && this.finalTarget.isSpectator()))) {
                this.targetPos = this.finalTarget.position().add(0, this.finalTarget.getBbHeight() * 0.1D, 0);
            }
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        super.onHitBlock(hitResult);
        this.discard();
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        Entity entity = this.getOwner();
        int i = entity == null ? 0 : entity.getId();

        return new ClientboundAddEntityPacket(
                this.getId(),
                this.getUUID(),
                this.getX(),
                this.getY(),
                this.getZ(),
                this.getXRot(),
                this.getYRot(),
                this.getType(),
                i,
                this.getDeltaMovement(),
                0.0D
        );
    }
    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        Vec3 vec3 = new Vec3(packet.getXa(), packet.getYa(), packet.getZa());
        this.setDeltaMovement(vec3);
        this.xRotO = this.getXRot();
        this.yRotO = this.getYRot();
    }

    private void assignDirectionalMovement(Vec3 movement, double accelerationPower) {
        this.setDeltaMovement(movement.normalize().scale(accelerationPower));
        this.hasImpulse = true;
    }
    protected float getInertia() {
        return 0.879F;
    }
}