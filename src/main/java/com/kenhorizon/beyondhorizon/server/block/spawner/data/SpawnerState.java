package com.kenhorizon.beyondhorizon.server.block.spawner.data;

import com.kenhorizon.beyondhorizon.server.init.BHSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;

import java.util.Optional;

public enum SpawnerState implements StringRepresentable {
    INACTIVE("inactive", 0, ParticleEmission.NONE, -1.0, false),
    WAITING_FOR_PLAYERS("waiting_for_players", 4, ParticleEmission.SMALL_FLAMES, 200.0, true),
    ACTIVE("active", 8, ParticleEmission.FLAMES_AND_SMOKE, 1000.0, true),
    WAITING_FOR_REWARD_EJECTION("waiting_for_reward_ejection", 8, ParticleEmission.SMALL_FLAMES, -1.0, false),
    EJECTING_REWARD("ejecting_reward", 8, ParticleEmission.SMALL_FLAMES, -1.0, false),
    COOLDOWN("cooldown", 0, ParticleEmission.SMOKE_INSIDE_AND_TOP_FACE, -1.0, false);

    private static final float DELAY_BEFORE_EJECT_AFTER_KILLING_LAST_MOB = 40.0F;
    private static final int TIME_BETWEEN_EACH_EJECTION = Mth.floor(30.0F);
    private final String name;
    private final int lightLevel;
    private final double spinningMobSpeed;
    private final ParticleEmission particleEmission;
    private final boolean isCapableOfSpawning;

    private SpawnerState(String name, int j, ParticleEmission particleEmission, double d, boolean bl) {
        this.name = name;
        this.lightLevel = j;
        this.particleEmission = particleEmission;
        this.spinningMobSpeed = d;
        this.isCapableOfSpawning = bl;
    }

    SpawnerState tickAndGetNext(BlockPos blockPos, BHBaseSpawner spawner, ServerLevel serverLevel) {
        BaseSpawnerData data = spawner.getData();
        SpawnerConfig config = spawner.getConfig();
        SpawnerState state;
        switch(this) {
            case INACTIVE:
                state = data.getOrCreateDisplayEntity(spawner, serverLevel, WAITING_FOR_PLAYERS) == null ? this : WAITING_FOR_PLAYERS;
                break;
            case WAITING_FOR_PLAYERS:
                if (!data.hasMobToSpawn(spawner)) {
                    state = INACTIVE;
                } else {
                    data.tryDetectPlayers(serverLevel, blockPos, spawner);
                    state = data.detectedPlayers.isEmpty() ? this : ACTIVE;
                }
                break;
            case ACTIVE:
                if (!data.hasMobToSpawn(spawner)) {
                    state = INACTIVE;
                } else {
                    int i = data.countAdditionalPlayers(blockPos);
                    data.tryDetectPlayers(serverLevel, blockPos, spawner);
                    if (data.hasFinishedSpawningAllMobs(config, i)) {
                        if (data.haveAllCurrentMobsDied()) {
                            data.cooldownEndsAt = serverLevel.getGameTime() + (long)config.targetCooldownLength();
                            data.totalMobsSpawned = 0;
                            data.nextMobSpawnsAt = 0L;
                            state = WAITING_FOR_REWARD_EJECTION;
                            break;
                        }
                    } else if (data.isReadyToSpawnNextMob(serverLevel, config, i)) {
                        spawner.spawnMob(serverLevel, blockPos).ifPresent(uuid -> {
                            data.currentMobs.add(uuid);
                            ++data.totalMobsSpawned;
                            data.nextMobSpawnsAt = serverLevel.getGameTime() + (long)config.ticksBetweenSpawn();
                            data.spawnPotentials.getRandom(serverLevel.getRandom()).ifPresent(data1 -> {
                                data.nextSpawnData = Optional.of(data1.getData());
                                spawner.markUpdated();
                            });
                        });
                    }

                    state = this;
                }
                break;
            case WAITING_FOR_REWARD_EJECTION:
                if (data.isReadyToOpenShutter(serverLevel, config, 40.0F)) {
                    serverLevel.playSound(null, blockPos, BHSounds.SPAWNER_OPEN_SHUTTER.get(), SoundSource.BLOCKS);
                    state = EJECTING_REWARD;
                } else {
                    state = this;
                }
                break;
            case EJECTING_REWARD:
                if (!data.isReadyToEjectItems(serverLevel, config, (float)TIME_BETWEEN_EACH_EJECTION)) {
                    state = this;
                } else if (data.detectedPlayers.isEmpty()) {
                    serverLevel.playSound(null, blockPos, BHSounds.SPAWNER_CLOSE_SHUTTER.get(), SoundSource.BLOCKS);
                    data.ejectingLootTable = Optional.empty();
                    state = COOLDOWN;
                } else {
                    if (data.ejectingLootTable.isEmpty()) {
                        data.ejectingLootTable = config.lootTablesToEject().getRandomValue(serverLevel.getRandom());
                    }

                    data.ejectingLootTable.ifPresent(location -> spawner.ejectReward(serverLevel, blockPos, location));
                    data.detectedPlayers.remove(data.detectedPlayers.iterator().next());
                    state = this;
                }
                break;
            case COOLDOWN:
                if (data.isCooldownFinished(serverLevel)) {
                    data.cooldownEndsAt = 0L;
                    state = WAITING_FOR_PLAYERS;
                } else {
                    state = this;
                }
                break;
            default:
                state = INACTIVE;
        }

        return state;
    }

    public int lightLevel() {
        return this.lightLevel;
    }

    public double spinningMobSpeed() {
        return this.spinningMobSpeed;
    }

    public boolean hasSpinningMob() {
        return this.spinningMobSpeed >= 0.0;
    }

    public boolean isCapableOfSpawning() {
        return this.isCapableOfSpawning;
    }

    public void emitParticles(Level level, BlockPos blockPos) {
        this.particleEmission.emit(level, level.getRandom(), blockPos);
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    static class LightLevel {
        private static final int UNLIT = 0;
        private static final int HALF_LIT = 4;
        private static final int LIT = 8;

        private LightLevel() {
        }
    }

    static class SpinningMob {
        private static final double NONE = -1.0;
        private static final double SLOW = 200.0;
        private static final double FAST = 1000.0;

        private SpinningMob() {
        }
    }
}
