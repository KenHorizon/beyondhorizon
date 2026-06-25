package com.kenhorizon.beyondhorizon.server.entity.boss.pyrolliger;

import com.kenhorizon.beyondhorizon.client.particle.RingParticles;
import com.kenhorizon.beyondhorizon.client.particle.TrailParticles;
import com.kenhorizon.beyondhorizon.client.particle.world.RingParticleOptions;
import com.kenhorizon.beyondhorizon.client.particle.world.TrailParticleOptions;
import com.kenhorizon.beyondhorizon.client.render.misc.tooltips.Tooltips;
import com.kenhorizon.beyondhorizon.client.render.util.ColorUtil;
import com.kenhorizon.beyondhorizon.server.entity.ability.BurningHexTrapAbility;
import com.kenhorizon.beyondhorizon.server.entity.ai.*;
import com.kenhorizon.beyondhorizon.server.entity.boss.BHBossEntity;
import com.kenhorizon.beyondhorizon.server.entity.projectiles.PyroLance;
import com.kenhorizon.beyondhorizon.server.entity.projectiles.Pyrobolt;
import com.kenhorizon.beyondhorizon.server.entity.util.AnimationTickers;
import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.init.BHParticle;
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
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
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

import java.util.Arrays;
import java.util.List;

public class Pyrolliger extends BHBossEntity {
    public enum Mode {
        MELEE,
        RANGED
    }
    private float dodgeYaw = 0;
    protected Pyrolliger.Mode mode = Mode.RANGED;
    public static int animationId = 1;
    public AnimationState animationPyrobolt = new AnimationState();
    public AnimationState animationPyrolance = new AnimationState();
    public AnimationState animationBurningHexTrap = new AnimationState();
    public AnimationState animationDodge = new AnimationState();
    public static final int ID_PYROBOLT = createAnimationID();
    public static final int ID_PYROLANCE = createAnimationID();
    public static final int ID_BURNING_HEX_TRAP = createAnimationID();
    public static final int ID_DODGE = createAnimationID();
    public AnimationTickers dodgeCooldown = AnimationTickers.create(Maths.sec(7));
    public AnimationTickers pyroboltCooldown = AnimationTickers.create(Maths.sec(3));
    public AnimationTickers pyrolanceCooldown = AnimationTickers.create(Maths.sec(3));
    public AnimationTickers burningHexTrapCooldown = AnimationTickers.create(Maths.sec(52));

    public static final String NBT_MANA = "mana";
    public static final String NBT_MAX_MANA = "max_mana";
    private static final EntityDataAccessor<Integer> MANA = SynchedEntityData.defineId(Pyrolliger.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> MAX_MANA = SynchedEntityData.defineId(Pyrolliger.class, EntityDataSerializers.INT);

    public Pyrolliger(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        this.setExp(500);
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
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(MANA, 0);
        this.entityData.define(MAX_MANA, 100);
    }
    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.setMana(nbt.getInt(NBT_MANA));
        this.setMaxMana(nbt.getInt(NBT_MAX_MANA));
    }
    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt(NBT_MANA, this.getMana());
        nbt.putInt(NBT_MAX_MANA, this.getMaxMana());
    }
    @Override
    public boolean hurt(DamageSource source, float amount) {
        return super.hurt(source, amount);
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
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
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
        }
        this.setMana(this.getMana() + mana);
    }

    public void setMana(int mana) {
        this.entityData.set(MANA, mana);
    }
    public void setMaxMana(int mana) {
        this.entityData.set(MAX_MANA, mana);
    }
    public int getMaxMana() {
        return this.entityData.get(MAX_MANA);
    }

    public int getMana() {
        return this.entityData.get(MANA);
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
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(0, new NaturalHealingGoal(this));
        this.goalSelector.addGoal(1, new MobMoveGoal(this, false, 1.0F));
        this.goalSelector.addGoal(1, new MobStateGoal<>(this, ID_ANIMATION_EMPTY, ID_ANIMATION_EMPTY, ID_ANIMATION_EMPTY, 0, 0));
//        this.goalSelector.addGoal(1, new MobAttackGoal<>(this, ID_ANIMATION_EMPTY, ID_PYROBOLT, ID_ANIMATION_EMPTY, 20, Maths.sec(5)) {
//            @Override
//            public boolean canUse() {
//                return super.canUse() && this.entity.pyroboltCooldown.isReadyToUse();
//            }
//
//            @Override
//            public void stop() {
//                super.stop();
//                this.entity.pyroboltCooldown.setCooldown();
//            }
//        });
//        this.goalSelector.addGoal(2, new MobAttackGoal<>(this, ID_ANIMATION_EMPTY, ID_DODGE, ID_ANIMATION_EMPTY, 30, Maths.sec(5)) {
//            @Override
//            public boolean canUse() {
//                List<LivingEntity> nearby = this.entity.getEntitiesNearby(LivingEntity.class, 4.0D);
//                LivingEntity target = this.entity.getTarget();
//                return super.canUse() && target != null && target.isAlive() && this.entity.distanceTo(target) <= 4 && this.entity.dodgeCooldown.isReadyToUse();
//            }
//
//            @Override
//            public void stop() {
//                super.stop();
//                this.entity.dodgeCooldown.setCooldown();
//            }
//        });
//        this.goalSelector.addGoal(1, new MobAttackGoal<>(this, ID_ANIMATION_EMPTY, ID_BURNING_HEX_TRAP, ID_ANIMATION_EMPTY, 30, Maths.sec(5)) {
//            @Override
//            public boolean canUse() {
//                return super.canUse() && this.entity.burningHexTrapCooldown.isReadyToUse();
//            }
//
//            @Override
//            public void stop() {
//                super.stop();
//                this.entity.burningHexTrapCooldown.setCooldown();
//            }
//        });
        this.goalSelector.addGoal(1, new MobAttackGoal<>(this, ID_ANIMATION_EMPTY, ID_PYROLANCE, ID_ANIMATION_EMPTY, 30, Maths.sec(5)) {
            @Override
            public boolean canUse() {
                return super.canUse() && this.entity.pyrolanceCooldown.isReadyToUse();
            }

            @Override
            public void stop() {
                super.stop();
                this.entity.pyrolanceCooldown.setCooldown();
            }
        });
        this.targetSelector.addGoal(1, new HurtByNearestTargetGoal(this));
        this.goalSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.goalSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractGolem.class, true));
    }

    @Override
    public void tick() {
        super.tick();
        this.pyroboltCooldown.cooldownTick();
        this.burningHexTrapCooldown.cooldownTick();
        this.dodgeCooldown.cooldownTick();
        this.pyrolanceCooldown.cooldownTick();
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
            if (this.getAnimationState(ID_PYROBOLT)) {
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
                    float r = ColorUtil.getFARGB(0xFF0000)[0];
                    float g = ColorUtil.getFARGB(0xFF0000)[1];
                    float b = ColorUtil.getFARGB(0xFF0000)[2];
                    double x = this.getX();
                    double y = this.getY() + this.getBbHeight() / 2;
                    double z = this.getZ();
                    float yaw = (float) Math.toRadians(-this.getYRot());
                    float yaw2 = (float) Math.toRadians(-this.getYRot() + 180);
                    float pitch = (float) Math.toRadians(-this.getXRot());
                    this.level().addAlwaysVisibleParticle(BHParticle.HELLFIRE_ORB_EXPLOSION.get(), x, y, z, 0, 0, 0);
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
            if (this.getAnimationState(ID_PYROBOLT)) {
                if (this.getAnimationTick() <= 60 && target != null) {
                    this.getLookControl().setLookAt(target, 30, 30);
                }
                this.performRangedAttack(20, 20, 60, target);
            }

            if (this.getAnimationState(ID_BURNING_HEX_TRAP)) {
                if (this.getAnimationTick() <= 60 && target != null) {
                    this.getLookControl().setLookAt(target, 30, 30);
                }
                if (this.getAnimationTick() == Maths.sec(4)) {
                    int count = 10;
                    for (int i = 0; i < count; i++) {
                        float angle = i * Mth.PI / (count / 2);
                        for (int k = 0; k < count; ++k) {
                            double d2 = 1.15D * (double) (k + 1);
                            double x = (this.getX() + (this.getRandom().nextInt(10))) + (double) Mth.cos(angle) * 1.25D * d2;
                            double z = (this.getZ() + (this.getRandom().nextInt(10))) + (double) Mth.sin(angle) * 1.25D * d2;
                            this.createHexTrap(x, z, this.getY(), this.getY() + 2);
                        }
                    }
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

    public void doJumpTarget(LivingEntity target, double distance, double y) {
        this.getNavigation().stop();
        double posX = target == null ? 0 : (target.getX() - this.getX()) * distance;
        double posY = y;
        double posZ = target == null ? 0 : (target.getZ() - this.getZ()) * distance;
        this.setDeltaMovement(posX, posY, posZ);
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
        projectile.setPos(spawnX, spawnY, spawnZ);
        this.level().addFreshEntity(projectile);

    }
    private void createHexTrap(int count) {
        for (int i = 0; i < count; i++) {
            float angle = i * Mth.PI / (count / 2);
            for (int k = 0; k < 8; ++k) {
                double d2 = 1.15D * (double) (k + 1);
                this.createHexTrap(this.getX() + (double) Mth.cos(angle) * 1.25D * d2, this.getZ() + (double) Mth.sin(angle) * 1.25D * d2, this.getY(), this.getY() + 2);
            }
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
        PyroLance projectile = new PyroLance(this.level(), this);
        position = position.yRot(-this.getYRot() * ((float) Math.PI / 180F));
        projectile.setDamageType(DamageType.PHYSICAL_DAMAGE);
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
    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        if (ANIMATION_STATE.equals(accessor)) {
            if (this.getAnimation() == ID_ANIMATION_EMPTY) {
                this.stopAnimations();
            }
            if (this.getAnimation() == ID_PYROBOLT) {
                this.stopAnimations();
                this.animationPyrobolt.startIfStopped(this.tickCount);
            }
            if (this.getAnimation() == ID_PYROLANCE) {
                this.stopAnimations();
                this.animationPyrolance.startIfStopped(this.tickCount);
            }
            if (this.getAnimation() == ID_BURNING_HEX_TRAP) {
                this.stopAnimations();
                this.animationBurningHexTrap.startIfStopped(this.tickCount);
            }
            if (this.getAnimation() == ID_DODGE) {
                this.stopAnimations();
                this.animationDodge.startIfStopped(this.tickCount);
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
                this.animationPyrobolt,
                this.animationDodge,
                this.animationPyrolance,
                this.animationBurningHexTrap
        };
    }
}
