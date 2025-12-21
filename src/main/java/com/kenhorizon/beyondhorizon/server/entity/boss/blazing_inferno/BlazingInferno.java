package com.kenhorizon.beyondhorizon.server.entity.boss.blazing_inferno;

import com.kenhorizon.beyondhorizon.server.entity.boss.BHBossEntity;
import com.kenhorizon.beyondhorizon.server.entity.CameraShake;
import com.kenhorizon.beyondhorizon.server.entity.goal.*;
import com.kenhorizon.beyondhorizon.server.entity.projectiles.BlazingRod;
import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.init.BHEffects;
import com.kenhorizon.beyondhorizon.server.init.BHItems;
import com.kenhorizon.beyondhorizon.server.init.BHSounds;
import com.kenhorizon.beyondhorizon.server.level.CombatUtil;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
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
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

public class BlazingInferno extends BHBossEntity {
    public AnimationState animationDodge = new AnimationState();
    public AnimationState animationActive = new AnimationState();
    public AnimationState animationInactive = new AnimationState();
    public AnimationState animationMelee0 = new AnimationState();
    public AnimationState animationMelee1 = new AnimationState();
    public AnimationState animationMelee2 = new AnimationState();
    public AnimationState animationComboMelee0 = new AnimationState();
    public AnimationState animationComboMelee1 = new AnimationState();
    public AnimationState animationComboMelee2 = new AnimationState();
    public AnimationState animationShield = new AnimationState();
    public AnimationState animationDeathRay = new AnimationState();
    public AnimationState animationPrepareDeathRay = new AnimationState();
    public AnimationState animationFireballSpread = new AnimationState();
    public AnimationState animationFireballBurst = new AnimationState();
    public AnimationState animationFireballNormal = new AnimationState();
    public AnimationState animationGroundSlam = new AnimationState();
    public AnimationState animationShockwave = new AnimationState();
    public AnimationState animationDashes = new AnimationState();
    public AnimationState animationDeath = new AnimationState();
    public AnimationState animationEnragedPhase = new AnimationState();
    public AnimationState animationJump = new AnimationState();
    public List<InfernoShield> infernoShields = new ArrayList<>();
    public int fireballCooldown = 0;
    public final int FIREBALL_COOLDOWN = Maths.sec(6);
    public int spearCooldown = 0;
    public final int SPEAR_COOLDOWN = Maths.sec(7);
    public int comboMeleeCooldown = 0;
    public final int COMBO_MELEE_COOLDOWN = Maths.sec(12);
    public int deathRayCooldown = 0;
    public final int DEATH_RAY_COOLDOWN = Maths.sec(20);
    public int groundSlamCooldown = 0;
    public final int GROUND_SLAM_COOLDOWN = Maths.sec(16);
    public int dashCooldown = 0;
    public final int DASH_COOLDOWN = Maths.sec(24);
    public int shockwaveCooldown = 0;
    public final int SHOCKWAVE_COOLDOWN = Maths.sec(32);
    public int shieldCooldown = 0;
    public final int SHIELD_COOLDOWN = Maths.mins(1);
    public int jumpCooldown = 0;
    public final int JUMP_COOLDOWN = Maths.sec(25);
    public final int AWAKEN_COOLDOWN = Maths.sec(5);
    public static final String NBT_POWERED = "is_powered";
    public static final String NBT_ENRAGED = "is_enraged";
    public static final String NBT_SHIELD_COUNT = "shield_count";
    public static final String NBT_SHIELD_ACTIVE = "shield_active";
    public static final String NBT_AWAKEN_PROGRESS = "awaken_progress";
    public static final EntityDataAccessor<Boolean> POWERED = SynchedEntityData.defineId(BlazingInferno.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> IS_DASHING = SynchedEntityData.defineId(BlazingInferno.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> ENRAGED = SynchedEntityData.defineId(BlazingInferno.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Integer> SHIELD_COUNT = SynchedEntityData.defineId(BlazingInferno.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> AWAKEN_PROGRESS = SynchedEntityData.defineId(BlazingInferno.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> SHIELD_ACTIVE = SynchedEntityData.defineId(BlazingInferno.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DEATH_RAY = SynchedEntityData.defineId(BlazingInferno.class, EntityDataSerializers.BOOLEAN);

    public BlazingInferno(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        this.setExp(500);
        this.setMaxUpStep(2.0F);
        this.setMaxBossPhase(5);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.LAVA, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.UNPASSABLE_RAIL, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, 0.0F);
    }

    public static AttributeSupplier createAttributes() {
        return createEntityAttributes()
                .add(Attributes.MAX_HEALTH, 800.0D)
                .add(Attributes.ARMOR, 25.0D)
                .add(BHAttributes.DAMAGE_TAKEN.get(), -0.15D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2555F)
                .add(Attributes.ATTACK_DAMAGE, 15.0F)
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
        this.goalSelector.addGoal(0, new BlazingInfernoInactive());
        this.goalSelector.addGoal(4, new MobMoveGoal(this, false, 1.0F));
        this.goalSelector.addGoal(1, new MobStateGoal<>(this, 1,1, 0, 0, 0) {
            @Override
            public void tick() {
                this.entity.setDeltaMovement(0, this.entity.getDeltaMovement().y, 0);
            }
        });
        this.goalSelector.addGoal(0, new BlazingInfernoAwakenGoal(this, 1,2, 0, 0, Maths.sec(5)));
        this.targetSelector.addGoal(1, new HurtByNearestTargetGoal(this));
        this.targetSelector.addGoal(1, new FireballAttackGoal(this, 0, this.randomFireballAttack(), 0, 30, Maths.sec(3) ,32));
        this.targetSelector.addGoal(2, new SpearAttackGoal(this, 0, this.randomSpearAttack(), 0, 30, Maths.sec(3) ,32));
        this.targetSelector.addGoal(3, new GroundSlamAttackGoal(this, 0, 9, 10, 30, Maths.sec(5) ,32));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractGolem.class, true));
    }
    public int randomSpearAttack() {
        int[] nums = {6, 7, 8};
        return nums[this.getRandom().nextInt(nums.length)];
    }
    public int randomFireballAttack() {
        int[] nums = {3, 4, 5};
        return nums[this.getRandom().nextInt(nums.length)];
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(AWAKEN_PROGRESS, 4);
        this.entityData.define(SHIELD_COUNT, 4);
        this.entityData.define(ENRAGED, false);
        this.entityData.define(SHIELD_ACTIVE, false);
        this.entityData.define(IS_DASHING, false);
        this.entityData.define(POWERED, true);
        this.entityData.define(DEATH_RAY, false);
    }
    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        if (this.isPowered()) {
            this.bossInfo().addPlayer(player);
        }
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        if (this.isPowered()) {
            this.bossInfo().removePlayer(player);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.setIsPowered(nbt.getBoolean(NBT_POWERED));
        this.setIsEnraged(nbt.getBoolean(NBT_ENRAGED));
        this.setShieldCount(nbt.getInt(NBT_SHIELD_COUNT));
        this.setInfernoShieldActive(nbt.getBoolean(NBT_SHIELD_ACTIVE));
        this.setAwakenProgress(nbt.getInt(NBT_AWAKEN_PROGRESS));
    }
    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean(NBT_POWERED, this.isPowered());
        nbt.putBoolean(NBT_ENRAGED, this.isEnraged());
        nbt.putInt(NBT_SHIELD_COUNT, this.getShieldCount());
        nbt.putBoolean(NBT_SHIELD_ACTIVE, this.isInfernoShieldActive());
        nbt.putInt(NBT_AWAKEN_PROGRESS, this.getAwakenProgress());
    }

    public void setAwakenProgress(int awakenProgress) {
        this.entityData.set(AWAKEN_PROGRESS, Math.min(awakenProgress, 100));
    }

    public int getAwakenProgress() {
        return this.entityData.get(AWAKEN_PROGRESS);
    }

    public boolean isSleep() {
        return this.getAnimation() == 1 || this.getAnimation() == 2;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypes.GENERIC) || source.is(DamageTypes.GENERIC_KILL)) {
            return super.hurt(source, amount);
        } else {
            boolean gotHurt = super.hurt(source, amount);
            if (this.isInfernoShieldActive()) {
                amount = CombatUtil.multiplier(amount, -0.85F);
                if (source.getEntity() instanceof Player player) {
                    if (player.getMainHandItem().getItem() instanceof AxeItem) {
                        amount *= 2.0F;
                    }
                }
                if (gotHurt) {
                    for (InfernoShield shield : this.infernoShields) {
                        shield.hurt(source, amount);
                    }
                }
            } else {
                if (this.isSleep()) {
                    if (!source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
                        return false;
                    }
                }
            }
            return gotHurt;
        }
    }

    @Override
    protected int decreaseAirSupply(int currentAir) {
        return currentAir;
    }

    @Override
    protected void playStepSound(BlockPos blockPos, BlockState blockState) {
        this.playSound(BHSounds.BLAZING_INFERNO_STEP.get());
    }

    @Override
    public boolean isSensitiveToWater() {
        return false;
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
            this.setAnimation(1);
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
    public void tick() {
        super.tick();
        if (this.spearCooldown > 0) this.spearCooldown--;
        if (this.comboMeleeCooldown > 0) this.comboMeleeCooldown--;
        if (this.dashCooldown > 0) this.dashCooldown--;
        if (this.deathRayCooldown > 0) this.deathRayCooldown--;
        if (this.shockwaveCooldown > 0) this.shockwaveCooldown--;
        if (this.groundSlamCooldown > 0) this.groundSlamCooldown--;
        if (!this.isEnraged() && !this.isInfernoShieldActive() && this.shieldCooldown > 0) this.shieldCooldown--;
        if (this.fireballCooldown > 0) this.fireballCooldown--;
        if (this.getAnimation() == 2) {
            this.setAwakenProgress(this.getAwakenProgress() + 1);
        }
    }

    @Override
    protected void playHurtSound(DamageSource source) {
        this.playSound(BHSounds.BLAZING_INFERNO_HURT.get());
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.isPowered()) {
            if (this.level().isClientSide()) {
                int flameCount = this.isEnraged() ? 5 : 2;
                for (int i = 0; i < flameCount; ++i) {
                    this.level().addParticle(ParticleTypes.FLAME, this.getRandomX(0.5D), this.getBbHeight() * 0.01D, this.getRandomZ(0.5D), 0.0D, 0.0D, 0.0D);
                }
            }
        }
        this.infernoShields = this.level().getEntitiesOfClass(InfernoShield.class, BlazingInferno.this.getBoundingBox().inflate(16.0D));
        for (InfernoShield affectedInfernoShield : this.infernoShields) {
            if (affectedInfernoShield.getEntityUUID().isPresent() && affectedInfernoShield.getEntityUUID().get() == this.getUUID()) {
                this.setShieldCount(this.infernoShields.size());
            }
        }
        this.setInfernoShieldActive(!this.infernoShields.isEmpty());
        if (!this.isSleep() && this.isPowered() && !this.isEnraged() && !this.isInfernoShieldActive() && this.shieldCooldown <= 0) {
            this.shieldCooldown = this.SHIELD_COOLDOWN;
            int shield = this.getShieldCount();
            this.setShieldCount(shield);
            float rotate = 360F / shield;
            for (int i = 0; i < shield; i++) {
                InfernoShield summonShield = new InfernoShield(this.level(), this, 0.25F, i * rotate);
                summonShield.isAlliedTo(this);
                this.level().addFreshEntity(summonShield);
            }
        }
        LivingEntity target = this.getTarget();
        if (this.getAnimation() == 18) {
            if (this.level().isClientSide()) {
                if (this.getAnimationTick() == 90) {
                    CameraShake.spawn(this.level(), this.position(), 24, 0.12F, 20, 10);
                    this.level().playSound((Player) null, this, BHSounds.BLAZING_INFERNO_SCREAM.get(), SoundSource.HOSTILE, 3.0F, 1.0F);
                    this.sphereParticle(12);
                }
            }
        }
        if (target != null) {
            if (this.getAnimation() == 2) {
                if (this.getAnimationTick() % 5 == 0) {
                    this.playSound(BHSounds.BLAZING_INFERNO_SHOOT.get());
                    this.shoot(1, target, 1.0F, 0.2F);
                }
            }
            if (this.getAnimation() == 3) {
                if (this.getAnimationTick() % 10 == 0) {
                    this.playSound(BHSounds.BLAZING_INFERNO_SHOOT.get());
                    this.shoot(3, target, 1.0F, 1.2F);
                }
            }
            if (this.getAnimation() == 4) {
                if (this.getAnimationTick() % 10 == 0) {
                    this.playSound(BHSounds.BLAZING_INFERNO_SHOOT.get());
                    this.shoot(4, target, 1.0F, 2.2F);
                }
            }

            if (this.getAnimation() == 5) {
                if (this.getAnimationTick() == 40) {
                    this.playSound(BHSounds.BLAZING_INFERNO_SHOOT.get());
                    CameraShake.spawn(this.level(), this.position(), 32.0F, 0.15F, 5, 20);
                    for (int i = 0; i < 12; i++) {
                        int randomNms = random.nextIntBetweenInclusive(-12, 12);
                        this.shootSpear(new Vec3(randomNms, 1, randomNms), Maths.sec(i), target);
                    }
                }
            }
            if (this.getAnimation() == 6) {
                if (this.getAnimationTick() == 40) {
                    this.playSound(BHSounds.BLAZING_INFERNO_SHOOT.get());
                    CameraShake.spawn(this.level(), this.position(), 32.0F, 0.15F, 5, 20);
                    for (int i = 0; i < 12; i++) {
                        float rotate = (360F / 12) * i;
                        this.shootSpear(new Vec3(Math.cos(rotate), 1, Math.sin(rotate)), Maths.sec(i), target);
                    }
                }
            }
            if (this.getAnimation() == 7) {
                if (this.getAnimationTick() == 40) {
                    this.playSound(BHSounds.BLAZING_INFERNO_SHOOT.get());
                    this.shootSpear(new Vec3(2, 1, 0), Maths.sec(3), target);
                    this.shootSpear(new Vec3(-2, 1, 0), Maths.sec(4), target);
                    this.shootSpear(new Vec3(0, 1, 0), Maths.sec(5), target);
                }
            }
            if (this.getAnimation() == 9) {
                if (this.getAnimationTick() < 20) {
                    this.setDeltaMovement(0, 0, 0);
                }
                if (this.getAnimationTick() == 20) {
                    double posX = (target.getX() - this.getX()) * 0.10D;
                    double posY = 1.3D;
                    double posZ = (target.getZ() - this.getZ()) * 0.10D;
                    this.setDeltaMovement(posX, posY, posZ);
                }
            }
            if (this.getAnimation() == 10) {
                if (this.getAnimationTick() == 5) {
                    this.setDeltaMovement(0, 0, 0);
                    CameraShake.spawn(this.level(), this.position(), 16.0F, 0.05F, 5, 20);
                    float damage = (float) (getAttributeValue(Attributes.ATTACK_DAMAGE) * 1.0F);
                    if (this.hurtEntitiesAround(this.position(), 6.5F, damage, 2.75F, true, true)) {
                        target.addEffect(new MobEffectInstance(BHEffects.STUN.get(), 40, 0, true, true));
                        this.playSound(BHSounds.BLAZING_INFERNO_SCREAM.get());
                    }
                }
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
    private void shoot(int count, LivingEntity target, float velocity, float inaccuracy) {
        for (int i = 0; i < count; ++i) {
            BlazingRod projectile = new BlazingRod(this.level(), this);
            projectile.setBaseDamage(CombatUtil.currentHealth(target, 3, 0.02F));
            double d0 = this.getX();
            double d1 = this.getY() + (this.getBbHeight() / 2) + 0.5D;
            double d2 = this.getZ();
            double d3 = target.getX() - this.getX();
            double d4 = target.getBoundingBox().minY + target.getBbHeight() / 2 - projectile.getY();
            double d5 = target.getZ() - this.getZ();
            projectile.shoot(d3, d4, d5, velocity, inaccuracy);
            projectile.setPosRaw(d0, d1, d2);
            this.level().addFreshEntity(projectile);
        }
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
            this.setIsPowered(true);
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }
    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData data, @Nullable CompoundTag dataNbt) {
        return super.finalizeSpawn(level, difficulty, reason, data, dataNbt);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        if (ANIMATION_STATE.equals(accessor)) {
            switch (this.getAnimation()) {
                case 0 -> this.stopAnimations();
                case 1 -> {
                    this.stopAnimations();
                    this.animationInactive.startIfStopped(this.tickCount);
                }
                case 2 -> {
                    this.stopAnimations();
                    this.animationActive.startIfStopped(this.tickCount);
                }
                case 3 -> {
                    this.stopAnimations();
                    this.animationFireballNormal.startIfStopped(this.tickCount);
                }
                case 4 -> {
                    this.stopAnimations();
                    this.animationFireballSpread.startIfStopped(this.tickCount);
                }
                case 5 -> {
                    this.stopAnimations();
                    this.animationFireballBurst.startIfStopped(this.tickCount);
                }
                case 6 -> {
                    this.stopAnimations();
                    this.animationMelee0.startIfStopped(this.tickCount);
                }
                case 7 -> {
                    this.stopAnimations();
                    this.animationMelee1.startIfStopped(this.tickCount);
                }
                case 8 -> {
                    this.stopAnimations();
                    this.animationMelee2.startIfStopped(this.tickCount);
                }
                case 9 -> {
                    this.stopAnimations();
                    this.animationJump.startIfStopped(this.tickCount);
                }
                case 10 -> {
                    this.stopAnimations();
                    this.animationGroundSlam.startIfStopped(this.tickCount);
                }
            }
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
                this.animationMelee0,
                this.animationMelee1,
                this.animationMelee2,
                this.animationComboMelee0,
                this.animationComboMelee1,
                this.animationComboMelee2,
                this.animationShield,
                this.animationDeathRay,
                this.animationPrepareDeathRay,
                this.animationFireballSpread,
                this.animationFireballBurst,
                this.animationFireballNormal,
                this.animationGroundSlam,
                this.animationShockwave,
                this.animationDashes,
                this.animationDeath,
                this.animationJump,
                this.animationEnragedPhase
        };
    }

    @Override
    public boolean canBeSeenAsEnemy() {
        return !this.isSleep() && super.canBeSeenAsEnemy();
    }

    private void shootSpear(Vec3 position, int timer, LivingEntity target) {
        BlazingSpear projectile = new BlazingSpear(this.level(), this);
        position = position.yRot(-this.getYRot() * ((float) Math.PI / 180F));
        projectile.setBaseDamage(CombatUtil.currentHealth(target, 3, 0.055F));
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

    public float getAwakenProgress(float partialTicks) {
        if (!this.isSleep()) {
            return 1.0F;
        }
        return Mth.clamp((float) this.getAwakenProgress() / this.AWAKEN_COOLDOWN, 0.0F, 1.0F);
    }

    class BlazingInfernoInactive extends Goal {
        public BlazingInfernoInactive() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
        }
        @Override
        public boolean canUse() {
            return !BlazingInferno.this.isPowered();
        }
    }

    public static class BlazingInfernoAwakenGoal extends MobStateGoal<BlazingInferno> {

        public BlazingInfernoAwakenGoal(BlazingInferno entity, int getAnimation, int startAnimation, int endAnimation, int attackSeeTick, int attackMaxTick) {
            super(entity, getAnimation, startAnimation, endAnimation, attackSeeTick, attackMaxTick);
        }

        @Override
        public boolean canUse() {
            return this.entity.isPowered() && this.entity.getAnimation() == this.getAnimation;
        }

        @Override
        public void start() {
            if (this.getAnimation != this.startAnimation) {
                this.entity.setAnimation(this.startAnimation);
            }
        }

        @Override
        public void stop() {
            this.entity.setAnimation(this.endAnimation);
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

        public FireballAttackGoal(BlazingInferno entity,int getAnimation, int startAnimation, int endAnimation, int attackSeeTick, int attackMaxTick, float attackRange) {
            super(entity, 1.0F, getAnimation, startAnimation, endAnimation, attackSeeTick, attackMaxTick, attackRange);
        }

        @Override
        public boolean canUse() {
            return super.canUse() && this.entity.fireballCooldown <= 0;
        }

        @Override
        public void stop() {
            super.stop();
            this.entity.fireballCooldown = this.entity.FIREBALL_COOLDOWN;
        }
    }
    public static class SpearAttackGoal extends MobAttackGoal<BlazingInferno> {

        public SpearAttackGoal(BlazingInferno entity, int getAnimation, int startAnimation, int endAnimation, int attackSeeTick, int attackMaxTick, float attackRange) {
            super(entity, 0.75F, getAnimation, startAnimation, endAnimation, attackSeeTick, attackMaxTick, attackRange);
        }

        @Override
        public boolean canUse() {
            return super.canUse() && this.entity.spearCooldown <= 0;
        }

        @Override
        public void stop() {
            super.stop();
            this.entity.spearCooldown = this.entity.SPEAR_COOLDOWN;
        }
    }
    public static class GroundSlamAttackGoal extends MobAttackGoal<BlazingInferno> {

        public GroundSlamAttackGoal(BlazingInferno entity, int getAnimation, int startAnimation, int endAnimation, int attackSeeTick, int attackMaxTick, float attackRange) {
            super(entity, 0.75F, getAnimation, startAnimation, endAnimation, attackSeeTick, attackMaxTick, attackRange);
        }

        @Override
        public boolean canUse() {
            return super.canUse() && this.entity.groundSlamCooldown <= 0;
        }

        @Override
        public void stop() {
            super.stop();
            this.entity.groundSlamCooldown = this.entity.GROUND_SLAM_COOLDOWN;
        }
    }
}
