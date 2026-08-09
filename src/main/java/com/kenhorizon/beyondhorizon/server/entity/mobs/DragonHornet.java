package com.kenhorizon.beyondhorizon.server.entity.mobs;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.entity.BHLibEntity;
import com.kenhorizon.beyondhorizon.server.entity.ai.HurtByNearestTargetGoal;
import com.kenhorizon.beyondhorizon.server.entity.ai.MobAttackGoal;
import com.kenhorizon.beyondhorizon.server.entity.ai.MobMoveGoal;
import com.kenhorizon.beyondhorizon.server.entity.ai.control.FlightMoveControl;
import com.kenhorizon.beyondhorizon.server.entity.util.AnimationTickers;
import com.kenhorizon.beyondhorizon.server.init.BHSounds;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageType;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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

public class DragonHornet extends BHLibEntity implements FlyingAnimal {
    public AnimationState animationAttack = new AnimationState();
    public AnimationState animationDeath = new AnimationState();
    public static int animationId = 1;
    public static final int ID_ATTACK = createAnimationID();
    public static final int ID_DEATH = createAnimationID();
    public AnimationTickers attackCooldown = AnimationTickers.create(Maths.sec(3));

    public DragonHornet(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        this.setExp(10);
        this.moveControl = new FlightMoveControl(this, 1.21F, true);
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
                .add(Attributes.ARMOR, 2.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3F)
                .add(Attributes.ATTACK_DAMAGE, 8.0F)
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
        } else if (entity instanceof DragonHornet) {
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
        this.goalSelector.addGoal(1, new MobAttackGoal<>(this, ID_ANIMATION_EMPTY, ID_ATTACK, ID_ANIMATION_EMPTY, 20, Maths.sec(2)) {

            @Override
            public boolean canUse() {
                return super.canUse() && this.entity.attackCooldown.isReadyToUse();
            }

            @Override
            public void stop() {
                super.stop();
                this.entity.attackCooldown.setCooldown();
            }
        });

        this.goalSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.goalSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractGolem.class, true));
    }

    @Override
    protected void playHurtSound(DamageSource source) {
        this.playSound(BHSounds.FAYE_FLARES_HURT.get());
    }

    @Override
    protected SoundEvent getDeathSound() {
        return BHSounds.FAYE_FLARES_DEATH.get();
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
    protected @Nullable SoundEvent getAmbientSound() {
        return BHSounds.FAYE_FLARES_IDLE.get();
    }

    @Override
    protected AABB makeBoundingBox() {
        return super.makeBoundingBox().move(0 ,-0.5D, 0);
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
    public int getAnimationDeath() {
        return ID_DEATH;
    }

    @Override
    protected int getDeathDuration() {
        return 40;
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        if (ANIMATION_STATE.equals(accessor)) {
            if (this.getAnimationState(ID_ANIMATION_EMPTY)) {
                this.stopAnimations();
            }
            if (this.getAnimationState(ID_ATTACK)) {
                this.stopAnimations();
                this.animationAttack.startIfStopped(this.tickCount);
            }
            if (this.getAnimationState(ID_DEATH)) {
                this.stopAnimations();
                this.animationDeath.startIfStopped(this.tickCount);
            }
        }
        super.onSyncedDataUpdated(accessor);
    }

    @Override
    public AnimationState[] getAnimations() {
        return new AnimationState[] {
                this.animationAttack,
                this.animationDeath
        };
    }

    @Override
    public void tick() {
        super.tick();
        Vec3 vector3d = this.getDeltaMovement();
        boolean flag = this.getDeltaMovement().x * this.getDeltaMovement().x + this.getDeltaMovement().z * this.getDeltaMovement().z >= 1.0E-3D;
        LivingEntity target = this.getTarget();

        this.attackCooldown.cooldownTick();
        if (!this.level().isClientSide()) {
            boolean flag1 = this.getAnimationState(ID_ATTACK) && this.getAnimationTick() <= 20;
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

        } else {
            if (this.getAnimationState(ID_ATTACK)) {
                if (this.getAnimationTick() <= 40 && target != null) {
                    this.getLookControl().setLookAt(target, 30, 30);
                }
                if (this.getAnimationTick() > 20) {
                    this.navigation.stop();
                }
                if (this.getAnimationTick() == 36 && target != null) {
                    this.doJumpTarget(1.110F, 0.0F);
                    boolean flag = this.checkAndDealDamage(target, 1.0F, 2.0F, DamageType.PHYSICAL_DAMAGE);
                    if (flag) {
                        target.addEffect(new MobEffectInstance(MobEffects.POISON, Maths.sec(3)));
                    }
                }
            }
        }
    }
}
