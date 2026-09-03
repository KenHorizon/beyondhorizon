package com.kenhorizon.beyondhorizon.server.entity.ability;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.model.util.ControlledAnimation;
import com.kenhorizon.beyondhorizon.client.particle.TrailParticles;
import com.kenhorizon.beyondhorizon.client.particle.world.TrailParticleOptions;
import com.kenhorizon.beyondhorizon.client.render.util.Colors;
import com.kenhorizon.beyondhorizon.client.sound.DeathRaySound;
import com.kenhorizon.beyondhorizon.server.entity.CameraShake;
import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import com.kenhorizon.beyondhorizon.server.init.BHSounds;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageHandler;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageType;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AbstractDeathRayAbility extends Entity implements IDeathRayType {
    public byte LASER_BEAM_FIRING = 21;
    public double laserBeamRange = 30;
    public LivingEntity caster;
    public LivingEntity source;
    public int delay;
    public double endPosX;
    public double endPosY;
    public double endPosZ;
    public double collidePosX;
    public double collidePosY;
    public double collidePosZ;
    public double prevCollidePosX;
    public double prevCollidePosY;
    public double prevCollidePosZ;
    public float renderYaw;
    public float renderPitch;
    public float baseDamage = 2.0F;
    public boolean scaleCurrentHealth;
    public float scaleCurrentHealthDamage = 0.02F;
    public boolean scaleMaxHealth;
    public float scaleMaxHealthDamage = 0.02F;
    public boolean scaleMissingHealth;
    public float scaleMissingHealthDamage = 0.02F;
    public boolean canIgnoreFrame = false;
    public boolean canBurnTarget = false;
    public BeamDamageTags beamDamageTags = BeamDamageTags.DEFAULT;
    public DamageType damageType = DamageType.PHYSICAL_DAMAGE;
    public ControlledAnimation appear = new ControlledAnimation(3);

    public boolean on = true;

    public Direction blockSide = null;

    private static final EntityDataAccessor<Float> YAW = SynchedEntityData.defineId(AbstractDeathRayAbility.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> PITCH = SynchedEntityData.defineId(AbstractDeathRayAbility.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DELAY = SynchedEntityData.defineId(AbstractDeathRayAbility.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DURATION = SynchedEntityData.defineId(AbstractDeathRayAbility.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IGNORE_IMMUNITY_FRAME = SynchedEntityData.defineId(AbstractDeathRayAbility.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CAN_BURN_TARGET = SynchedEntityData.defineId(AbstractDeathRayAbility.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> HAS_PLAYER = SynchedEntityData.defineId(AbstractDeathRayAbility.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> CASTER = SynchedEntityData.defineId(AbstractDeathRayAbility.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SOURCE = SynchedEntityData.defineId(AbstractDeathRayAbility.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DAMAGE_TYPES = SynchedEntityData.defineId(AbstractDeathRayAbility.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> R = SynchedEntityData.defineId(AbstractDeathRayAbility.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> G = SynchedEntityData.defineId(AbstractDeathRayAbility.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> B = SynchedEntityData.defineId(AbstractDeathRayAbility.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> SCALE = SynchedEntityData.defineId(AbstractDeathRayAbility.class, EntityDataSerializers.FLOAT);

    public float prevYaw;
    public float prevPitch;

    @OnlyIn(Dist.CLIENT)
    private Vec3[] attractorPos;
    protected boolean didRaytrace;

    public AbstractDeathRayAbility(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.noCulling = true;
        if (level.isClientSide()) {
            this.attractorPos = new Vec3[] {new Vec3(0, 0, 0)};
        }
    }

    public AbstractDeathRayAbility(EntityType<? extends AbstractDeathRayAbility> type, Level world, LivingEntity caster, double x, double y, double z, float yaw, float pitch, int duration) {
        this(type, world);
        this.caster = caster;
        this.setDelay(20);
        this.setYaw(yaw);
        this.setPitch(pitch);
        this.setDuration(duration);
        this.setPos(x, y, z);
        this.calculateEndPos();
        this.level().broadcastEntityEvent(this, LASER_BEAM_FIRING);
        if (!world.isClientSide()) {
            this.setCasterID(caster.getId());
        }
    }

    public AbstractDeathRayAbility(EntityType<? extends AbstractDeathRayAbility> type, Level world, LivingEntity caster, double x, double y, double z, float yaw, float pitch, int duration, float range) {
        this(type, world, caster, x, y, z, yaw, pitch, duration);
    }


    @Override
    protected void defineSynchedData() {
        getEntityData().define(YAW, 0F);
        getEntityData().define(PITCH, 0F);
        getEntityData().define(DELAY, 20);
        getEntityData().define(DURATION, 0);
        getEntityData().define(HAS_PLAYER, false);
        getEntityData().define(IGNORE_IMMUNITY_FRAME, false);
        getEntityData().define(CAN_BURN_TARGET, false);
        getEntityData().define(CASTER, -1);
        getEntityData().define(SOURCE, -1);
        getEntityData().define(R, 255);
        getEntityData().define(G, 255);
        getEntityData().define(B, 255);
        getEntityData().define(SCALE, 1.0F);
        getEntityData().define(DAMAGE_TYPES, 0);
    }
    public void setColor(int r, int g, int b) {
        this.getEntityData().set(R, r);
        this.getEntityData().set(G, g);
        this.getEntityData().set(B, b);
    }

    public void setDamageType(DamageType damageType) {
        this.damageType = damageType;
        this.entityData.set(DAMAGE_TYPES, damageType.ordinal());
    }

    public DamageType getDamageType() {
        return this.level().isClientSide() ? DamageType.values()[this.entityData.get(DAMAGE_TYPES)] : this.damageType;
    }

    public int[] getColors() {
        return new int[] {this.entityData.get(R), this.entityData.get(G), this.entityData.get(B)};
    }
    public void setScale(float scale) {
        this.getEntityData().set(SCALE, scale);
    }

    public float getScale() {
        return this.entityData.get(SCALE);
    }

    public void setDelay(int delay) {
        this.delay = delay;
        this.entityData.set(DELAY, delay);
    }

    public int getDelay() {
        return this.level().isClientSide() ? this.entityData.get(DELAY) : this.delay;
    }

    @Override
    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == LASER_BEAM_FIRING) {
            BeyondHorizon.PROXY.playSound(new DeathRaySound(this, BHSounds.BLAZING_INFERNO_DEATH_RAY.get(), true));
            BeyondHorizon.PROXY.playSound(new DeathRaySound(this, BHSounds.BLAZING_INFERNO_DEATH_RAY.get(), false));
        }
        super.handleEntityEvent(id);
    }

    @Override
    public void tick() {
        super.tick();
        prevCollidePosX = collidePosX;
        prevCollidePosY = collidePosY;
        prevCollidePosZ = collidePosZ;
        prevYaw = renderYaw;
        prevPitch = renderPitch;
        xo = getX();
        yo = getY();
        zo = getZ();
        if (tickCount == 1 && level().isClientSide()) {
            this.caster = (LivingEntity) level().getEntity(getCasterID());
            this.source = (LivingEntity) level().getEntity(getSourceID());
        }
        if (!this.level().isClientSide()) {
            if (this.getHasPlayer()) {
                this.updateWithPlayer();
            } else {
                this.updateWithMob();
            }
        }
        if (this.caster != null) {
            this.renderYaw = (float) ((this.caster.yHeadRot + 90.0d) * Math.PI / 180.0d);
            this.renderPitch = (float) (-this.caster.getXRot() * Math.PI / 180.0d);
        }

        if (!this.on && this.appear.getTimer() == 0) {
            this.discard();
        }
        if (this.on && tickCount > (Math.max(10, this.getDelay()))) {
            this.appear.increaseTimer();
        } else {
            this.appear.decreaseTimer();
        }
        if (this.caster != null && !this.caster.isAlive()) discard();

        if (this.tickCount > (Math.max(10, this.getDelay()))) {
            this.rayTicks();
        }
        if (this.tickCount - (Math.max(10, this.getDelay())) > this.getDuration()) {
            on = false;
        }
    }
    protected void rayTicks() {
        this.calculateEndPos();
        List<LivingEntity> hit = raytraceEntities(level(), new Vec3(getX(), getY(), getZ()), new Vec3(endPosX, endPosY, endPosZ), false, true, true).entities;
        if (this.blockSide != null) {
            this.spawnExplosionParticles(2);
        }
        if (!level().isClientSide()) {
            for (LivingEntity target : hit) {
                if (this.caster != null) {
                    if (this.caster.isAlliedTo(target)) continue;
                    if (this.source != null) {
                        if (this.source.isAlliedTo(target)) continue;
                    }
                    boolean flag;
                    if (this.getDamageType() == DamageType.PHYSICAL_DAMAGE) {
                        flag = target.hurt(BHDamageTypes.AOEphysicalDamage(this, null), this.rayDamages(target));
                    } else if (this.getDamageType() == DamageType.MAGIC_DAMAGE) {
                        flag = target.hurt(BHDamageTypes.AOEmagicDamage(this, null), this.rayDamages(target));
                    } else if (this.getDamageType() == DamageType.TRUE_DAMAGE) {
                        flag = target.hurt(BHDamageTypes.AOEtrueDamage(this, null), this.rayDamages(target));
                    } else {
                        flag = false;
                    }
                    if (flag) {
                        if (this.isImmunityFrameIgnore()) {
                            target.hurtDuration = 0;
                            target.invulnerableTime = 0;
                        }
                        if (this.isCanBurnTarget()) {
                            int fireAspectLevel = EnchantmentHelper.getFireAspect(caster);
                            target.setSecondsOnFire(3 + fireAspectLevel);
                        }
                    }
                    this.knockbackTarget(target, 0.0F, this.getX() - target.getX(), this.getZ() - target.getZ(), true);
                }
            }
        } else {
            if (this.getDuration() <= 5) {
                this.onStartParticle();
            } else {
                if (tickCount - (Math.max(10, this.getDelay() - 5)) < this.getDuration()) {
                    this.onStartParticle();
                }
            }
        }
    }

    protected void onStartParticle() {
        this.start();
        int particleCount = 4;
        float[] colors = Colors.getFARGB(Colors.combineRGB(this.getColors()[0],this.getColors()[1],this.getColors()[2]));
        while (particleCount --> 0) {
            double radius = 1f;
            float yaw = (float) (random.nextFloat() * 2 * Math.PI);
            float pitch = (float) (random.nextFloat() * 2 * Math.PI);
            double ox = (float) (radius * Math.sin(yaw) * Math.sin(pitch));
            double oy = (float) (radius * Math.cos(pitch));
            double oz = (float) (radius * Math.cos(yaw) * Math.sin(pitch));
            double o2x = (float) (-1 * Math.cos(getYaw()) * Math.cos(getPitch()));
            double o2y = (float) (-1 * Math.sin(getPitch()));
            double o2z = (float) (-1 * Math.sin(getYaw()) * Math.cos(getPitch()));
            TrailParticleOptions.add(level(),
                    TrailParticles.Behavior.DEFAULT,getX() + o2x + ox, getY() + o2y + oy  + 0.1, getZ() + o2z + oz,
                    1.25F, 1.0F, colors[0], colors[1], colors[2], 10, new Vec3(this.collidePosX, this.collidePosY, this.collidePosZ));
        }
    }

    protected void start() {

    }

    private void knockbackTarget(LivingEntity target, double strength, double x, double z, boolean ignoreResistance) {
        if (!ignoreResistance) {
            strength *= 1.0D - target.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
        }
        if (!(strength <= 0.0D)) {
            this.hasImpulse = true;
            Vec3 vec3 = this.getDeltaMovement();
            Vec3 vec31 = (new Vec3(x, 0.0D, z)).normalize().scale(strength);
            target.setDeltaMovement(vec3.x / 2.0D - vec31.x, this.onGround() ? Math.min(0.4D, vec3.y / 2.0D + strength) : vec3.y, vec3.z / 2.0D - vec31.z);
        }
    }

    protected void spawnExplosionParticles(int amount) {
        for (int i = 0; i < amount; i++) {
            final float velocity = 0.1F;
            float yaw = (float) (random.nextFloat() * 2 * Math.PI);
            float motionY = random.nextFloat() * 0.08F;
            float motionX = velocity * Mth.cos(yaw);
            float motionZ = velocity * Mth.sin(yaw);
            level().addParticle(ParticleTypes.FLAME, collidePosX, collidePosY + 0.1, collidePosZ, motionX, motionY, motionZ);
        }
        for (int i = 0; i < amount / 2; i++) {
            level().addParticle(ParticleTypes.LAVA, collidePosX, collidePosY + 0.1, collidePosZ, 0, 0, 0);
        }
    }

    public boolean isImmunityFrameIgnore() {
        if (this.level().isClientSide()) {
            return this.getEntityData().get(IGNORE_IMMUNITY_FRAME);
        } else {
            return this.canIgnoreFrame;
        }
    }

    public void setImmunityFrameIgnore(boolean canIgnoreFrame) {
        this.canIgnoreFrame = canIgnoreFrame;
        this.getEntityData().set(IGNORE_IMMUNITY_FRAME, canIgnoreFrame);
    }

    public boolean isCanBurnTarget() {
        if (this.level().isClientSide()) {
            return this.getEntityData().get(CAN_BURN_TARGET);
        } else {
            return this.canBurnTarget;
        }
    }

    public void setCanBurnTarget(boolean canBurnTarget) {
        this.canBurnTarget = canBurnTarget;
        this.getEntityData().set(CAN_BURN_TARGET, canBurnTarget);
    }

    public float getYaw() {
        return this.getEntityData().get(YAW);
    }

    public void setYaw(float yaw) {
        this.getEntityData().set(YAW, yaw);
    }

    public float getPitch() {
        return this.getEntityData().get(PITCH);
    }

    public void setPitch(float pitch) {
        this.getEntityData().set(PITCH, pitch);
    }

    public int getDuration() {
        return this.getEntityData().get(DURATION);
    }

    public void setDuration(int duration) {
        this.getEntityData().set(DURATION, duration);
    }

    public boolean getHasPlayer() {
        return this.getEntityData().get(HAS_PLAYER);
    }

    public void setHasPlayer(boolean player) {
        this.getEntityData().set(HAS_PLAYER, player);
    }

    public int getCasterID() {
        return this.getEntityData().get(CASTER);
    }

    public void setCasterID(int id) {
        this.getEntityData().set(CASTER, id);
    }

    public int getSourceID() {
        return this.getEntityData().get(SOURCE);
    }

    public void setSourceID(int id) {
        this.getEntityData().set(SOURCE, id);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt) {
        this.setYaw(nbt.getFloat("yaw"));
        this.setPitch(nbt.getFloat("pitch"));
        this.setScale(nbt.getFloat("scale"));
        this.setColor(nbt.getInt("r"),nbt.getInt("g"),nbt.getInt("b"));
        this.damageType = DamageType.values()[nbt.getInt("damage_type")];
        this.beamDamageTags = BeamDamageTags.values()[nbt.getInt("damage_types_tags")];
        this.setCanBurnTarget(nbt.getBoolean("can_burn_target"));
        this.setImmunityFrameIgnore(nbt.getBoolean("ignore_immunity_frame"));
        this.setIsScaleCurrentHealth(nbt.getBoolean("scale_with_current_health"));
        this.setIsScaleMaxHealth(nbt.getBoolean("scale_with_max_health"));
        this.setIsScaleMissingHealth(nbt.getBoolean("scale_with_missing_health"));
        this.scaleCurrentHealthDamage(nbt.getFloat("max_current_damage"));
        this.scaleMissingHealthDamage(nbt.getFloat("max_missing_damage"));
        this.scaleMaxHealthDamage(nbt.getFloat("max_health_damage"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt) {
        nbt.putInt("r", this.getColors()[0]);
        nbt.putInt("g", this.getColors()[1]);
        nbt.putInt("b", this.getColors()[2]);
        nbt.putFloat("yaw", this.getYaw());
        nbt.putFloat("pitch", this.getPitch());
        nbt.putFloat("scale", this.getScale());
        nbt.putInt("damage_type", this.damageType.ordinal());
        nbt.putInt("damage_types_tags", this.beamDamageTags.ordinal());
        nbt.putBoolean("can_burn_target", this.isCanBurnTarget());
        nbt.putBoolean("ignore_immunity_frame", this.isImmunityFrameIgnore());
        nbt.putBoolean("scale_with_current_health", this.isScaleCurrentHealth());
        nbt.putBoolean("scale_with_missing_health", this.isScaleMissingHealth());
        nbt.putBoolean("scale_with_max_health", this.isScaleMaxHealth());
        nbt.putFloat("max_current_damage", this.getScaleCurrentHealthDamage());
        nbt.putFloat("max_missing_damage", this.getScaleMissingHealthDamage());
        nbt.putFloat("max_health_damage", this.getScaleMaxHealthDamage());
    }

    protected void calculateEndPos() {
        double radius = laserBeamRange;
        if (this.level().isClientSide()) {
            endPosX = getX() + radius * Math.cos(renderYaw) * Math.cos(renderPitch);
            endPosZ = getZ() + radius * Math.sin(renderYaw) * Math.cos(renderPitch);
            endPosY = getY() + radius * Math.sin(renderPitch);
        } else {
            endPosX = getX() + radius * Math.cos(this.getYaw()) * Math.cos(this.getPitch());
            endPosZ = getZ() + radius * Math.sin(this.getYaw()) * Math.cos(this.getPitch());
            endPosY = getY() + radius * Math.sin(this.getPitch());
        }
    }

    public boolean hasDoneRaytrace() {
        return didRaytrace;
    }

    public DeathLaserBeamHitResult raytraceEntities(Level world, Vec3 from, Vec3 to, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock) {
        this.didRaytrace = true;
        DeathLaserBeamHitResult result = new DeathLaserBeamHitResult();
        result.setBlockHit(world.clip(new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)));
        if (result.blockHit != null) {
            Vec3 hitVec = result.blockHit.getLocation();
            this.collidePosX = hitVec.x;
            this.collidePosY = hitVec.y;
            this.collidePosZ = hitVec.z;
            this.blockSide = result.blockHit.getDirection();
        } else {
            this.collidePosX = endPosX;
            this.collidePosY = endPosY;
            this.collidePosZ = endPosZ;
            this.blockSide = null;
        }
        List<LivingEntity> entities = world.getEntitiesOfClass(LivingEntity.class,
                new AABB(Math.min(getX(), this.collidePosX), Math.min(getY(), this.collidePosY), Math.min(getZ(), this.collidePosZ), Math.max(getX(), this.collidePosX), Math.max(getY(), this.collidePosY), Math.max(getZ(), this.collidePosZ)).inflate(1, 1, 1));
        for (LivingEntity entity : entities) {
            if (entity == caster) {
                continue;
            }
            float pad = entity.getPickRadius() + 0.5f;
            AABB aabb = entity.getBoundingBox().inflate(pad, pad, pad);
            Optional<Vec3> hit = aabb.clip(from, to);
            if (aabb.contains(from)) {
                result.addEntityHit(entity);
            } else if (hit.isPresent()) {
                result.addEntityHit(entity);
            }
        }
        return result;
    }

    @Override
    public void push(Entity entityIn) {
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return distance < 1024;
    }

    protected void updateWithPlayer() {
        this.setYaw((float) ((caster.yHeadRot + 90) * Math.PI / 180.0D));
        this.setPitch((float) (-caster.getXRot() * Math.PI / 180.0D));
        Vec3 vecOffset = caster.getLookAngle().normalize().scale(1);
        this.setPos(caster.getX() + vecOffset.x(), caster.getY() + 1.2f + vecOffset.y(), caster.getZ() + vecOffset.z());
    }

    protected void updateWithMob() {
        if (this.caster != null) {
            this.setYaw((float) ((caster.yHeadRot + 90) * Math.PI / 180.0D));
            this.setPitch((float) (-caster.getXRot() * Math.PI / 180.0D));
            Vec3 vecOffset1 = new Vec3(0, 0, 0.6).yRot((float) Math.toRadians(-caster.getYRot()));
            Vec3 vecOffset2 = new Vec3(1.2, 0, 0).yRot(-getYaw()).xRot(getPitch());
            this.setPos(caster.getX() + vecOffset1.x() + vecOffset2.x(), caster.getY() + 1.5f + vecOffset1.y() + vecOffset2.y(), caster.getZ() + vecOffset1.z() + vecOffset2.z());
        }
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
    }

    public float getBaseDamage() {
        return this.baseDamage;
    }

    public void setBaseDamage(float baseDamage) {
        this.baseDamage = baseDamage;
    }

    private float rayDamages(LivingEntity target) {
        switch (this.beamDamageTags) {
            case MAX_HEALTH -> {
                return DamageHandler.maxHealth(target, this.getBaseDamage(), this.getScaleMaxHealthDamage());
            }
            case MISSING_HEALTH -> {
                return DamageHandler.missingHealth(target, this.getBaseDamage(), this.getScaleMissingHealthDamage());
            }
            case CURRENT_HEALTH -> {
                return DamageHandler.currentHealth(target, this.getBaseDamage(), this.getScaleCurrentHealthDamage());
            }
            default -> {
                return this.getBaseDamage();
            }
        }
    }

    public void damageConfig(BeamDamageTags types, float deathLaserBaseDamage) {
        this.beamDamageTags = types;
        this.baseDamage = this.baseDamage + deathLaserBaseDamage;
    }

    public void scaleCurrentHealthDamage(float scaleCurrentHealth) {
        this.scaleCurrentHealthDamage = Mth.clamp(scaleCurrentHealth, 0.0F, 1.0F);
    }

    public void scaleMaxHealthDamage(float scaleMaxHealth) {
        this.scaleMaxHealthDamage = Mth.clamp(scaleMaxHealth, 0.0F, 1.0F);
    }

    public void scaleMissingHealthDamage(float scaleMissingHealth) {
        this.scaleMissingHealthDamage = Mth.clamp(scaleMissingHealth, 0.0F, 1.0F);
    }

    public void setIsScaleCurrentHealth(boolean scaleCurrentHealth) {
        this.scaleCurrentHealth = scaleCurrentHealth;
    }

    public void setIsScaleMaxHealth(boolean scaleMaxHealth) {
        this.scaleMaxHealth = scaleMaxHealth;
    }

    public void setIsScaleMissingHealth(boolean scaleMissingHealth) {
        this.scaleMissingHealth = scaleMissingHealth;
    }

    public boolean isScaleCurrentHealth() {
        return scaleCurrentHealth;
    }

    public boolean isScaleMaxHealth() {
        return scaleMaxHealth;
    }

    public boolean isScaleMissingHealth() {
        return scaleMissingHealth;
    }

    public float getScaleCurrentHealthDamage() {
        return scaleCurrentHealthDamage;
    }

    public float getScaleMissingHealthDamage() {
        return scaleMissingHealthDamage;
    }

    public float getScaleMaxHealthDamage() {
        return scaleMaxHealthDamage;
    }

    public enum BeamDamageTags {
        DEFAULT,
        MAX_HEALTH,
        MISSING_HEALTH,
        CURRENT_HEALTH;
    }
    public static class DeathLaserBeamHitResult {
        protected BlockHitResult blockHit;

        private final List<LivingEntity> entities = new ArrayList<>();

        public BlockHitResult getBlockHit() {
            return blockHit;
        }

        public void setBlockHit(HitResult rayTraceResult) {
            if (rayTraceResult.getType() == HitResult.Type.BLOCK)
                this.blockHit = (BlockHitResult) rayTraceResult;
        }

        public void addEntityHit(LivingEntity entity) {
            entities.add(entity);
        }
    }
}
