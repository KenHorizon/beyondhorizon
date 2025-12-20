package com.kenhorizon.beyondhorizon.server.entity.boss.blazing_inferno;


import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import com.kenhorizon.beyondhorizon.server.init.BHEffects;
import com.kenhorizon.beyondhorizon.server.init.BHEntity;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import com.kenhorizon.beyondhorizon.server.util.RaycastUtil;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;

public class BlazingSpear extends Projectile {
    private static final EntityDataAccessor<Byte> ID_FLAGS = SynchedEntityData.defineId(BlazingSpear.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> PIERCE_LEVEL = SynchedEntityData.defineId(BlazingSpear.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> FIRED = SynchedEntityData.defineId(BlazingSpear.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DURATION = SynchedEntityData.defineId(BlazingSpear.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> LIFE_SPAN = SynchedEntityData.defineId(BlazingSpear.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> DELAY = SynchedEntityData.defineId(BlazingSpear.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Float> FADE = SynchedEntityData.defineId(BlazingSpear.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> DAMAGE = SynchedEntityData.defineId(BlazingSpear.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> SPEED = SynchedEntityData.defineId(BlazingSpear.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Boolean> CAN_LIGHT_FIRE = SynchedEntityData.defineId(BlazingSpear.class, EntityDataSerializers.BOOLEAN);
    protected int duration = 160;
    protected int lifespan = 0;
    protected int delay = 40;
    protected float speed = 0.25F;
    protected float fade = 0.0F;
    protected float baseDamage = 1.0F;
    protected boolean canLightFire;
    protected boolean inGround;
    public double xPower;
    public double yPower;
    public double zPower;
    private final IntOpenHashSet ignoredEntities = new IntOpenHashSet();

    public static final String NBT_DURATION = "duration";
    public static final String NBT_LIFESPAN = "lifespan";
    public static final String NBT_FADE = "fade";
    public static final String NBT_DAMAGE = "damage";
    public static final String NBT_SPEED = "speed";
    public static final String NBT_CAN_LIGHT_FIRE = "can_light_fire";
    public static final String NBT_IS_FIRED = "is_fired";
    public static final String NBT_POWER = "power";
    @Nullable
    private BlockState lastState;

    public BlazingSpear(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
        this.setDuration(160);
        this.setCanLightFire(true);
    }

    public BlazingSpear(Level level, LivingEntity shooter) {
        this(BHEntity.BLAZING_SPEAR.get(), level);
        this.setOwner(shooter);
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(ID_FLAGS, (byte) 0);
        this.entityData.define(PIERCE_LEVEL, (byte) 0);
        this.entityData.define(DAMAGE, 1.0F);
        this.entityData.define(DURATION, 160);
        this.entityData.define(LIFE_SPAN, 0);
        this.entityData.define(DELAY, 20);
        this.entityData.define(FADE, 0.0F);
        this.entityData.define(SPEED, 0.25F);
        this.entityData.define(FIRED, false);
        this.entityData.define(CAN_LIGHT_FIRE, false);
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);
        if (!this.level().isClientSide() && this.getFired()) {
            Entity entity = hitResult.getEntity();
            LivingEntity projectileOwner = (LivingEntity) this.getOwner();
            if (entity instanceof LivingEntity target) {
                target.setSecondsOnFire(5);
                if (target.hurt(BHDamageTypes.magicDamage(projectileOwner, target), this.getBaseDamage())) {
                    target.addEffect(new MobEffectInstance(BHEffects.ROOTED.get(), Maths.sec(2)));
                    if (projectileOwner != null) {
                        this.doEnchantDamageEffects(projectileOwner, entity);
                    }
                }
            }
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        super.onHitBlock(hitResult);
        if (!this.level().isClientSide()) {
            Entity entity = this.getOwner();
            if (isCanLightFire()) {
                BlockPos blockPos = hitResult.getBlockPos().relative(hitResult.getDirection());
                if (this.level().isEmptyBlock(blockPos)) {
                    this.level().setBlockAndUpdate(blockPos, BaseFireBlock.getState(this.level(), blockPos));
                }
            } else {
                if (!(entity instanceof Mob) || ForgeEventFactory.getMobGriefingEvent(this.level(), entity)) {
                    BlockPos blockPos = hitResult.getBlockPos().relative(hitResult.getDirection());
                    if (this.level().isEmptyBlock(blockPos)) {
                        this.level().setBlockAndUpdate(blockPos, BaseFireBlock.getState(this.level(), blockPos));
                    }
                }
            }
        }
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
    }

    @Override
    public void tick() {
        this.setLifeSpan(this.getLifeSpan() + 1);
        Entity entity = this.getOwner();
        if (!this.level().isClientSide()) {
            this.setFade(Mth.clamp((float) this.getLifeSpan() / this.getDelay(), 0.0F, 1.0F));
            if (!this.getFired()) {
                this.setFired(true);
            }
        }
        boolean flag = this.isNoPhysics();
        Vec3 vec3 = this.getDeltaMovement();
        if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
            double d0 = vec3.horizontalDistance();
            this.setYRot((float)(Mth.atan2(vec3.x, vec3.z) * (double)(180F / (float)Math.PI)));
            this.setXRot((float)(Mth.atan2(vec3.y, d0) * (double)(180F / (float)Math.PI)));
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
        }
        BlockPos blockpos = this.blockPosition();
        BlockState blockstate = this.level().getBlockState(blockpos);
        if (!blockstate.isAir() && !flag) {
            VoxelShape voxelshape = blockstate.getCollisionShape(this.level(), blockpos);
            if (!voxelshape.isEmpty()) {
                Vec3 vec31 = this.position();

                for(AABB aabb : voxelshape.toAabbs()) {
                    if (aabb.move(blockpos).contains(vec31)) {
                        this.inGround = true;
                        break;
                    }
                }
            }
        }
        if (this.isInWaterOrRain() || blockstate.is(Blocks.POWDER_SNOW) || this.isInFluidType((fluidType, height) -> this.canFluidExtinguish(fluidType))) {
            this.clearFire();
        }

        if (this.getLifeSpan() >= this.getDuration()) {
            this.discard();
        }
        if (this.inGround) {
            this.level().addParticle(ParticleTypes.LAVA, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
            if (this.lastState != blockstate && this.shouldFall()) {
                this.startFalling();
            } else if (!this.level().isClientSide) {
                this.tickDespawn();
            }
        } else {
            Vec3 vec32 = this.position();
            Vec3 vec33 = vec32.add(vec3);
            HitResult hitresult = this.level().clip(new ClipContext(vec32, vec33, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
            if (hitresult.getType() != HitResult.Type.MISS) {
                vec33 = hitresult.getLocation();
            }

            while(!this.isRemoved()) {
                EntityHitResult entityhitresult = this.findHitEntity(vec32, vec33);
                if (entityhitresult != null) {
                    hitresult = entityhitresult;
                }

                if (hitresult != null && hitresult.getType() == HitResult.Type.ENTITY) {
                    Entity entity1 = ((EntityHitResult)hitresult).getEntity();
                    Entity entity2 = this.getOwner();
                    if (entity1 instanceof Player && entity2 instanceof Player && !((Player)entity1).canHarmPlayer((Player)entity1)) {
                        hitresult = null;
                        entityhitresult = null;
                    }
                }

                if (hitresult != null && hitresult.getType() != HitResult.Type.MISS && !flag) {
                    switch (net.minecraftforge.event.ForgeEventFactory.onProjectileImpactResult(this, hitresult)) {
                        case SKIP_ENTITY:
                            if (hitresult.getType() != HitResult.Type.ENTITY) { // If there is no entity, we just return default behaviour
                                this.onHit(hitresult);
                                this.hasImpulse = true;
                                break;
                            }
                            ignoredEntities.add(entityhitresult.getEntity().getId());
                            entityhitresult = null; // Don't process any further
                            break;
                        case STOP_AT_CURRENT_NO_DAMAGE:
                            this.discard();
                            entityhitresult = null; // Don't process any further
                            break;
                        case STOP_AT_CURRENT:
                            this.setPierceLevel((byte) 0);
                        case DEFAULT:
                            this.onHit(hitresult);
                            this.hasImpulse = true;
                            break;
                    }
                }

                if (entityhitresult == null || this.getPierceLevel() <= 0) {
                    break;
                }

                hitresult = null;
            }

            if (this.isRemoved())
                return;

            vec3 = this.getDeltaMovement();
            double d5 = vec3.x;
            double d6 = vec3.y;
            double d1 = vec3.z;

            double d7 = this.getX() + d5;
            double d2 = this.getY() + d6;
            double d3 = this.getZ() + d1;
            double d4 = vec3.horizontalDistance();
            if (flag) {
                this.setYRot((float)(Mth.atan2(-d5, -d1) * (double)(180F / (float)Math.PI)));
            } else {
                this.setYRot((float)(Mth.atan2(d5, d1) * (double)(180F / (float)Math.PI)));
            }

            this.setXRot((float)(Mth.atan2(d6, d4) * (double)(180F / (float)Math.PI)));
            this.setXRot(lerpRotation(this.xRotO, this.getXRot()));
            this.setYRot(lerpRotation(this.yRotO, this.getYRot()));
            float motion = 0.99F;
            float motionValue = 0.05F;
            if (this.isInWater()) {
                for(int j = 0; j < 4; ++j) {
                    float motionDrag = 0.25F;
                    this.level().addParticle(ParticleTypes.BUBBLE, d7 - d5 * motionDrag, d2 - d6 * motionDrag, d3 - d1 * motionDrag, d5, d6, d1);
                }
                motion = this.getWaterInertia();
            }

            this.setDeltaMovement(vec3.add(this.xPower, this.yPower, this.zPower).scale((double) motion));


            this.setPos(d7, d2, d3);
            this.checkInsideBlocks();

            if (this.getLifeSpan() == this.getDelay()) {
                if (entity instanceof Player player) {
                    LivingEntity target = (LivingEntity) RaycastUtil.getEntityLookedAt(player);
                    if (target != null) {
                        double dx = target.getX() - this.getX();
                        double dy = target.getY() + target.getBbHeight() * 0.5F - this.getY();
                        double dz = target.getZ() - this.getZ();

                        double d = Math.sqrt(dx * dx + dy * dy + dz * dz);

                        dx /= d;
                        dy /= d;
                        dz /= d;
                        this.xPower = dx * this.getSpeed();
                        this.yPower = dy * this.getSpeed();
                        this.zPower = dz * this.getSpeed();
                    }
                }
                if (entity instanceof Mob && ((Mob) entity).getTarget() != null) {
                    LivingEntity target = ((Mob) entity).getTarget();
                    double dx = target.getX() - this.getX();
                    double dy = target.getY() + target.getBbHeight() * 0.5F - this.getY();
                    double dz = target.getZ() - this.getZ();
                    double d = Math.sqrt(dx * dx + dy * dy + dz * dz);
                    dx /= d;
                    dy /= d;
                    dz /= d;
                    this.xPower = dx * this.getSpeed();
                    this.yPower = dy * this.getSpeed();
                    this.zPower = dz * this.getSpeed();
                }
            }
        }
    }

    private boolean shouldFall() {
        return this.inGround && this.level().noCollision((new AABB(this.position(), this.position())).inflate(0.06D));
    }

    private void startFalling() {
        this.inGround = false;
        Vec3 vec3 = this.getDeltaMovement();
        this.setDeltaMovement(vec3.multiply((double)(this.random.nextFloat() * 0.2F), (double)(this.random.nextFloat() * 0.2F), (double)(this.random.nextFloat() * 0.2F)));
        this.setLifeSpan(0);
    }

    public void move(MoverType pType, Vec3 pPos) {
        super.move(pType, pPos);
        if (pType != MoverType.SELF && this.shouldFall()) {
            this.startFalling();
        }

    }

    protected void tickDespawn() {
        if (this.getLifeSpan() >= 1200) {
            this.discard();
        }

    }

    private void projectileLogic() {

    }
    public void setNoPhysics(boolean pNoPhysics) {
        this.noPhysics = pNoPhysics;
        this.setFlag(2, pNoPhysics);
    }

    public boolean isNoPhysics() {
        if (!this.level().isClientSide) {
            return this.noPhysics;
        } else {
            return (this.entityData.get(ID_FLAGS) & 2) != 0;
        }
    }

    public void setPierceLevel(byte pierceLevel) {
        this.entityData.set(PIERCE_LEVEL, pierceLevel);
    }

    public byte getPierceLevel() {
        return this.entityData.get(PIERCE_LEVEL);
    }

    private void setFlag(int id, boolean value) {
        byte idFlags = this.entityData.get(ID_FLAGS);
        if (value) {
            this.entityData.set(ID_FLAGS, (byte)(idFlags | id));
        } else {
            this.entityData.set(ID_FLAGS, (byte)(idFlags & ~id));
        }

    }

    protected float getWaterInertia() {
        return 0.6F;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }

    public int getLifeSpan() {
        if (this.level().isClientSide()) {
            return this.entityData.get(LIFE_SPAN);
        } else {
            return this.lifespan;
        }
    }

    public void setLifeSpan(int lifeSpan) {
        this.lifespan = lifeSpan;
        this.entityData.set(LIFE_SPAN, lifeSpan);
    }

    public int getDuration() {
        if (this.level().isClientSide()) {
            return this.entityData.get(DURATION);
        } else {
            return this.duration;
        }
    }

    public void setDuration(int duration) {
        this.duration = duration;
        this.entityData.set(DURATION, duration);
    }

    public int getDelay() {
        if (this.level().isClientSide()) {
            return this.entityData.get(DELAY);
        } else {
            return this.delay;
        }
    }

    public void setDelay(int delay) {
        this.delay = delay;
        this.entityData.set(DELAY, delay);
    }

    public float getFade() {
        if (this.level().isClientSide()) {
            return this.entityData.get(FADE);
        } else {
            return this.fade;
        }
    }

    public void setFade(float fade) {
        this.fade = fade;
        this.entityData.set(FADE, fade);
    }

    public float getSpeed() {
        if (this.level().isClientSide()) {
            return this.entityData.get(SPEED);
        } else {
            return this.speed;
        }
    }
    public void setSpeed(float speed) {
        this.speed = speed;
        this.entityData.set(SPEED, speed);
    }

    public float getBaseDamage() {
        if (this.level().isClientSide()) {
            return this.entityData.get(DAMAGE);
        } else {
            return this.baseDamage;
        }
    }
    public void setBaseDamage(float baseDamage) {
        this.baseDamage = baseDamage;
        this.entityData.set(DAMAGE, baseDamage);
    }

    public boolean isCanLightFire() {
        if (this.level().isClientSide()) {
            return this.entityData.get(CAN_LIGHT_FIRE);
        } else {
            return this.canLightFire;
        }
    }

    public void setCanLightFire(boolean value) {
        this.canLightFire = value;
        this.entityData.set(CAN_LIGHT_FIRE, value);
    }

    public void setFired(boolean fired) {
        this.entityData.set(FIRED, fired);
    }

    public boolean getFired() {
        return this.entityData.get(FIRED);
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        return super.canHitEntity(entity) && !entity.noPhysics;
    }

    @Nullable
    protected EntityHitResult findHitEntity(Vec3 start, Vec3 end) {
        return ProjectileUtil.getEntityHitResult(this.level(), this, start, end, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D), this::canHitEntity);
    }
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt(NBT_DURATION, this.getDuration());
        tag.putInt(NBT_LIFESPAN, this.getLifeSpan());
        tag.putFloat(NBT_FADE, this.getFade());
        tag.putFloat(NBT_DAMAGE, this.getBaseDamage());
        tag.putFloat(NBT_SPEED, this.getSpeed());
        tag.putBoolean(NBT_CAN_LIGHT_FIRE, this.isCanLightFire());
        tag.putBoolean(NBT_IS_FIRED, this.getFired());
        tag.put(NBT_POWER, this.newDoubleList(new double[]{this.xPower, this.yPower, this.zPower}));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setDuration(tag.getInt(NBT_DURATION));
        this.setLifeSpan(tag.getInt(NBT_LIFESPAN));
        this.setFade(tag.getFloat(NBT_FADE));
        this.setBaseDamage(tag.getFloat(NBT_DAMAGE));
        this.setSpeed(tag.getFloat(NBT_SPEED));
        this.setCanLightFire(tag.getBoolean(NBT_CAN_LIGHT_FIRE));
        this.setFired(tag.getBoolean(NBT_IS_FIRED));
        if (tag.contains(NBT_POWER, 9)) {
            ListTag listtag = tag.getList(NBT_POWER, 6);
            if (listtag.size() == 3) {
                this.xPower = listtag.getDouble(0);
                this.yPower = listtag.getDouble(1);
                this.zPower = listtag.getDouble(2);
            }
        }
    }

    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        Entity entity = this.getOwner();
        int i = entity == null ? 0 : entity.getId();
        return new ClientboundAddEntityPacket(this.getId(), this.getUUID(), this.getX(), this.getY(), this.getZ(), this.getXRot(), this.getYRot(), this.getType(), i, new Vec3(this.xPower, this.yPower, this.zPower), 0.0D);
    }

    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        double d0 = packet.getXa();
        double d1 = packet.getYa();
        double d2 = packet.getZa();
        double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
        if (d3 != 0.0D) {
            this.xPower = d0 / d3 * 0.1D;
            this.yPower = d1 / d3 * 0.1D;
            this.zPower = d2 / d3 * 0.1D;
        }
    }
}