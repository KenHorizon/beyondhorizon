package com.kenhorizon.beyondhorizon.server.entity.ability;

import com.kenhorizon.beyondhorizon.client.particle.RingParticles;
import com.kenhorizon.beyondhorizon.client.particle.world.RingParticleOptions;
import com.kenhorizon.beyondhorizon.client.particle.world.SlashParticleOptions;
import com.kenhorizon.beyondhorizon.client.render.util.ColorUtil;
import com.kenhorizon.beyondhorizon.server.api.skills.WeaponActiveSkills;
import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import com.kenhorizon.beyondhorizon.server.init.BHEntity;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageType;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageTypeTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.*;

import java.util.List;
import java.util.Optional;

public class InfernalSlashAbility extends AbilityEntity {
    public double xPower;
    public double yPower;
    public double zPower;

    public InfernalSlashAbility(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public InfernalSlashAbility(EntityType<?> entityType, Level level,
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
        this.setDuration(80);
        this.setRadius(1.5F);
    }
    public InfernalSlashAbility(Level level, DamageType damageType, LivingEntity owner, float damage,
                                double dx, double dy, double dz) {
        this(BHEntity.INFERNAL_SLASH_ABILITY.get(), level, owner.getX(), owner.getY(), owner.getZ(), dx, dy, dz);
        this.setCaster(owner);
        this.setRot(owner.getYRot(), owner.getXRot());
        this.setBaseDamage(damage);
        this.setDamageType(damageType);
    }

    public static void spawn(Level level, LivingEntity owner, float damage, DamageType damageType, double dx, double dy, double dz) {
        InfernalSlashAbility ability = new InfernalSlashAbility(level, damageType, owner, damage, dx, dy, dz);
        double spawnX = ability.getX();
        double spawnY = owner.getY(0.5D) + 0.5D;
        double spawnZ = ability.getZ();
        ability.setPos(spawnX, spawnY, spawnZ);
        level.addFreshEntity(ability);
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        var entity = entityHitResult.getEntity();
        this.getDamageType().dealDamage((LivingEntity) entity, this.getCaster(), this.getBaseDamage());
    }

    @Override
    public void spawnParticles() {
        var owner = this.getCaster();
        if (!this.level().isClientSide() || owner == null) {
            return;
        }

        var color = ColorUtil.RED;
        double d0 = this.getX();
        double d1 = this.getY() + this.getBbHeight() / 2;
        double d2 = this.getZ();

        float r = ColorUtil.getFARGB(color)[0];
        float g = ColorUtil.getFARGB(color)[1];
        float b = ColorUtil.getFARGB(color)[2];

        float yaw = (float) Math.toRadians(this.getYRot() + 90);
        float pitch = (float) Math.toRadians(-this.getXRot() + 180);
        this.level().addParticle(new SlashParticleOptions(yaw, pitch, r, g, b, 1, 0.50F), d0, d1, d2, 0, 0, 0);
    }

    @Override
    public void tick() {

        super.tick();
    }

    @Override
    protected void onDuration() {
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
        this.level().addParticle(ParticleTypes.FLAME, this.getX() - vec3.x, this.getY() - vec3.y, this.getZ() - vec3.z, 0.0D, 0.0D, 0.0D);
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
    protected float getInertia() {
        return 0.85F;
    }
}
