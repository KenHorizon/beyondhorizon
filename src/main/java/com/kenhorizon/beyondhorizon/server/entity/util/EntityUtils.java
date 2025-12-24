package com.kenhorizon.beyondhorizon.server.entity.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class EntityUtils {
    public static void groundSlamParticles(Level level, float yBodyRot, double x, double y, double z, float radius, float vec, float math) {
        RandomSource random = level.getRandom();
        for (int i1 = 0; i1 < 120 + random.nextInt(12); i1++) {
            double motionX = random.nextGaussian() * 0.07D;
            double motionY = random.nextGaussian() * 0.07D;
            double motionZ = random.nextGaussian() * 0.07D;
            float angle = (0.01745329251F * yBodyRot) + i1;
            float f = Mth.cos(yBodyRot * ((float) Math.PI / 180F));
            float f1 = Mth.sin(yBodyRot * ((float) Math.PI / 180F));
            double extraX = radius * Mth.sin((float) (Math.PI + angle));
            double extraY = 0.3F;
            double extraZ = radius * Mth.cos(angle);
            double theta = (yBodyRot) * (Math.PI / 180);
            theta += Math.PI / 2;
            double vecX = Math.cos(theta);
            double vecZ = Math.sin(theta);
            int hitX = Mth.floor(x + vec * vecX + extraX);
            int hitY = Mth.floor(y);
            int hitZ = Mth.floor(z + vec * vecZ + extraZ);
            BlockPos hit = new BlockPos(hitX, hitY, hitZ);
            BlockState block = level.getBlockState(hit.below());
            level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, block), x + vec * vecX + extraX + f * math, y + extraY, z + vec * vecZ + extraZ + f1 * math, motionX, motionY, motionZ);
        }
    }
}
