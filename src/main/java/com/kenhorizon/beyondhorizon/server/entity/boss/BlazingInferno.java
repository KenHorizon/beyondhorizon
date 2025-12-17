package com.kenhorizon.beyondhorizon.server.entity.boss;

import com.kenhorizon.beyondhorizon.server.entity.BHBossEntity;
import com.kenhorizon.beyondhorizon.server.entity.BHLibEntity;
import com.kenhorizon.beyondhorizon.server.entity.goal.NaturalHealingGoal;
import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

public class BlazingInferno extends BHBossEntity {
    public AnimationState ANIMATION_DODGE = new AnimationState();
    public AnimationState ANIMATION_FOLLOW_TARGET = new AnimationState();
    public AnimationState ANIMATION_ACTIVE = new AnimationState();
    public AnimationState ANIMATION_INACTIVE = new AnimationState();
    public AnimationState ANIMATION_MELEE_0 = new AnimationState();
    public AnimationState ANIMATION_MELEE_1 = new AnimationState();
    public AnimationState ANIMATION_MELEE_2 = new AnimationState();
    public AnimationState ANIMATION_COMBO_MELEE_0 = new AnimationState();
    public AnimationState ANIMATION_COMBO_MELEE_1 = new AnimationState();
    public AnimationState ANIMATION_COMBO_MELEE_2 = new AnimationState();
    public AnimationState ANIMATION_SHIELD = new AnimationState();
    public AnimationState ANIMATION_DEATH_RAY = new AnimationState();
    public AnimationState ANIMATION_PREPARE_DEATH_RAY = new AnimationState();
    public AnimationState ANIMATION_FIREBALL_SPREAD = new AnimationState();
    public AnimationState ANIMATION_FIREBALL_BURST = new AnimationState();
    public AnimationState ANIMATION_FIREBALL_NORMAL = new AnimationState();
    public AnimationState ANIMATION_GROUND_SLAM = new AnimationState();
    public AnimationState ANIMATION_SHOCKWAVE = new AnimationState();
    public AnimationState ANIMATION_DASHES = new AnimationState();
    public AnimationState ANIMATION_DEATH = new AnimationState();
    public AnimationState ANIMATION_ENRAGED_PHASE = new AnimationState();

    public static final EntityDataAccessor<Boolean> POWERED = SynchedEntityData.defineId(BlazingInferno.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> IS_DASHING = SynchedEntityData.defineId(BlazingInferno.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> ENRAGED = SynchedEntityData.defineId(BlazingInferno.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Integer> BOSS_PHASE = SynchedEntityData.defineId(BlazingInferno.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> SHIELD_COUNT = SynchedEntityData.defineId(BlazingInferno.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> INFERNO_SHIELD_ACTIVE = SynchedEntityData.defineId(BlazingInferno.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DEATH_RAY = SynchedEntityData.defineId(BlazingInferno.class, EntityDataSerializers.BOOLEAN);

    public BlazingInferno(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        this.setActive(false);
        this.setExp(500);
        this.setMaxUpStep(2.0F);
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
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(0, new NaturalHealingGoal<>(this));
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        if (ATTACK_STATE.equals(accessor)) {
            switch (this.getAttackState()) {
                case 0 -> this.stopAnimations();
                case 1 -> {
                    this.stopAnimations();
                    this.ANIMATION_MELEE_0.start(this.tickCount);
                }
                case 2 -> {
                    this.stopAnimations();
                    this.ANIMATION_MELEE_1.start(this.tickCount);
                }
                case 3 -> {
                    this.stopAnimations();
                    this.ANIMATION_MELEE_2.start(this.tickCount);
                }
                case 4 -> {
                    this.stopAnimations();
                    this.ANIMATION_COMBO_MELEE_0.start(this.tickCount);
                }
                case 5 -> {
                    this.stopAnimations();
                    this.ANIMATION_COMBO_MELEE_1.start(this.tickCount);
                }
                case 6 -> {
                    this.stopAnimations();
                    this.ANIMATION_COMBO_MELEE_2.start(this.tickCount);
                }
                case 7 -> {
                    this.stopAnimations();
                    this.ANIMATION_FIREBALL_NORMAL.start(this.tickCount);
                }
                case 8 -> {
                    this.stopAnimations();
                    this.ANIMATION_FIREBALL_SPREAD.start(this.tickCount);
                }
                case 9 -> {
                    this.stopAnimations();
                    this.ANIMATION_FIREBALL_BURST.start(this.tickCount);
                }
                case 10 -> {
                    this.stopAnimations();
                    this.ANIMATION_PREPARE_DEATH_RAY.start(this.tickCount);
                }
                case 11 -> {
                    this.stopAnimations();
                    this.ANIMATION_DEATH_RAY.start(this.tickCount);
                }
                case 12 -> {
                    this.stopAnimations();
                    this.ANIMATION_SHOCKWAVE.start(this.tickCount);
                }
                case 13 -> {
                    this.stopAnimations();
                    this.ANIMATION_GROUND_SLAM.start(this.tickCount);
                }
                case 14 -> {
                    this.stopAnimations();
                    this.ANIMATION_DODGE.start(this.tickCount);
                }
                case 15 -> {
                    this.stopAnimations();
                    this.ANIMATION_DASHES.start(this.tickCount);
                }
                case 16 -> {
                    this.stopAnimations();
                    this.ANIMATION_FOLLOW_TARGET.start(this.tickCount);
                }
                case 17 -> {
                    this.stopAnimations();
                    this.ANIMATION_SHIELD.start(this.tickCount);
                }
                case 18 -> {
                    this.stopAnimations();
                    this.ANIMATION_ENRAGED_PHASE.start(this.tickCount);
                }
            }
        }
        super.onSyncedDataUpdated(accessor);
    }

    public void stopAnimations() {
        this.ANIMATION_ACTIVE.stop();
        this.ANIMATION_INACTIVE.stop();
        this.ANIMATION_FIREBALL_SPREAD.stop();
        this.ANIMATION_FIREBALL_NORMAL.stop();
        this.ANIMATION_FIREBALL_BURST.stop();
        this.ANIMATION_MELEE_0.stop();
        this.ANIMATION_MELEE_1.stop();
        this.ANIMATION_MELEE_2.stop();
        this.ANIMATION_COMBO_MELEE_0.stop();
        this.ANIMATION_COMBO_MELEE_1.stop();
        this.ANIMATION_COMBO_MELEE_2.stop();
        this.ANIMATION_DEATH_RAY.stop();
        this.ANIMATION_PREPARE_DEATH_RAY.stop();
        this.ANIMATION_DODGE.stop();
        this.ANIMATION_DASHES.stop();
        this.ANIMATION_GROUND_SLAM.stop();
        this.ANIMATION_FOLLOW_TARGET.stop();
        this.ANIMATION_SHIELD.stop();
        this.ANIMATION_SHOCKWAVE.stop();
        this.ANIMATION_ENRAGED_PHASE.stop();
    }
}
