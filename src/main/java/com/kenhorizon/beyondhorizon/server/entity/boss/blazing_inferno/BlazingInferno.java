package com.kenhorizon.beyondhorizon.server.entity.boss.blazing_inferno;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.render.misc.tooltips.Tooltips;
import com.kenhorizon.beyondhorizon.client.render.util.ColorUtil;
import com.kenhorizon.beyondhorizon.client.particle.RingParticles;
import com.kenhorizon.beyondhorizon.client.particle.TrailParticles;
import com.kenhorizon.beyondhorizon.client.particle.world.ParticleTrailOptions;
import com.kenhorizon.beyondhorizon.client.particle.world.RingParticleOptions;
import com.kenhorizon.beyondhorizon.client.particle.world.RoarParticleOptions;
import com.kenhorizon.beyondhorizon.client.sound.BossMusic;
import com.kenhorizon.beyondhorizon.client.sound.BossMusicPlayer;
import com.kenhorizon.beyondhorizon.client.sound.DeathRayChargingSound;
import com.kenhorizon.beyondhorizon.server.entity.BHLibEntity;
import com.kenhorizon.beyondhorizon.server.entity.ability.AbstractDeathRayAbility;
import com.kenhorizon.beyondhorizon.server.entity.ability.BlazingInfernoRayAbility;
import com.kenhorizon.beyondhorizon.server.entity.ability.EruptionAbility;
import com.kenhorizon.beyondhorizon.server.entity.boss.BHBossEntity;
import com.kenhorizon.beyondhorizon.server.entity.CameraShake;
import com.kenhorizon.beyondhorizon.server.entity.ai.*;
import com.kenhorizon.beyondhorizon.server.entity.misc.BHFallingBlocks;
import com.kenhorizon.beyondhorizon.server.entity.projectiles.BlazingRod;
import com.kenhorizon.beyondhorizon.server.entity.projectiles.BlazingSpear;
import com.kenhorizon.beyondhorizon.server.util.DefaultDamageCaps;
import com.kenhorizon.beyondhorizon.server.entity.util.EntityUtils;
import com.kenhorizon.beyondhorizon.server.init.*;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageTags;
import com.kenhorizon.beyondhorizon.server.util.MathUtils;
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
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class BlazingInferno extends BHBossEntity {
    private static final Predicate<Entity> SHOCKWAVE_EXCEPTION = (entity) -> {
        return entity.isAlive() && !(entity instanceof BlazingInferno);
    };
    public AnimationState animationIdle = new AnimationState();
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
    public AnimationState animationIdleState = new AnimationState();
    public static int animationId = 1;
    public static final int DASH_COUNT_NORMAL = 1;
    public static final int DASH_COUNT_ENRAGED = 3;
    public static final int GS_COUNT_NORMAL = 1;
    public static final int GS_COUNT_ENRAGED = 3;
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
    public static final int ID_IDLE_STATE = createAnimationID();
    public static final int BLAZING_ROD_IA_NORMAL = 30;
    public static final int BLAZING_ROD_IA_ENRAGED = 10;
    public List<InfernoShield> infernoShields = new ArrayList<>();
    public int fireballCooldown = 0;
    public static final int FIREBALL_COOLDOWN = MathUtils.sec(6);
    public int spearCooldown = 0;
    public static final int SPEAR_COOLDOWN = MathUtils.sec(6);
    public int idleCooldown = 0;
    public static final int IDLE_COOLDOWN = MathUtils.sec(3);
    public int deathRayCooldown = 0;
    public static final int DEATH_RAY_COOLDOWN = MathUtils.mins(1);
    public int groundSlamCooldown = 0;
    public static final int GROUND_SLAM_COOLDOWN = MathUtils.sec(8);
    public int dashCooldown = 0;
    public static final int DASH_COOLDOWN = MathUtils.sec(24);
    public int shockwaveCooldown = 0;
    public static final int SHOCKWAVE_COOLDOWN = MathUtils.sec(7);
    public int shieldCooldown = 0;
    public static final int SHIELD_COOLDOWN = MathUtils.mins(5);
    public int eruptionCooldown = 0;
    public static final int ERUPTION_COOLDOWN = MathUtils.sec(10);
    public static final int ENRAGED_COOLDOWN = MathUtils.sec(5);
    public static final int AWAKEN_COOLDOWN = MathUtils.sec(5);
    private boolean doGroundSmashFX = false;
    private boolean overheated = false;
    private int dashCount = 1;
    private int groundSlamCount = 1;
    private int dashProgress = 1;
    private int groundSlamProgress = 1;
    private boolean hit = false;
    public static final String NBT_POWERED = "IsPowered";
    public static final String NBT_ENRAGED = "IsEnraged";
    public static final String NBT_SHIELD_COUNT = "ShieldCount";
    public static final String NBT_SHIELD_ACTIVE = "ShieldActive";
    public static final String NBT_AWAKEN_PROGRESS = "AwakenProgress";
    public static final String NBT_ENRAGED_PROGRESS = "EnragedProgress";
    public static final String NBT_SHIELD_COOLDOWN = "ShieldCooldown";
    public static final String NBT_GROUND_SLAM_COUNT = "GroundSlamCount";
    public static final String NBT_DASH_COUNT = "DashCount";
    public static final String NBT_DASH_PROGRESS = "DashProgress";
    public static final String NBT_GROUND_SLAM_PROGRESS = "GroundSlamProgress";
    public static final String NBT_SWELL_DIR = "SwellProgress";
    private int oldSwell;
    private int swell;
    private final double powerShockwaveX = 4.0D;
    private final double powerShockwaveY = 1.0D;
    private final double powerShockwaveZ = 4.0D;
    public static final EntityDataAccessor<Integer> SWELL = SynchedEntityData.defineId(BlazingInferno.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Boolean> OVERHEAT = SynchedEntityData.defineId(BlazingInferno.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> POWERED = SynchedEntityData.defineId(BlazingInferno.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> IS_DASHING = SynchedEntityData.defineId(BlazingInferno.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> ENRAGED = SynchedEntityData.defineId(BlazingInferno.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Integer> DATA_SHIELD_COOLDOWN = SynchedEntityData.defineId(BlazingInferno.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> SHIELD_COUNT = SynchedEntityData.defineId(BlazingInferno.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> AWAKEN_PROGRESS = SynchedEntityData.defineId(BlazingInferno.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> ENRAGED_PROGRESS = SynchedEntityData.defineId(BlazingInferno.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> DASH_PROGRESS = SynchedEntityData.defineId(BlazingInferno.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> DASH_COUNT = SynchedEntityData.defineId(BlazingInferno.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> GROUND_SLAM_PROGRESS = SynchedEntityData.defineId(BlazingInferno.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> GROUND_SLAM_COUNT = SynchedEntityData.defineId(BlazingInferno.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> SHIELD_ACTIVE = SynchedEntityData.defineId(BlazingInferno.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DEATH_RAY = SynchedEntityData.defineId(BlazingInferno.class, EntityDataSerializers.BOOLEAN);
    private int spearOfChoices;

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
                .add(Attributes.MAX_HEALTH, 400.0D)
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
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0D, 80) {
            @Override
            public boolean canUse() {
                return super.canUse() && !(((BlazingInferno) this.mob).getAnimationState(ID_IDLE_STATE)
                        || ((BlazingInferno) this.mob).getAnimationState(ID_DEATH_RAY));
            }
        });
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(0, new NaturalHealingGoal(this));
        this.goalSelector.addGoal(0, new BossInactiveGoal(!this.isPowered() || this.getAnimation() == 3));
        this.goalSelector.addGoal(1, new BlazingInfernoMoveGoal(this, false, 1.0F));
        this.goalSelector.addGoal(1, new MobStateGoal<>(this, ID_INACTIVE,ID_INACTIVE, ID_ANIMATION_EMPTY, 0, 0) {
            @Override
            public void tick() {
                this.entity.setCantMoved();
            }
        });
        this.goalSelector.addGoal(0, new BlazingInfernoAwakenGoal(this, ID_INACTIVE, ID_ACTIVE, ID_IDLE_STATE, 0, MathUtils.sec(5)));
        this.targetSelector.addGoal(1, new HurtByNearestTargetGoal(this));
        this.goalSelector.addGoal(1, new PrepareDeathRayAttackGoal(this, ID_ANIMATION_EMPTY, ID_PREPARE_DEATH_RAY, ID_DEATH_RAY, MathUtils.sec(3), MathUtils.sec(5)));
        this.goalSelector.addGoal(1, new DeathRayAttackGoal(this, ID_DEATH_RAY, ID_DEATH_RAY, ID_IDLE_STATE, MathUtils.sec(5), MathUtils.sec(5)));
//        this.goalSelector.addGoal(1, new EnragedDashAttackGoal(this, ID_ANIMATION_EMPTY, ID_DASHES, ID_SHOCKWAVE, 40));
//        this.goalSelector.addGoal(1, new DashAttackGoal(this, ID_ANIMATION_EMPTY, ID_DASHES, ID_IDLE_STATE, 40, MathUtils.sec(3)));
//        this.goalSelector.addGoal(1, new ShockwaveAttackGoal(this, ID_ANIMATION_EMPTY, ID_SHOCKWAVE, ID_IDLE_STATE, 40, MathUtils.sec(5)));
//        this.goalSelector.addGoal(1, new EruptionAttackGoal(this, ID_ANIMATION_EMPTY, ID_ERUPTION, ID_IDLE_STATE, 20, MathUtils.sec(2)));
//        this.goalSelector.addGoal(1, new RangedAttackGoal(this, ID_ANIMATION_EMPTY, ID_BLAZING_ROD, ID_IDLE_STATE, 30, MathUtils.sec(5)));
//        this.goalSelector.addGoal(1, new SpearAttackGoal(this, ID_ANIMATION_EMPTY, ID_SPEAR, ID_IDLE_STATE, 30, MathUtils.sec(3)));
        this.goalSelector.addGoal(1, new GroundSlamAttackGoal(this, ID_ANIMATION_EMPTY, ID_GROUND_SLAM, ID_IDLE_STATE, MathUtils.sec(3)));

        this.goalSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.goalSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractGolem.class, true));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SWELL, -1);
        this.entityData.define(DASH_PROGRESS, 0);
        this.entityData.define(GROUND_SLAM_PROGRESS, 0);
        this.entityData.define(DASH_COUNT, 1);
        this.entityData.define(GROUND_SLAM_COUNT, 1);
        this.entityData.define(DATA_SHIELD_COOLDOWN, 0);
        this.entityData.define(ENRAGED_PROGRESS, 0);
        this.entityData.define(AWAKEN_PROGRESS, 0);
        this.entityData.define(SHIELD_COUNT, 4);
        this.entityData.define(ENRAGED, false);
        this.entityData.define(SHIELD_ACTIVE, false);
        this.entityData.define(IS_DASHING, false);
        this.entityData.define(POWERED, true);
        this.entityData.define(DEATH_RAY, false);
        this.entityData.define(OVERHEAT, false);
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
        this.setDashCount(nbt.getInt(NBT_DASH_COUNT));
        this.setGroundSlamCount(nbt.getInt(NBT_GROUND_SLAM_COUNT));
        this.setDashProgress(nbt.getInt(NBT_DASH_PROGRESS));
        this.setGroundSlamProgress(nbt.getInt(NBT_GROUND_SLAM_PROGRESS));
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
        nbt.putInt(NBT_DASH_COUNT, this.getDashCount());
        nbt.putInt(NBT_DASH_PROGRESS, this.getDashProgress());
        nbt.putInt(NBT_GROUND_SLAM_COUNT, this.getGroundSlamProgress());
    }

    public void setSwell(int count) {
        this.entityData.set(SWELL, count);
    }

    public int getSwell() {
        return this.entityData.get(SWELL);
    }

    @Override
    public boolean isAlliedTo(Entity entity) {
        if (entity == null) {
            return false;
        } else if (entity == this) {
            return true;
        } else if (super.isAlliedTo(entity)) {
            return true;
        } else if (entity instanceof InfernoShield) {
            return this.isAlliedTo(((InfernoShield) entity).getUsingEntity());
        } else {
            return false;
        }
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
        return MathUtils.sec(10);
    }

    @Override
    public int getAnimationDeath() {
        return ID_DEATH;
    }

    public boolean isBossImmune() {
        return this.getAnimationState(ID_ENRAGED_PHASE) || this.isSleep();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean flag = source.is(DamageTypes.GENERIC) || source.is(DamageTypes.GENERIC_KILL);
        boolean immune = this.isBossImmune();

        if (immune) {
            return false;
        } else {
            if (!this.infernoShields.isEmpty()) {
                amount *= 0.80F;
            }
            if (source.getEntity() instanceof AbstractArrow) {
                amount *= 0.75F;
            }
            if (source.getEntity() instanceof AbstractGolem) {
                amount *= 0.25F;
            }
            if (this.isHalfHealth() && !this.isEnraged() && !flag) {
                this.setAnimation(ID_ENRAGED_PHASE);
                this.setHealth(this.getMaxHealth() / 2);
            }
            if (!flag) {
                boolean gotHurt = super.hurt(source, amount);
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
                return gotHurt;
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
        if (!this.isEnraged() && !this.isInfernoShieldActive() && this.getShieldCooldown() > 0) this.setShieldCooldown(this.getShieldCooldown() - 1);
        if (!this.getAnimationState(ID_IDLE_STATE)) {
            if (this.eruptionCooldown > 0) this.eruptionCooldown--;
            if (this.spearCooldown > 0) this.spearCooldown--;
            if (this.dashCooldown > 0) this.dashCooldown--;
            if (this.deathRayCooldown > 0) this.deathRayCooldown--;
            if (this.shockwaveCooldown > 0) this.shockwaveCooldown--;
            if (this.groundSlamCooldown > 0) this.groundSlamCooldown--;
            if (this.fireballCooldown > 0) this.fireballCooldown--;
            if (this.idleCooldown > 0) this.idleCooldown--;
        }
        if (this.getAnimationState(ID_ACTIVE)) {
            this.setAwakenProgress(this.getAwakenProgress() + 1);
        }
        this.infernoShields = this.getEntitiesNearby(InfernoShield.class, 16.0F);
        if (this.infernoShields.isEmpty() && this.shieldCooldown <= 0) {
            this.summonShield();
        }
        if (!this.isEnraged()) {
            if (this.shieldCooldown > 0 && this.infernoShields.isEmpty()) this.shieldCooldown--;
        }
        this.setInfernoShieldActive(!this.infernoShields.isEmpty());
        if (this.getAnimationState(ID_ENRAGED_PHASE)) {
            this.setCantMoved();
            this.setEnragedProgress(this.getEnragedProgress() + 1);
            if (this.getEnragedProgress() % 20L == 0) {
                if (this.level().isClientSide()) {
                    int particleCount = this.getEnragedProgress();
                    while (particleCount --> 0) {
                        double radius = 8.0F;
                        float r = ColorUtil.getFARGB(0xFFFFFF)[0];
                        float g = ColorUtil.getFARGB(0xFFFFFF)[1];
                        float b = ColorUtil.getFARGB(0xFFFFFF)[2];

                        float yaw = (float) (this.random.nextFloat() * 2 * Math.PI);
                        float pitch = (float) (this.random.nextFloat() * 2 * Math.PI);
                        double ox = (float) (radius * Math.sin(yaw) * Math.sin(pitch));
                        double oy = (float) (radius * Math.cos(pitch));
                        double oz = (float) (radius * Math.cos(yaw) * Math.sin(pitch));
                        ParticleTrailOptions.add(this.level(), TrailParticles.Behavior.SHRINK, getX() + ox, getY() + oy + 0.1, getZ() + oz, 3.0F, 1, r, g, b, 20, new Vec3(this.getX(), this.getY() + this.getBbHeight() / 2 + 0.5F, this.getZ()));
                    }
                    CameraShake.spawn(this, 32.0F, 0.55F, 10, 5);
                }
            }
            if (this.getEnragedProgress() >= MathUtils.sec(5)) {
                this.playSound(BHSounds.BLAZING_INFERNO_SCREAM.get());
                this.doRoarParticle(this.getX(), this.getEyeY(), this.getZ(), 10, 255, 0, 0, 1.0F, 1.0F, 5.0F, 0.1F);
                this.setIsEnraged(true);
                this.bossInfo().setRenderType(1);
                this.setHealth(this.getMaxHealth() / 2);
                this.setAnimation(ID_ANIMATION_EMPTY);
                this.setDashCount(DASH_COUNT_ENRAGED);
                this.setGroundSlamCount(GS_COUNT_ENRAGED);
            }
        }
        if (this.isEnraged()) {
            this.bossInfo().setRenderType(1);
        } else {
            this.bossInfo().setRenderType(0);
        }
    }

    public void setDashCount(int count) {
        this.entityData.set(DASH_COUNT, count);
        this.dashCount = count;
    }

    public int getDashCount() {
        return this.level().isClientSide() ? this.entityData.get(DASH_COUNT) : this.dashCount;
    }

    public void setGroundSlamCount(int count) {
        this.entityData.set(GROUND_SLAM_COUNT, count);
        this.groundSlamCount = count;
    }

    public int getGroundSlamCount() {
        return this.level().isClientSide() ? this.entityData.get(GROUND_SLAM_COUNT) : this.groundSlamCount;
    }

    public void setDashProgress(int count) {
        this.entityData.set(DASH_PROGRESS, count);
        this.dashProgress = count;
    }

    public int getDashProgress() {
        return this.level().isClientSide() ? this.entityData.get(DASH_PROGRESS) : this.dashProgress;
    }

    public void setGroundSlamProgress(int count) {
        this.entityData.set(GROUND_SLAM_PROGRESS, count);
        this.groundSlamProgress = count;
    }

    public int getGroundSlamProgress() {
        return this.level().isClientSide() ? this.entityData.get(GROUND_SLAM_PROGRESS) : this.groundSlamProgress;
    }

    @Override
    protected void playHurtSound(DamageSource source) {
        this.playSound(BHSounds.BLAZING_INFERNO_HURT.get());
    }

    public void setOverheated(boolean v) {
        this.entityData.set(OVERHEAT, v);
        this.overheated = v;
    }

    public boolean isOverheated() {
        return this.level().isClientSide() ? this.entityData.get(OVERHEAT) : this.overheated;
    }

    @Override
    public BossMusic getBossMusic() {
        return BossMusicPlayer.BLAZING_INFERNO_MUSIC;
    }

    @Override
    protected boolean canPlayMusic() {
        return super.canPlayMusic() && !this.isSleep();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.isSleep()) {
            if (this.level().isClientSide()) {
                int particleCount = 2;
                while (particleCount --> 0) {
                    double radius = 3.0F;
                    float yaw = (float) (this.random.nextFloat() * 2 * Math.PI);
                    float pitch = (float) (this.random.nextFloat() * 2 * Math.PI);
                    double ox = (float) (radius * Math.sin(yaw) * Math.sin(pitch));
                    double oy = (float) (radius * Math.cos(pitch));
                    double oz = (float) (radius * Math.cos(yaw) * Math.sin(pitch));
                    ParticleTrailOptions.add(this.level(), TrailParticles.Behavior.SHRINK, getX() + ox, getY() + oy + 0.1, getZ() + oz, 1.50F, 1,this.isEnraged() ? 0 : 1.0F, this.isEnraged() ? 1.0F : 0.0F, this.isEnraged() ? 0 : 1.0F, 10, new Vec3(this.getX(), this.getY() + this.getBbHeight() / 2 + 0.5F, this.getZ()));
                }
            }
        }
        if (this.getAnimationState(ID_ENRAGED_PHASE)) {
            this.setCantMoved();
        }
        LivingEntity target = this.getTarget();
        if (this.getAnimationState(ID_IDLE_STATE)) {
            if (this.isOverheated()) {
                if (this.getAnimationTick() == 2) {
                    this.playSound(BHSounds.BLAZING_INFERNO_GROWL.get());
                }
                if (this.level().isClientSide()) {
                    int flameCount = 2;
                    for (int i = 0; i < flameCount; ++i) {
                        this.level().addParticle(ParticleTypes.LARGE_SMOKE, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), 0.0D, 0.0D, 0.0D);
                        this.level().addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), 0.0D, 0.0D, 0.0D);
                    }
                }
                this.setCantMoved();
                this.setXRot(30.0F);
                this.xRotO = 30.0F;
                if (this.getAnimationTick() >= MathUtils.sec(6)) {
                    this.setAnimation(ID_ANIMATION_EMPTY);
                    this.overheated = false;
                }
            } else {
                if (this.getAnimationTick() >= MathUtils.sec(3)) {
                    this.setAnimation(ID_ANIMATION_EMPTY);
                }
                if (target != null) {
                    this.getLookControl().setLookAt(target);
                }
            }
        }

        if (this.getAnimationState(ID_ANIMATION_EMPTY)) {
            this.doDodge(25);
        }
        if (this.getAnimationState(ID_INACTIVE) || this.isInfernoShieldActive()) {
            this.shieldCooldown = SHIELD_COOLDOWN;
        }
        if (this.getAnimationState(ID_ACTIVE)) {
            if (this.getAnimationTick() == 90) {
                this.summonShield();
                CameraShake.spawn(this.level(), this.position(), 24, 0.12F, 20, 10);
                if (this.level().isClientSide()) {
                    float yaw = (float) Math.toRadians(-this.getYRot());
                    float pitch = (float) Math.toRadians(-this.getXRot());
                    int r = ColorUtil.getARGB(0xFFFFFF)[0];
                    int g = ColorUtil.getARGB(0xFFFFFF)[1];
                    int b = ColorUtil.getARGB(0xFFFFFF)[2];
                    this.doRoarParticle(this.getX(), this.getY(0.5D), this.getZ(), 10, 255, 255, 255, 1.0F, 1.0F, 0.1F, 10.0F);
                    this.level().addAlwaysVisibleParticle(new RingParticleOptions(0, (float) Math.PI / 2, 11, r, g, b, 1.0F, 16.0F, false, RingParticles.Behavior.GROW), this.getX(), this.getY(), this.getZ(), 0, 0, 0);
                    this.level().addAlwaysVisibleParticle(new RingParticleOptions(yaw, pitch, 15, r, g, b, 1.0F, 32.0F, false, RingParticles.Behavior.SHRINK), this.getX(), this.getY() + this.getBbHeight() / 2 + 0.5F, this.getZ(), 0, 0, 0);
                    if (!this.isSilent()) {
                        this.level().playSound((Player) null, this, BHSounds.BLAZING_INFERNO_SCREAM.get(), SoundSource.HOSTILE, 3.0F, 1.0F);
                    }
                }
            }
        }
        // TODO: Ranged Attack
        if (this.getAnimationState(ID_BLAZING_ROD)) {
            this.setCantMoved();
            int fireRate = this.isEnraged() ? BLAZING_ROD_IA_ENRAGED : BLAZING_ROD_IA_NORMAL;
            if (target != null) {
                float velocity = 1.20F;
                this.setOverheated(true);
                if (this.isEnraged()) {
                    if (this.inBetweenHealth(0.25F, 0.20F)) {
                        this.performRangedAttack(4, target, fireRate, velocity, 1.25F);
                    } else if (this.inBetweenHealth(0.20F, 0.10F)) {
                        this.performRangedAttack(5, target, fireRate, velocity, 1.05F);
                    } else if (this.inBetweenHealth(0.10F, 0.05F)) {
                        this.performRangedAttack(6, target, fireRate, velocity, 0.75F);
                    } else if (this.inBetweenHealth(0.5F, 0.0F)) {
                        this.performRangedAttack(10, target, fireRate, velocity, 0.50F);
                    } else {
                        this.performRangedAttack(3, target, fireRate, 1.55F);
                    }
                }
                if (!this.isEnraged()) {
                    this.performRangedAttack(3, target, fireRate, velocity, 3.50F);
                }
            }
            if (this.level().isClientSide() && this.getAnimationTick() % fireRate == 0) {
                float r = ColorUtil.getFARGB(0xFF0000)[0];
                float g = ColorUtil.getFARGB(0xFF0000)[1];
                float b = ColorUtil.getFARGB(0xFF0000)[2];
                double x = this.getX();
                double y = this.getY() + this.getBbHeight() / 2;
                double z = this.getZ();
                float yaw = (float) Math.toRadians(-this.getYRot());
                float yaw2 = (float) Math.toRadians(-this.getYRot() + 180);
                float pitch = (float) Math.toRadians(-this.getXRot());
                this.level().addAlwaysVisibleParticle(new RingParticleOptions(yaw, pitch, 40, r, g, b, 1.0F, 32F, false, RingParticles.Behavior.GROW), x, y, z, 0, 0, 0);
                this.level().addAlwaysVisibleParticle(new RingParticleOptions(yaw2, pitch, 40, r, g, b, 1.0F, 32F, false, RingParticles.Behavior.GROW), x, y, z, 0, 0, 0);
            }
        }

        if (this.getAnimationState(ID_SPEAR)) {
            if (this.inRangeOf(8.0D) && this.getAnimationTick() == 1 && target != null) {
                this.spearOfChoices = this.getRandom().nextInt(3);
                this.doAvoidTarget(target);
            }
            if (this.getAnimationTick() == 40) {
                CameraShake.spawn(this.level(), this.position(), 16.0F, 0.05F, 5, 20);
                this.doRoarParticle(this.getX(), this.getEyeY(), this.getZ(), 10, 255, 0, 0, 1.0F, 1.0F, 5.0F, 0.1F);

                this.playSound(BHSounds.BLAZING_INFERNO_SPEAR.get());
            }
            switch (this.spearOfChoices) {
                case 0 -> {
                    if (this.getAnimationTick() == 40) {
                        if (!this.isSilent()) {
                            this.level().playSound((Player) null, this, BHSounds.BLAZING_INFERNO_SHOOT.get(), SoundSource.HOSTILE, 3.0F, 1.0F);
                        }
                        this.shootSpear(target, new Vec3(2, 1, 0), MathUtils.sec(2));
                        this.shootSpear(target, new Vec3(-2, 1, 0), MathUtils.sec(2));
                        this.shootSpear(target, new Vec3(0, 1, 0), MathUtils.sec(2));
                    }
                }
                case 1 -> {
                    if (this.getAnimationTick() == 40) {
                        if (!this.isSilent()) {
                            this.level().playSound((Player) null, this, BHSounds.BLAZING_INFERNO_SHOOT.get(), SoundSource.HOSTILE, 3.0F, 1.0F);
                        }
                        this.shootSpear(target, new Vec3(2, 1, 0));
                        this.shootSpear(target, new Vec3(-2, 1, 0));
                        this.shootSpear(target, new Vec3(0, 1, 0));
                    }
                }
                case 2 -> {
                    if (this.getAnimationTick() % 20 == 0) {
                        if (!this.isSilent()) {
                            this.level().playSound((Player) null, this, BHSounds.BLAZING_INFERNO_SHOOT.get(), SoundSource.HOSTILE, 3.0F, 1.0F);
                        }
                        CameraShake.spawn(this.level(), this.position(), 32.0F, 0.15F, 5, 20);
                        this.shootSpear(target, new Vec3(0, 1, 0));
                    }
                }
            }
        }
        if (this.getAnimationState(ID_ERUPTION)) {
            if (this.getAnimationTick() == 10) {
                if (!this.isSilent()) {
                    this.level().playSound((Player) null, this, BHSounds.BLAZING_INFERNO_GROWL.get(), SoundSource.HOSTILE, 3.0F, 1.0F);
                }
                float r = ColorUtil.getFARGB(0xFFFFFF)[0];
                float g = ColorUtil.getFARGB(0xFFFFFF)[1];
                float b = ColorUtil.getFARGB(0xFFFFFF)[2];
                this.level().addAlwaysVisibleParticle(new RingParticleOptions(0, (float) Math.PI / 2, 11, r, g, b, 1.0F, 128.0F, true, RingParticles.Behavior.GROW), this.getX(), this.getY(0.05D), this.getZ(), 0, 0, 0);
                CameraShake.spawn(this.level(), this.position(), 16.0F, 0.05F, 5, 20);
                if (this.level().isClientSide()) {
                    float r1 = ColorUtil.getFARGB(0xFFFFFF)[0];
                    float g2 = ColorUtil.getFARGB(0xFFFFFF)[1];
                    float b3 = ColorUtil.getFARGB(0xFFFFFF)[2];
                    this.level().addAlwaysVisibleParticle(new RingParticleOptions(0, (float) -Math.PI / 2, 10, r1, g2, b3, 1.0F, 128.0F, false, RingParticles.Behavior.GROW), this.getX(), this.getY(0.55D), this.getZ(), 0, 0, 0);
                    this.level().addAlwaysVisibleParticle(new RingParticleOptions(0, (float) Math.PI / 2, 10, r1, g2, b3, 1.0F, 128.0F, false, RingParticles.Behavior.GROW), this.getX(), this.getY(0.55D), this.getZ(), 0, 0, 0);
                    this.level().addParticle(new RoarParticleOptions(10, 255, 255, 255, 1.0F, 1.0F, 0.1F, 64.0F), this.getX(), this.getY(0.5D), this.getZ(), 0, 0, 0);
                }

                for (LivingEntity livingentity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(8.0), SHOCKWAVE_EXCEPTION)) {
                    double d0 = livingentity.getX() - this.getX();
                    double d1 = livingentity.getZ() - this.getZ();
                    double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
                    livingentity.hurt(this.level().damageSources().mobAttack(this), this.getAttackDamage(0.15F) + livingentity.getMaxHealth() * 0.10F);
                    livingentity.push(d0 / d2 * this.powerShockwaveX, 0.2D * this.powerShockwaveY, d1 / d2 * this.powerShockwaveZ);
                }
                this.playSound(BHSounds.BLAZING_INFERNO_SHOCKWAVE.get());
                this.createEruption(64);
            }
        }
        if (this.getAnimationState(ID_PREPARE_DEATH_RAY)) {
            if (this.getAnimationTick() == 1) {
                BeyondHorizon.PROXY.playSound(new DeathRayChargingSound(this, BHSounds.BLAZING_INFERNO_DEATH_RAY_CHARGING.get()));
            }
            if (this.getAnimationTick() < MathUtils.sec(5)) {
                if (this.getAnimationTick() % 20L == 0) {
                    if (this.level().isClientSide()) {
                        int particleCount = 128;
                        while (particleCount --> 0) {
                            double radius = 6.0F;
                            float yaw = (float) (this.random.nextFloat() * 2 * Math.PI);
                            float pitch = (float) (this.random.nextFloat() * 2 * Math.PI);
                            double ox = (float) (radius * Math.sin(yaw) * Math.sin(pitch));
                            double oy = (float) (radius * Math.cos(pitch));
                            double oz = (float) (radius * Math.cos(yaw) * Math.sin(pitch));
                            ParticleTrailOptions.add(this.level(), TrailParticles.Behavior.SHRINK, getX() + ox, getY() + oy + 0.1, getZ() + oz, 3.0F, 1, 0.0F, 0.0F, 1.0F, 20, new Vec3(this.getX(), this.getY() + this.getBbHeight() / 2 + 0.5F, this.getZ()));
                        }
                        CameraShake.spawn(this, 32.0F, 0.55F, 10, 10);
                        float yaw = (float) Math.toRadians(-this.getYRot());
                        float pitch = (float) Math.toRadians(-this.getXRot());
                        float r = ColorUtil.getFARGB(0xFFFFFF)[0];
                        float g = ColorUtil.getFARGB(0xFFFFFF)[1];
                        float b = ColorUtil.getFARGB(0xFFFFFF)[2];
                        this.level().addAlwaysVisibleParticle(new RingParticleOptions(yaw, pitch, 15, r, g, b, 1.0F, 32.0F, true, RingParticles.Behavior.SHRINK), this.getX(), this.getY() + this.getBbHeight() / 2 + 0.5F, this.getZ(), 0, 0, 0);
                    }
                }
            }
        }
        this.oldSwell = this.swell;
        if (!this.getAnimationState(ID_SHOCKWAVE)) {
            this.setSwell(-1);
            this.swell = 0;
        }
        if (this.getAnimationState(ID_SHOCKWAVE)) {
            this.shockwaveAttack();
        }
        if (this.getAnimationState(ID_GROUND_SLAM)) {
            this.groundSlamAttack(target);
        }
        this.dashAttack();
        if (this.getAnimationState(ID_DASHES)) {
            int dashStart = 45;
            int dashEnd = 65;
            if (target != null) {
                if (this.getAnimationTick() < dashStart) {
                    this.lookAt(target, 30.0F, 3.0F);
                    this.getLookControl().setLookAt(target, 30.0F, 30.0F);
                } else {
                    this.setYRot(this.yRotO);
                }
            }
            if (this.getAnimationTick() == dashEnd) {
                this.setDashProgress(this.getDashProgress() + 1);
                this.setIsDashing(false);
                this.setAnimation(ID_DASHES);
            }
            if (this.getDashProgress() >= this.getDashCount()) {
                CameraShake.spawn(this.level(), this.position(), 16.0F, 0.05F, 5, 20);
                if (this.level().isClientSide()) {
                    float r = ColorUtil.getFARGB(0xFFFFFF)[0];
                    float g = ColorUtil.getFARGB(0xFFFFFF)[1];
                    float b = ColorUtil.getFARGB(0xFFFFFF)[2];
                    this.level().addAlwaysVisibleParticle(new RingParticleOptions(0, (float) -Math.PI / 2, 10, r, g, b, 1.0F, 128.0F, false, RingParticles.Behavior.GROW), this.getX(), this.getY(0.55D), this.getZ(), 0, 0, 0);
                    this.level().addAlwaysVisibleParticle(new RingParticleOptions(0, (float) Math.PI / 2, 10, r, g, b, 1.0F, 128.0F, false, RingParticles.Behavior.GROW), this.getX(), this.getY(0.55D), this.getZ(), 0, 0, 0);
                    this.level().addParticle(new RoarParticleOptions(10, 255, 255, 255, 1.0F, 1.0F, 0.1F, 64.0F), this.getX(), this.getY(0.5D), this.getZ(), 0, 0, 0);
                }

                for (LivingEntity livingentity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(8.0), SHOCKWAVE_EXCEPTION)) {
                    double d0 = livingentity.getX() - this.getX();
                    double d1 = livingentity.getZ() - this.getZ();
                    double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
                    livingentity.push(d0 / d2 * (this.powerShockwaveX / 2), 0.2D * this.powerShockwaveY, d1 / d2 * (this.powerShockwaveZ / 2));
                }
                this.playSound(BHSounds.BLAZING_INFERNO_SHOCKWAVE.get());
            }
            if (this.getAnimationTick() < dashEnd && this.getAnimationTick() > dashStart) {
                this.setIsDashing(true);
                Vec3 vec3 = this.getDeltaMovement();
                this.playSound(BHSounds.BLAZING_INFERNO_GROWL.get());
                float rot = this.getYRot() * ((float) Math.PI / 180.0F);
                Vec3 newVec = new Vec3(-Mth.sin(rot), this.getDeltaMovement().y, Mth.cos(rot)).scale(1.0D).add(vec3.scale(0.5D));
                this.setDeltaMovement(newVec.x, this.getDeltaMovement().y, newVec.z);
            }
        }
    }
    // TODO: Shockwave Attack
    private void shockwaveAttack() {
        this.setCantMoved();
        this.setSwell(1);
        int swell = this.getSwell();
        if (swell > 0 && this.swell == 0) {
            this.playSound(BHSounds.BLAZING_INFERNO_SCREAM.get());
            float r = ColorUtil.getFARGB(0xFFFFFF)[0];
            float g = ColorUtil.getFARGB(0xFFFFFF)[1];
            float b = ColorUtil.getFARGB(0xFFFFFF)[2];
            this.level().addAlwaysVisibleParticle(new RingParticleOptions(0, (float) Math.PI / 2, MathUtils.sec(8) - 1, r, g, b, 1.0F, 128.0F, false, RingParticles.Behavior.SHRINK), this.getX(), this.getY(0.05D), this.getZ(), 0, 0, 0);
        }

        this.swell += swell;
        if (this.swell < 0) {
            this.swell = 0;
        }
        if (this.swell >= MathUtils.sec(4)) {
            this.swell = MathUtils.sec(4);
        }

        if (this.getAnimationTick() % MathUtils.sec(1) == 0 && this.getAnimationTick() < MathUtils.sec(4)) {
            if (this.level().isClientSide()) {
                int particleCount = 64;
                while (particleCount --> 0) {
                    double radius = 5.0F;
                    float yaw = (float) (this.random.nextFloat() * 2 * Math.PI);
                    float pitch = (float) (this.random.nextFloat() * 2 * Math.PI);
                    double ox = (float) (radius * Math.sin(yaw) * Math.sin(pitch));
                    double oy = (float) (radius * Math.cos(pitch));
                    double oz = (float) (radius * Math.cos(yaw) * Math.sin(pitch));
                    ParticleTrailOptions.add(this.level(), TrailParticles.Behavior.SHRINK, this.getX() + ox, this.getY() + oy + 0.1, this.getZ() + oz, 4.0F, 1, 0.0F, 0.0F, 1.0F, 10, new Vec3(this.getX(), this.getY() + this.getBbHeight() / 2 + 0.5F, this.getZ()));
                }
                for (int i = 0; i < 10; ++i) {
                    this.level().addParticle(ParticleTypes.LARGE_SMOKE, this.getRandomX(0.5D), this.getBbHeight() * 0.01D, this.getRandomZ(0.5D), 0.0D, 0.0D, 0.0D);
                    this.level().addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getRandomX(0.5D), this.getBbHeight() * 0.01D, this.getRandomZ(0.5D), 0.0D, 0.0D, 0.0D);
                }
                this.level().addParticle(new RoarParticleOptions(10, 255, 255, 255, 1.0F, 1.0F, 0.1F, 25.0F), this.getX(), this.getY(0.5D), this.getZ(), 0, 0, 0);
            }
        }
        if (this.getAnimationTick() == MathUtils.sec(4)) {
            CameraShake.spawn(this.level(), this.position(), 24.0F, 0.05F, 5, 20);
            if (this.level().isClientSide()) {
                float r = ColorUtil.getFARGB(0xFFFFFF)[0];
                float g = ColorUtil.getFARGB(0xFFFFFF)[1];
                float b = ColorUtil.getFARGB(0xFFFFFF)[2];
                this.level().addAlwaysVisibleParticle(new RingParticleOptions(0, (float) -Math.PI / 2, 10, r, g, b, 1.0F, 128.0F, false, RingParticles.Behavior.GROW), this.getX(), this.getY(0.55D), this.getZ(), 0, 0, 0);
                this.level().addAlwaysVisibleParticle(new RingParticleOptions(0, (float) Math.PI / 2, 10, r, g, b, 1.0F, 128.0F, false, RingParticles.Behavior.GROW), this.getX(), this.getY(0.55D), this.getZ(), 0, 0, 0);
                this.level().addParticle(new RoarParticleOptions(40, 255, 255, 255, 1.0F, 1.0F, 0.1F, 128.0F), this.getX(), this.getY(0.5D), this.getZ(), 0, 0, 0);
            }

            for (LivingEntity livingentity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(8.0), SHOCKWAVE_EXCEPTION)) {
                double d0 = livingentity.getX() - this.getX();
                double d1 = livingentity.getZ() - this.getZ();
                double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
                if (livingentity.isAlliedTo(this)) continue;
                if (livingentity.isInvulnerable()) continue;
                livingentity.hurt(this.level().damageSources().mobAttack(this), this.getAttackDamage(0.15F) + livingentity.getMaxHealth() * (this.isEnraged() ? 0.020F : 0.010F));
                if (livingentity instanceof Player player) {
                    EntityUtils.disableShield(player, MathUtils.sec(5));
                }
                livingentity.push(d0 / d2 * this.powerShockwaveX, 0.2D * this.powerShockwaveY, d1 / d2 * this.powerShockwaveZ);
            }

            this.playSound(BHSounds.BLAZING_INFERNO_SHOCKWAVE.get());
        }
    }

    private void groundSlamAttack(LivingEntity target) {
        if (this.getAnimationTick() == 1) {
            this.playSound(BHSounds.BLAZING_INFERNO_SCREAM.get());
        }
        if (this.getAnimationTick() == MathUtils.sec(3)) {
            if (this.level().isClientSide()) {
                for (int i = 0; i < 24; i++) {
                    if (i % 4 == 0) {
                        this.doGroundSlamIndicator(0.9F, i, i, 0.9F, 0.0F, 1.4F, MathUtils.sec(3), 1, 0.10F, 0);
                    }
                }
            }
        }
        if (this.getAnimationTick() > MathUtils.sec(4) && this.getAnimationTick() <= 85) {
            this.doJump(0.42255D);
            this.doGroundSmashFX = true;
        }
        if (this.getAnimationTick() > MathUtils.sec(6) && this.doGroundSmashFX && this.onGround()) {
            this.doGroundSmashFX = false;
            if (this.level().isClientSide()) {
                float r = ColorUtil.getFARGB(0xFFFFFF)[0];
                float g = ColorUtil.getFARGB(0xFFFFFF)[1];
                float b = ColorUtil.getFARGB(0xFFFFFF)[2];
                this.level().addAlwaysVisibleParticle(new RingParticleOptions(0, (float) -Math.PI / 2, 32, r, g, b, 1.0F, 128.0F, false, RingParticles.Behavior.GROW), this.getX(), this.getY(), this.getZ(), 0, 0, 0);
                this.level().addAlwaysVisibleParticle(new RingParticleOptions(0, (float) Math.PI / 2, 32, r, g, b, 1.0F, 128.0F, false, RingParticles.Behavior.GROW), this.getX(), this.getY(), this.getZ(), 0, 0, 0);
                this.level().addAlwaysVisibleParticle(new RingParticleOptions(0, (float) Math.PI / 2, 64, r, g, b, 1.0F, 128.0F, false, RingParticles.Behavior.GROW), this.getX(), this.getY(), this.getZ(), 0, 0, 0);
                EntityUtils.groundSlamParticles(this.level(), this.yBodyRot, this.getX(), this.getY(0.5D), this.getZ(), 6.5F,  0.25F, 0.065F);
                this.level().addParticle(new RoarParticleOptions(32, 255, 255, 255, 1.0F, 1.0F, 0.1F, 64.0F), this.getX(), this.getY(0.5D), this.getZ(), 0, 0, 0);
            } else {
                this.playSound(BHSounds.BLAZING_INFERNO_SHOCKWAVE.get());
                CameraShake.spawn(this.level(), this.position(), 24.0F, 0.05F, 5, 20);
                this.setGroundSlamProgress(this.getGroundSlamProgress() + 1);
//                    for (LivingEntity livingentity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(8.0), SHOCKWAVE_EXCEPTION)) {
//                        double d0 = livingentity.getX() - this.getX();
//                        double d1 = livingentity.getZ() - this.getZ();
//                        double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
//                        if (livingentity.isAlliedTo(this)) continue;
//                        if (livingentity.isInvulnerable()) continue;
//                        livingentity.hurt(this.level().damageSources().mobAttack(this), this.getAttackDamage(0.15F) + livingentity.getMaxHealth() * (this.isEnraged() ? 0.020F : 0.010F));
//                        if (livingentity instanceof Player player) {
//                            EntityUtils.disableShield(player, MathUtils.sec(5));
//                        }
//                        livingentity.push(d0 / d2 * 2.0D, 0.6D, d1 / d2 * 2.0D);
//                    }
                for (int i = 0; i < 24; i++) {
                    if (i % 4 == 0) {
                        this.doGroundSlam(0.9F, i, i, 0.9F, 0.0F, 1.4F, MathUtils.sec(3), 1, 0.10F, 0);
                    }
                }
                if (this.isEnraged()) {
                    this.createEruption(32);
                }
            }
        }
        if (this.getAnimationTick() > MathUtils.sec(6) && this.doGroundSmashFX && !this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0, -1.52255D, 0));
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

    private void dashAttack() {
        if (this.isDashing()) {
            if (this.tickCount % 4 == 0) {
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
    private void doGroundSlamIndicator(float spreadarc, int distance, int height, float mxy, float vec, float math, int shieldbreakticks, float damage, float hpdamage, float airborne) {
        if (this.level().isClientSide()) {

            double bodyRotRad = this.yBodyRot * (Math.PI / 180.0);
            double cosBodyRot = Math.cos(bodyRotRad);
            double sinBodyRot = Math.sin(bodyRotRad);

            double facingAngle = bodyRotRad + Math.PI / 2.0;

            double commonOffsetX = vec * -sinBodyRot + cosBodyRot * math;
            double commonOffsetZ = vec * cosBodyRot + sinBodyRot * math;

            double baseX = this.getX() + commonOffsetX;
            double baseZ = this.getZ() + commonOffsetZ;

            int hitY = Mth.floor(this.getBoundingBox().minY - 0.5);
            double spread = Math.PI * spreadarc;
            int arcLen = Mth.ceil(distance * spread);


            float factor = 1.0F - (float) distance / 12.0F;

            for (int i = 0; i < arcLen; i++) {
                double thetaRatio = (arcLen > 1) ? (double) i / (double) (arcLen - 1) : 0.5;
                double theta = (thetaRatio - 0.5) * spread + facingAngle;

                double vx = Math.cos(theta);
                double vz = Math.sin(theta);
                double px = baseX + vx * distance;
                double pz = baseZ + vz * distance;

                int hitX = Mth.floor(px);
                int hitZ = Mth.floor(pz);
                BlockPos pos = new BlockPos(hitX, hitY + height, hitZ);
                BlockState block = this.level().getBlockState(pos);

                int maxDepth = 30;
                for (int depthCount = 0; depthCount < maxDepth; depthCount++) {
                    if (block.getRenderShape() == RenderShape.MODEL) {
                        break;
                    }
                    pos = pos.below();
                    block = this.level().getBlockState(pos);
                }

                if (block.getRenderShape() != RenderShape.MODEL) {
                    block = Blocks.AIR.defaultBlockState();
                }
                float r = ColorUtil.getFARGB(0xFF0000)[0];
                float g = ColorUtil.getFARGB(0xFF0000)[1];
                float b = ColorUtil.getFARGB(0xFF0000)[2];
                this.level().addAlwaysVisibleParticle(new RingParticleOptions(0, (float) Math.PI / 2, MathUtils.sec(3), r, g, b, 1.0F, 16.0F, false, RingParticles.Behavior.SHRINK), hitX, pos.getY() + 1.5D, hitZ,0, 0, 0);
                this.level().addAlwaysVisibleParticle(new RingParticleOptions(0, -(float) Math.PI / 2, MathUtils.sec(3), r, g, b, 1.0F, 16.0F, false, RingParticles.Behavior.SHRINK), hitX, pos.getY() + 1.5D, hitZ,0, 0, 0);

            }
        }
    }
    private void doGroundSlam(float spreadarc, int distance, int height, float mxy, float vec, float math, int shieldbreakticks, float damage, float hpdamage, float airborne) {
        if (!this.level().isClientSide()) {

            double bodyRotRad = this.yBodyRot * (Math.PI / 180.0);
            double cosBodyRot = Math.cos(bodyRotRad);
            double sinBodyRot = Math.sin(bodyRotRad);

            double facingAngle = bodyRotRad + Math.PI / 2.0;

            double commonOffsetX = vec * -sinBodyRot + cosBodyRot * math;
            double commonOffsetZ = vec * cosBodyRot + sinBodyRot * math;

            double baseX = this.getX() + commonOffsetX;
            double baseZ = this.getZ() + commonOffsetZ;

            int hitY = Mth.floor(this.getBoundingBox().minY - 0.5);
            double spread = Math.PI * spreadarc;
            int arcLen = Mth.ceil(distance * spread);


            float factor = 1.0F - (float) distance / 12.0F;

            for (int i = 0; i < arcLen; i++) {
                double thetaRatio = (arcLen > 1) ? (double) i / (double) (arcLen - 1) : 0.5;
                double theta = (thetaRatio - 0.5) * spread + facingAngle;

                double vx = Math.cos(theta);
                double vz = Math.sin(theta);
                double px = baseX + vx * distance;
                double pz = baseZ + vz * distance;

                int hitX = Mth.floor(px);
                int hitZ = Mth.floor(pz);
                BlockPos pos = new BlockPos(hitX, hitY + height, hitZ);
                BlockState block = this.level().getBlockState(pos);

                int maxDepth = 30;
                for (int depthCount = 0; depthCount < maxDepth; depthCount++) {
                    if (block.getRenderShape() == RenderShape.MODEL) {
                        break;
                    }
                    pos = pos.below();
                    block = this.level().getBlockState(pos);
                }

                if (block.getRenderShape() != RenderShape.MODEL) {
                    block = Blocks.AIR.defaultBlockState();
                }

                spawnBlocks(hitX, hitY + height, hitZ, (int) (this.getY() - height), block, px, pz, mxy, vx, vz, factor, shieldbreakticks, damage, hpdamage);
            }
        }
    }

    private void spawnBlocks(int hitX, int hitY, int hitZ, int lowestYCheck, BlockState blockState, double px, double pz, float mxy, double vx, double vz, float factor, int shieldbreakticks, float damage, float hpdamage) {
        BlockPos blockpos = new BlockPos(hitX, hitY, hitZ);
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
                break;
            }
            blockpos = blockpos.below();
        } while (blockpos.getY() >= Mth.floor(lowestYCheck) - 1);

        BHFallingBlocks fallingBlockEntity = new BHFallingBlocks(this.level(), hitX + 0.5D, (double) blockpos.getY() + d0 + 0.5D, hitZ + 0.5D, blockState, 10);
        fallingBlockEntity.push(0, 0.2D + this.getRandom().nextGaussian() * 0.04D, 0);
        this.level().addFreshEntity(fallingBlockEntity);

        AABB selection = new AABB(px - 0.5, (double) blockpos.getY() + d0 - 1, pz - 0.5, px + 0.5, (double) blockpos.getY() + d0 + mxy, pz + 0.5);
        List<LivingEntity> hitbox = this.level().getEntitiesOfClass(LivingEntity.class, selection);

        if (!hitbox.isEmpty()) {
            DamageSource damagesource = this.damageSources().mobAttack(this);
            float baseDamage = (float) (this.getAttributeValue(Attributes.ATTACK_DAMAGE) * damage);

            for (LivingEntity entity : hitbox) {
                if (!this.isAlliedTo(entity) && !(entity instanceof BlazingInferno) && entity != this) {
                    float finalDamage = baseDamage + (entity.getMaxHealth() * hpdamage);
                    boolean flag = entity.hurt(damagesource, finalDamage);

                    if (entity.isDamageSourceBlocked(damagesource) && entity instanceof Player player && shieldbreakticks > 0) {
                        EntityUtils.disableShield(player, shieldbreakticks);
                    }
                    if (flag) {
                        this.hit = true;
                        double magnitude = -4;
                        double x = vx * (1 - factor) * magnitude;
                        double y = entity.onGround() ? 0.15 : 0.0;
                        double z = vz * (1 - factor) * magnitude;
                        entity.setDeltaMovement(entity.getDeltaMovement().add(x, y, z));
                    }
                }
            }
        }
    }
    private void summonShield() {
        this.summonShield(this.getShieldCount());
    }

    private void summonShield(int count) {
        float rotate = 360F / count;
        for (int i = 0; i < count; i++) {
            InfernoShield summonShield = new InfernoShield(this.level(), this, 0.25F, i * rotate);
            summonShield.isAlliedTo(this);
            summonShield.setCantDespawn(true);
            this.level().addFreshEntity(summonShield);
        }
        this.shieldCooldown = SHIELD_COOLDOWN;
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
            if (this.getAnimation() == ID_IDLE_STATE) {
                this.stopAnimations();
                this.animationIdleState.startIfStopped(this.tickCount);
            }
        }
        super.onSyncedDataUpdated(accessor);
    }

    private void performRangedAttack(int count, LivingEntity target, int initialFireRate, float velocity) {
        this.performRangedAttack(count, target, initialFireRate, velocity, 0.0F);
    }

    private void performRangedAttack(int count, LivingEntity target, int initialFireRate, float velocity, float inaccuracy) {

        for (int i = 0; i < count; i++) {
            if (this.getAnimationTick() % (initialFireRate - i) == 0) {
                this.doRoarParticle(this.getX(), this.getEyeY(), this.getZ(), 10, 255, 0, 0, 1.0F, 1.0F, 5.0F, 0.1F);
                if (!this.isSilent()) {
                    this.level().playSound((Player) null, this, BHSounds.BLAZING_INFERNO_SHOOT.get(), SoundSource.HOSTILE, 3.0F, 1.0F);
                }
                this.shoot(1, target, velocity, inaccuracy, false, this.isEnraged());
            }
        }

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
                this.animationPrepareDeathRay,
                this.animationDeathRay,
                this.animationBlazingRod,
                this.animationGroundSlam,
                this.animationShockwave,
                this.animationDashes,
                this.animationDeath,
                this.animationJump,
                this.animationEruption,
                this.animationIdleState,
                this.animationEnragedPhase
        };
    }

    @Override
    public boolean canBeSeenAsEnemy() {
        return !this.isSleep() && super.canBeSeenAsEnemy();
    }

    private void shootSpear(LivingEntity target, Vec3 position) {
        this.shootSpear(target, position, MathUtils.sec(1));
    }

    private void shootSpear(LivingEntity target, Vec3 position, int timer) {
        BlazingSpear projectile = new BlazingSpear(this.level(), this);
        position = position.yRot(-this.getYRot() * ((float) Math.PI / 180F));
        projectile.setDamage(DamageTags.TARGET_CURRENT_HEALTH, 0.05F);
        projectile.setBaseDamage(3);
        projectile.setPos(this.getX() - (double) (this.getBbWidth() + 1.0F) * 0.15D * (double) Mth.sin(this.yBodyRot * ((float) Math.PI / 180F)), this.getY() + (double) 1F, this.getZ() + (double) (this.getBbWidth() + 1.0F) * 0.15D * (double) Mth.cos(this.yBodyRot * ((float) Math.PI / 180F)));
        double d0 = position.x;
        double d1 = position.y;
        double d2 = position.z;
        float f = Mth.sqrt((float) (d0 * d0 + d2 * d2)) * 0.35F;
        projectile.shoot(d0, d1 + f, d2, 0.25F, 0.0F);
        projectile.setDelay(timer);
        if (target == null) {
            double d3 = this.getX();
            double d4 = this.getY() + (this.getBbHeight() / 2) + 0.5D;
            double d5 = this.getZ();
            projectile.setWantedTarget((float)d3, (float)d4,(float) d5);
        } else {
            projectile.setWantedTarget((float) target.getX(), (float) target.getY(), (float) target.getZ());
        }
        this.level().addFreshEntity(projectile);
    }

    private void shoot(int count, LivingEntity target, float velocity, float inaccuracy, boolean empowered) {
        this.shoot(count, target, velocity, inaccuracy, true, empowered);
    }

    private void shoot(int count, LivingEntity target, float velocity, float inaccuracy, boolean spread, boolean empowered) {
        double offsetangle = Math.toRadians(12);
        for (int i = 0; i < count; ++i) {
            double angle = spread ? (i - (count - 1) / 2.0F) * offsetangle : 0;
            double d0 = this.getX();
            double d1 = this.getY() + (this.getBbHeight() / 2) + 0.5D;
            double d2 = this.getZ();
            BlazingRod projectile = new BlazingRod(this.level(), d0, d1, d2, this);
            if (empowered) {
                projectile.setDamage(DamageTags.TARGET_CURRENT_HEALTH, 0.02F);
            } else {
                projectile.setDamage(DamageTags.DEFAULT, 0.02F);
            }
            projectile.setBaseDamage(empowered ? 2 : 1);
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
        return Mth.lerp(partialTicks, (float)this.oldSwell, (float)this.swell) / (float)(MathUtils.sec(4) - 2);
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

    public void doJumpTarget(LivingEntity target, double distance, double y) {
        this.getNavigation().stop();
        double posX = target == null ? 0 : (target.getX() - this.getX()) * distance;
        double posY = y;
        double posZ = target == null ? 0 : (target.getZ() - this.getZ()) * distance;
        this.setDeltaMovement(posX, posY, posZ);
    }
    public void doJump(double distance) {
        this.getNavigation().stop();
        this.setDeltaMovement(this.getDeltaMovement().add(new Vec3(0, distance, 0)));
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
    public static class BlazingInfernoMoveGoal extends MobMoveGoal {

        public BlazingInfernoMoveGoal(BHLibEntity boss, boolean followingTargetEvenIfNotSeen, double moveSpeed) {
            super(boss, followingTargetEvenIfNotSeen, moveSpeed);
        }

        @Override
        public boolean canUse() {
            if (this.entity.getAnimationState(ID_ENRAGED_PHASE) || this.entity.getAnimationState(ID_DEATH_RAY) || this.entity.getAnimationState(ID_IDLE_STATE)) {
                return false;
            } else {
                return super.canUse();
            }
        }

        @Override
        public boolean canContinueToUse() {
            if (this.entity.getAnimationState(ID_DEATH_RAY) || this.entity.getAnimationState(ID_IDLE_STATE)) {
                return false;
            } else {
                return super.canContinueToUse();
            }
        }
    }

    public static class RangedAttackGoal extends MobAttackGoal<BlazingInferno> {

        public RangedAttackGoal(BlazingInferno entity, int getAnimation, int start, int end, int seeTick, int maxDuration) {
            super(entity, getAnimation, start, end, seeTick, maxDuration);
        }

        @Override
        public boolean canUse() {
            return super.canUse() && this.entity.fireballCooldown <= 0 && this.entity.getRandomChances(75);
        }

        @Override
        public void stop() {
            super.stop();
            this.entity.fireballCooldown = FIREBALL_COOLDOWN;
        }
    }
    public static class SpearAttackGoal extends MobAttackGoal<BlazingInferno> {

        public SpearAttackGoal(BlazingInferno entity, int getAnimation, int start, int end, int seeTick, int maxDuration) {
            super(entity, getAnimation, start, end, seeTick, maxDuration);
        }

        @Override
        public boolean canUse() {
            return super.canUse() && this.entity.spearCooldown <= 0 && this.entity.getRandomChances(32);
        }

        @Override
        public void stop() {
            super.stop();
            this.entity.spearCooldown = SPEAR_COOLDOWN;
        }
    }
    public static class EruptionAttackGoal extends MobAttackGoal<BlazingInferno> {

        public EruptionAttackGoal(BlazingInferno entity, int animation, int start, int  end, int seeTick, int maxDuration) {
            super(entity, animation, start, end, seeTick, maxDuration);
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.entity.getTarget();
            return super.canUse() && this.entity.eruptionCooldown <= 0 && target != null && target.isAlive() && this.entity.getRandomChances(75) && this.entity.targetDistance < 5;
        }

        @Override
        public void stop() {
            super.stop();
            this.entity.eruptionCooldown = ERUPTION_COOLDOWN;
        }
    }

    public static class ShockwaveAttackGoal extends MobAttackGoal<BlazingInferno> {

        public ShockwaveAttackGoal(BlazingInferno entity, int animation, int start, int  end, int seeTick, int maxDuration) {
            super(entity, animation, start, end, seeTick, maxDuration);
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.entity.getTarget();
            return super.canUse() && this.entity.shockwaveCooldown <= 0 && target != null && target.isAlive() && this.entity.targetDistance < 8 && this.entity.getRandomChances(75);
        }

        @Override
        public void stop() {
            super.stop();
            this.entity.shockwaveCooldown = SHOCKWAVE_COOLDOWN;
        }
    }
    public static class GroundSlamAttackGoal extends MobAttackGoal<BlazingInferno> {

        public GroundSlamAttackGoal(BlazingInferno entity, int animation, int start, int  end, int seeTick) {
            super(entity, animation, start, end, seeTick);
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.entity.getTarget();
            return super.canUse() && this.entity.groundSlamCooldown <= 0 ;
        }

        @Override
        public void stop() {
            super.stop();
            this.entity.groundSlamCooldown = GROUND_SLAM_COOLDOWN;
            this.entity.setAnimation(ID_IDLE_STATE);
        }

        @Override
        public boolean canContinueToUse() {
            if (this.entity.getAnimationTick() > MathUtils.sec(5) && this.entity.onGround() && this.entity.doGroundSmashFX) {
                return false;
            } else {
                return super.canContinueToUse();
            }
        }
    }

    public static class EnragedDashAttackGoal extends MobAttackGoal<BlazingInferno> {

        public EnragedDashAttackGoal(BlazingInferno entity, int animation, int start, int  end, int seeTick) {
            super(entity, animation, start, end, seeTick);
        }

        @Override
        public boolean canUse() {
            return super.canUse() && this.entity.isEnraged() && this.entity.dashCooldown <= 0 && this.entity.getBossPhase() >= 2 && this.entity.getRandomChances(75);
        }

        @Override
        public boolean canContinueToUse() {
            if (this.entity.getDashProgress() >= this.entity.getDashCount()) {
                return false;
            } else {
                return super.canContinueToUse();
            }
        }

        @Override
        public void stop() {
            super.stop();
            this.entity.dashCooldown = DASH_COOLDOWN;
            this.entity.setIsDashing(false);
            this.entity.setDashProgress(0);
            this.entity.setAnimation(ID_IDLE_STATE);

        }
    }
    public static class DashAttackGoal extends MobAttackGoal<BlazingInferno> {

        public DashAttackGoal(BlazingInferno entity, int animation, int start, int  end, int seeTick, int maxDuration) {
            super(entity, animation, start, end, seeTick, maxDuration);
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !this.entity.isEnraged() && this.entity.dashCooldown <= 0 && this.entity.getBossPhase() >= 2 && this.entity.getRandomChances(75);
        }

        @Override
        public void stop() {
            super.stop();
            this.entity.dashCooldown = DASH_COOLDOWN;
            this.entity.setDashProgress(0);
            this.entity.setIsDashing(false);
        }
    }
    public static class PrepareDeathRayAttackGoal extends MobAttackGoal<BlazingInferno> {
        public PrepareDeathRayAttackGoal(BlazingInferno entity, int animation, int start, int  end, int seeTick, int maxDuration) {
            super(entity, animation, start, end, seeTick, maxDuration);
        }
        @Override
        public void start() {
            super.start();
        }

        @Override
        public boolean canUse() {
            return super.canUse() && this.entity.deathRayCooldown <= 0 && this.entity.isEnraged();
        }

        @Override
        public void stop() {
            super.stop();
        }
    }

    public static class DeathRayAttackGoal extends MobAttackGoal<BlazingInferno> {

        public DeathRayAttackGoal(BlazingInferno entity, int animation, int start, int  end, int seeTick, int maxDuration) {
            super(entity, animation, start, end, seeTick, maxDuration);
        }

        @Override
        public void start() {
            super.start();
            this.entity.setIsUsingDeathRay(true);
        }

        @Override
        public boolean canUse() {
            return super.canUse() && this.entity.deathRayCooldown <= 0 && this.entity.isEnraged();
        }

        @Override
        public void stop() {
            super.stop();
            this.entity.deathRayCooldown = DEATH_RAY_COOLDOWN;
            this.entity.setIsUsingDeathRay(false);
        }

        @Override
        public void tick() {
            LivingEntity target = this.entity.getTarget();
            super.tick();
            if (this.entity.getAnimationTick() == 2) {
                this.entity.setIsUsingDeathRay(true);
                BeyondHorizon.PROXY.playSound(new DeathRayChargingSound(this.entity, BHSounds.BLAZING_INFERNO_DEATH_RAY.get()));
                float radius = 0.80F;
                int duration = 80;
                BlazingInfernoRayAbility ability = new BlazingInfernoRayAbility(BHEntity.BLAZING_INFERNO_RAY.get(),
                        this.entity.level(), this.entity, this.entity.getX() + radius * Math.sin(-this.entity.getYRot() * Math.PI / 180),
                        this.entity.getY() + 1.4, this.entity.getZ() + radius * Math.cos(-this.entity.getYRot() * Math.PI / 180),
                        (float) ((this.entity.yHeadRot + 90) * Math.PI / 180), (float) (-this.entity.getXRot() * Math.PI / 180), duration);
                ability.laserBeamConfiguration(AbstractDeathRayAbility.DamageTypes.CURRENT_HEALTH, 1.0F);
                ability.setCanBurnTarget(true);
                ability.scaleCurrentHealthDamage(0.2F);
                ability.setImmunityFrameIgnore(false);
                this.entity.level().addFreshEntity(ability);
            }
            if (this.entity.getAnimationTick() >= 2) {
                if (target != null) {
                    this.entity.getLookControl().setLookAt(target.getX(),target.getY() + target.getBbHeight() / 2, target.getZ(), 2.0F, 45.0F);
                }
            }
        }
    }
}
