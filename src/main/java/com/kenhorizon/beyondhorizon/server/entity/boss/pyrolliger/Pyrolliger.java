package com.kenhorizon.beyondhorizon.server.entity.boss.pyrolliger;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.particle.RingParticles;
import com.kenhorizon.beyondhorizon.client.particle.TrailParticles;
import com.kenhorizon.beyondhorizon.client.particle.world.RingParticleOptions;
import com.kenhorizon.beyondhorizon.client.particle.world.TrailParticleOptions;
import com.kenhorizon.beyondhorizon.client.render.misc.tooltips.Tooltips;
import com.kenhorizon.beyondhorizon.client.render.util.Colors;
import com.kenhorizon.beyondhorizon.client.sound.DeathRayChargingSound;
import com.kenhorizon.beyondhorizon.server.entity.BHBossInfo;
import com.kenhorizon.beyondhorizon.server.entity.ability.AbstractDeathRayAbility;
import com.kenhorizon.beyondhorizon.server.entity.ability.BlazingInfernoRayAbility;
import com.kenhorizon.beyondhorizon.server.entity.ability.BurningHexTrapAbility;
import com.kenhorizon.beyondhorizon.server.entity.ai.*;
import com.kenhorizon.beyondhorizon.server.entity.ai.ability.DodgeAbility;
import com.kenhorizon.beyondhorizon.server.entity.ai.control.SmartBodyControl;
import com.kenhorizon.beyondhorizon.server.entity.boss.BHBossEntity;
import com.kenhorizon.beyondhorizon.server.entity.projectiles.Pyrobolt;
import com.kenhorizon.beyondhorizon.server.entity.projectiles.Pyrolance;
import com.kenhorizon.beyondhorizon.server.entity.util.AnimationTickers;
import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.init.BHEntityDataSerializer;
import com.kenhorizon.beyondhorizon.server.init.BHSounds;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageType;
import com.kenhorizon.beyondhorizon.server.util.DefaultDamageCaps;
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
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Pyrolliger extends BHBossEntity {
    public enum Mode {
        MELEE,
        RANGED
    }
    private float dodgeYaw = 0;
    private int mana = 0;
    private int maxMana = 100;
    protected Pyrolliger.Mode mode = Mode.RANGED;
    private final DodgeAbility dodgeAbility = new DodgeAbility(this);
    public static int animationId = 1;
    public AnimationState animationIdle1 = new AnimationState();
    public AnimationState animationIdle2 = new AnimationState();
    public AnimationState animationPyrobolt1 = new AnimationState();
    public AnimationState animationPyrolance = new AnimationState();
    public AnimationState animationRangedUlt = new AnimationState();
    public AnimationState animationMeleeUlt = new AnimationState();
    public AnimationState animationBurningHexTrap = new AnimationState();
    public AnimationState animationDodge = new AnimationState();
    public AnimationState animationStanceRanged = new AnimationState();
    public AnimationState animationStanceMelee = new AnimationState();
    public AnimationState animationAtk1 = new AnimationState();
    public AnimationState animationAtk2 = new AnimationState();
    public AnimationState animationAtk3 = new AnimationState();

    // Animation Id
    public static final int ID_DODGE = createAnimationID();
    public static final int ID_IDLE1 = createAnimationID();
    public static final int ID_IDLE2 = createAnimationID();
    // RANGED
    public static final int ID_PYROBOLT1 = createAnimationID();
    public static final int ID_DRACONIC_FIRELORD = createAnimationID();
    public static final int ID_PYROLANCE = createAnimationID();
    public static final int ID_BURNING_HEX_TRAP = createAnimationID();
    public static final int ID_PYRO_GEM = createAnimationID();
    public static final int ID_PYRO_SLASH = createAnimationID();
    // MELEE
    public static final int ID_ATTACK_1 = createAnimationID();
    public static final int ID_ATTACK_2 = createAnimationID();
    public static final int ID_ATTACK_3 = createAnimationID();
    public static final int ID_CROSS_BLADE = createAnimationID();
    public static final int ID_SLASH_N_DASH = createAnimationID();
    public static final int ID_HEX_EYE = createAnimationID();
    public static final int ID_HAIL_RAIN = createAnimationID();
    public static final int ID_BURNING_POINT = createAnimationID();
    //
    public static final int ID_TRANSITION_STANCE_RANGED = createAnimationID();
    public static final int ID_TRANSITION_STANCE_MELEE = createAnimationID();
    // Ability Cooldowns
    public AnimationTickers dodgeCooldown = AnimationTickers.create(Maths.sec(7));
    public AnimationTickers pyroboltCooldown = AnimationTickers.create(Maths.sec(3));
    public AnimationTickers pyrolanceCooldown = AnimationTickers.create(Maths.sec(4));
    public AnimationTickers burningHexTrapCooldown = AnimationTickers.create(Maths.sec(52));

    public AnimationTickers attack1Cooldown = AnimationTickers.create(Maths.sec(3));
    public AnimationTickers attack2Cooldown = AnimationTickers.create(Maths.sec(3));
    public AnimationTickers attack3Cooldown = AnimationTickers.create(Maths.sec(3));
    public AnimationTickers idle1Cooldown = AnimationTickers.create(Maths.sec(3));
    public AnimationTickers idle2Cooldown = AnimationTickers.create(Maths.sec(3));

    public static final String NBT_MANA = "mana";
    public static final String NBT_MAX_MANA = "max_mana";
    public static final String NBT_SWORD_VISIBLE = "sword_visible";
    public static final String NBT_MODE = "mode";
    private static final EntityDataAccessor<Integer> ATTACK_COUNT = SynchedEntityData.defineId(Pyrolliger.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> MANA = SynchedEntityData.defineId(Pyrolliger.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> MAX_MANA = SynchedEntityData.defineId(Pyrolliger.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> VISIBLE_SWORD = SynchedEntityData.defineId(Pyrolliger.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> USING_ATTACK1 = SynchedEntityData.defineId(Pyrolliger.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Pyrolliger.Mode> MODE = SynchedEntityData.defineId(Pyrolliger.class, BHEntityDataSerializer.PYROLLIGER_MODE.get());
    private final BHBossInfo abilityMana = new BHBossInfo(this, Component.empty(), 3);
    public Pyrolliger(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        this.setExp(500);
        this.abilityMana.setVisible(true);
        this.setMana(0);
        this.setMaxMana(100);
        this.setMode(Mode.RANGED);
        this.setDamageCap(DefaultDamageCaps.PYROLLIGER);
        this.setMaxUpStep(1.5F);
        this.setMaxBossPhase(3);
        this.bossInfo().setRenderType(2);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.LAVA, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.UNPASSABLE_RAIL, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, 0.0F);
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new SmartBodyControl(this);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(USING_ATTACK1, false);
        this.entityData.define(VISIBLE_SWORD, false);
        this.entityData.define(ATTACK_COUNT, 0);
        this.entityData.define(MANA, 0);
        this.entityData.define(MAX_MANA, 100);
        this.entityData.define(MODE, Mode.RANGED);
    }
    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.dodgeAbility.loadNbt(nbt);
        this.setMana(nbt.getInt(NBT_MANA));
        this.setMaxMana(nbt.getInt(NBT_MAX_MANA));
        this.setVisibleSword(nbt.getBoolean(NBT_SWORD_VISIBLE));
        this.setMode(Pyrolliger.Mode.values()[nbt.getInt(NBT_MODE)]);
    }
    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        this.dodgeAbility.saveNbt();
        nbt.putInt(NBT_MANA, this.getMana());
        nbt.putInt(NBT_MAX_MANA, this.getMaxMana());
        nbt.putBoolean(NBT_SWORD_VISIBLE, this.isVisibleSword());
        nbt.putInt(NBT_MODE, this.getMode().ordinal());
    }
    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.getMode() == Mode.MELEE) {
            this.addMana(1);
        }
        return super.hurt(source, amount);
    }

    public void setAttackCount(int v) {
        this.entityData.set(ATTACK_COUNT, v);
    }

    public int getAttackCount() {
        return this.entityData.get(ATTACK_COUNT);
    }

    public void setVisibleSword(boolean v) {
        this.entityData.set(VISIBLE_SWORD, v);
    }

    public boolean isVisibleSword() {
        return this.entityData.get(VISIBLE_SWORD);
    }

    public void setUsingAttack1(boolean v) {
        this.entityData.set(USING_ATTACK1, v);
    }

    public boolean isUsingAttack1() {
        return this.entityData.get(USING_ATTACK1);
    }

    @Override
    protected int decreaseAirSupply(int currentAir) {
        return currentAir;
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    public Mode getMode() {
        return this.level().isClientSide() ? this.entityData.get(MODE) : mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
        this.entityData.set(MODE, mode);
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
    public boolean isAlliedTo(Entity entity) {
        if (entity == null) {
            return false;
        } else if (entity == this) {
            return true;
        } else if (super.isAlliedTo(entity)) {
            return true;
        } else if (entity instanceof Mob mob && mob.getMobType() == MobType.ILLAGER) {
            return true;
        } else {
            return false;
        }
    }

    public void addMana(int mana) {
        if (this.getMana() >= this.getMaxMana()) {
            this.setMana(this.getMaxMana());
        } else {
            this.setMana(this.getMana() + mana);
        }
    }

    public void setMana(int mana) {
        this.entityData.set(MANA, mana);
        this.mana = mana;
    }

    public void setMaxMana(int mana) {
        this.entityData.set(MAX_MANA, mana);
        this.maxMana = mana;
    }

    public int getMaxMana() {
        return this.level().isClientSide() ? this.entityData.get(MAX_MANA) : this.maxMana;
    }

    public int getMana() {
        return this.level().isClientSide() ? this.entityData.get(MANA) : this.mana;
    }

    private static int createAnimationID() {
        return animationId++;
    }

    public static AttributeSupplier createAttributes() {
        return createEntityAttributes()
                .add(Attributes.MAX_HEALTH, 250.0D)
                .add(Attributes.ARMOR, 32.0D)
                .add(BHAttributes.DAMAGE_TAKEN.get(), -0.20D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2555F)
                .add(Attributes.ATTACK_DAMAGE, 16.0D)
                .add(Attributes.FOLLOW_RANGE, 70.0F)
                .build();
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 16.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(0, new NaturalHealingGoal(this));
        this.goalSelector.addGoal(1, new MobMoveGoal(this, false, 1.0F));

        this.goalSelector.addGoal(1, new MobAttackGoal<>(this, ID_ANIMATION_EMPTY, ID_DRACONIC_FIRELORD, ID_TRANSITION_STANCE_MELEE, 30, Maths.sec(10)) {
            @Override
            public boolean canUse() {
                return super.canUse() && this.entity.isUltForRangedReady();
            }
            @Override
            public void stop() {
                super.stop();
                this.entity.setMana(0);
                this.entity.setMode(Mode.MELEE);
            }
        });
        this.goalSelector.addGoal(1, new MobAttackGoal<>(this, ID_ANIMATION_EMPTY, ID_BURNING_POINT, ID_TRANSITION_STANCE_RANGED, 30, Maths.sec(5)) {
            @Override
            public boolean canUse() {
                return super.canUse() && this.entity.isUltForMeleeReady();
            }

            @Override
            public void stop() {
                super.stop();
                this.entity.setMana(0);
                this.entity.setMode(Mode.RANGED);
            }
        });
        this.goalSelector.addGoal(1, new MobAttackGoal<>(this, ID_ANIMATION_EMPTY, ID_DODGE, ID_ANIMATION_EMPTY, 30, Maths.sec(5)) {
            @Override
            public boolean canUse() {
                if (this.entity.isUltCanBeCast()) {
                    return false;
                }
                List<LivingEntity> nearby = this.entity.getEntitiesNearby(LivingEntity.class, 4.0D);
                LivingEntity target = this.entity.getTarget();
                return super.canUse() && target != null && target.isAlive() && this.entity.distanceTo(target) <= 4 && this.entity.dodgeCooldown.isReadyToUse();
            }

            @Override
            public void stop() {
                super.stop();
                this.entity.dodgeCooldown.setCooldown();
            }
        });
        this.goalSelector.addGoal(1, new MobAttackGoal<>(this, ID_ANIMATION_EMPTY, ID_BURNING_HEX_TRAP, ID_ANIMATION_EMPTY, 30, Maths.sec(5)) {
            @Override
            public boolean canUse() {
                if (this.entity.isUltCanBeCast() || this.entity.isMelee()) {
                    return false;
                }
                return super.canUse() && !this.entity.isUltCanBeCast() && this.entity.burningHexTrapCooldown.isReadyToUse();
            }

            @Override
            public void stop() {
                super.stop();
                this.entity.burningHexTrapCooldown.setCooldown();
            }
        });
        this.goalSelector.addGoal(1, new MobAttackGoal<>(this, ID_ANIMATION_EMPTY, ID_PYROBOLT1, ID_ANIMATION_EMPTY, 20, Maths.sec(5)) {
            @Override
            public boolean canUse() {
                if (this.entity.isUltCanBeCast() || this.entity.isMelee()) {
                    return false;
                }
                return super.canUse() && this.entity.pyroboltCooldown.isReadyToUse();
            }

            @Override
            public void stop() {
                super.stop();
                this.entity.pyroboltCooldown.setCooldown();
            }
        });
        this.goalSelector.addGoal(1, new MobAttackGoal<>(this, ID_ANIMATION_EMPTY, ID_PYROLANCE, ID_ANIMATION_EMPTY, 30, Maths.sec(5)) {
            @Override
            public boolean canUse() {
                if (this.entity.isUltCanBeCast() || this.entity.isMelee()) {
                    return false;
                }
                return super.canUse() && this.entity.pyrolanceCooldown.isReadyToUse();
            }

            @Override
            public void stop() {
                super.stop();
                this.entity.pyrolanceCooldown.setCooldown();
            }
        });
        // Melee attacks
        this.goalSelector.addGoal(1, new MobAttackGoal<>(this, ID_ANIMATION_EMPTY, ID_ATTACK_1, ID_ANIMATION_EMPTY, 30, Maths.sec(2)) {
            @Override
            public boolean canUse() {
                if (this.entity.isUltCanBeCast() || this.entity.isRanged()) {
                    return false;
                }
                return super.canUse() && this.entity.attack1Cooldown.isReadyToUse();
            }

            @Override
            public void start() {
                this.entity.setUsingAttack1(true);
                super.start();
            }

            @Override
            public void stop() {
                super.stop();
                if (this.entity.getAttackCount() >= 3) {
                    this.entity.attack1Cooldown.setCooldown();
                    this.entity.setAttackCount(0);
                    this.entity.setUsingAttack1(false);
                }
            }
        });

        this.goalSelector.addGoal(1, new MobAttackGoal<>(this, ID_ANIMATION_EMPTY, ID_ATTACK_2, ID_ANIMATION_EMPTY, 30, Maths.sec(2)) {
            @Override
            public boolean canUse() {
                if (this.entity.isUltCanBeCast() && this.entity.isRanged()) {
                    return false;
                }
                return super.canUse() && this.entity.attack2Cooldown.isReadyToUse();
            }

            @Override
            public void stop() {
                super.stop();
                this.entity.attack2Cooldown.setCooldown();
            }
        });
        this.targetSelector.addGoal(1, new HurtByNearestTargetGoal(this));
        this.goalSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.goalSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractGolem.class, true));
    }

    public boolean isMelee() {
        return this.getMode() == Mode.MELEE;
    }

    public boolean isRanged() {
        return this.getMode() == Mode.RANGED;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getMode() == Mode.RANGED && this.tickCount % 20 == 0) {
            this.addMana(1);
        }

        if (this.getAnimationState(ID_TRANSITION_STANCE_MELEE)) {
            if (this.getAnimationTick() == 30) {
                this.setVisibleSword(true);
                this.setAnimation(ID_ANIMATION_EMPTY);
            }
        }
        if (this.getAnimationState(ID_TRANSITION_STANCE_RANGED)) {
            if (this.getAnimationTick() == 30) {
                this.setVisibleSword(false);
                this.setAnimation(ID_ANIMATION_EMPTY);
            }
        }
        float progressMana = (float) this.getMana() / this.getMaxMana();
        this.idle1Cooldown.cooldownTick();
        this.idle2Cooldown.cooldownTick();
        this.attack1Cooldown.cooldownTick();
        this.attack2Cooldown.cooldownTick();
        this.attack3Cooldown.cooldownTick();
        this.abilityMana.setProgress(progressMana);
        this.pyroboltCooldown.cooldownTick();
        this.burningHexTrapCooldown.cooldownTick();
        this.dodgeCooldown.cooldownTick();
        this.pyrolanceCooldown.cooldownTick();
    }

    public boolean isUltForRangedReady() {
        return this.isRanged() && this.isUltCanBeCast();
    }

    public boolean isUltForMeleeReady() {
        return this.isMelee() && this.isUltCanBeCast();
    }

    public boolean isUltCanBeCast() {
        return this.getMana() >= this.getMaxMana();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        LivingEntity target = this.getTarget();
        if (this.level().isClientSide()) {
            int flameCount = 2;
            for (int i = 0; i < flameCount; ++i) {
                this.level().addParticle(ParticleTypes.FLAME, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), 0.0D, 0.0D, 0.0D);

            }
            if (this.getAnimationState(ID_PYROBOLT1)) {
                if (this.getAnimationTick() == 1) {
                    int particleCount = 64;
                    while (particleCount --> 0) {
                        double radius = 5.0F;
                        float yaw = (float) (this.random.nextFloat() * 2 * Math.PI);
                        float pitch = (float) (this.random.nextFloat() * 2 * Math.PI);
                        double ox = (float) (radius * Math.sin(yaw) * Math.sin(pitch));
                        double oy = (float) (radius * Math.cos(pitch));
                        double oz = (float) (radius * Math.cos(yaw) * Math.sin(pitch));
                        TrailParticleOptions.add(this.level(), TrailParticles.Behavior.SHRINK, this.getX() + ox, this.getY() + oy + 0.1, this.getZ() + oz, 4.0F, 1, 0.0F, 0.0F, 1.0F, 10, new Vec3(this.getX(), this.getY() + this.getBbHeight() / 2 + 0.5F, this.getZ()));
                    }
                }
                if (this.getAnimationTick() == 60) {
                    float r = Colors.getFARGB(0xFF0000)[0];
                    float g = Colors.getFARGB(0xFF0000)[1];
                    float b = Colors.getFARGB(0xFF0000)[2];
                    double x = this.getX();
                    double y = this.getY() + this.getBbHeight() / 2;
                    double z = this.getZ();
                    float yaw = (float) Math.toRadians(-this.getYRot());
                    float yaw2 = (float) Math.toRadians(-this.getYRot() + 180);
                    float pitch = (float) Math.toRadians(-this.getXRot());
                    this.level().addAlwaysVisibleParticle(new RingParticleOptions(yaw, pitch, 40, r, g, b, 1.0F, 50F, false, RingParticles.Behavior.GROW), x, y, z, 0, 0, 0);
                    this.level().addAlwaysVisibleParticle(new RingParticleOptions(yaw2, pitch, 40, r, g, b, 1.0F, 50F, false, RingParticles.Behavior.GROW), x, y, z, 0, 0, 0);
                }
            }
        } else {
            if (this.getAnimationState(ID_DODGE)) {
                if (this.getMode() == Mode.RANGED) {
                    getNavigation().stop();
                    if (this.getAnimationTick() == 2) {
                        dodgeYaw = (float) Math.toRadians(targetAngle + 90 + random.nextFloat() * 150 - 75);
                    }
                    if (this.getAnimationTick() == 6 && (onGround() || isInLava() || isInWater())) {
                        float speed = 1.7f;
                        Vec3 m = getDeltaMovement().add(speed * Math.cos(dodgeYaw), 0, speed * Math.sin(dodgeYaw));
                        setDeltaMovement(m.x, 0.6, m.z);
                    }
                    if (target != null) lookControl.setLookAt(target, 30, 30);
                }

            }
            if (this.getAnimationState(ID_PYROBOLT1)) {
                if (this.getAnimationTick() <= 60 && target != null) {
                    this.getLookControl().setLookAt(target, 30, 30);
                }
                if (this.getAnimationTick() > 60) {
                    this.performRangedAttack(20, 20, 60, target);
                }
            }
            if (this.getAnimationState(ID_BURNING_HEX_TRAP)) {
                if (this.getAnimationTick() <= 60 && target != null) {
                    this.getLookControl().setLookAt(target, 30, 30);
                }
                if (this.getAnimationTick() == Maths.sec(4)) {
                    this.createLinearHexTrap(10);
                }
            }
            if (this.getAnimationState(ID_DRACONIC_FIRELORD)) {
                int start = 60;
                if (this.getAnimationTick() == start) {
                    BeyondHorizon.PROXY.playSound(new DeathRayChargingSound(this, BHSounds.BLAZING_INFERNO_DEATH_RAY.get()));
                    float radius = 0.80F;
                    int duration = 80;
                    BlazingInfernoRayAbility ability = new BlazingInfernoRayAbility(this.level(), this,
                            this.getX() + radius * Math.sin(-this.getYRot() * Math.PI / 180),
                            this.getY() + 1.4, this.getZ() + radius * Math.cos(-this.getYRot() * Math.PI / 180),
                            (float) ((this.yHeadRot + 90) * Math.PI / 180), (float) (-this.getXRot() * Math.PI / 180), duration);
                    ability.damageConfig(AbstractDeathRayAbility.BeamDamageTags.MISSING_HEALTH, 1.0F);
                    ability.setCanBurnTarget(true);
                    ability.scaleCurrentHealthDamage(0.2F);
                    ability.setImmunityFrameIgnore(true);
                    this.level().addFreshEntity(ability);
                }
                if (this.getAnimationTick() >= start) {
                    if (target != null) {
                        this.getLookControl().setLookAt(target.getX(),target.getY() + target.getBbHeight() / 2, target.getZ(), 2.0F, 45.0F);
                    }
                }
            }


            if (this.getAnimationState(ID_ATTACK_1)) {
                if (this.getAnimationTick() <= 40 && target != null) {
                    this.getLookControl().setLookAt(target, 30, 30);
                }
                if (this.getAnimationTick() < 20) {
                    this.navigation.stop();
                    this.setCantMoved();
                }
                if (this.getAnimationTick() == 20) {
                    this.doJumpTarget(1.352F, 0.025D);
                }
                if (this.getAnimationTick() == 30) {
                    this.setAttackCount(this.getAttackCount() + 1);
                }
                if (this.getAnimationTick() > 30) {
                    if (target == null) return;
                    this.checkAndDealDamage(target, 1.0F, 1.0F, DamageType.PHYSICAL_DAMAGE);
                }
            }


            if (this.getAnimationState(ID_ATTACK_2)) {
                if (this.getAnimationTick() <= 40 && target != null) {
                    this.getLookControl().setLookAt(target, 30, 30);
                }
                if (this.getAnimationTick() == 30) {
                    this.navigation.stop();
                    this.setCantMoved();
                    this.doAreaAttack(4.0F, 180.0F, 1.25F, Maths.sec(5), 0.0F, DamageType.PHYSICAL_DAMAGE);
                }
            }
        }
    }

    @Override
    public void onStartAnimation() {
        LivingEntity target = this.getTarget();
        if (this.getAnimationState(ID_PYROLANCE)) {
            int count = 20;
            for (int i = 0; i < count; i++) {
                if (i >= (count / 2)) {
                    this.shootLance(target, new Vec3(-i, 1, 0), Maths.sec(2));
                } else {
                    this.shootLance(target, new Vec3(i, 1, 0), Maths.sec(2));
                }
            }
        }
    }

    private void performRangedAttack(int count, int initialFireRate, int tickStartAt, LivingEntity target) {
        if (this.getAnimationTick() > tickStartAt && this.getAnimationTick() <= tickStartAt + (initialFireRate)) {
            for (int i = 0; i < count; i++) {
                if (this.getAnimationTick() % (initialFireRate + i) == 0) {
                    this.doRoarParticle(this.getX(), this.getEyeY(), this.getZ(), 10, 255, 0, 0, 1.0F, 1.0F, 5.0F, 0.1F);
                    if (!this.isSilent()) {
                        this.level().playSound((Player) null, this, BHSounds.FAYE_FLARES_SHOOT.get(), SoundSource.HOSTILE, 3.0F, 1.0F);
                    }
                    this.shoot(target);
                }
            }
        }
    }

    private void shoot(LivingEntity target) {
        Vec3 rotation = this.getLookAngle().normalize();
        var pos = this.position().add(rotation.scale(1.6));
        double dx = (pos.x) - this.getX();
        double dz = (pos.z) - this.getZ();
        Pyrobolt projectile = new Pyrobolt(this.level(), DamageType.PHYSICAL_DAMAGE, this, this.getAttackDamage(0.20F),
                this.getRandom().triangle(dx,dx  * (this.getRandom().nextFloat() * 4.12F)), 0, this.getRandom().triangle(dz,dz  * (this.getRandom().nextFloat() * 4.12F)), false);
        double spawnX = projectile.getX();
        double spawnY = this.getY(0.5D) + 0.5D;
        double spawnZ = projectile.getZ();
        projectile.postEffectDamage = proj -> {
            var entity = proj.getOwner();
            if (entity instanceof LivingEntity living && living == this) {
                if (this.getMode() == Mode.RANGED) {
                    this.addMana(1);
                }
            }
        };
        projectile.setPos(spawnX, spawnY, spawnZ);
        this.level().addFreshEntity(projectile);

    }
    private void createCircularHexTrap(int count) {
        for (int i = 0; i < count; i++) {
            float angle = i * Mth.PI / (count / 2);
            for (int k = 0; k < 8; ++k) {
                double d2 = 1.15D * (double) (k + 1);
                this.createHexTrap(this.getX() + (double) Mth.cos(angle) * 1.25D * d2, this.getZ() + (double) Mth.sin(angle) * 1.25D * d2, this.getY(), this.getY() + 2);
            }
        }
    }

    private void createLinearHexTrap(int count) {
        Vec3 rotation = this.getLookAngle().normalize();
        var pos = this.position().add(rotation.scale(1.6));
        double d0 = Math.min(pos.y(), this.getY());
        double d1 = Math.min(pos.y(), this.getY()) + 1;
        float f = (float) Mth.atan2(pos.z() - this.getZ(), pos.x() - this.getX());
        for (int i = 0; i < count; ++i) {
            double d2 = 1.25 * (i + 1);
            this.createHexTrap(this.getX() + Mth.cos(f) * d2, this.getZ() + Mth.sin(f) * d2, d0, d1);
        }
    }

    private void createHexTrap(double x, double y, double minY, double maxY) {
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
            BurningHexTrapAbility.spawn(this.level(), blockpos.getX() + 0.5, (double) blockpos.getY() + d0, (double) blockpos.getZ() + 0.5, this.getAttackDamage(), this);
        }
    }
    private void shootLance(LivingEntity target, Vec3 position, int timer) {
        Vec3 rotation = this.getLookAngle().normalize();
        var pos = this.position().add(rotation.scale(1.6));
        double dx = (pos.x) - this.getX();
        double dz = (pos.z) - this.getZ();
        Pyrolance projectile = new Pyrolance(this.level(), DamageType.PHYSICAL_DAMAGE, this, this.getAttackDamage(0.20F),
                this.getRandom().triangle(dx,dx  * (this.getRandom().nextFloat() * 14.12F)), 0, this.getRandom().triangle(dz,dz  * (this.getRandom().nextFloat() * 14.12F)), false);
        projectile.setPos(this.getX() - (double) (this.getBbWidth() + 1.0F) * 0.15D * (double) Mth.sin(this.yBodyRot * ((float) Math.PI / 180F)), this.getY() + (double) 1F, this.getZ() + (double) (this.getBbWidth() + 1.0F) * 0.15D * (double) Mth.cos(this.yBodyRot * ((float) Math.PI / 180F)));
        projectile.setDelay(timer);
        projectile.setTarget(target);
        double spawnX = projectile.getX();
        double spawnY = this.getY(0.5D) + 0.5D;
        double spawnZ = projectile.getZ();
        projectile.setPos(spawnX, spawnY, spawnZ);
        this.level().addFreshEntity(projectile);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        if (ANIMATION_STATE.equals(accessor)) {
            if (this.getAnimationState(ID_ANIMATION_EMPTY)) {
                this.stopAnimations();
            }
            if (this.getAnimationState(ID_PYROBOLT1)) {
                this.stopAnimations();
                this.animationPyrobolt1.startIfStopped(this.tickCount);
            }
            if (this.getAnimationState(ID_PYROLANCE)) {
                this.stopAnimations();
                this.animationPyrolance.startIfStopped(this.tickCount);
            }
            if (this.getAnimationState(ID_BURNING_HEX_TRAP)) {
                this.stopAnimations();
                this.animationBurningHexTrap.startIfStopped(this.tickCount);
            }
            if (this.getAnimationState(ID_DRACONIC_FIRELORD)) {
                this.stopAnimations();
                this.animationRangedUlt.startIfStopped(this.tickCount);
            }
            if (this.getAnimationState(ID_BURNING_POINT)) {
                this.stopAnimations();
                this.animationMeleeUlt.startIfStopped(this.tickCount);
            }
            if (this.getAnimationState(ID_DODGE)) {
                this.stopAnimations();
                this.animationDodge.startIfStopped(this.tickCount);
            }
            if (this.getAnimationState(ID_TRANSITION_STANCE_RANGED)) {
                this.stopAnimations();
                this.animationStanceRanged.startIfStopped(this.tickCount);
            }
            if (this.getAnimationState(ID_TRANSITION_STANCE_MELEE)) {
                this.stopAnimations();
                this.animationStanceMelee.startIfStopped(this.tickCount);
            }
            if (this.getAnimationState(ID_ATTACK_1)) {
                this.stopAnimations();
                this.animationAtk1.startIfStopped(this.tickCount);
            }
            if (this.getAnimationState(ID_ATTACK_2)) {
                this.stopAnimations();
                this.animationAtk2.startIfStopped(this.tickCount);
            }
            if (this.getAnimationState(ID_ATTACK_3)) {
                this.stopAnimations();
                this.animationAtk3.startIfStopped(this.tickCount);
            }
            if (this.getAnimationState(ID_IDLE1)) {
                this.stopAnimations();
                this.animationIdle1.startIfStopped(this.tickCount);
            }
            if (this.getAnimationState(ID_IDLE2)) {
                this.stopAnimations();
                this.animationIdle2.startIfStopped(this.tickCount);
            }
        }
        super.onSyncedDataUpdated(accessor);
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.abilityMana.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.abilityMana.removePlayer(player);
    }

    @Override
    public AnimationState[] getAnimations() {
        return new AnimationState[] {
                this.animationIdle1,
                this.animationIdle2,
                this.animationAtk1,
                this.animationAtk2,
                this.animationAtk3,
                this.animationPyrobolt1,
                this.animationDodge,
                this.animationPyrolance,
                this.animationRangedUlt,
                this.animationMeleeUlt,
                this.animationStanceRanged,
                this.animationStanceMelee,
                this.animationBurningHexTrap
        };
    }
}
