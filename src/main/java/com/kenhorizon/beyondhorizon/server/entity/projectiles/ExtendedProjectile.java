package com.kenhorizon.beyondhorizon.server.entity.projectiles;

import com.google.common.collect.Lists;
import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageType;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageScaling;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

/**
 * <p>Extended projectile add more properties to enhance the projectiles</p>
 * <p>Copy from Abstract Arrow with modifition</p>
 * @see net.minecraft.world.entity.projectile.AbstractArrow
 * @author KenHorizon
 * @version 1.0
 * */
public abstract class ExtendedProjectile extends Projectile {

    public static enum PickupType {
        DISALLOWED,
        ALLOWED,
        CREATIVE_ONLY;

        public static PickupType byOrdinal(int ordinal) {
            if (ordinal < 0 || ordinal > values().length) {
                ordinal = 0;
            }

            return values()[ordinal];
        }
    }
    private static final EntityDataAccessor<Byte> ID_FLAGS = SynchedEntityData.defineId(ExtendedProjectile.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> PIERCE_LEVEL = SynchedEntityData.defineId(ExtendedProjectile.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> FIRED = SynchedEntityData.defineId(ExtendedProjectile.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DURATION = SynchedEntityData.defineId(ExtendedProjectile.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> LIFE_SPAN = SynchedEntityData.defineId(ExtendedProjectile.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DELAY = SynchedEntityData.defineId(ExtendedProjectile.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> HP_DAMAGE = SynchedEntityData.defineId(ExtendedProjectile.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> FADE = SynchedEntityData.defineId(ExtendedProjectile.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DAMAGE = SynchedEntityData.defineId(ExtendedProjectile.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> SPEED = SynchedEntityData.defineId(ExtendedProjectile.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> RADIUS = SynchedEntityData.defineId(ExtendedProjectile.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> IGNITE_ATTACK = SynchedEntityData.defineId(ExtendedProjectile.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CAN_LIGHT_FIRE = SynchedEntityData.defineId(ExtendedProjectile.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> RICOCHET = SynchedEntityData.defineId(ExtendedProjectile.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> RICOCHET_BOUNCE = SynchedEntityData.defineId(ExtendedProjectile.class, EntityDataSerializers.INT);
    protected int duration = 160;
    protected int lifespan = 0;
    protected float damageScalingHp = 0.0F;
    protected float radius = 1.0F;
    protected float baseDamage = 1.0F;
    protected float speed = 0.25F;
    protected boolean ignitedAttack;
    protected boolean canLightFire;
    protected boolean inGround;
    protected boolean isCrit;
    protected float fade = 0.0F;
    protected int delay = 0;
    protected double xPower;
    protected double yPower;
    protected double zPower;
    protected boolean ricochet = false;
    protected int ricochetBounce = 1;
    private final Vec3[] trailPositions = new Vec3[64];
    private int trailPointer = -1;
    public DamageType damageType = DamageType.PHYSICAL_DAMAGE;
    public DamageScaling damageScaling = DamageScaling.NONE;
    public static final String NBT_RICOCHET_BOUNCE = "RicochetBounce";
    public static final String NBT_RICOCHET = "Ricochet";
    public static final String NBT_DURATION = "Duration";
    public static final String NBT_DAMAGE_TYPE = "DamageType";
    public static final String NBT_IS_CRIT = "Crit";
    public static final String NBT_LIFESPAN = "Lifespan";
    public static final String NBT_FADE = "Fade";
    public static final String NBT_DAMAGE = "Damage";
    public static final String NBT_SPEED = "Speed";
    public static final String NBT_CAN_LIGHT_FIRE = "CanLightFire";
    public static final String NBT_IS_FIRED = "IsFired";
    public static final String NBT_POWER = "Power";
    public static final String NBT_DAMAGE_SCALING = "DamageScaling";
    public static final String NBT_HP_DAMAGE = "HpDamage";
    public static final String NBT_RADIUS = "Radius";
    public Consumer<ExtendedProjectile> postEffectDamage;
    @Nullable
    protected BlockState lastState;
    @Nullable
    protected IntOpenHashSet piercingIgnoreEntityIds;
    @Nullable
    protected List<Entity> piercedAndKilledEntities;
    protected final IntOpenHashSet ignoredEntities = new IntOpenHashSet();
    protected PickupType pickup;

    protected ExtendedProjectile(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
        this.pickup = PickupType.DISALLOWED;
    }
    protected ExtendedProjectile(EntityType<? extends Projectile> entityType, double x, double y, double z, Level level) {
        this(entityType, level);
        this.setPos(x, y, z);
    }
    protected ExtendedProjectile(EntityType<? extends Projectile> entityType, LivingEntity shooter, Level level) {
        this(entityType, shooter.getX(), shooter.getEyeY() - 0.1F, shooter.getZ(), level);
        this.setOwner(shooter);
        if (shooter instanceof Player) {
            this.pickup = PickupType.ALLOWED;
        }
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
        this.entityData.define(HP_DAMAGE, 0.0F);
        this.entityData.define(RADIUS, 1.0F);
        this.entityData.define(FIRED, true);
        this.entityData.define(IGNITE_ATTACK, false);
        this.entityData.define(CAN_LIGHT_FIRE, false);
    }

    public void setScalingDamage(float scale, DamageScaling damageScaling) {
        this.damageScaling = damageScaling;
        this.setHPDamage(scale);
    }

    public void setHPDamage(float damageScalingHp) {
        this.damageScalingHp = damageScalingHp;
        this.entityData.set(HP_DAMAGE, damageScalingHp);
    }
    public float getHPDamage() {
        return this.level().isClientSide() ? entityData.get(HP_DAMAGE) : damageScalingHp;
    }

    public void setRadius(float radius) {
        this.radius = radius;
        this.entityData.set(RADIUS, radius);
    }
    public float getRadius() {
        return this.level().isClientSide() ? entityData.get(RADIUS) : radius;
    }

    public DamageType getDamageType() {
        return damageType;
    }

    public void setDamageType(DamageType damageType) {
        this.damageType = damageType;
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);
        if (!this.level().isClientSide() && this.getFired()) {
            Entity entity = hitResult.getEntity();
            LivingEntity projectileOwner = (LivingEntity) this.getOwner();
            boolean flag = entity.getType() == EntityType.ENDERMAN;
            float damage = this.getBaseDamage();
            if (this.getPierceLevel() > 0) {
                if (this.piercingIgnoreEntityIds == null) {
                    this.piercingIgnoreEntityIds = new IntOpenHashSet(5);
                    if (this.piercedAndKilledEntities == null) {
                        this.piercedAndKilledEntities = Lists.newArrayListWithCapacity(5);
                    }
                    if (this.piercingIgnoreEntityIds.size() >= this.getPierceLevel() + 1) {
                        this.discard();
                        return;
                    }
                    this.piercingIgnoreEntityIds.add(entity.getId());
                }
            }
            if (this.isCrit()) {
                damage *= (float) projectileOwner.getAttributeValue(BHAttributes.CRITICAL_DAMAGE.get());
            }
            if (entity instanceof LivingEntity target) {
                if (this.isIgnitedAttack()) {
                    target.setSecondsOnFire(5);
                }
                float bonusDamage = 0.0F;
                switch (this.getDamageScaling()) {
                    case MAX_HEALTH:
                        bonusDamage += this.getDamageScaling().MaxHP(target, this.getHPDamage());
                        break;
                    case MISSING_HEALTH:
                        bonusDamage += this.getDamageScaling().MissingHP(target, this.getHPDamage());
                        break;
                    case CURRENT_HEALTH:
                        bonusDamage += this.getDamageScaling().CurentHP(target, this.getHPDamage());
                        break;
                }
                if (this.isOnFire() && !flag) {
                    target.setSecondsOnFire(5);
                }
                if (this.doDamage(target, projectileOwner, damage + bonusDamage)) {
                    if (flag) return;
                    if (projectileOwner != null) {
                        if (this.postEffectDamage != null) {
                            this.postEffectDamage.accept(this);
                        }
                        this.afterGotHit(target);
                        this.doEnchantDamageEffects(projectileOwner, entity);
                    }
                    if (!entity.isAlive() && this.piercedAndKilledEntities != null) {
                        this.piercedAndKilledEntities.add(target);
                    }
                }
            }
        }
    }

    public boolean doDamage(LivingEntity target, LivingEntity holder, float damage) {
        return this.getDamageType().dealDamage(target, this, holder, damage);
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

//    protected void checkEntityHit() {
//        if (!level().isClientSide()) {
//            for (Entity entity : this.getSubEntityCollisions()) {
//                this.onHitEntity(new EntityHitResult(entity));
//            }
//        }
//    }
//
//    protected Set<Entity> getSubEntityCollisions() {
//        List<Entity> collisions = new ArrayList<>(this.level().getEntities(this, this.getBoundingBox().inflate(1.0F)));
//        return collisions.stream().filter(target ->
//                target instanceof LivingEntity && target != getOwner()
//        ).collect(Collectors.toSet());
//    }

    @Override
    public void tick() {
        super.tick();
        this.trail();
        this.onStart();
        this.setLifeSpan(this.getLifeSpan() + 1);
        this.onDuration();
        if (this.getLifeSpan() >= (this.getDuration() + this.getDelay())) {
            this.onEnd();
        }
        if (this.getLifeSpan() > (this.getDuration() + this.getDelay())) {
            this.discard();
        }
    }

    public void onStart() {

    }
    public void onEnd() {

    }
    public void onDuration() {

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

    public void setCrit(boolean crit) {
        isCrit = crit;
    }

    public boolean isCrit() {
        return isCrit;
    }

    public void setDamageScaling(DamageScaling damageScaling) {
        this.damageScaling = damageScaling;
    }

    public DamageScaling getDamageScaling() {
        return damageScaling;
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
    public int getDurationWithDelay() {
        if (this.level().isClientSide()) {
            return this.entityData.get(DURATION) + this.entityData.get(DELAY);
        } else {
            return this.duration + delay;
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
        tag.putFloat(NBT_HP_DAMAGE, this.getHPDamage());
        tag.putInt(NBT_DURATION, this.getDuration());
        tag.putInt(NBT_LIFESPAN, this.getLifeSpan());
        tag.putFloat(NBT_FADE, this.getFade());
        tag.putFloat(NBT_DAMAGE, this.getBaseDamage());
        tag.putFloat(NBT_SPEED, this.getSpeed());
        tag.putBoolean(NBT_IS_CRIT, this.isCrit());
        tag.putBoolean(NBT_CAN_LIGHT_FIRE, this.isCanLightFire());
        tag.putBoolean(NBT_IS_FIRED, this.getFired());
        tag.put(NBT_POWER, this.newDoubleList(new double[]{this.xPower, this.yPower, this.zPower}));
        tag.putInt(NBT_DAMAGE_TYPE, this.getDamageType().ordinal());
        tag.putInt(NBT_DAMAGE_SCALING, this.getDamageScaling().ordinal());
    tag.putFloat(NBT_RADIUS, this.getRadius());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setRadius(tag.getFloat(NBT_RADIUS));
        this.setHPDamage(tag.getInt(NBT_HP_DAMAGE));
        this.setDuration(tag.getInt(NBT_DURATION));
        this.setLifeSpan(tag.getInt(NBT_LIFESPAN));
        this.setFade(tag.getFloat(NBT_FADE));
        this.setBaseDamage(tag.getFloat(NBT_DAMAGE));
        this.setSpeed(tag.getFloat(NBT_SPEED));
        this.setCanLightFire(tag.getBoolean(NBT_CAN_LIGHT_FIRE));
        this.setFired(tag.getBoolean(NBT_IS_FIRED));
        this.setCrit(tag.getBoolean(NBT_IS_CRIT));
        this.setDamageType(DamageType.values()[tag.getInt(NBT_DAMAGE_TYPE)]);
        this.setDamageScaling(DamageScaling.values()[tag.getInt(NBT_DAMAGE_SCALING)]);
        if (tag.contains(NBT_POWER, 9)) {
            ListTag listtag = tag.getList(NBT_POWER, 6);
            if (listtag.size() == 3) {
                this.xPower = listtag.getDouble(0);
                this.yPower = listtag.getDouble(1);
                this.zPower = listtag.getDouble(2);
            }
        }
    }

    public boolean onDelay() {
        return this.getLifeSpan() <= this.getDelay();
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

      //
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
                i, new Vec3(this.xPower, this.yPower, this.zPower), 0.0D);
    }

    @Override
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
        this.xRotO = this.getXRot();
        this.yRotO = this.getYRot();
    }

    protected void spawnParticle() {
    }
}
