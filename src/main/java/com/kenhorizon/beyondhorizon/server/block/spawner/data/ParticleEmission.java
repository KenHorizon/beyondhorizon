package com.kenhorizon.beyondhorizon.server.block.spawner.data;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public interface ParticleEmission {
    ParticleEmission NONE = (level, randomSource, blockPos) -> {};
    ParticleEmission SMALL_FLAMES = (level, randomSource, blockPos) -> {
        if (randomSource.nextInt(2) == 0) {
            Vec3 vec3 = blockPos.getCenter().offsetRandom(randomSource, 0.9F);
            addParticle(ParticleTypes.FLAME, vec3, level);
        }
    };
    ParticleEmission FLAMES_AND_SMOKE = (level, randomSource, blockPos) -> {
        Vec3 vec3 = blockPos.getCenter().offsetRandom(randomSource, 1.0F);
        addParticle(ParticleTypes.SMOKE, vec3, level);
        addParticle(ParticleTypes.FLAME, vec3, level);
    };
    ParticleEmission SMOKE_INSIDE_AND_TOP_FACE = (level, randomSource, blockPos) -> {
        Vec3 vec3 = blockPos.getCenter().offsetRandom(randomSource, 0.9F);
        if (randomSource.nextInt(3) == 0) {
            addParticle(ParticleTypes.SMOKE, vec3, level);
        }

        if (level.getGameTime() % 20L == 0L) {
            Vec3 vec32 = blockPos.getCenter().add(0.0, 0.5, 0.0);
            int i = level.getRandom().nextInt(4) + 20;

            for (int j = 0; j < i; j++) {
                addParticle(ParticleTypes.SMOKE, vec3, level);
            }
        }
    };

    private static void addParticle(ParticleOptions particleOptions, Vec3 vec3, Level level) {
        level.addParticle(particleOptions, vec3.x(), vec3.y(), vec3.z(), 0.0, 0.0, 0.0);
    }

    void emit(Level level, RandomSource randomSource, BlockPos blockPos);
}
