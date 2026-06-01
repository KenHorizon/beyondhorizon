package com.kenhorizon.beyondhorizon.client.particle;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.particle.world.ParticleTrailOptions;
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
    public boolean facesCamera;
    public float yaw;
    public float pitch;
    public float size;
    private final Vec3 target;
    private final Behavior behavior;
    protected float trailR = 1.0F;
    protected float trailG = 1.0F;
    protected float trailB = 1.0F;
    protected float trailA = 0.50F;
    private Vec3[] trailPositions = new Vec3[64];
    private int trailPointer = -1;
    private static final ResourceLocation TRAIL_TEXTURE = BeyondHorizon.resource("textures/particle/lightning.png");

    public enum Behavior {
        DEFAULT,
        FADE,
        SHRINK,
        FADE_N_SHRINK
    }

    public TrailParticles(ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ, float yaw, float pitch, int duration, float r, float g, float b, float opacity, float size, boolean facesCamera, Behavior behavior, Vec3 target) {
        super(world, x, y, z);
        this.target = target;
        this.setSize(1, 1);
        this.size = size * 0.1f;
        this.lifetime = duration;
        this.alpha = 1;
        this.r = r;
        this.g = g;
        this.b = b;
        this.trailR = r;
        this.trailG = g;
        this.trailB = b;
        this.opacity = opacity;
        this.yaw = yaw;
        this.pitch = pitch;
        this.facesCamera = facesCamera;
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
        this.rCol = this.r;
        this.gCol = this.g;
        this.bCol = this.b;
        super.render(buffer, camera, partialTick);
    }
    private float particleBehavior(float var) {
        if (this.behavior == Behavior.SHRINK || this.behavior == Behavior.FADE_N_SHRINK) {
            return this.size * (1 - var);
        } else {
            return this.size;
        }
    }
    private void renderTrail(Camera camera, float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        if (this.trailPointer > -1) {
            MultiBufferSource.BufferSource multibuffersource$buffersource = minecraft.renderBuffers().bufferSource();
            VertexConsumer buffer = this.getVetrexConsumer(multibuffersource$buffersource);

            Vec3 cameraPos = camera.getPosition();
            double x = (float) (Mth.lerp((double) partialTick, this.xo, this.x));
            double y = (float) (Mth.lerp((double) partialTick, this.yo, this.y));
            double z = (float) (Mth.lerp((double) partialTick, this.zo, this.z));

            PoseStack posestack = new PoseStack();
            posestack.pushPose();
//            posestack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
            int samples = 0;
            Vec3 drawFrom = new Vec3(x, y, z);
            float zRot = this.getTrailRot(camera);
            Vec3 topAngleVec = new Vec3(0, this.getTrailHeight() / 2F, 0).zRot(zRot);
            Vec3 bottomAngleVec = new Vec3(0, this.getTrailHeight() / -2F, 0).zRot(zRot);
            int j = this.getLightColor(partialTick);
            while (samples < sampleCount()) {
                Vec3 sample = this.getTrailPosition(samples * sampleStep(), partialTick);
                float u1 = samples / (float) sampleCount();
                float u2 = u1 + 1 / (float) sampleCount();

                Vec3 draw1 = drawFrom;
                Vec3 draw2 = sample;

                PoseStack.Pose posestack$pose = posestack.last();
                Matrix4f matrix4f = posestack$pose.pose();
                Matrix3f matrix3f = posestack$pose.normal();
                buffer.vertex(matrix4f, (float) draw1.x + (float) bottomAngleVec.x, (float) draw1.y + (float) bottomAngleVec.y, (float) draw1.z + (float) bottomAngleVec.z).color(trailR, trailG, trailB, trailA).uv(u1, 1F).overlayCoords(NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
                buffer.vertex(matrix4f, (float) draw2.x + (float) bottomAngleVec.x, (float) draw2.y + (float) bottomAngleVec.y, (float) draw2.z + (float) bottomAngleVec.z).color(trailR, trailG, trailB, trailA).uv(u2, 1F).overlayCoords(NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
                buffer.vertex(matrix4f, (float) draw2.x + (float) topAngleVec.x, (float) draw2.y + (float) topAngleVec.y, (float) draw2.z + (float) topAngleVec.z).color(trailR, trailG, trailB, trailA).uv(u2, 0).overlayCoords(NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
                buffer.vertex(matrix4f, (float) draw1.x + (float) topAngleVec.x, (float) draw1.y + (float) topAngleVec.y, (float) draw1.z + (float) topAngleVec.z).color(trailR, trailG, trailB, trailA).uv(u1, 0).overlayCoords(NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
                samples++;
                drawFrom = sample;
            }
            posestack.popPose();
        }
    }

    protected VertexConsumer getVetrexConsumer(MultiBufferSource.BufferSource multibuffersource$buffersource) {
        return multibuffersource$buffersource.getBuffer(RenderType.itemEntityTranslucentCull(this.getTrailTexture()));
    }

    public float getTrailRot(Camera camera) {
        return -0.017453292F * camera.getXRot();
    }
    public ResourceLocation getTrailTexture() {
        return TRAIL_TEXTURE;
    }
    public float getTrailHeight() {
        return 0.22F;
    }
    public int sampleCount() {
        return 4;
    }

    public int sampleStep() {
        return 1;
    }

    public void tickTrail() {
        Vec3 currentPosition = new Vec3(this.x, this.y, this.z);
        if (trailPointer == -1) {
            for (int i = 0; i < trailPositions.length; i++) {
                trailPositions[i] = currentPosition;
            }
        }
        if (++this.trailPointer == this.trailPositions.length) {
            this.trailPointer = 0;
        }
        this.trailPositions[this.trailPointer] = currentPosition;
    }

    public Vec3 getTrailPosition(int pointer, float partialTick) {
        if (this.removed) {
            partialTick = 1.0F;
        }
        int i = this.trailPointer - pointer & 63;
        int j = this.trailPointer - pointer - 1 & 63;
        Vec3 d0 = this.trailPositions[j];
        Vec3 d1 = this.trailPositions[i].subtract(d0);
        return d0.add(d1.scale(partialTick));
    }

    @Override
    public void tick() {
        this.tickTrail();
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
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    @OnlyIn(Dist.CLIENT)
    public static final class Provider implements ParticleProvider<ParticleTrailOptions> {
        private final SpriteSet spriteSet;
        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(ParticleTrailOptions typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            TrailParticles particles = new TrailParticles(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, typeIn.getYaw(), typeIn.getPitch(), typeIn.getDuration(), typeIn.getR(), typeIn.getG(), typeIn.getB(), typeIn.getA(), typeIn.getScale(), typeIn.getFacesCamera(), typeIn.getBehavior(), typeIn.getTarget());
            particles.setSpriteFromAge(this.spriteSet);
            return particles;
        }
    }
}