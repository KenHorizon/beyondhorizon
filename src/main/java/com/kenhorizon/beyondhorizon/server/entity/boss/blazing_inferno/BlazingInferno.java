package com.kenhorizon.beyondhorizon.server.entity.boss.blazing_inferno;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.render.misc.tooltips.Tooltips;
import com.kenhorizon.beyondhorizon.client.render.util.ColorUtil;
import com.kenhorizon.beyondhorizon.client.particle.RingParticles;
import com.kenhorizon.beyondhorizon.client.particle.TrailParticles;
import com.kenhorizon.beyondhorizon.client.particle.world.ParticleTrailOptions;
import com.kenhorizon.beyondhorizon.client.particle.world.RingParticleOptions;
import com.kenhorizon.beyondhorizon.client.particle.world.RoarParticleOptions;
import com.kenhorizon.beyondhorizon.client.sound.DeathRayChargingSound;
import com.kenhorizon.beyondhorizon.server.entity.ability.AbstractDeathRayAbility;
import com.kenhorizon.beyondhorizon.server.entity.ability.BlazingInfernoRayAbility;
import com.kenhorizon.beyondhorizon.server.entity.ability.EruptionAbility;
import com.kenhorizon.beyondhorizon.server.entity.ability.FlameStrikeAbility;
import com.kenhorizon.beyondhorizon.server.entity.boss.BHBossEntity;
import com.kenhorizon.beyondhorizon.server.entity.CameraShake;
import com.kenhorizon.beyondhorizon.server.entity.ai.*;
import com.kenhorizon.beyondhorizon.server.entity.projectiles.BlazingRod;
import com.kenhorizon.beyondhorizon.server.entity.projectiles.HellfireOrb;
import com.kenhorizon.beyondhorizon.server.entity.projectiles.HellfireRod;
import com.kenhorizon.beyondhorizon.server.util.DefaultDamageCaps;
import com.kenhorizon.beyondhorizon.server.entity.util.EntityUtils;
import com.kenhorizon.beyondhorizon.server.init.*;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageTags;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlazingInferno extends BHBossEntity {
    public AnimationState animationActive = new AnimationState();
    public AnimationState animationInactive = new AnimationState();
    public AnimationState animationEnragedPhase = new AnimationState();
    public AnimationState animationDodge = new AnimationState();
    public AnimationState animationSpear = new AnimationState();
    public AnimationState animationPrepareDeathRay = new AnimationState();
    public AnimationState animationDeathRay = new AnimationState();
    public AnimationState animationBlazingRod = new AnimationState();
    public AnimationState animationGroundSlam = new AnimationState();
    public AnimationState animationEruption = new AnimationState();
    public AnimationState animationShockwave = new AnimationState();
    public AnimationState animationDashes = new AnimationState();
    public AnimationState animationDeath = new AnimationState();
    public AnimationState animationJump = new AnimationState();
    public AnimationState animationHellfireDown = new AnimationState();
    public AnimationState animationFlameStrike = new AnimationState();
    public AnimationState animationHellOrb = new AnimationState();
    public static int animationId = 1;
    public static final int ID_ACTIVE = createAnimationID();
    public static final int ID_INACTIVE = createAnimationID();
    public static final int ID_ENRAGED_PHASE = createAnimationID();
    public static final int ID_DEATH = createAnimationID();
    public static final int ID_DODGE = createAnimationID();
    public static final int ID_SPEAR = createAnimationID();
    public static final int ID_PREPARE_DEATH_RAY = createAnimationID();
    public static final int ID_DEATH_RAY = createAnimationID();
    public static final int ID_BLAZING_ROD = createAnimationID();
    public static final int ID_GROUND_SLAM = createAnimationID();
    public static final int ID_ERUPTION = createAnimationID();
    public static final int ID_SHOCKWAVE = createAnimationID();
    public static final int ID_DASHES = createAnimationID();
    public static final int ID_JUMP = createAnimationID();
    public static final int ID_HELLFIRE_DOWN = createAnimationID();
    public static final int ID_FLAME_STRIKE = createAnimationID();
    public static final int ID_HELLORB = createAnimationID();

    public List<InfernoShield> infernoShields = new ArrayList<>();
    public int fireballCooldown = 0;
    public static final int FIREBALL_COOLDOWN = Maths.sec(5);
    public int spearCooldown = 0;
    public static final int SPEAR_COOLDOWN = Maths.sec(6);
    public int deathRayCooldown = 0;
    public static final int DEATH_RAY_COOLDOWN = Maths.sec(35);
    public int groundSlamCooldown = 0;
    public static final int GROUND_SLAM_COOLDOWN = Maths.sec(16);
    public int dashCooldown = 0;
    public static final int DASH_COOLDOWN = Maths.sec(24);
    public int shockwaveCooldown = 0;
    public static final int SHOCKWAVE_COOLDOWN = Maths.sec(7);
    public int shieldCooldown = 0;
    public static final int SHIELD_COOLDOWN = Maths.mins(1);
    public int flameStrikeCooldown = 0;
    public static final int FLAME_STRIKE_COOLDOWN = Maths.sec(12);
    public int hellfireDownCooldown = 0;
    public static final int HELLFIRE_DOWN_COOLDOWN = Maths.sec(55);
    public int hellfireOrbCooldown = 0;
    public static final int HELLFIRE_ORB_COOLDOWN = Maths.sec(10);
    public int eruptionCooldown = 0;
    public static final int ERUPTION_COOLDOWN = Maths.sec(10);
    public static final int ENRAGED_COOLDOWN = Maths.sec(5);
    public static final int AWAKEN_COOLDOWN = Maths.sec(5);

    public static final String NBT_POWERED = "IsPowered";
    public static final String NBT_ENRAGED = "IsEnraged";
    public static final String NBT_SHIELD_COUNT = "ShieldCount";
    public static final String NBT_SHIELD_ACTIVE = "ShieldActive";
    public static final String NBT_AWAKEN_PROGRESS = "AwakenProgress";
    public static final String NBT_ENRAGED_PROGRESS = "EnragedProgress";
    public static final String NBT_SHIELD_COOLDOWN = "ShieldCooldown";
    public static final EntityDataAccessor<Boolean> POWERED = SynchedEntityData.defineId(BlazingInferno.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> IS_DASHING = SynchedEntityData.defineId(BlazingInferno.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> ENRAGED = SynchedEntityData.defineId(BlazingInferno.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Integer> DATA_SHIELD_COOLDOWN = SynchedEntityData.defineId(BlazingInferno.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> SHIELD_COUNT = SynchedEntityData.defineId(BlazingInferno.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> AWAKEN_PROGRESS = SynchedEntityData.defineId(BlazingInferno.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> ENRAGED_PROGRESS = SynchedEntityData.defineId(BlazingInferno.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> SHIELD_ACTIVE = SynchedEntityData.defineId(BlazingInferno.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DEATH_RAY = SynchedEntityData.defineId(BlazingInferno.class, EntityDataSerializers.BOOLEAN);

    public BlazingInferno(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        this.setExp(500);
        this.setDamageCap(DefaultDamageCaps.BLAZING_INFERNO);
        this.setMaxUpStep(2.0F);
        this.setMaxBossPhase(5);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.LAVA, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.UNPASSABLE_RAIL, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, 0.0F);
    }

    private static int createAnimationID() {
        return animationId++;
    }

    public static AttributeSupplier createAttributes() {
        return createEntityAttributes()
                .add(Attributes.MAX_HEALTH, 800.0D)
                .add(Attributes.ARMOR, 24.0D)
                .add(BHAttributes.DAMAGE_TAKEN.get(), -0.15D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2555F)
                .add(Attributes.ATTACK_DAMAGE, 16.0D)
                .add(Attributes.ATTACK_SPEED, 1.0F)
                .add(Attributes.FOLLOW_RANGE, 70.0F)
                .build();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0D, 80));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(0, new NaturalHealingGoal(this));
        this.goalSelector.addGoal(0, new BossInactiveGoal(!this.isPowered() || this.getAnimation() == 3));
        this.goalSelector.addGoal(4, new MobMoveGoal(this, false, 1.0F));
        this.goalSelector.addGoal(1, new MobStateGoal<>(this, ID_INACTIVE,ID_INACTIVE, ID_ANIMATION_EMPTY, 0, 0) {
            @Override
            public void tick() {
                this.entity.setCantMoved();
            }
        });
        this.goalSelector.addGoal(0, new BlazingInfernoAwakenGoal(this, ID_INACTIVE, ID_ACTIVE, ID_ANIMATION_EMPTY, 0, Maths.sec(5)));
        this.targetSelector.addGoal(1, new HurtByNearestTargetGoal(this));
        this.targetSelector.addGoal(1, new FireballAttackGoal(this, ID_DEATH_RAY, ID_BLAZING_ROD, ID_ANIMATION_EMPTY, 30, Maths.sec(5), 80.0D));
        this.targetSelector.addGoal(1, new FireballAttackGoal(this, ID_ANIMATION_EMPTY, ID_BLAZING_ROD, ID_ANIMATION_EMPTY, 30, Maths.sec(5), 80.0D));
        this.targetSelector.addGoal(1, new SpearAttackGoal(this, ID_ANIMATION_EMPTY, ID_SPEAR, ID_ANIMATION_EMPTY, 30, Maths.sec(3), 80.0D));
        this.targetSelector.addGoal(1, new GroundSlamAttackGoal(this, ID_ANIMATION_EMPTY, ID_GROUND_SLAM, ID_ANIMATION_EMPTY, 20, 80.0D));
        this.targetSelector.addGoal(1, new EruptionAttackGoal(this, ID_ANIMATION_EMPTY, ID_ERUPTION, ID_ANIMATION_EMPTY, 20, Maths.sec(2), 80.0D));
        this.targetSelector.addGoal(1, new FlameStrikeAttackGoal(this, ID_ANIMATION_EMPTY, ID_FLAME_STRIKE, ID_ANIMATION_EMPTY, 40, Maths.sec(3), 80.0D));
        this.targetSelector.addGoal(1, new HellOrbAttackGoal(this, ID_ANIMATION_EMPTY, ID_HELLORB, ID_ANIMATION_EMPTY, 40, Maths.sec(6), 80.0D));
        this.targetSelector.addGoal(1, new HellfireDownAttackGoal(this, ID_ANIMATION_EMPTY, ID_HELLFIRE_DOWN, ID_ANIMATION_EMPTY, 40, Maths.sec(5), 80.0D));
        this.targetSelector.addGoal(1, new ShockwaveAttackGoal(this, ID_ANIMATION_EMPTY, ID_SHOCKWAVE, ID_ANIMATION_EMPTY, 40, Maths.sec(5), 80.0D));
        this.targetSelector.addGoal(1, new DashAttackGoal(this, ID_ANIMATION_EMPTY, ID_DASHES, ID_ANIMATION_EMPTY, 40, Maths.sec(3), 80.0D));
        this.targetSelector.addGoal(1, new DeathRayAttackGoal(this, ID_ANIMATION_EMPTY, ID_PREPARE_DEATH_RAY, ID_DEATH_RAY, Maths.sec(5), 100.0D));

        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractGolem.class, true));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SHIELD_COOLDOWN, 0);
        this.entityData.define(ENRAGED_PROGRESS, 0);
        this.entityData.define(AWAKEN_PROGRESS, 0);
        this.entityData.define(SHIELD_COUNT, 4);
        this.entityData.define(ENRAGED, false);
        this.entityData.define(SHIELD_ACTIVE, false);
        this.entityData.define(IS_DASHING, false);
        this.entityData.define(POWERED, true);
        this.entityData.define(DEATH_RAY, false);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.setIsPowered(nbt.getBoolean(NBT_POWERED));
        this.setIsEnraged(nbt.getBoolean(NBT_ENRAGED));
        this.setShieldCount(nbt.getInt(NBT_SHIELD_COUNT));
        this.setInfernoShieldActive(nbt.getBoolean(NBT_SHIELD_ACTIVE));
        this.setAwakenProgress(nbt.getInt(NBT_AWAKEN_PROGRESS));
        this.setEnragedProgress(nbt.getInt(NBT_ENRAGED_PROGRESS));
        this.setShieldCooldown(nbt.getInt(NBT_SHIELD_COOLDOWN));
    }
    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean(NBT_POWERED, this.isPowered());
        nbt.putBoolean(NBT_ENRAGED, this.isEnraged());
        nbt.putInt(NBT_SHIELD_COUNT, this.getShieldCount());
        nbt.putBoolean(NBT_SHIELD_ACTIVE, this.isInfernoShieldActive());
        nbt.putInt(NBT_AWAKEN_PROGRESS, this.getAwakenProgress());
        nbt.putInt(NBT_ENRAGED_PROGRESS, this.getEnragedProgress());
        nbt.putInt(NBT_SHIELD_COOLDOWN, this.getShieldCooldown());
    }
    public void setShieldCooldown(int shieldCooldown) {
        this.shieldCooldown = shieldCooldown;
        this.entityData.set(DATA_SHIELD_COOLDOWN, shieldCooldown);
    }

    public int getShieldCooldown() {
        if (this.level().isClientSide()) {
            return this.entityData.get(DATA_SHIELD_COOLDOWN);
        } else {
            return this.shieldCooldown;
        }
    }

    public void setAwakenProgress(int awakenProgress) {
        this.entityData.set(AWAKEN_PROGRESS, Math.min(awakenProgress, 100));
    }

    public int getAwakenProgress() {
        return this.entityData.get(AWAKEN_PROGRESS);
    }

    public void setEnragedProgress(int enragedProgress) {
        this.entityData.set(ENRAGED_PROGRESS, Math.min(enragedProgress, 100));
    }

    public int getEnragedProgress() {
        return this.entityData.get(ENRAGED_PROGRESS);
    }

    public boolean isSleep() {
        return this.getAnimation() == ID_INACTIVE || this.getAnimation() == ID_ACTIVE;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.isEnraged() ? BHSounds.BLAZING_INFERNO_EXPLOSION.get() : BHSounds.BLAZING_INFERNO_DEATH.get();
    }

    @Override
    protected int getDeathDuration() {
        return Maths.sec(10);
    }

    @Override
    public int getAnimationDeath() {
        return ID_DEATH;
    }

    public boolean isBossImmune() {
        return this.getAnimationState(ID_ENRAGED_PHASE) || this.isSleep() || this.isInfernoShieldActive();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean flag = source.is(DamageTypes.GENERIC) || source.is(DamageTypes.GENERIC_KILL);
        boolean immune = this.isBossImmune();
        if (this.getAnimationState(ID_SHOCKWAVE) && this.getAnimationTick() <= Maths.sec(2)) {
            amount *= 0.5F;
        }
        if (this.getAnimationState(ID_HELLFIRE_DOWN)) {
            amount *= 0.5F;
        }
        if (source.getEntity() instanceof AbstractGolem) {
            amount *= 0.25F;
        }
        if (this.isHalfHealth() && !this.isEnraged() && !flag) {
            this.setAnimation(ID_ENRAGED_PHASE);
            this.setHealth(this.getMaxHealth() / 2);
        }
        if (immune) {
            boolean gotHurt = super.hurt(source, amount);
            if (!flag) {
                float shieldDamage = amount;
                if (source.getEntity() instanceof LivingEntity entity) {
                    if (entity.getMainHandItem().getItem() instanceof AxeItem) {
                        shieldDamage *= 2.0F;
                    }
                }
                if (gotHurt && !this.infernoShields.isEmpty()) {
                    InfernoShield shield = this.infernoShields.get(this.getRandom().nextInt(this.infernoShields.size()));
                    shield.hurt(source, shieldDamage);
                }
                return false;
            }
        }
        return super.hurt(source, amount);
    }

    @Override
    protected int decreaseAirSupply(int currentAir) {
        return currentAir;
    }

    @Override
    public boolean canStandOnFluid(FluidState fluidState) {
        return fluidState.is(FluidTags.LAVA) || fluidState.is(FluidTags.WATER);
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    public void setInfernoShieldActive(boolean isActive) {
        this.entityData.set(SHIELD_ACTIVE, isActive);
    }

    public boolean isInfernoShieldActive() {
        return this.entityData.get(SHIELD_ACTIVE);
    }

    public void setIsEnraged(boolean enraged) {
        this.entityData.set(ENRAGED, enraged);
    }

    public boolean isEnraged() {
        return this.entityData.get(ENRAGED);
    }

    public void setIsDashing(boolean dash) {
        this.entityData.set(IS_DASHING, dash);
    }

    public boolean isDashing() {
        return this.entityData.get(IS_DASHING);
    }

    public void setIsUsingDeathRay(boolean isUsingDeathRay) {
        this.entityData.set(DEATH_RAY, isUsingDeathRay);
    }

    public boolean isUsingDeathRay() {
        return entityData.get(DEATH_RAY);
    }

    public void setIsPowered(boolean powered) {
        this.entityData.set(POWERED, powered);
        this.bossInfo().setVisible(powered);
        if (!powered) {
            this.setAnimation(ID_INACTIVE);
        }
    }

    public boolean isPowered() {
        return this.entityData.get(POWERED);
    }

    public int getShieldCount() {
        return this.entityData.get(SHIELD_COUNT);
    }

    public void setShieldCount(int count) {
        this.entityData.set(SHIELD_COUNT, count);
    }

    @Override
    protected void afterItDefeated(@Nullable LivingEntity entity) {
        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.getPlayers(EntitySelector.NO_SPECTATORS).forEach(serverPlayer -> {
                Component bossName = this.getDisplayName();
                serverPlayer.displayClientMessage(Component.translatable(Tooltips.BOSS_IS_DEFEATED, bossName).withStyle(ChatFormatting.GOLD), false);
                serverPlayer.displayClientMessage(Component.translatable(Tooltips.getBossMessage(this.getType())).withStyle(ChatFormatting.GOLD), false);
            });
        }
    }

    @Override
    public void bossPhases() {
        if (this.getBossPhase() == 0 && this.inBetweenHealth(1.0F, 0.75F)) {
            this.setBossPhase(1);
        }
        if (this.getBossPhase() == 0 && this.inBetweenHealth(0.75F, 0.50F)) {
            this.setBossPhase(2);
        }
        if (this.getBossPhase() == 1 && this.inBetweenHealth(0.50F, 0.25F)) {
            this.setBossPhase(3);
        }
        if (this.getBossPhase() == 2 && this.inBetweenHealth(0.25F, 0.15F)) {
            this.setBossPhase(4);
        }
        if (this.getBossPhase() == 3 && this.inBetweenHealth(0.15F, 0.0F)) {
            this.setBossPhase(5);
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.setInfernoShieldActive(!this.infernoShields.isEmpty());
        if (!this.isEnraged() && !this.isInfernoShieldActive() && this.getShieldCooldown() > 0) this.setShieldCooldown(this.getShieldCooldown() - 1);
        if (this.hellfireOrbCooldown > 0) this.hellfireOrbCooldown--;
        if (this.hellfireDownCooldown > 0) this.hellfireDownCooldown--;
        if (this.flameStrikeCooldown > 0) this.flameStrikeCooldown--;
        if (this.eruptionCooldown > 0) this.eruptionCooldown--;
        if (this.spearCooldown > 0) this.spearCooldown--;
        if (this.dashCooldown > 0) this.dashCooldown--;
        if (this.deathRayCooldown > 0) this.deathRayCooldown--;
        if (this.shockwaveCooldown > 0) this.shockwaveCooldown--;
        if (this.groundSlamCooldown > 0) this.groundSlamCooldown--;
        if (this.fireballCooldown > 0) this.fireballCooldown--;
        if (this.getAnimationState(ID_ACTIVE)) {
            this.setAwakenProgress(this.getAwakenProgress() + 1);
        }
        if (this.getAwakenProgress() > 100) {
            this.summonShield(true);
        }
        this.setIsUsingDeathRay(this.getAnimationState(ID_PREPARE_DEATH_RAY));
        this.infernoShields = this.getEntitiesNearby(InfernoShield.class, 16.0F);
        if (this.infernoShields.isEmpty()) {
            this.summonShield();
        }
        if (this.getAnimationState(ID_ENRAGED_PHASE)) {
            this.setEnragedProgress(this.getEnragedProgress() + 1);
            if (this.getEnragedProgress() >= Maths.sec(5)) {
                this.doRoarParticle(this.getX(), this.getEyeY(), this.getZ(), 10, 255, 0, 0, 1.0F, 1.0F, 5.0F, 0.1F);
                this.setIsEnraged(true);
                this.bossInfo().setRenderType(1);
                this.setHealth(this.getMaxHealth() / 2);
                this.setAnimation(ID_ANIMATION_EMPTY);
            }
        }
        if (this.isEnraged()) {
            this.bossInfo().setRenderType(1);
        } else {
            this.bossInfo().setRenderType(0);
        }
    }

    @Override
    protected void playHurtSound(DamageSource source) {
        this.playSound(BHSounds.BLAZING_INFERNO_HURT.get());
    }


    @Override
    public void aiStep() {
        super.aiStep();
        if (this.getAnimation() != this.getPrevAnimationState()) {
            BeyondHorizon.LOGGER.debug("Animation State: {}", this.getAnimation());
        }
        if (!this.isSleep()) {
            if (this.level().isClientSide()) {
                int flameCount = this.isEnraged() ? 5 : 2;
                for (int i = 0; i < flameCount; ++i) {
                    this.level().addParticle(ParticleTypes.FLAME, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), 0.0D, 0.0D, 0.0D);
                }
            }
        }
        if (this.getAnimationState(ID_ENRAGED_PHASE)) {
            this.setCantMoved();
        }

        LivingEntity target = this.getTarget();
        if (this.getAnimationState(ID_ANIMATION_EMPTY)) {
            this.doDodge(25);
        }
        if (this.getAnimationState(ID_INACTIVE)) {
            this.shieldCooldown = SHIELD_COOLDOWN;
        }
        if (this.getAnimationState(ID_ACTIVE)) {
            if (this.level().isClientSide()) {
                if (this.getAnimationTick() == 90) {
                    this.shieldCooldown = 0;
                    this.summonShield();
                    float yaw = (float) Math.toRadians(-this.getYRot());
                    float pitch = (float) Math.toRadians(-this.getXRot());
                    int r = ColorUtil.getARGB(0xFFFFFF)[0];
                    int g = ColorUtil.getARGB(0xFFFFFF)[1];
                    int b = ColorUtil.getARGB(0xFFFFFF)[2];
                    this.doRoarParticle(this.getX(), this.getY(0.5D), this.getZ(), 10, 255, 255, 255, 1.0F, 1.0F, 0.1F, 10.0F);
                    this.level().addAlwaysVisibleParticle(new RingParticleOptions(0, (float) Math.PI / 2, 11, r, g, b, 1.0F, 16.0F, false, RingParticles.Behavior.GROW), this.getX(), this.getY(), this.getZ(), 0, 0, 0);
                    this.level().addAlwaysVisibleParticle(new RingParticleOptions(yaw, pitch, 15, r, g, b, 1.0F, 32.0F, false, RingParticles.Behavior.SHRINK), this.getX(), this.getY() + this.getBbHeight() / 2 + 0.5F, this.getZ(), 0, 0, 0);

                    CameraShake.spawn(this.level(), this.position(), 24, 0.12F, 20, 10);
                    if (!this.isSilent()) {
                        this.level().playSound((Player) null, this, BHSounds.BLAZING_INFERNO_SCREAM.get(), SoundSource.HOSTILE, 3.0F, 1.0F);
                    }
                }
            }
        }
        if (this.getAnimationState(ID_BLAZING_ROD)) {
            this.setCantMoved();
            if (target != null) {
                if (this.getAnimationTick() > 50 && this.isEnraged()) {
                    this.doRoarParticle(this.getX(), this.getEyeY(), this.getZ(), 10, 255, 0, 0, 1.0F, 1.0F, 5.0F, 0.1F);
                    if (!this.isSilent()) {
                        this.level().playSound((Player) null, this, BHSounds.BLAZING_INFERNO_SHOOT.get(), SoundSource.HOSTILE, 3.0F, 1.0F);
                    }
                    this.shoot(3, target, 1.0F, 0.2F, false);
                } else {
                    int fireRate = this.isEnraged() ? 5 : 10;
                    float velocity = this.isEnraged() ? 1.25F : 1.0F;
                    if (this.getAnimationTick() % fireRate == 0) {
                        this.doRoarParticle(this.getX(), this.getEyeY(), this.getZ(), 10, 255, 0, 0, 1.0F, 1.0F, 5.0F, 0.1F);
                        if (!this.isSilent()) {
                            this.level().playSound((Player) null, this, BHSounds.BLAZING_INFERNO_SHOOT.get(), SoundSource.HOSTILE, 3.0F, 1.0F);
                        }
                        this.shoot(3, target, velocity, 0.2F);
                    }
                }
            }
        }
        if (this.getAnimationState(ID_SPEAR)) {
            if (this.getAnimationTick() == 1 && target != null) {
                this.doAvoidTarget(target);
            }

            if (this.getAnimationTick() == 40) {
                this.doRoarParticle(this.getX(), this.getEyeY(), this.getZ(), 10, 255, 0, 0, 1.0F, 1.0F, 5.0F, 0.1F);
            }
            switch (this.getRandom().nextInt(3)) {
                case 1 -> {
                    if (target != null && this.getAnimationTick() == 40) {
                        if (!this.isSilent()) {
                            this.level().playSound((Player) null, this, BHSounds.BLAZING_INFERNO_SHOOT.get(), SoundSource.HOSTILE, 3.0F, 1.0F);
                        }
                        this.shootSpear(new Vec3(2, 1, 0), Maths.sec(3));
                        this.shootSpear(new Vec3(-2, 1, 0), Maths.sec(4));
                        this.shootSpear(new Vec3(0, 1, 0), Maths.sec(5));
                    }
                }
                case 2 -> {
                    if (target != null && this.getAnimationTick() == 40) {
                        if (!this.isSilent()) {
                            this.level().playSound((Player) null, this, BHSounds.BLAZING_INFERNO_SHOOT.get(), SoundSource.HOSTILE, 3.0F, 1.0F);
                        }
                        CameraShake.spawn(this.level(), this.position(), 32.0F, 0.15F, 5, 20);
                        int count = 12;
                        for (int i = 0; i < count; i++) {
                            float rotate = (360F / count) * i;
                            this.shootSpear(new Vec3(Math.cos(rotate), 1, Math.sin(rotate)), Maths.sec(1));
                        }
                    }
                }
                case 3 -> {
                    if (target != null && this.getAnimationTick() == 40) {
                        if (!this.isSilent()) {
                            this.level().playSound((Player) null, this, BHSounds.BLAZING_INFERNO_SHOOT.get(), SoundSource.HOSTILE, 3.0F, 1.0F);
                        }
                        CameraShake.spawn(this.level(), this.position(), 32.0F, 0.15F, 5, 20);
                        int count = 8;
                        for (int i = 0; i < count; i++) {
                            float rotate = (360F / count) * i;
                            this.shootSpear(new Vec3(Math.cos(rotate), 1, Math.sin(rotate)), Maths.sec(3));
                        }
                    }
                }
            }
        }
        if (this.getAnimationState(ID_GROUND_SLAM)) {
            if (this.getAnimationTick() == 20) {
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), BHSounds.BLAZING_INFERNO_SCREAM.get(), SoundSource.HOSTILE, 1.0F, 1.0F);
                if (this.level().isClientSide()) {
                    int particleCount = 32;
                    while (particleCount --> 0) {
                        double radius = 5.0F;
                        float yaw = (float) (this.random.nextFloat() * 2 * Math.PI);
                        float pitch = (float) (this.random.nextFloat() * 2 * Math.PI);
                        double ox = (float) (radius * Math.sin(yaw) * Math.sin(pitch));
                        double oy = (float) (radius * Math.cos(pitch));
                        double oz = (float) (radius * Math.cos(yaw) * Math.sin(pitch));
                        ParticleTrailOptions.add(this.level(), TrailParticles.Behavior.FADE, this.getX() + ox, this.getY() + oy + 0.1, this.getZ() + oz, 6.0F, 1, 0.0F, 0.0F, 1.0F, 10, new Vec3(this.getX(), this.getY() + this.getBbHeight() / 2 + 0.5F, this.getZ()));
                    }
                    for (int i = 0; i < 10; ++i) {
                        this.level().addParticle(ParticleTypes.LARGE_SMOKE, this.getRandomX(0.5D), this.getBbHeight() * 0.01D, this.getRandomZ(0.5D), 0.0D, 0.0D, 0.0D);
                        this.level().addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getRandomX(0.5D), this.getBbHeight() * 0.01D, this.getRandomZ(0.5D), 0.0D, 0.0D, 0.0D);
                    }
                    float r = ColorUtil.getFARGB(0xFFFFFF)[0];
                    float g = ColorUtil.getFARGB(0xFFFFFF)[1];
                    float b = ColorUtil.getFARGB(0xFFFFFF)[2];
                    this.level().addAlwaysVisibleParticle(new RingParticleOptions(0, (float) Math.PI / 2, 30, r, g, b, 1.0F, 64.0F, false, RingParticles.Behavior.SHRINK), this.getX(), this.getY(0.55D), this.getZ(), 0, 0, 0);
                    this.level().addParticle(new RoarParticleOptions(10, 255, 255, 255, 1.0F, 1.0F, 0.1F, 25.0F), this.getX(), this.getY(0.5D), this.getZ(), 0, 0, 0);
                }
            }
            if (this.getAnimationTick() == 40) {
                this.doJumpTarget(target, 0.15D);
            }
            if (this.getAnimationTick() > 60 && this.onGround()) {
                this.doShakeBlock(this, this.getAnimationTick());
                CameraShake.spawn(this.level(), this.position(), 16.0F, 0.05F, 5, 20);
                if (this.level().isClientSide()) {
                    EntityUtils.groundSlamParticles(this.level(), this.yBodyRot, this.getX(), this.getY(0.5D), this.getZ(), 6.5F,  0.25F, 0.065F);
                }

                if (this.isEnraged()) {
                    double d0 = this.getY();
                    double d1 = this.getY() + 1.0D;
                    int range = 32;
                    RandomSource random = RandomSource.create();
                    int randomNms = random.nextIntBetweenInclusive(-range, range);
                    float inBetween = (float) Mth.atan2((this.getZ() + randomNms) - this.getZ(), (this.getX() + randomNms) - this.getX());
                    for (int i = 0; i < (range / 2); ++i) {
                        double d2 = i + 1;
                        this.createEruption(this.getX() + (double) Mth.cos(inBetween) * d2, this.getZ() + (double) Mth.sin(inBetween) * d2, d0, d1, i);
                        this.createEruption(this.getX() - (double) Mth.cos(inBetween) * d2, this.getZ() + (double) Mth.sin(inBetween) * d2, d0, d1, i);
                        this.createEruption(this.getX() + (double) Mth.cos(inBetween) * d2, this.getZ() - (double) Mth.sin(inBetween) * d2, d0, d1, i);
                        this.createEruption(this.getX() - (double) Mth.cos(inBetween) * d2, this.getZ() - (double) Mth.sin(inBetween) * d2, d0, d1, i);
                    }
                }
                float damage = (this.getAttackDamage() * 0.55F);
                if (target != null && this.hurtEntitiesAround(this.position(), 6.5F, damage, 2.75F, true, true)) {
                    target.addEffect(new MobEffectInstance(BHEffects.STUN.get(), 40, 0, true, true));
                    if (!this.isSilent()) {
                        this.level().playSound((Player) null, this, BHSounds.BLAZING_INFERNO_SHOCKWAVE.get(), SoundSource.HOSTILE, 3.0F, 1.0F);
                    }
                }
                this.setAnimation(0);
            }
        }
        if (this.getAnimationState(ID_FLAME_STRIKE)) {
            int intervalAttack = 20;
            if (target != null) {
                if (this.getAnimationTick() % intervalAttack == 0) {
                    if (!this.isSilent()) {
                        this.level().playSound((Player) null, this, BHSounds.BLAZING_INFERNO_CHARGE.get(), SoundSource.HOSTILE, 3.0F, 1.0F);
                    }
                    double d0 = this.getY();
                    double d1 = this.getY() + 1.0D;
                    double x = target.getX();
                    double z = target.getZ();
                    this.createFlameStrike(x, z, d0, d1, 3);
                }
            } else {
                this.createFlameStrike(8);
            }
        }

        if (this.getAnimationState(ID_HELLORB)) {
            this.setCantMoved();
            if (target != null && this.getAnimationTick() == 20) {
                this.shootHellfireRod(3, target, 0.75F, 0, true);
            }
            if (target != null && this.getAnimationTick() == 40) {
                this.shootHellfireRod(6, target, 0.75F, 0, true);
            }
            if (target != null && this.getAnimationTick() == 60) {
                this.shootHellfireRod(9, target, 0.75F, 0, true);
            }
        }

        if (this.getAnimationState(ERUPTION_COOLDOWN)) {
            if (this.getAnimationTick() == 10) {
                if (!this.isSilent()) {
                    this.level().playSound((Player) null, this, BHSounds.BLAZING_INFERNO_GROWL.get(), SoundSource.HOSTILE, 3.0F, 1.0F);
                }
                float r = ColorUtil.getFARGB(0xFFFFFF)[0];
                float g = ColorUtil.getFARGB(0xFFFFFF)[1];
                float b = ColorUtil.getFARGB(0xFFFFFF)[2];
                this.level().addAlwaysVisibleParticle(new RingParticleOptions(0, (float) Math.PI / 2, 11, r, g, b, 1.0F, 16.0F, true, RingParticles.Behavior.GROW), this.getX(), this.getBbHeight() / 2, this.getZ(), 0, 0, 0);
                this.createEruption(32);
            }
        }
        if (this.getAnimationState(ID_PREPARE_DEATH_RAY)) {
            if (this.getAnimationTick() == 1) {
                BeyondHorizon.PROXY.playSound(new DeathRayChargingSound(this, BHSounds.BLAZING_INFERNO_DEATH_RAY_CHARGING.get()));
            }
            if (this.getAnimationTick() % 20L == 0) {
                if (this.level().isClientSide()) {
                    int particleCount = 32;
                    while (particleCount --> 0) {
                        double radius = 5.0F;
                        float yaw = (float) (this.random.nextFloat() * 2 * Math.PI);
                        float pitch = (float) (this.random.nextFloat() * 2 * Math.PI);
                        double ox = (float) (radius * Math.sin(yaw) * Math.sin(pitch));
                        double oy = (float) (radius * Math.cos(pitch));
                        double oz = (float) (radius * Math.cos(yaw) * Math.sin(pitch));
                        ParticleTrailOptions.add(this.level(), TrailParticles.Behavior.FADE, getX() + ox, getY() + oy + 0.1, getZ() + oz, 3.0F, 1, 0.0F, 0.0F, 1.0F, 20, new Vec3(this.getX(), this.getY() + this.getBbHeight() / 2 + 0.5F, this.getZ()));
                    }

                    float yaw = (float) Math.toRadians(-this.getYRot());
                    float pitch = (float) Math.toRadians(-this.getXRot());
                    float r = ColorUtil.getFARGB(0xFFFFFF)[0];
                    float g = ColorUtil.getFARGB(0xFFFFFF)[1];
                    float b = ColorUtil.getFARGB(0xFFFFFF)[2];
                    this.level().addAlwaysVisibleParticle(new RingParticleOptions(yaw, pitch, 15, r, g, b, 1.0F, 32.0F, true, RingParticles.Behavior.SHRINK), this.getX(), this.getY() + this.getBbHeight() / 2 + 0.5F, this.getZ(), 0, 0, 0);
                }
            }
        }
        if (this.getAnimationState(ID_DEATH_RAY)) {
            float radius = 0.8f;
            this.setCantMoved();
            if (target != null && this.getAnimationTick() >= 2) {
                this.getLookControl().setLookAt(target.getX(), target.getY() + target.getBbHeight() / 2, target.getZ(), 4, 90.0F);
            }
            if (this.getAnimationTick() == 2) {
                int duration = 80;
                BlazingInfernoRayAbility ability = new BlazingInfernoRayAbility(BHEntity.BLAZING_INFERNO_RAY.get(), this.level(), this, this.getX() + radius * Math.sin(-this.getYRot() * Math.PI / 180), this.getY() + 1.4, this.getZ() + radius * Math.cos(-this.getYRot() * Math.PI / 180), (float) ((this.yHeadRot + 90) * Math.PI / 180), (float) (-this.getXRot() * Math.PI / 180), duration);
                ability.laserBeamConfiguration(AbstractDeathRayAbility.DamageTypes.CURRENT_HEALTH, 1.0F);
                ability.setCanBurnTarget(true);
                ability.scaleCurrentHealthDamage(0.2F);
                ability.setImmunityFrameIgnore(false);
                this.level().addFreshEntity(ability);
            }
            if (this.getAnimationTick() == 95) {
                if (this.level().isClientSide()) {
                    for (int i = 0; i < 10; ++i) {
                        this.level().addParticle(ParticleTypes.LARGE_SMOKE, this.getRandomX(0.5D), this.getBbHeight() * 0.01D, this.getRandomZ(0.5D), 0.0D, 0.0D, 0.0D);
                        this.level().addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getRandomX(0.5D), this.getBbHeight() * 0.01D, this.getRandomZ(0.5D), 0.0D, 0.0D, 0.0D);
                    }
                    float r = ColorUtil.getFARGB(0xFFFFFF)[0];
                    float g = ColorUtil.getFARGB(0xFFFFFF)[1];
                    float b = ColorUtil.getFARGB(0xFFFFFF)[2];
                    this.level().addAlwaysVisibleParticle(new RingParticleOptions(0, 0, 11, r, g, b, 1.0F, 16.0F, true, RingParticles.Behavior.GROW), this.getX(), this.getY(0.5D), this.getZ(), 0, 0, 0);
                }
                this.setAnimation(0);
            }
        }
        if (this.getAnimationState(ID_SHOCKWAVE)) {
            if (this.getAnimationTick() == Maths.sec(1)) {
                if (this.level().isClientSide()) {
                    int particleCount = 32;
                    while (particleCount --> 0) {
                        double radius = 5.0F;
                        float yaw = (float) (this.random.nextFloat() * 2 * Math.PI);
                        float pitch = (float) (this.random.nextFloat() * 2 * Math.PI);
                        double ox = (float) (radius * Math.sin(yaw) * Math.sin(pitch));
                        double oy = (float) (radius * Math.cos(pitch));
                        double oz = (float) (radius * Math.cos(yaw) * Math.sin(pitch));
                        ParticleTrailOptions.add(this.level(), TrailParticles.Behavior.FADE, this.getX() + ox, this.getY() + oy + 0.1, this.getZ() + oz, 6.0F, 1, 0.0F, 0.0F, 1.0F, 10, new Vec3(this.getX(), this.getY() + this.getBbHeight() / 2 + 0.5F, this.getZ()));
                    }
                    for (int i = 0; i < 10; ++i) {
                        this.level().addParticle(ParticleTypes.LARGE_SMOKE, this.getRandomX(0.5D), this.getBbHeight() * 0.01D, this.getRandomZ(0.5D), 0.0D, 0.0D, 0.0D);
                        this.level().addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getRandomX(0.5D), this.getBbHeight() * 0.01D, this.getRandomZ(0.5D), 0.0D, 0.0D, 0.0D);
                    }
                    float r = ColorUtil.getFARGB(0xFFFFFF)[0];
                    float g = ColorUtil.getFARGB(0xFFFFFF)[1];
                    float b = ColorUtil.getFARGB(0xFFFFFF)[2];
                    this.level().addAlwaysVisibleParticle(new RingParticleOptions(0, (float) Math.PI / 2, 30, r, g, b, 1.0F, 64.0F, false, RingParticles.Behavior.SHRINK), this.getX(), this.getY(0.55D), this.getZ(), 0, 0, 0);
                    this.level().addParticle(new RoarParticleOptions(10, 255, 255, 255, 1.0F, 1.0F, 0.1F, 25.0F), this.getX(), this.getY(0.5D), this.getZ(), 0, 0, 0);

                }
            }
            if (this.getAnimationTick() == Maths.sec(4)) {
                this.setDeltaMovement(0, 0, 0);
                CameraShake.spawn(this.level(), this.position(), 16.0F, 0.05F, 5, 20);
                if (this.level().isClientSide()) {
                    float r = ColorUtil.getFARGB(0xFFFFFF)[0];
                    float g = ColorUtil.getFARGB(0xFFFFFF)[1];
                    float b = ColorUtil.getFARGB(0xFFFFFF)[2];
                    this.level().addAlwaysVisibleParticle(new RingParticleOptions(0, (float) Math.PI / 2, 10, r, g, b, 1.0F, 64.0F, false, RingParticles.Behavior.GROW), this.getX(), this.getY(0.55D), this.getZ(), 0, 0, 0);
                }
                float damage = this.getAttackDamage(0.15F);
                if (target != null && this.hurtEntitiesAround(this.position(), 10, damage, 2.75F, true, true)) {
                    target.addEffect(new MobEffectInstance(BHEffects.STUN.get(), 40, 0, true, true));
                    this.playSound(BHSounds.BLAZING_INFERNO_SHOCKWAVE.get());
                }
            }
            if (this.level() instanceof ServerLevel && this.getAnimationTick() > Maths.sec(2)) {
                this.doShakeBlock(this, this.getAnimationTick(), 20);
            }
        }
        this.dashAttack();
        if (this.getAnimationState(ID_DASHES)) {
            if (target != null) {
                if (this.getAnimationTick() < 45) {
                    this.lookAt(target, 30.0F, 3.0F);
                    this.getLookControl().setLookAt(target, 30.0F, 30.0F);
                } else {
                    this.setYRot(this.yRotO);
                }
            }
            if (this.getAnimationTick() < 55 && this.getAnimationTick() > 45) {
                this.setIsDashing(true);
                Vec3 vec3 = this.getDeltaMovement();
                this.playSound(BHSounds.BLAZING_INFERNO_GROWL.get());
                float rot = this.getYRot() * ((float) Math.PI / 180.0F);
                Vec3 newVec = new Vec3(-Mth.sin(rot), this.getDeltaMovement().y, Mth.cos(rot)).scale(1.0D).add(vec3.scale(0.5D));
                this.setDeltaMovement(newVec.x, this.getDeltaMovement().y, newVec.z);
            }
        }
        // Hellfire Down
        if (this.getAnimationState(ID_HELLFIRE_DOWN)) {
            this.setCantMoved();
            if (this.level().isClientSide()) {
                if (this.getAnimationTick() == 1) {
                    this.playSound(BHSounds.BLAZING_INFERNO_SCREAM.get());
                    float r = ColorUtil.getFARGB(0xFFFFFF)[0];
                    float g = ColorUtil.getFARGB(0xFFFFFF)[1];
                    float b = ColorUtil.getFARGB(0xFFFFFF)[2];
                    this.level().addAlwaysVisibleParticle(new RingParticleOptions(0, (float) Math.PI / 2, 11, r, g, b, 1.0F, 16.0F, true, RingParticles.Behavior.GROW), this.getX(), this.getBbHeight() / 2, this.getZ(), 0, 0, 0);
                }
            }
            int intervalAttack = 10;
            if (target != null && this.getAnimationTick() % intervalAttack == 0) {
                if (!this.isSilent()) {
                    this.level().playSound((Player)null, this.getX(), this.getY(), this.getZ(), BHSounds.BLAZING_INFERNO_SHOOT.get(), SoundSource.HOSTILE, 1.0F, 0.8F + this.random.nextFloat() * 0.4F);
                }
                HellfireOrb orb = new HellfireOrb(this.level(), HellfireOrb.EffectOnHit.STUN, this);
                orb.setDamageTag(DamageTags.TARGET_CURRENT_HEALTH);
                orb.setDamageTagModiifer(0.10F);
                orb.setStartPos(new Vec3(this.getX(), this.getEyeY(), this.getZ()));
                orb.setTargetPos(target.position());
                orb.setBaseDamage(this.getAttackDamage(0.20F));
                this.level().addFreshEntity(orb);
            }
        }
    }

    private void createEruption(int count) {
        for (int i = 0; i < count; i++) {
            float angle = i * Mth.PI / (count / 2);
            for (int k = 0; k < 8; ++k) {
                double d2 = 1.15D * (double) (k + 1);
                this.createEruption(this.getX() + (double) Mth.cos(angle) * 1.25D * d2, this.getZ() + (double) Mth.sin(angle) * 1.25D * d2, this.getY(), this.getY() + 2, i);
            }
        }
    }

    private void createFlameStrike(int count) {
        for (int i = 0; i < count; i++) {
            float angle = i * Mth.PI / (count / 2);
            for (int k = 0; k < 8; ++k) {
                double d2 = 1.15D * (double) (k + 1);
                this.createFlameStrike(this.getX() + (double) Mth.cos(angle) * 1.25D * d2, this.getZ() + (double) Mth.sin(angle) * 1.25D * d2, this.getY(), this.getY() + 2, i);
            }
        }
    }

    private void dashAttack() {
        if (this.isDashing()) {
            if (this.tickCount % 0.5F == 0) {
                float yaw = (float) Math.toRadians(-this.getYRot());
                float yaw1 = (float) Math.toRadians(-this.getYRot() + 180.0F);
                float pitch = (float) Math.toRadians(-this.getXRot());
                float r = ColorUtil.getFARGB(0xFFFFFF)[0];
                float g = ColorUtil.getFARGB(0xFFFFFF)[1];
                float b = ColorUtil.getFARGB(0xFFFFFF)[2];
                this.level().addAlwaysVisibleParticle(new RingParticleOptions(yaw, pitch, 15, r, g, b, 1.0F, 32.0F, false, RingParticles.Behavior.SHRINK), this.getX(), this.getY() + this.getBbHeight() / 2 + 0.5F, this.getZ(), 0, 0, 0);
                this.level().addAlwaysVisibleParticle(new RingParticleOptions(yaw1, pitch, 15, r, g, b, 1.0F, 32.0F, false, RingParticles.Behavior.SHRINK), this.getX(), this.getY() + this.getBbHeight() / 2 + 0.5F, this.getZ(), 0, 0, 0);
                float rangeAttack = this.isInfernoShieldActive() ? 2.5F : 1.0F;
                for (LivingEntity target : this.getEntitiesNearby(LivingEntity.class, rangeAttack)) {
                    if (!isAlliedTo(target) && !(target instanceof BlazingInferno) && target != this) {
                        if (target.hurt(this.level().damageSources().mobAttack(this), (float) (this.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.25F))) {
                            if (target.onGround()) {
                                double d0 = target.getX() - this.getX();
                                double d1 = target.getZ() - this.getZ();
                                double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
                                float f = 1.5F;
                                target.push(d0 / d2 * f, 0.5F, d1 / d2 * f);
                            }
                        }
                    }
                }
            }
        }
    }
    private void summonShield(boolean force) {
        this.summonShield(this.getShieldCount(), force);
    }
    private void summonShield() {
        this.summonShield(this.getShieldCount(), false);
    }

    private void summonShield(int count, boolean force) {
        if (!force) {
            if (!this.isSleep() && !this.isInfernoShieldActive() && this.shieldCooldown <= 0) {
                this.shieldCooldown = SHIELD_COOLDOWN;
                this.setShieldCount(count);
                float rotate = 360F / count;
                for (int i = 0; i < count; i++) {
                    InfernoShield summonShield = new InfernoShield(this.level(), this, 0.25F, i * rotate);
                    summonShield.isAlliedTo(this);
                    this.level().addFreshEntity(summonShield);
                }
            }
        } else {
            this.shieldCooldown = SHIELD_COOLDOWN;
            this.setShieldCount(count);
            float rotate = 360F / count;
            for (int i = 0; i < count; i++) {
                InfernoShield summonShield = new InfernoShield(this.level(), this, 0.25F, i * rotate);
                summonShield.isAlliedTo(this);
                this.level().addFreshEntity(summonShield);
            }
        }
    }

    private void sphereParticle(float size) {
        double d0 = this.blockPosition().getX() + 0.5F;
        double d1 = this.blockPosition().getY() + this.getBbHeight() / 2;
        double d2 = this.blockPosition().getZ() + 0.5F;
        for (float i = -size; i <= size; ++i) {
            for (float j = -size; j <= size; ++j) {
                for (float k = -size; k <= size; ++k) {
                    double d3 = (double) j + (this.random.nextDouble() - this.random.nextDouble()) * 0.5D;
                    double d4 = (double) i + (this.random.nextDouble() - this.random.nextDouble()) * 0.5D;
                    double d5 = (double) k + (this.random.nextDouble() - this.random.nextDouble()) * 0.5D;
                    double d6 = (double) Mth.sqrt((float) (d3 * d3 + d4 * d4 + d5 * d5)) / 0.5 + this.random.nextGaussian() * 0.05D;

                    this.level().addParticle(ParticleTypes.FLAME, d0, d1, d2, d3 / d6, d4 / d6, d5 / d6);

                    if (i != -size && i != size && j != -size && j != size) {
                        k += size * 2 - 1;
                    }
                }
            }
        }
    }
    private void createEruption(double x, double y, double minY, double maxY, int delay) {
        BlockPos blockpos = BlockPos.containing(x, maxY, y);
        boolean flag = false;
        double d0 = 0.0D;

        do {
            BlockPos blockpos1 = blockpos.below();
            BlockState blockstate = this.level().getBlockState(blockpos1);
            if (blockstate.isFaceSturdy(this.level(), blockpos1, Direction.UP)) {
                if (!this.level().isEmptyBlock(blockpos)) {
                    BlockState blockstate1 = this.level().getBlockState(blockpos);
                    VoxelShape voxelshape = blockstate1.getCollisionShape(this.level(), blockpos);
                    if (!voxelshape.isEmpty()) {
                        d0 = voxelshape.max(Direction.Axis.Y);
                    }
                }

                flag = true;
                break;
            }

            blockpos = blockpos.below();
        } while (blockpos.getY() >= Mth.floor(minY) - 1);

        if (flag) {
            EruptionAbility.spawn(this.level(), blockpos.getX() + 0.5, (double) blockpos.getY() + d0, (double) blockpos.getZ() + 0.5, this.getAttackDamage(), 0.0F, 0, this);
        }
    }

    private void createEruption(double x, double y, double minY, double maxY) {
        this.createEruption(x, y, minY, maxY, 3);
    }

    private void createFlameStrike(double x, double y, double minY, double maxY, int delay) {
        BlockPos blockpos = BlockPos.containing(x, maxY, y);
        boolean flag = false;
        double d0 = 0.0D;

        do {
            BlockPos blockpos1 = blockpos.below();
            BlockState blockstate = this.level().getBlockState(blockpos1);
            if (blockstate.isFaceSturdy(this.level(), blockpos1, Direction.UP)) {
                if (!this.level().isEmptyBlock(blockpos)) {
                    BlockState blockstate1 = this.level().getBlockState(blockpos);
                    VoxelShape voxelshape = blockstate1.getCollisionShape(this.level(), blockpos);
                    if (!voxelshape.isEmpty()) {
                        d0 = voxelshape.max(Direction.Axis.Y);
                    }
                }

                flag = true;
                break;
            }

            blockpos = blockpos.below();
        } while (blockpos.getY() >= Mth.floor(minY) - 1);

        if (flag) {
            FlameStrikeAbility.spawn(this.level(), blockpos.getX() + 0.5, (double) blockpos.getY() + d0, (double) blockpos.getZ() + 0.5, this.getAttackDamage(), 2.5F, Maths.sec(delay), this);
        }
    }

    private void createFlameStrike(double x, double y, double minY, double maxY) {
        this.createFlameStrike(x, y, minY, maxY, 3);
    }

    @Override
    public boolean onClimbable() {
        return false;
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        Item item = itemStack.getItem();
        if (item == BHItems.FLAME_CELL.get() && !this.isPowered()) {
            if (!player.isCreative()) {
                itemStack.shrink(1);
            }
            CameraShake.spawn(this.level(), this.position(), 64, 0.12F, 10, 20);
            this.setIsPowered(true);
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData data, @Nullable CompoundTag dataNbt) {
        if (reason == MobSpawnType.COMMAND) {
            this.setIsPowered(true);
        }
        return super.finalizeSpawn(level, difficulty, reason, data, dataNbt);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        if (ANIMATION_STATE.equals(accessor)) {
            if (this.getAnimation() == ID_ANIMATION_EMPTY) {
                this.stopAnimations();
            }
            if (this.getAnimation() == ID_INACTIVE) {
                this.stopAnimations();
                this.animationInactive.startIfStopped(this.tickCount);
            }
            if (this.getAnimation() == ID_ACTIVE) {
                this.stopAnimations();
                this.animationActive.startIfStopped(this.tickCount);
            }
            if (this.getAnimation() == ID_ENRAGED_PHASE) {
                this.stopAnimations();
                this.animationEnragedPhase.startIfStopped(this.tickCount);
            }
            if (this.getAnimation() == ID_DEATH) {
                this.stopAnimations();
                this.animationDeath.startIfStopped(this.tickCount);
            }
            if (this.getAnimation() == ID_BLAZING_ROD) {
                this.stopAnimations();
                this.animationBlazingRod.startIfStopped(this.tickCount);
            }
            if (this.getAnimation() == ID_SPEAR) {
                this.stopAnimations();
                this.animationSpear.startIfStopped(this.tickCount);
            }
            if (this.getAnimation() == ID_GROUND_SLAM) {
                this.stopAnimations();
                this.animationGroundSlam.startIfStopped(this.tickCount);
            }
            if (this.getAnimation() == ID_FLAME_STRIKE) {
                this.stopAnimations();
                this.animationFlameStrike.startIfStopped(this.tickCount);
            }
            if (this.getAnimation() == ID_PREPARE_DEATH_RAY) {
                this.stopAnimations();
                this.animationPrepareDeathRay.startIfStopped(this.tickCount);
            }
            if (this.getAnimation() == ID_DEATH_RAY) {
                this.stopAnimations();
                this.animationDeathRay.startIfStopped(this.tickCount);
            }

            if (this.getAnimation() == ID_SHOCKWAVE) {
                this.stopAnimations();
                this.animationShockwave.startIfStopped(this.tickCount);
            }
            if (this.getAnimation() == ID_DASHES) {
                this.stopAnimations();
                this.animationDashes.startIfStopped(this.tickCount);
            }
            if (this.getAnimation() == ID_HELLFIRE_DOWN) {
                this.stopAnimations();
                this.animationHellfireDown.startIfStopped(this.tickCount);
            }
//            switch (this.getAnimation()) {
//                case 0 -> this.stopAnimations();
//                case 1 -> {
//                    this.stopAnimations();
//                    this.animationInactive.startIfStopped(this.tickCount);
//                }
//                case 2 -> {
//                    this.stopAnimations();
//                    this.animationActive.startIfStopped(this.tickCount);
//                }
//                case 3 -> {
//                    this.stopAnimations();
//                    this.animationEnragedPhase.startIfStopped(this.tickCount);
//                }
//                case 4 -> {
//                    this.stopAnimations();
//                    this.animationDeath.startIfStopped(this.tickCount);
//                }
//                case 5 -> {
//                    this.stopAnimations();
//                    this.animationBlazingRod.startIfStopped(this.tickCount);
//                }
//                case 6 -> {
//                    this.stopAnimations();
//                    this.animationSpear0.startIfStopped(this.tickCount);
//                }
//                case 7 -> {
//                    this.stopAnimations();
//                    this.animationSpear1.startIfStopped(this.tickCount);
//                }
//                case 8 -> {
//                    this.stopAnimations();
//                    this.animationSpear2.startIfStopped(this.tickCount);
//                }
//                case 9 -> {
//                    this.stopAnimations();
//                    this.animationGroundSlam.startIfStopped(this.tickCount);
//                }
//                case 10 -> {
//                    this.stopAnimations();
//                    this.animationFlameStrike.startIfStopped(this.tickCount);
//                }
//                case 11 -> {
//                    this.stopAnimations();
//                    this.animationHellOrb.startIfStopped(this.tickCount);
//                }
//                case 12 -> {
//                    this.stopAnimations();
//                    this.animationEruption.startIfStopped(this.tickCount);
//                }
//                case 13 -> {
//                    this.stopAnimations();
//                    this.animationPrepareDeathRay.startIfStopped(this.tickCount);
//                }
//                case 14 -> {
//                    this.stopAnimations();
//                    this.animationDeathRay.startIfStopped(this.tickCount);
//                }
//                case 15 -> {
//                    this.stopAnimations();
//                    this.animationShockwave.startIfStopped(this.tickCount);
//                }
//                case 16 -> {
//                    this.stopAnimations();
//                    this.animationDashes.startIfStopped(this.tickCount);
//                }
//                case 17 -> {
//                    this.stopAnimations();
//                    this.animationHellfireDown.startIfStopped(this.tickCount);
//                }
//            }
        }
        super.onSyncedDataUpdated(accessor);
    }

    public void stopAnimations() {
        List<AnimationState> animationList = Arrays.stream(this.getAnimations()).toList();
        animationList.forEach(AnimationState::stop);
    }

    @Override
    public AnimationState[] getAnimations() {
        return new AnimationState[] {
                this.animationDodge,
                this.animationActive,
                this.animationInactive,
                this.animationSpear,
                this.animationDeathRay,
                this.animationPrepareDeathRay,
                this.animationFlameStrike,
                this.animationHellOrb,
                this.animationHellfireDown,
                this.animationFlameStrike,
                this.animationBlazingRod,
                this.animationGroundSlam,
                this.animationShockwave,
                this.animationDashes,
                this.animationDeath,
                this.animationJump,
                this.animationEruption,
                this.animationEnragedPhase
        };
    }

    @Override
    public boolean canBeSeenAsEnemy() {
        return !this.isSleep() && super.canBeSeenAsEnemy();
    }

    private void shootSpear(Vec3 position, int timer) {
        BlazingSpear projectile = new BlazingSpear(this.level(), this);
        position = position.yRot(-this.getYRot() * ((float) Math.PI / 180F));
        projectile.setDamage(DamageTags.TARGET_CURRENT_HEALTH, 0.05F);
        projectile.setBaseDamage(3);
        projectile.setPos(this.getX() - (double) (this.getBbWidth() + 1.0F) * 0.15D * (double) Mth.sin(this.yBodyRot * ((float) Math.PI / 180F)), this.getY() + (double) 1F, this.getZ() + (double) (this.getBbWidth() + 1.0F) * 0.15D * (double) Mth.cos(this.yBodyRot * ((float) Math.PI / 180F)));
        double d0 = position.x;
        double d1 = position.y;
        double d2 = position.z;
        float f = Mth.sqrt((float) (d0 * d0 + d2 * d2)) * 0.35F;
        projectile.setDelay(Maths.sec(2));
        projectile.shoot(d0, d1 + f, d2, 0.25F, 0.0F);
        projectile.setDelay(timer);
        this.level().addFreshEntity(projectile);
    }
    private void shoot(int count, LivingEntity target, float velocity, float inaccuracy) {
        this.shoot(count, target, velocity, inaccuracy, true);
    }
    private void shoot(int count, LivingEntity target, float velocity, float inaccuracy, boolean spread) {
        double offsetangle = Math.toRadians(12);
        for (int i = 0; i < count; ++i) {
            double angle = spread ? (i - (count - 1) / 2.0F) * offsetangle : 0;
            double d0 = this.getX();
            double d1 = this.getY() + (this.getBbHeight() / 2) + 0.5D;
            double d2 = this.getZ();
            BlazingRod projectile = new BlazingRod(this.level(), d0, d1, d2, this);
            projectile.setDamage(DamageTags.DEFAULT, 0.02F);
            projectile.setBaseDamage(3);
            double shootX = target.getX() - this.getX();
            double shootY = target.getBoundingBox().minY + target.getBbHeight() / 2 - projectile.getY();
            double shootZ = target.getZ() - this.getZ();
            double x = shootX * Math.cos(angle) + shootZ * Math.sin(angle);
            double z = -shootX * Math.sin(angle) + shootZ * Math.cos(angle);
            projectile.shoot(x, shootY, z, velocity, inaccuracy);
            this.level().addFreshEntity(projectile);
        }
    }
    private void shootHellfireRod(int count, LivingEntity target, float velocity, float inaccuracy, boolean spread) {
        double offsetangle = Math.toRadians(12);
        for (int i = 0; i < count; ++i) {
            double angle = spread ? (i - (count - 1) / 2.0F) * offsetangle : 0;
            double d0 = this.getX();
            double d1 = this.getY() + (this.getBbHeight() / 2) + 0.5D;
            double d2 = this.getZ();
            HellfireRod projectile = new HellfireRod(this.level(), d0, d1, d2, this);
            projectile.setDamage(DamageTags.DEFAULT, 0.02F);
            projectile.setBaseDamage(3);
            double shootX = target.getX() - this.getX();
            double shootY = target.getBoundingBox().minY + target.getBbHeight() / 2 - projectile.getY();
            double shootZ = target.getZ() - this.getZ();
            double x = shootX * Math.cos(angle) + shootZ * Math.sin(angle);
            double z = -shootX * Math.sin(angle) + shootZ * Math.cos(angle);
            projectile.shoot(x, shootY, z, velocity, inaccuracy);
            this.level().addFreshEntity(projectile);
        }
    }
    public float getEnragedProgress(float partialTicks) {
        if (this.isEnraged()) {
            return 1.0F;
        }
        return Mth.clamp((float) this.getEnragedProgress() / ENRAGED_COOLDOWN, 0.0F, 1.0F);
    }
    public float getAwakenProgress(float partialTicks) {
        if (!this.isSleep()) {
            return 1.0F;
        }
        return Mth.clamp((float) this.getAwakenProgress() / AWAKEN_COOLDOWN, 0.0F, 1.0F);
    }

    public float getShockwaveProgress(float partialTicks) {
        if (!this.getAnimationState(15) || this.getAnimationTick() < Maths.sec(2)) return 1;
        return Mth.lerp(partialTicks, this.getAnimationTick(), ((float) this.getAnimationTick() / Maths.sec(1) - 2));
    }

    public void doAvoidTarget(LivingEntity target) {
        this.getNavigation().stop();
        float dodgeYaw = (float) Math.toRadians(this.targetAngle + 90 + this.getRandom().nextFloat() * 150 - 75);
        if ((this.onGround() || this.isInLava() || this.isInWater())) {
            float speed = 1.7f;
            Vec3 m = this.getDeltaMovement().add(speed * Math.cos(dodgeYaw), 0, speed * Math.sin(dodgeYaw));
            this.setDeltaMovement(m.x, 0.6, m.z);
        }
        this.getLookControl().setLookAt(target, 30, 30);
    }

    public void doJumpTarget(LivingEntity target, double distance) {
        this.getNavigation().stop();
        double posX = target == null ? 0 : (target.getX() - this.getX()) * distance;
        double posY = 1.3D;
        double posZ = target == null ? 0 : (target.getZ() - this.getZ()) * distance;
        this.setDeltaMovement(posX, posY, posZ);
    }

    public static class BlazingInfernoAwakenGoal extends MobStateGoal<BlazingInferno> {

        public BlazingInfernoAwakenGoal(BlazingInferno entity, int animation, int start, int end, int seeTick, int maxDuration) {
            super(entity, animation, start, end, seeTick, maxDuration);
        }

        @Override
        public boolean canUse() {
            return this.entity.isPowered() && this.entity.getAnimation() == this.animation;
        }

        @Override
        public void start() {
            if (this.animation != this.start) {
                this.entity.setAnimation(this.start);
            }
        }

        @Override
        public void stop() {
            this.entity.setAnimation(this.end);
        }

        @Override
        public void tick() {
            super.tick();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return false;
        }
    }

    public static class FireballAttackGoal extends MobAttackGoal<BlazingInferno> {

        public FireballAttackGoal(BlazingInferno entity, int getAnimation, int start, int end, int seeTick, int maxDuration, double random) {
            super(entity, getAnimation, start, end, seeTick, maxDuration, random);
        }

        @Override
        public boolean canUse() {
            return super.canUse() && this.entity.fireballCooldown <= 0;
        }

        @Override
        public void stop() {
            super.stop();
            this.entity.fireballCooldown = FIREBALL_COOLDOWN;
        }
    }
    public static class SpearAttackGoal extends MobAttackGoal<BlazingInferno> {

        public SpearAttackGoal(BlazingInferno entity, int getAnimation, int start, int end, int seeTick, int maxDuration, double random) {
            super(entity, getAnimation, start, end, seeTick, maxDuration, random);
        }

        @Override
        public boolean canUse() {
            return super.canUse() && this.entity.spearCooldown <= 0 && this.entity.getRandom().nextDouble() <= 0.75D;
        }

        @Override
        public void stop() {
            super.stop();
            this.entity.spearCooldown = SPEAR_COOLDOWN;
        }
    }
    public static class GroundSlamAttackGoal extends MobAttackGoal<BlazingInferno> {

        public GroundSlamAttackGoal(BlazingInferno entity, int animation, int start, int  end, int seeTick, double random) {
            super(entity, animation, start, end, seeTick, random);
        }

        @Override
        public boolean canUse() {
            return super.canUse() && this.entity.groundSlamCooldown <= 0 && this.entity.getBossPhase() <= 1;
        }

        @Override
        public void stop() {
            super.stop();
            this.entity.groundSlamCooldown = GROUND_SLAM_COOLDOWN;
        }
    }

    public static class EruptionAttackGoal extends MobAttackGoal<BlazingInferno> {

        public EruptionAttackGoal(BlazingInferno entity, int animation, int start, int  end, int seeTick, int maxDuration, double random) {
            super(entity, animation, start, end, seeTick, maxDuration, random);
        }

        @Override
        public boolean canUse() {
            return super.canUse() && this.entity.eruptionCooldown <= 0;
        }

        @Override
        public void stop() {
            super.stop();
            this.entity.eruptionCooldown = ERUPTION_COOLDOWN;
        }
    }
    public static class ShockwaveAttackGoal extends MobAttackGoal<BlazingInferno> {

        public ShockwaveAttackGoal(BlazingInferno entity, int animation, int start, int  end, int seeTick, int maxDuration, double random) {
            super(entity, animation, start, end, seeTick, maxDuration, random);
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.entity.getTarget();
            return super.canUse() && this.entity.shockwaveCooldown <= 0 && target != null && target.isAlive() && this.entity.distanceTo(target) < 8;
        }

        @Override
        public void stop() {
            super.stop();
            this.entity.shockwaveCooldown = SHOCKWAVE_COOLDOWN;
        }
    }
    public static class DashAttackGoal extends MobAttackGoal<BlazingInferno> {

        public DashAttackGoal(BlazingInferno entity, int animation, int start, int  end, int seeTick, int maxDuration, double random) {
            super(entity, animation, start, end, seeTick, maxDuration, random);
        }

        @Override
        public boolean canUse() {
            return super.canUse() && this.entity.dashCooldown <= 0 && this.entity.getBossPhase() >= 2;
        }

        @Override
        public void stop() {
            super.stop();
            this.entity.dashCooldown = DASH_COOLDOWN;
            this.entity.setIsDashing(false);
        }
    }
    public static class HellOrbAttackGoal extends MobAttackGoal<BlazingInferno> {

        public HellOrbAttackGoal(BlazingInferno entity, int animation, int start, int  end, int seeTick, int maxDuration, double random) {
            super(entity, animation, start, end, seeTick, maxDuration, random);
        }

        @Override
        public boolean canUse() {
            return super.canUse() && this.entity.hellfireOrbCooldown <= 0 && this.entity.isEnraged();
        }

        @Override
        public void stop() {
            super.stop();
            this.entity.hellfireOrbCooldown = HELLFIRE_ORB_COOLDOWN;
        }
    }
    public static class FlameStrikeAttackGoal extends MobAttackGoal<BlazingInferno> {

        public FlameStrikeAttackGoal(BlazingInferno entity, int animation, int start, int  end, int seeTick, int maxDuration, double random) {
            super(entity, animation, start, end, seeTick, maxDuration, random);
        }

        @Override
        public boolean canUse() {
            return super.canUse() && this.entity.flameStrikeCooldown <= 0 && this.entity.isEnraged();
        }

        @Override
        public void stop() {
            super.stop();
            this.entity.flameStrikeCooldown = FLAME_STRIKE_COOLDOWN;
        }
    }
    public static class HellfireDownAttackGoal extends MobAttackGoal<BlazingInferno> {

        public HellfireDownAttackGoal(BlazingInferno entity, int animation, int start, int  end, int seeTick, int maxDuration, double random) {
            super(entity, animation, start, end, seeTick, maxDuration, random);
        }

        @Override
        public boolean canUse() {
            return super.canUse() && this.entity.hellfireDownCooldown <= 0 && this.entity.isEnraged() && this.entity.getBossPhase() >= 3;
        }

        @Override
        public void stop() {
            super.stop();
            this.entity.hellfireDownCooldown = HELLFIRE_DOWN_COOLDOWN;
        }
    }

    public static class DeathRayAttackGoal extends MobAttackGoal<BlazingInferno> {

        public DeathRayAttackGoal(BlazingInferno entity, int animation, int start, int  end, int maxDuration, double random) {
            super(entity, animation, start, end, 0, maxDuration, random);
        }

        @Override
        public boolean canUse() {
            return super.canUse() && this.entity.deathRayCooldown <= 0 && this.entity.isEnraged();
        }

        @Override
        public void stop() {
            super.stop();
            this.entity.deathRayCooldown = DEATH_RAY_COOLDOWN;
        }
    }
}
