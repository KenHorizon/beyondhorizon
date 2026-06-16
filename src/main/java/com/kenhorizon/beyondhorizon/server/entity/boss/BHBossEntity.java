package com.kenhorizon.beyondhorizon.server.entity.boss;

import com.kenhorizon.beyondhorizon.server.entity.BHLibEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

public class BHBossEntity extends BHLibEntity implements Enemy {
    private static final EntityDataAccessor<BlockPos> DATA_HOME_POS = SynchedEntityData.defineId(BHBossEntity.class, EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<String> DATA_DIMENSION_TYPE = SynchedEntityData.defineId(BHBossEntity.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<Integer> DATA_BOSS_PHASE = SynchedEntityData.defineId(BHBossEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> DATA_MAX_BOSS_PHASE = SynchedEntityData.defineId(BHBossEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> DATA_PLAYER_COUNT = SynchedEntityData.defineId(BHBossEntity.class, EntityDataSerializers.INT);
    public static final String NBT_BOSS_PHASE = "boss_phase";
    public static final String NBT_MAX_BOSS_PHASE = "boss_phase_max";
    public static final String NBT_HOME_X = "home_x";
    public static final String NBT_HOME_Y = "home_y";
    public static final String NBT_HOME_Z = "home_z";
    public static final String NBT_DIMENSION_TYPE = "dimension_type";
    public static final String NBT_PLAYER_COUNT = "player_count";
    private int returnState;
    protected final int RETURN_STATE_COOLDOWN = 20;
    public BHBossEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }
    public void setMaxBossPhase(int phase) {
        this.entityData.set(DATA_MAX_BOSS_PHASE, phase);
    }

    public int getMaxBossPhase() {
        return this.entityData.get(DATA_MAX_BOSS_PHASE);
    }

    public void setBossPhase(int phase) {
        this.entityData.set(DATA_BOSS_PHASE, phase);
    }

    public int getBossPhase() {
        return this.entityData.get(DATA_BOSS_PHASE);
    }

    public BlockPos getHomePos() {
        return this.entityData.get(DATA_HOME_POS);
    }

    public void setHomePos(BlockPos blockPos) {
        this.entityData.set(DATA_HOME_POS, blockPos);
    }

    public void setDimensionType(String dimension) {
        this.entityData.set(DATA_DIMENSION_TYPE, dimension);
    }

    public String getDimensionType() {
        return this.entityData.get(DATA_DIMENSION_TYPE);
    }
    public void setPlayerCount(int v) {
        this.entityData.set(DATA_PLAYER_COUNT, v);
    }

    public int getPlayerCount() {
        return this.entityData.get(DATA_PLAYER_COUNT);
    }
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_PLAYER_COUNT, 0);
        this.entityData.define(DATA_BOSS_PHASE, 0);
        this.entityData.define(DATA_MAX_BOSS_PHASE, 0);
        this.entityData.define(DATA_HOME_POS, BlockPos.ZERO);
        this.entityData.define(DATA_DIMENSION_TYPE, "minecraft:overworld");
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        double distSqr = calculateRange(source);
        if (distSqr != -1) {
            double limit = this.sourceRangeLimit();
            double maxLimit = limit * 1.5;

            double limitSqr = limit * limit;
            double maxLimitSqr = maxLimit * maxLimit;

            if (distSqr >= maxLimitSqr) {
                return false;
            }

            if (distSqr > limitSqr) {
                double distance = Math.sqrt(distSqr);
                float multiplier = (float) ((maxLimit - distance) / (maxLimit - limit));
                amount *= multiplier;

                if (amount <= 0) return false;
            }
        }
        return super.hurt(source, amount);
    }

    public double sourceRangeLimit() {
        return Double.MAX_VALUE;
    }

    protected int getPlayerCounter(Level level) {
        double range = 64.0D;
        int playerCount = level.getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(range), player -> {
            return !player.isSpectator()
                    && !player.isCreative()
                    && player.isAlive()
                    && player.distanceToSqr(this) < (range * range);
        }).size();
        return playerCount;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt(NBT_MAX_BOSS_PHASE, this.getMaxBossPhase());
        nbt.putInt(NBT_BOSS_PHASE, this.getBossPhase());
        nbt.putInt(NBT_HOME_X, this.getHomePos().getX());
        nbt.putInt(NBT_HOME_Y, this.getHomePos().getY());
        nbt.putInt(NBT_HOME_Z, this.getHomePos().getZ());
        nbt.putString(NBT_DIMENSION_TYPE, this.getDimensionType());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        int homeX = nbt.getInt(NBT_HOME_X);
        int homeY = nbt.getInt(NBT_HOME_Y);
        int homeZ = nbt.getInt(NBT_HOME_Z);
        this.setMaxBossPhase(nbt.getInt(NBT_MAX_BOSS_PHASE));
        this.setBossPhase(nbt.getInt(NBT_BOSS_PHASE));
        this.setHomePos(new BlockPos(homeX, homeY, homeZ));
        this.setDimensionType(nbt.getString(NBT_DIMENSION_TYPE));
        super.readAdditionalSaveData(nbt);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData data, @Nullable CompoundTag dataNbt) {
        this.returnState = this.RETURN_STATE_COOLDOWN;
        if (reason == MobSpawnType.SPAWNER) {

        }
        return super.finalizeSpawn(level, difficulty, reason, data, dataNbt);
    }

    public void bossPhases() {
    }

    @Override
    public void tick() {
        super.tick();
        this.bossPhases();
        if (!this.level().isClientSide()) {
            LivingEntity target = this.getTarget();
            if (this.returnState > 0) this.returnState--;
            if (target != null) {
                this.returnState = this.RETURN_STATE_COOLDOWN;
            }
            if (this.returnState <= 0) {
                this.resetState();
            }
        }
    }
    @Override
    public boolean hasBossMusic() {
        return true;
    }

    @Override
    public boolean hasBossBar() {
        return true;
    }

    public boolean isHalfHealth() {
        return this.getHealth() <= (this.getMaxHealth() / 2);
    }

    public void resetState() {
        if (this.getHomePos() != null && this.getHomePos() != BlockPos.ZERO) {
            if (this.level() instanceof ServerLevel serverLevel) {
                String dimStr = this.getDimensionType();
                ResourceLocation parsed = ResourceLocation.tryParse(dimStr);
                boolean isInvalidDim = dimStr == null || dimStr.contains("ResourceKey") || parsed == null;

                if (isInvalidDim) {
                    System.err.println("[DIM] Malformed dimension string detected: " + dimStr + " → Replacing with current dimension.");
                    parsed = serverLevel.dimension().location();
                    this.setDimensionType(parsed.toString());
                }
                ResourceKey<Level> targetDim = ResourceKey.create(Registries.DIMENSION, parsed);

                if (!serverLevel.dimension().location().equals(parsed)) {
                    ServerLevel targetLevel = serverLevel.getServer().getLevel(targetDim);
                    if (targetLevel != null) {
                        this.changeDimension(targetLevel);
                        this.moveTo(this.getHomePos().getX() + 0.5, this.getHomePos().getY(), this.getHomePos().getZ() + 0.5, this.getYRot(), this.getXRot());
                        this.returnState = this.RETURN_STATE_COOLDOWN;
                        return;
                    }
                }

                if (!this.getHomePos().closerToCenterThan(this.position(), 16.0)) {
                    this.moveTo(this.getHomePos().getX() + 0.5, this.getHomePos().getY(), this.getHomePos().getZ() + 0.5, this.getYRot(), this.getXRot());
                    this.returnState = this.RETURN_STATE_COOLDOWN;
                }
            }
        }
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return false;
    }

    @Override
    public boolean canBeAffected(MobEffectInstance mobEffectInstance) {
        return super.canBeAffected(mobEffectInstance);
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    protected boolean canRide(Entity entity) {
        return false;
    }
}
