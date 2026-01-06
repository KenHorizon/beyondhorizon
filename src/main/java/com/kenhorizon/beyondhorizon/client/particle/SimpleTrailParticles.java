package com.kenhorizon.beyondhorizon.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SimpleTrailParticles extends TextureSheetParticle {
    private SpriteSet sprites;

    protected SimpleTrailParticles(ClientLevel world, double x, double y, double z, double xSpeed, SpriteSet sprites) {
        super(world, x, y, z, 0, 0, 0);
        this.quadSize = 0.20F;
        this.lifetime = 7 + this.random.nextInt(4);
        this.sprites = sprites;

    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        }else{
            this.setSpriteFromAge(this.sprites);
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    @Override
    public float getQuadSize(float scaleFactor) {
        return super.getQuadSize(scaleFactor);
    }

    @Override
    public int getLightColor(float partialTicks) {
        return 240;
    }

    @OnlyIn(Dist.CLIENT)
    public static class DefaultProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public DefaultProvider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
            SimpleTrailParticles particle = new SimpleTrailParticles(level, x, y, z, dx, this.sprites);
            particle.setSpriteFromAge(sprites);
            return particle;
        }
    }
    @OnlyIn(Dist.CLIENT)
    public static class HellfireOrb implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public HellfireOrb(SpriteSet sprites) {
            this.sprites = sprites;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
            SimpleTrailParticles particle = new SimpleTrailParticles(level, x, y, z, dx, this.sprites);
            particle.setSpriteFromAge(sprites);
            particle.lifetime = 60;
            return particle;
        }
    }
}
