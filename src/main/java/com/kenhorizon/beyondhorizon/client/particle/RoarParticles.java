package com.kenhorizon.beyondhorizon.client.particle;

import com.kenhorizon.beyondhorizon.client.particle.world.RoarParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Created by BobMowzie on 6/2/2017.
 */
public class RoarParticles extends TextureSheetParticle {
    public int r;
    public int g;
    public int b;
    public float endsize;
    public float startsize;
    public float increase;
    private final SpriteSet sprites;


    public RoarParticles(ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ, int duration, int r, int g, int b, float opacity, float startsize, float increase, float endsize, SpriteSet sprites) {
        super(world, x, y, z);
        this.sprites = sprites;
        this.setSize(1, 1);
        this.setSpriteFromAge(this.sprites);
        this.quadSize = startsize;
        this.startsize = startsize;
        this.increase = increase;
        this.endsize = endsize;
        this.lifetime = duration;
        this.rCol = r/255F;
        this.gCol = g/255F;
        this.bCol = b/255F;
        this.alpha = opacity;
        this.xd = motionX;
        this.yd = motionY;
        this.zd = motionZ;
    }

    @Override
    public int getLightColor(float delta) {
        return 240 | super.getLightColor(delta) & 0xFF0000;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.age++ >= this.lifetime) {
            this.remove();
        }else{
            this.setSpriteFromAge(this.sprites);
            this.quadSize += this.increase;
            if (this.increase > 0) {
                this.quadSize = Math.min(this.quadSize, this.endsize);
            } else if (this.increase < 0) {
                this.quadSize = Math.max(this.quadSize, this.endsize);
            }
            this.alpha = Math.max(0f, 1f - ((float) this.age / this.lifetime));
        }
    }



    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static final class Provider implements ParticleProvider<RoarParticleOptions> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle createParticle(RoarParticleOptions typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new RoarParticles(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, typeIn.getDuration(), typeIn.getR(), typeIn.getG(), typeIn.getB(), typeIn.getA(), typeIn.getStart(),typeIn.getIncrease(),typeIn.getEnd(),spriteSet);
        }
    }
}

