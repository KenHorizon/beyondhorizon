package com.kenhorizon.beyondhorizon.server.entity.mobs;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.particle.RingParticles;
import com.kenhorizon.beyondhorizon.client.particle.TrailParticles;
import com.kenhorizon.beyondhorizon.client.particle.world.ParticleTrailOptions;
import com.kenhorizon.beyondhorizon.client.particle.world.RingParticleOptions;
import com.kenhorizon.beyondhorizon.client.render.util.ColorUtil;
import com.kenhorizon.beyondhorizon.server.entity.BHLibEntity;
import com.kenhorizon.beyondhorizon.server.entity.ai.*;
import com.kenhorizon.beyondhorizon.server.entity.ai.control.FlightMoveControl;
import com.kenhorizon.beyondhorizon.server.entity.boss.blazing_inferno.InfernoShield;
import com.kenhorizon.beyondhorizon.server.entity.projectiles.BlazingRod;
import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.init.BHParticle;
import com.kenhorizon.beyondhorizon.server.init.BHSounds;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageTags;
import com.kenhorizon.beyondhorizon.server.util.MathUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
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

import java.util.Arrays;
import java.util.List;

public class FayeFlares extends BHLibEntity implements FlyingAnimal {
    public AnimationState animationBlazingRod = new AnimationState();
    public static int animationId = 1;
    public static final int ID_BLAZING_ROD = createAnimationID();
    public int fireballCooldown = 0;
    public static final int FIREBALL_COOLDOWN = MathUtils.sec(3);

    public FayeFlares(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        this.setExp(10);
        this.moveControl = new FlightMoveControl(this, 1.7F, true);
        this.setMaxUpStep(2.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.LAVA, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, 0.0F);
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
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.ARMOR, 2.0D)
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
        } else if (entity instanceof FayeFlares) {
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
        this.goalSelector.addGoal(1, new MobAttackGoal<FayeFlares>(this, ID_ANIMATION_EMPTY, ID_BLAZING_ROD, ID_ANIMATION_EMPTY, 30, MathUtils.sec(5)) {
            @Override
            public boolean canUse() {
                return super.canUse() && this.entity.fireballCooldown <= 0;
            }

            @Override
            public void stop() {
                super.stop();
                this.entity.fireballCooldown = FIREBALL_COOLDOWN;
            }
        });

        this.goalSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.goalSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractGolem.class, true));
    }

    @Override
    protected void playHurtSound(DamageSource source) {
        this.playSound(BHSounds.BLAZING_INFERNO_HURT.get());
    }

    @Override
    protected SoundEvent getDeathSound() {
        return BHSounds.BLAZING_INFERNO_DEATH.get();
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

    private void rotateTargets(Entity entity, boolean counterClockwise) {
        float rot = (counterClockwise ? 0.25F : -0.25F) + entity.tickCount * 3;
        Vec3 orbitBy = new Vec3(0.0D, 0.50D, 8.0D).yRot((float) -Math.toRadians(rot));
        Vec3 orbitTarget = entity.position().add(orbitBy).subtract(this.position());
        this.setXRot(10.0F);
        this.setDeltaMovement(orbitTarget.scale(0.25F));
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
        if (this.fireballCooldown > 0) this.fireballCooldown--;
        if (!this.onGround() && vector3d.y < 0.0D) {
            this.setDeltaMovement(vector3d.multiply(1.0D, 0.4D, 1.0D));
        }
        this.setNoGravity(true);
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
                        ParticleTrailOptions.add(this.level(), TrailParticles.Behavior.SHRINK, this.getX() + ox, this.getY() + oy + 0.1, this.getZ() + oz, 4.0F, 1, 0.0F, 0.0F, 1.0F, 10, new Vec3(this.getX(), this.getY() + this.getBbHeight() / 2 + 0.5F, this.getZ()));
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
                if (this.getAnimationTick() == 60) {
                    if (target != null) {
                        this.performRangedAttack(3, target, 10, 1.0F, 5.0F);
                        if (!this.isSilent()) {
                            this.playSound(BHSounds.BLAZING_INFERNO_SHOOT.get(), 1.0F, 1.0F);
                        }
                    }
                }
            }
        }
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
                this.shoot(1, target, velocity, inaccuracy, false);
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
            projectile.setDamage(DamageTags.DEFAULT, 0.02F);
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
