package com.kenhorizon.beyondhorizon.server.level.block.spawner.data;

import com.kenhorizon.beyondhorizon.client.particle.TrailParticles;
import com.kenhorizon.beyondhorizon.client.particle.world.TrailParticleOptions;
import com.kenhorizon.beyondhorizon.client.render.util.Colors;
import com.kenhorizon.beyondhorizon.server.init.BHSounds;
import com.kenhorizon.beyondhorizon.server.level.entity.EquipmentTable;
import com.kenhorizon.beyondhorizon.server.util.PlayerDetector;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class BHBaseSpawner {

    public static final int DETECT_PLAYER_SPAWN_BUFFER = 40;
    private static final int MAX_MOB_TRACKING_DISTANCE = 47;
    private static final int MAX_MOB_TRACKING_DISTANCE_SQR = Mth.square(47);
    private static final float SPAWNING_AMBIENT_SOUND_CHANCE = 0.02F;
    private SpawnerConfig config;
    private BaseSpawnerData data;
    private final StateAccessor stateAccessor;
    private PlayerDetector playerDetector;
    private final PlayerDetector.EntitySelector entitySelector;
    private boolean overridePeacefulAndMobSpawnRule;

    public Codec<BHBaseSpawner> codec() {
        return RecordCodecBuilder.create(
                instance -> instance.group(SpawnerConfig.MAP_CODEC.forGetter(BHBaseSpawner::getConfig), BaseSpawnerData.MAP_CODEC.forGetter(BHBaseSpawner::getData))
                        .apply(instance, (config, data) -> new BHBaseSpawner(config, data, this.stateAccessor, this.playerDetector, this.entitySelector))
        );
    }

    public BHBaseSpawner(StateAccessor stateAccessor, PlayerDetector playerDetector, PlayerDetector.EntitySelector entitySelector) {
        this(SpawnerConfig.DEFAULT, new BaseSpawnerData(), stateAccessor, playerDetector, entitySelector);
    }

    public BHBaseSpawner(final SpawnerConfig config, BaseSpawnerData data, StateAccessor stateAccessor, PlayerDetector playerDetector, PlayerDetector.EntitySelector entitySelector) {
        this.config = config;
        this.data = data;
        this.data.setSpawnPotentialsFromConfig(config);
        this.stateAccessor = stateAccessor;
        this.playerDetector = playerDetector;
        this.entitySelector = entitySelector;
    }

    public SpawnerConfig getConfig() {
        return this.config;
    }

    public void setConfig(SpawnerConfig config) {
        this.config = config;
    }

    public int getTargetCooldownLength() {
        return this.config.targetCooldownLength();
    }

    public int getRequiredPlayerRange() {
        return this.config.requiredPlayerRange();
    }

    public void setData(BaseSpawnerData data) {
        this.data = data;
    }

    public BaseSpawnerData getData() {
        return this.data;
    }

    public SpawnerState getState() {
        return this.stateAccessor.getState();
    }

    public void setState(Level level, SpawnerState trialSpawnerState) {
        this.stateAccessor.setState(level, trialSpawnerState);
    }

    public void markUpdated() {
        this.stateAccessor.markUpdated();
    }

    public PlayerDetector getPlayerDetector() {
        return this.playerDetector;
    }

    public PlayerDetector.EntitySelector getEntitySelector() {
        return this.entitySelector;
    }

    public boolean canSpawnInLevel(Level level) {
        if (this.overridePeacefulAndMobSpawnRule) {
            return true;
        } else {
            return level.getDifficulty() != Difficulty.PEACEFUL;
        }
    }
    public Optional<UUID> spawnMob(ServerLevel serverLevel, BlockPos blockPos) {
        RandomSource random = serverLevel.getRandom();
        SpawnerSpawnData spawnData = this.data.getOrCreateNextSpawnData(this, serverLevel.getRandom());
        CompoundTag entityToSpawn = spawnData.entityToSpawn();
        ListTag nbtPos = entityToSpawn.getList("Pos", 6);
        Optional<EntityType<?>> optional = EntityType.by(entityToSpawn);
        Optional<EquipmentTable> equipments = spawnData.getEquipmentTable();
        if (optional.isEmpty()) {
            return Optional.empty();
        } else {
            int entrySize = nbtPos.size();
            double blockX = entrySize >= 1 ? nbtPos.getDouble(0) : blockPos.getX() + (random.nextDouble() - random.nextDouble()) * this.config.spawnRange() + 0.5;
            double blockY = entrySize >= 2 ? nbtPos.getDouble(1) : blockPos.getY() + random.nextInt(3) - 1;
            double blockZ = entrySize >= 3 ? nbtPos.getDouble(2) : blockPos.getZ() + (random.nextDouble() - random.nextDouble()) * this.config.spawnRange() + 0.5;
            if (!serverLevel.noCollision(optional.get().getAABB(blockX, blockY, blockZ))) {
                return Optional.empty();
            } else {
                Vec3 blockPositionVec = new Vec3(blockX, blockY, blockZ);
                if (!inLineOfSight(serverLevel, blockPos.getCenter(), blockPositionVec)) {
                    return Optional.empty();
                } else {
                    BlockPos mobSpawnPos = BlockPos.containing(blockPositionVec);
                    Entity entity = EntityType.loadEntityRecursive(entityToSpawn, serverLevel, entityIn -> {
                        entityIn.moveTo(blockX, blockY, blockZ, random.nextFloat() * 360.0F, 0.0F);
                        equipments.ifPresent(equipmentTable -> equipMobsWithEquipments(serverLevel, equipmentTable, entityIn));
                        return entityIn;
                    });
                    if (entity == null) {
                        return Optional.empty();
                    } else {
                        if (entity instanceof Mob mob) {
                            if (!mob.checkSpawnObstruction(serverLevel)) {
                                return Optional.empty();
                            }

                            if (spawnData.getEntityToSpawn().size() == 1 && spawnData.getEntityToSpawn().contains("id", 8)) {
                                ForgeEventFactory.onFinalizeSpawn(mob, serverLevel, serverLevel.getCurrentDifficultyAt(mob.blockPosition()), MobSpawnType.SPAWNER, null, null);
                                mob.setPersistenceRequired();
                            }
                        }

                        if (!serverLevel.tryAddFreshEntityWithPassengers(entity)) {
                            return Optional.empty();
                        } else {
                            addSpawnParticles(serverLevel, blockPos, serverLevel.getRandom());
                            serverLevel.playSound(null, mobSpawnPos, BHSounds.SPAWNER_SPAWN.get(), SoundSource.BLOCKS, 1.0F, (serverLevel.getRandom().nextFloat() - serverLevel.getRandom().nextFloat()) * 0.2F + 1.0F);
                            addSpawnParticles(serverLevel, mobSpawnPos, serverLevel.getRandom());
                            serverLevel.gameEvent(entity, GameEvent.ENTITY_PLACE, mobSpawnPos);
                            return Optional.of(entity.getUUID());
                        }
                    }
                }
            }
        }
    }

    public void equipMobsWithEquipments(ServerLevel level, EquipmentTable table, Entity entity) {
        LootTable lootTable = level.getServer().getLootData().getLootTable(table.getLootTable());
//        BeyondHorizon.LOGGER.info("Loot Table:{}", lootTable.getLootTableId());
        Map<EquipmentSlot, Float> map = table.getDropChances();
        LootParams params = new LootParams.Builder(level).create(LootContextParamSets.EMPTY);
        ObjectArrayList<ItemStack> list = lootTable.getRandomItems(params);
//        BeyondHorizon.LOGGER.info("Equipment Table:{}", table);
//        BeyondHorizon.LOGGER.info("Items:{}", list);
        if (!list.isEmpty()) {
            for (ItemStack stacks : list) {
                EquipmentSlot dedicatedSlots = LivingEntity.getEquipmentSlotForItem(stacks);
                if (entity instanceof Mob mob) {
                    mob.setItemSlot(dedicatedSlots, stacks);
                    mob.setDropChance(dedicatedSlots, map.get(dedicatedSlots));
                }
            }
        }
    }


    public void ejectReward(ServerLevel level, BlockPos pos, ResourceLocation ejectRewards) {
        LootTable lootTable = level.getServer().getLootData().getLootTable(ejectRewards);
        LootParams params = new LootParams.Builder(level).create(LootContextParamSets.EMPTY);
        ObjectArrayList<ItemStack> lootDrops = lootTable.getRandomItems(params);
        if (!lootDrops.isEmpty()) {
            ObjectListIterator<ItemStack> var0 = lootDrops.iterator();
            while (var0.hasNext()) {
                ItemStack stack = var0.next();
                DefaultDispenseItemBehavior.spawnItem(level, stack, 2, Direction.UP, Vec3.atBottomCenterOf(pos).relative(Direction.UP, 1.2));
            }
            level.playSound(null, pos, BHSounds.SPAWNER_EJECT_ITEM.get(), SoundSource.BLOCKS, 1.0F, (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.2F + 1.0F);
            level.levelEvent(3014, pos, 0);
        }
        addEjectItemParticles(level, pos, level.getRandom());
    }

    public void tickClient(Level level, BlockPos blockPos) {
        if (!this.canSpawnInLevel(level)) {
            this.data.oSpin = this.data.spin;
        } else {
            SpawnerState trialSpawnerState = this.getState();
            trialSpawnerState.emitParticles(level, blockPos);
            if (trialSpawnerState.hasSpinningMob()) {
                double d = Math.max(0L, this.data.nextMobSpawnsAt - level.getGameTime());
                this.data.oSpin = this.data.spin;
                this.data.spin = (this.data.spin + trialSpawnerState.spinningMobSpeed() / (d + 200.0)) % 360.0;
            }

            if (trialSpawnerState.isCapableOfSpawning()) {
                RandomSource randomSource = level.getRandom();
                if (randomSource.nextFloat() <= 0.02F) {
                    level.playLocalSound(blockPos, BHSounds.SPAWNER_AMBIENT.get(), SoundSource.BLOCKS, randomSource.nextFloat() * 0.25F + 0.75F, randomSource.nextFloat() + 0.5F, false);
                }
            }
        }
    }

    public void tickServer(ServerLevel serverLevel, BlockPos blockPos) {
        SpawnerState state = this.getState();
        if (!this.canSpawnInLevel(serverLevel)) {
            if (state.isCapableOfSpawning()) {
                this.data.reset();
                this.setState(serverLevel, SpawnerState.INACTIVE);
            }
        } else {
            if (this.data.currentMobs.removeIf(uUID -> shouldMobBeUntracked(serverLevel, blockPos, uUID))) {
                this.data.nextMobSpawnsAt = serverLevel.getGameTime() + this.config.ticksBetweenSpawn();
            }

            SpawnerState next = state.tickAndGetNext(blockPos, this, serverLevel);
            if (next != state) {
                this.setState(serverLevel, next);
            }
        }
    }

    private static boolean shouldMobBeUntracked(ServerLevel serverLevel, BlockPos blockPos, UUID uUID) {
        Entity entity = serverLevel.getEntity(uUID);
        return entity == null
                || !entity.isAlive()
                || !entity.level().dimension().equals(serverLevel.dimension())
                || entity.blockPosition().distSqr(blockPos) > MAX_MOB_TRACKING_DISTANCE_SQR;
    }

    private static boolean inLineOfSight(Level level, Vec3 vec3, Vec3 vec32) {
        BlockHitResult blockHitResult = level.clip(new ClipContext(vec32, vec3, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, null));
        return blockHitResult.getBlockPos().equals(BlockPos.containing(vec3)) || blockHitResult.getType() == HitResult.Type.MISS;
    }

    public static void addSpawnParticles(ServerLevel level, BlockPos blockPos, RandomSource randomSource) {
        for (int i = 0; i < 20; i++) {
            double d = blockPos.getX() + 0.5D + (randomSource.nextDouble() - 0.5D) * 2.0D;
            double e = blockPos.getY() + 0.5D + (randomSource.nextDouble() - 0.5D) * 2.0D;
            double f = blockPos.getZ() + 0.5D + (randomSource.nextDouble() - 0.5D) * 2.0D;
            level.sendParticles(ParticleTypes.POOF, d, e, f, 1, 0.0D, 0.0D, 0.0D, 0.0F);
            level.sendParticles(ParticleTypes.FLAME, d, e, f, 1, 0.0D, 0.0D, 0.0D, 0.0F);
            float[] colors = Colors.getFARGB(Colors.BLUE);
            float[] colors1 = Colors.getFARGB(Colors.RED);
            level.sendParticles(new TrailParticleOptions(40, colors[0], colors[1], colors[2], colors[3], 1.15F, TrailParticles.Behavior.FADE_N_SHRINK, new Vec3(d, e + 4.0D, f)), d, e, f, 1, 0, 0, 0,0);
            level.sendParticles(new TrailParticleOptions(20, colors1[0], colors1[1], colors1[2], colors1[3], 1.15F, TrailParticles.Behavior.FADE_N_SHRINK, new Vec3(d, e + 4.0D, f)), d, e, f, 1, 0, 0, 0,0);

        }
    }

    public static void addDetectPlayerParticles(ServerLevel level, BlockPos blockPos, RandomSource randomSource, int i) {
        for (int j = 0; j < 30 + Math.min(i, 10) * 5; j++) {
            double d = (2.0F * randomSource.nextFloat() - 1.0F) * 0.65D;
            double e = (2.0F * randomSource.nextFloat() - 1.0F) * 0.65D;
            double f = blockPos.getX() + 0.5D + d;
            double g = blockPos.getY() + 0.1D + randomSource.nextFloat() * 0.8D;
            double h = blockPos.getZ() + 0.5D + e;
            float[] colors = Colors.getFARGB(Colors.BLUE);
            float[] colors1 = Colors.getFARGB(Colors.RED);
            level.sendParticles(new TrailParticleOptions(40, colors[0], colors[1], colors[2], colors[3], 1.15F, TrailParticles.Behavior.FADE_N_SHRINK, new Vec3(d, e + 4.0D, f)), d, e, f, 1, 0, 0, 0,0);
            level.sendParticles(new TrailParticleOptions(20, colors1[0], colors1[1], colors1[2], colors1[3], 1.15F, TrailParticles.Behavior.FADE_N_SHRINK, new Vec3(d, e + 4.0D, f)), d, e, f, 1, 0, 0, 0,0);

            level.sendParticles(ParticleTypes.FLAME, f, g, h, 1, 0.0D, 0.0D, 0.0D, 0.0F);
        }
    }

    public static void addEjectItemParticles(ServerLevel level, BlockPos blockPos, RandomSource randomSource) {
        for (int i = 0; i < 20; i++) {
            double d = blockPos.getX() + 0.4D + randomSource.nextDouble() * 0.2D;
            double e = blockPos.getY() + 0.4D + randomSource.nextDouble() * 0.2D;
            double f = blockPos.getZ() + 0.4D + randomSource.nextDouble() * 0.2D;
            double g = randomSource.nextGaussian() * 0.02D;
            double h = randomSource.nextGaussian() * 0.02D;
            double j = randomSource.nextGaussian() * 0.02D;
            level.sendParticles(ParticleTypes.FLAME, d, e, f, 0, g, h, j * 0.25D, 0.5F);
            level.sendParticles(ParticleTypes.POOF, d, e, f, 0, g, h, j, 0.5F);
            float[] colors = Colors.getFARGB(Colors.YELLOW);
            float[] colors1 = Colors.getFARGB(Colors.GREEN);
            level.sendParticles(new TrailParticleOptions(40, colors[0], colors[1], colors[2], colors[3], 1.15F, TrailParticles.Behavior.FADE_N_SHRINK, new Vec3(d, e + 4.0D, f)), d, e, f, 1, 0, 0, 0,0);
            level.sendParticles(new TrailParticleOptions(20, colors1[0], colors1[1], colors1[2], colors1[3], 1.15F, TrailParticles.Behavior.FADE_N_SHRINK, new Vec3(d, e + 4.0D, f)), d, e, f, 1, 0, 0, 0,0);

        }
    }

    public void save(CompoundTag nbt) {

    }

    public void load(CompoundTag nbt) {

    }

    public interface StateAccessor {
        void setState(Level level, SpawnerState trialSpawnerState);

        SpawnerState getState();

        void markUpdated();
    }
}
