package com.kenhorizon.beyondhorizon.client.particle;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.particle.world.ParticleTrailOptions;
import com.kenhorizon.beyondhorizon.client.render.BHParticleRenderType;
import com.kenhorizon.beyondhorizon.client.render.BHRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import static net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY;

public class TrailParticles extends TextureSheetParticle {
    public float r;
    public float g;
    public float b;
    public float opacity;
    public float size;
    private final Vec3 target;
    private final Behavior behavior;
    private static final ResourceLocation TRAIL_TEXTURE = BeyondHorizon.resource("textures/particle/lightning.png");

    public enum Behavior {
        DEFAULT,
        FADE,
        SHRINK,
        FADE_N_SHRINK
    }

    public TrailParticles(ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ,
                          int duration, float r, float g, float b, float opacity, float size,
                          Behavior behavior, Vec3 target) {
        super(world, x, y, z);
        this.target = target;
        this.setSize(1, 1);
        this.size = size * 0.1f;
        this.lifetime = duration;
        this.alpha = 1;
        this.rCol = r;
        this.gCol = g;
        this.bCol = b;
        this.opacity = opacity;
        this.xd = motionX;
        this.yd = motionY;
        this.zd = motionZ;
        this.behavior = behavior;
    }
    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTick) {
        float var = (age + partialTick) / this.lifetime;
        if (this.behavior == Behavior.FADE || this.behavior == Behavior.FADE_N_SHRINK) {
            this.alpha = this.opacity * 0.95f * (1 - (this.age + partialTick) / this.lifetime) + 0.05f;
        }
        this.quadSize = this.particleBehavior(var);
        super.render(buffer, camera, partialTick);
    }
    private float particleBehavior(float var) {
        if (this.behavior == Behavior.SHRINK || this.behavior == Behavior.FADE_N_SHRINK) {
            return this.size * (1 - var);
        } else {
            return this.size;
        }
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.xd *= 0.99;
        this.yd *= 0.99;
        this.zd *= 0.99;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.move(this.xd, this.yd, this.zd);
            this.yd -= (double) this.gravity;
        }
        if (this.target != null) {
            int i = this.lifetime - this.age;
            double d0 = 1.0 / (double)i;
            this.x = Mth.lerp(d0, this.x, this.target.x());
            this.y = Mth.lerp(d0, this.y, this.target.y());
            this.z = Mth.lerp(d0, this.z, this.target.z());
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return BHParticleRenderType.PARTICLE_EMISSIVE;
    }

    @OnlyIn(Dist.CLIENT)
    public static final class Provider implements ParticleProvider<ParticleTrailOptions> {
        private final SpriteSet spriteSet;
        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(ParticleTrailOptions typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            TrailParticles particles = new TrailParticles(worldIn, x, y, z, xSpeed, ySpeed, zSpeed,
                    typeIn.getDuration(), typeIn.getR(), typeIn.getG(), typeIn.getB(), typeIn.getA(), typeIn.getScale(),
                    typeIn.getBehavior(), typeIn.getTarget());
            particles.setSpriteFromAge(this.spriteSet);
            return particles;
        }
    }
}