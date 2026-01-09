package com.kenhorizon.beyondhorizon.server.entity.projectiles;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageHandler;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageTags;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;

/**
 * <p>Extended projectile add more properties to enhance the projectiles</p>
 * <p>Copy from Abstract Arrow with modifition</p>
 * @see net.minecraft.world.entity.projectile.AbstractArrow
 * @author KenHorizon
 * @version 1.0
 * */
public class ExtendedProjectile extends Projectile {
    private static final EntityDataAccessor<Byte> ID_FLAGS = SynchedEntityData.defineId(ExtendedProjectile.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> PIERCE_LEVEL = SynchedEntityData.defineId(ExtendedProjectile.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> FIRED = SynchedEntityData.defineId(ExtendedProjectile.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DURATION = SynchedEntityData.defineId(ExtendedProjectile.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> LIFE_SPAN = SynchedEntityData.defineId(ExtendedProjectile.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> DELAY = SynchedEntityData.defineId(ExtendedProjectile.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Float> FADE = SynchedEntityData.defineId(ExtendedProjectile.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> DAMAGE = SynchedEntityData.defineId(ExtendedProjectile.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> SPEED = SynchedEntityData.defineId(ExtendedProjectile.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Boolean> IGNITE_ATTACK = SynchedEntityData.defineId(ExtendedProjectile.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> CAN_LIGHT_FIRE = SynchedEntityData.defineId(ExtendedProjectile.class, EntityDataSerializers.BOOLEAN);
    protected int duration = 0;
    protected int lifespan = 0;
    protected float baseDamage = 1.0F;
    protected float speed = 0.25F;
    protected boolean ignitedAttack;
    protected boolean canLightFire;
    protected boolean inGround;
    protected float fade = 0.0F;
    protected int delay = 0;
    public double xPower;
    public double yPower;
    public double zPower;
    private Vec3[] trailPositions = new Vec3[64];
    private int trailPointer = -1;
    public DamageTags damageTags = DamageTags.DEFAULT;
    protected float damageTypeModifiers = 0.0F;
    public static final String NBT_DURATION = "Duration";
    public static final String NBT_LIFESPAN = "Lifespan";
    public static final String NBT_FADE = "Fade";
    public static final String NBT_DAMAGE = "Damage";
    public static final String NBT_SPEED = "Speed";
    public static final String NBT_CAN_LIGHT_FIRE = "CanLightFire";
    public static final String NBT_IS_FIRED = "IsFired";
    public static final String NBT_POWER = "Power";
    @Nullable
    protected BlockState lastState;
    protected final IntOpenHashSet ignoredEntities = new IntOpenHashSet();

    protected ExtendedProjectile(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
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
        this.entityData.define(FIRED, true);
        this.entityData.define(IGNITE_ATTACK, false);
        this.entityData.define(CAN_LIGHT_FIRE, false);
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    public void setDamage(DamageTags damageType, float damageModifiers) {
        this.damageTags = damageType;
        this.damageTypeModifiers = damageModifiers;
    }

    public void setDamageTypes(DamageTags damageTags) {
        this.damageTags = damageTags;
    }

    public DamageTags getDamageTypes() {
        return damageTags;
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);
        if (!this.level().isClientSide() && this.getFired()) {
            Entity entity = hitResult.getEntity();
            LivingEntity projectileOwner = (LivingEntity) this.getOwner();
            if (entity instanceof LivingEntity target) {
                if (this.isIgnitedAttack()) {
                    target.setSecondsOnFire(5);
                }
                boolean flag = DamageHandler.damage(target, this.setDamageSource(), this.getBaseDamage(), this.getDamageTypes(), this.damageTypeModifiers);

                BeyondHorizon.LOGGER.info("Projectile Hit! {} Hurt {}", target, flag);
                if (flag) {
                    if (projectileOwner != null) {
                        this.afterGotHit(target);
                        this.doEnchantDamageEffects(projectileOwner, entity);
                    }
                }
            }
        }
    }

    public void afterGotHit(LivingEntity entity) {

    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        super.onHitBlock(hitResult);
        if (!this.level().isClientSide()) {
            Entity entity = this.getOwner();
            if (this.isCanLightFire()) {
                if (!(entity instanceof Mob) || ForgeEventFactory.getMobGriefingEvent(this.level(), entity)) {
                    BlockPos blockPos = hitResult.getBlockPos().relative(hitResult.getDirection());
                    if (this.level().isEmptyBlock(blockPos)) {
                        this.level().setBlockAndUpdate(blockPos, BaseFireBlock.getState(this.level(), blockPos));
                    }
                }
            }
        }
    }

    public DamageSource setDamageSource() {
        return this.level().damageSources().mobProjectile((Entity) this, (LivingEntity) this.getOwner());
    }

    @Override
    public void tick() {
        super.tick();
        this.setLifeSpan(this.getLifeSpan() + 1);
        this.trail();
    }

    protected boolean shouldFall() {
        return this.inGround && this.level().noCollision((new AABB(this.position(), this.position())).inflate(0.06D));
    }

    protected void startFalling() {
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
        if (this.getLifeSpan() >= 1200 + this.getDelay()) {
            this.discard();
        }
    }

    public void setNoPhysics(boolean pNoPhysics) {
        this.noPhysics = pNoPhysics;
        this.setFlag(2, pNoPhysics);
    }


    protected void setFlag(int id, boolean value) {
        byte idFlags = this.entityData.get(ID_FLAGS);
        if (value) {
            this.entityData.set(ID_FLAGS, (byte)(idFlags | id));
        } else {
            this.entityData.set(ID_FLAGS, (byte)(idFlags & ~id));
        }

    }


    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
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

    protected float getWaterInertia() {
        return 0.6F;
    }

    @Override
    public boolean isPickable() {
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

    public boolean isIgnitedAttack() {
        if (this.level().isClientSide()) {
            return this.entityData.get(IGNITE_ATTACK);
        } else {
            return this.ignitedAttack;
        }
    }

    public void setIgniteAttack(boolean value) {
        this.ignitedAttack = value;
        this.entityData.set(IGNITE_ATTACK, value);
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
    private void trail() {
        Vec3 vec3 = this.getDeltaMovement();
        this.setYRot(-((float) Mth.atan2(vec3.x, vec3.z)) * (180F / (float) Math.PI)) ;
        Vec3 trailAt = this.position().add(0, this.getBbHeight() / 2F, 0);
        if (this.trailPointer == -1) {
            Vec3 backAt = trailAt;
            for (int i = 0; i < this.trailPositions.length; i++) {
                this.trailPositions[i] = backAt;
            }
        }
        if (++this.trailPointer == this.trailPositions.length) {
            this.trailPointer = 0;
        }
        this.trailPositions[this.trailPointer] = trailAt;
    }

    public boolean hasTrail() {
        return this.trailPointer != -1;
    }

    public Vec3 getTrailPosition(int pointer, float partialTick) {
        if (this.isRemoved()) {
            partialTick = 1.0F;
        }
        int i = this.trailPointer - pointer & 63;
        int j = this.trailPointer - pointer - 1 & 63;
        Vec3 d0 = this.trailPositions[j];
        Vec3 d1 = this.trailPositions[i].subtract(d0);
        return d0.add(d1.scale(partialTick));
    }

    @Override
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
