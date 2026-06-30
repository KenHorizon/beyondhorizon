package com.kenhorizon.beyondhorizon.server.entity.mobs;

import com.kenhorizon.beyondhorizon.client.particle.RingParticles;
import com.kenhorizon.beyondhorizon.client.particle.TrailParticles;
import com.kenhorizon.beyondhorizon.client.particle.world.TrailParticleOptions;
import com.kenhorizon.beyondhorizon.client.particle.world.RingParticleOptions;
import com.kenhorizon.beyondhorizon.client.render.util.ColorUtil;
import com.kenhorizon.beyondhorizon.server.entity.BHLibEntity;
import com.kenhorizon.beyondhorizon.server.entity.CameraShake;
import com.kenhorizon.beyondhorizon.server.entity.ability.AbstractDeathRayAbility;
import com.kenhorizon.beyondhorizon.server.entity.ability.InfernalRayAbility;
import com.kenhorizon.beyondhorizon.server.entity.ai.HurtByNearestTargetGoal;
import com.kenhorizon.beyondhorizon.server.entity.ai.MobAttackGoal;
import com.kenhorizon.beyondhorizon.server.entity.ai.MobMoveGoal;
import com.kenhorizon.beyondhorizon.server.entity.ai.control.FlightMoveControl;
import com.kenhorizon.beyondhorizon.server.entity.projectiles.BlazingRod;
import com.kenhorizon.beyondhorizon.server.entity.util.AnimationTickers;
import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.init.BHParticle;
import com.kenhorizon.beyondhorizon.server.init.BHSounds;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageScaling;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class FayeWildfire extends BHLibEntity implements FlyingAnimal {
    public AnimationState animationBlazingRod = new AnimationState();
    public AnimationState animationPrepDeathRay = new AnimationState();
    public AnimationState animationDeathRay = new AnimationState();
    public static int animationId = 1;
    public static final int ID_BLAZING_ROD = createAnimationID();
    public static final int ID_PREPARE_DEATH_RAY = createAnimationID();
    public static final int ID_DEATH_RAY = createAnimationID();
    public AnimationTickers fireballCooldown = AnimationTickers.create(Maths.sec(3));
    public AnimationTickers deathRayCooldown = AnimationTickers.create(Maths.sec(20));

    public FayeWildfire(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        this.setExp(10);
        this.moveControl = new FlightMoveControl(this, 1.21F);
        this.setMaxUpStep(2.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, -16.0F);
        this.setPathfindingMalus(BlockPathTypes.LAVA, -16.0F);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -16.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, -16.0F);
        this.refreshDimensions();
    }

    private static int createAnimationID() {
        return animationId++;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypes.FALL)) {
            return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    @Override
    public boolean onClimbable() {
        return false;
    }

    @Override
    public boolean isFlying() {
        return !this.onGround();
    }

    public static AttributeSupplier createAttributes() {
        return createEntityAttributes()
                .add(Attributes.MAX_HEALTH, 60.0D)
                .add(Attributes.ARMOR, 12.0D)
                .add(BHAttributes.DAMAGE_TAKEN.get(), -0.05D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3F)
                .add(Attributes.ATTACK_DAMAGE, 15.0F)
                .add(Attributes.ATTACK_SPEED, 1.0F)
                .add(Attributes.FOLLOW_RANGE, 24.0F)
                .add(Attributes.FLYING_SPEED, 0.45F)
                .build();
    }
    @Override
    public boolean isAlliedTo(Entity entity) {
        if (entity == null) {
            return false;
        } else if (entity == this) {
            return true;
        } else if (super.isAlliedTo(entity)) {
            return true;
        } else if (entity instanceof FayeWildfire) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0D, 80));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(1, new MobMoveGoal(this, false, 1.0F));
        this.targetSelector.addGoal(1, new HurtByNearestTargetGoal(this));
        this.goalSelector.addGoal(1, new MobAttackGoal<>(this, ID_ANIMATION_EMPTY, ID_BLAZING_ROD, ID_ANIMATION_EMPTY, 30, Maths.sec(3)) {
            @Override
            public boolean canUse() {
                return super.canUse() && this.entity.fireballCooldown.isReadyToUse();
            }

            @Override
            public void stop() {
                super.stop();
                this.entity.fireballCooldown.setCooldown();
            }
        });
        this.goalSelector.addGoal(1, new MobAttackGoal<>(this, ID_ANIMATION_EMPTY, ID_PREPARE_DEATH_RAY, ID_DEATH_RAY, 60, Maths.sec(5)) {
            @Override
            public boolean canUse() {
                return super.canUse() && this.entity.deathRayCooldown.isReadyToUse();
            }
        });
        this.goalSelector.addGoal(1, new MobAttackGoal<>(this, ID_DEATH_RAY, ID_DEATH_RAY, ID_ANIMATION_EMPTY, 30, Maths.sec(5)) {

            @Override
            public void stop() {
                super.stop();
                this.entity.deathRayCooldown.setCooldown();
            }

            @Override
            public void tick() {
                super.tick();
                LivingEntity target = this.entity.getTarget();
                super.tick();
                if (this.entity.getAnimationTick() == 2) {
                    float radius = 0.80F;
                    int duration = Maths.sec(3);
                    InfernalRayAbility ability = new InfernalRayAbility(this.entity.level(), this.entity,
                            this.entity.getX() + radius * Math.sin(-this.entity.getYRot() * Math.PI / 180),
                            this.entity.getY() + 1.4, this.entity.getZ() + radius * Math.cos(-this.entity.getYRot() * Math.PI / 180),
                            (float) ((this.entity.yHeadRot + 90) * Math.PI / 180), (float) (-this.entity.getXRot() * Math.PI / 180), duration);
                    ability.damageConfig(AbstractDeathRayAbility.BeamDamageTags.DEFAULT, 1.0F);
                    ability.setCanBurnTarget(true);
                    ability.setImmunityFrameIgnore(true);
                    this.entity.level().addFreshEntity(ability);
                }
                if (this.entity.getAnimationTick() >= 2) {
                    if (target != null) {
                        this.entity.getLookControl().setLookAt(target.getX(),target.getY() + target.getBbHeight() / 2, target.getZ(), 1.0F, 25.0F);
                    }
                }
            }
        });

        this.goalSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.goalSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractGolem.class, true));
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation flyingPathNavigation = new FlyingPathNavigation(this, level) {
            @Override
            public boolean isStableDestination(BlockPos blockPos) {
                return !this.level.getBlockState(blockPos.below(2)).isAir();
            }
        };
        flyingPathNavigation.setCanOpenDoors(false);
        flyingPathNavigation.setCanFloat(true);
        flyingPathNavigation.setCanPassDoors(true);
        return flyingPathNavigation;
    }


    @Override
    protected AABB makeBoundingBox() {
        return super.makeBoundingBox().move(0 ,0.5D, 0);
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState blockState, BlockPos blockPos) {

    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return super.getAmbientSound();
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource damageSource) {
        return super.getHurtSound(damageSource);
    }

    @Override
    protected void playMuffledStepSound(BlockState pState, BlockPos pos) {
        super.playMuffledStepSound(pState, pos);
    }

    @Override
    protected void playHurtSound(DamageSource source) {
        this.playSound(BHSounds.FAYE_WILDFIRE_HURT.get());
    }

    @Override
    protected SoundEvent getDeathSound() {
        return BHSounds.FAYE_WILDFIRE_DEATH.get();
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        if (ANIMATION_STATE.equals(accessor)) {
            if (this.getAnimation() == ID_ANIMATION_EMPTY) {
                this.stopAnimations();
            }
            if (this.getAnimation() == ID_BLAZING_ROD) {
                this.stopAnimations();
                this.animationBlazingRod.startIfStopped(this.tickCount);
            }
            if (this.getAnimation() == ID_PREPARE_DEATH_RAY) {
                this.stopAnimations();
                this.animationPrepDeathRay.startIfStopped(this.tickCount);
            }
            if (this.getAnimation() == ID_DEATH_RAY) {
                this.stopAnimations();
                this.animationDeathRay.startIfStopped(this.tickCount);
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
                this.animationBlazingRod
        };
    }

    @Override
    public void tick() {
        super.tick();
        Vec3 vector3d = this.getDeltaMovement();
        boolean flag = this.getDeltaMovement().x * this.getDeltaMovement().x + this.getDeltaMovement().z * this.getDeltaMovement().z >= 1.0E-3D;
        LivingEntity target = this.getTarget();
        this.fireballCooldown.cooldownTick();
        this.deathRayCooldown.cooldownTick();
        if (!this.level().isClientSide()) {
            boolean flag1 = this.getAnimationState(ID_BLAZING_ROD) && this.getAnimationTick() <= 60;
            this.switchNavigator(flag1);
        }
        if (!this.onGround() && vector3d.y < 0.0D) {
            this.setDeltaMovement(vector3d.multiply(1.0D, 0.4D, 1.0D));
        }
        this.setNoGravity(true);
    }

    private void switchNavigator(boolean focusOnTarget) {
        this.moveControl = new FlightMoveControl(this, 0.7F, focusOnTarget);
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
            if (this.getAnimationState(ID_BLAZING_ROD)) {
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
                    this.level().addAlwaysVisibleParticle(new RingParticleOptions(yaw, pitch, 40, r, g, b, 1.0F, 50F, false, RingParticles.Behavior.GROW), x, y, z, 0, 0, 0);
                    this.level().addAlwaysVisibleParticle(new RingParticleOptions(yaw2, pitch, 40, r, g, b, 1.0F, 50F, false, RingParticles.Behavior.GROW), x, y, z, 0, 0, 0);
                }
            }
            if (this.getAnimationState(ID_PREPARE_DEATH_RAY)) {
                if (this.getAnimationTick() < Maths.sec(5)) {
                    if (this.getAnimationTick() % 20L == 0) {
                        int particleCount = 128;
                        while (particleCount --> 0) {
                            double radius = 6.0F;
                            float yaw = (float) (this.random.nextFloat() * 2 * Math.PI);
                            float pitch = (float) (this.random.nextFloat() * 2 * Math.PI);
                            double ox = (float) (radius * Math.sin(yaw) * Math.sin(pitch));
                            double oy = (float) (radius * Math.cos(pitch));
                            double oz = (float) (radius * Math.cos(yaw) * Math.sin(pitch));
                            TrailParticleOptions.add(this.level(), TrailParticles.Behavior.FADE_N_SHRINK, getX() + ox, getY() + oy + 0.1, getZ() + oz, 3.0F, 1, 0.0F, 0.0F, 1.0F, 20, new Vec3(this.getX(), this.getY() + this.getBbHeight() / 2 + 0.5F, this.getZ()));
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
        } else {

            if (this.getAnimationState(ID_BLAZING_ROD)) {
                if (this.getAnimationTick() <= 60 && target != null) {
                    this.getLookControl().setLookAt(target, 30, 30);
                }
                if (this.targetDistance < 4) {
                    this.getMoveControl().strafe(-1.50F, 1.25F);
                } else {
                    if (target != null) {
                        this.getNavigation().moveTo(target, 1.0D);
                    }
                }
                if (target != null) {
                    this.performRangedAttack(3, target, 10, 2.0F, 5.0F, 25);

                }
            }

        }
    }

    @Override
    public void onStartAnimation() {
        if (this.getAnimationState(ID_PREPARE_DEATH_RAY)) {
            if (!this.isSilent()) {
                this.level().playSound((Player) null, this, BHSounds.FAYE_WILDFIRE_DEATH_RAY_CHARGING.get(), SoundSource.HOSTILE, 3.0F, 1.0F);
            }
            float r = ColorUtil.getFARGB(0xFFFFFF)[0];
            float g = ColorUtil.getFARGB(0xFFFFFF)[1];
            float b = ColorUtil.getFARGB(0xFFFFFF)[2];
            this.level().addAlwaysVisibleParticle(new RingParticleOptions(0, (float) -Math.PI/ 2, 15, r, g, b, 1.0F, 64.0F, true, RingParticles.Behavior.SHRINK), this.getX(), this.getY() + this.getBbHeight() / 2 + 0.5F, this.getZ(), 0, 0, 0);

        }
        if (this.getAnimationState(ID_DEATH_RAY)) {
            if (!this.isSilent()) {
                this.level().playSound((Player) null, this, BHSounds.FAYE_WILDFIRE_DEATH_RAY.get(), SoundSource.HOSTILE, 3.0F, 1.0F);
            }
            float r = ColorUtil.getFARGB(0xFFFFFF)[0];
            float g = ColorUtil.getFARGB(0xFFFFFF)[1];
            float b = ColorUtil.getFARGB(0xFFFFFF)[2];
            this.level().addAlwaysVisibleParticle(new RingParticleOptions(0, (float) -Math.PI/ 2, 15, r, g, b, 1.0F, 64.0F, true, RingParticles.Behavior.SHRINK), this.getX(), this.getY() + this.getBbHeight() / 2 + 0.5F, this.getZ(), 0, 0, 0);

        }
    }

    private void performRangedAttack(int count, LivingEntity target, int initialFireRate, float velocity, float inaccuracy, int tickStartAt) {
        if (this.getAnimationTick() > tickStartAt && this.getAnimationTick() <= tickStartAt + (initialFireRate)) {
            for (int i = 0; i < count; i++) {
                if (this.getAnimationTick() % (initialFireRate - i) == 0) {
                    this.doRoarParticle(this.getX(), this.getEyeY(), this.getZ(), 10, 255, 0, 0, 1.0F, 1.0F, 5.0F, 0.1F);
                    if (!this.isSilent()) {
                        this.level().playSound((Player) null, this, BHSounds.FAYE_WILDFIRE_SHOOT.get(), SoundSource.HOSTILE, 3.0F, 1.0F);
                    }
                    this.shoot(1, target, velocity, inaccuracy, false);
                }
            }
        }
    }

    private void shoot(int count, LivingEntity target, float velocity, float inaccuracy, boolean spread) {
        double offsetangle = Math.toRadians(12);
        for (int i = 0; i < count; ++i) {
            double angle = spread ? (i - (count - 1) / 2.0F) * offsetangle : 0;
            double d0 = this.getX();
            double d1 = this.getY() + (this.getBbHeight() / 2) + 0.5D;
            double d2 = this.getZ();
            BlazingRod projectile = new BlazingRod(this.level(), d0, d1, d2, this);
            projectile.setBaseDamage(1);
            double shootX = target.getX() - this.getX();
            double shootY = target.getBoundingBox().minY + target.getBbHeight() / 2 - projectile.getY();
            double shootZ = target.getZ() - this.getZ();
            double x = shootX * Math.cos(angle) + shootZ * Math.sin(angle);
            double z = -shootX * Math.sin(angle) + shootZ * Math.cos(angle);
            projectile.shoot(x, shootY, z, velocity, inaccuracy);
            this.level().addFreshEntity(projectile);
        }
    }
}
