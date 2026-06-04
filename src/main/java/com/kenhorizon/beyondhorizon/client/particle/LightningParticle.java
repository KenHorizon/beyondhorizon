package com.kenhorizon.beyondhorizon.client.particle;

import com.kenhorizon.beyondhorizon.client.particle.world.LightningParticleOptions;
import com.kenhorizon.beyondhorizon.client.render.LightningBoltData;
import com.kenhorizon.beyondhorizon.client.render.LightningRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector4f;

import java.util.Random;

public class LightningParticle extends Particle {
    private int r ;
    private int g ;
    private int b ;
    private float toX;
    private float toY;
    private float toZ;
    private LightningRenderer lightningRender = new LightningRenderer();

    public LightningParticle(ClientLevel level, double x, double y, double z, float xSpeed, float ySpeed, float zSpeed, int r, int g, int b) {
        super(level, x, y, z);
        this.setSize(1, 1);
        this.gravity = 0.0F;
        this.lifetime = 5 + new Random().nextInt(3);
        this.toX = xSpeed;
        this.toY = ySpeed;
        this.toZ = zSpeed;
        this.r = r;
        this.g = g;
        this.b = b;

    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    public void render(VertexConsumer vertexConsumer, Camera camera, float partialTick) {
        Vec3 vec3 = camera.getPosition();
        PoseStack posestack = new PoseStack();
        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        float f = (float)(Mth.lerp((double)partialTick, this.xo, this.x) - vec3.x());
        float f1 = (float)(Mth.lerp((double)partialTick, this.yo, this.y) - vec3.y());
        float f2 = (float)(Mth.lerp((double)partialTick, this.zo, this.z) - vec3.z());
        float lerpAge = this.age + partialTick;
        float ageProgress = lerpAge / (float) this.lifetime;
        float scale = 1.85F;
        posestack.pushPose();
        posestack.translate(f, f1, f2);
        posestack.scale(scale, scale, scale);
        LightningBoltData.BoltRenderInfo lightningBoltData = new LightningBoltData.BoltRenderInfo(0.5F, 0.1F, 0.5F, 0.85F, new Vector4f((float) r /255, (float) g /255, (float) b /255, (1.0F - ageProgress) * 0.8F), 0.1F);
        LightningBoltData bolt = new LightningBoltData(lightningBoltData, Vec3.ZERO, new Vec3(toX, toY, toZ), 4)
                .size(0.05F)
                .lifespan(this.lifetime)
                .spawn(LightningBoltData.SpawnFunction.CONSECUTIVE);
        lightningRender.update(this, bolt, partialTick);
        lightningRender.render(partialTick, posestack, multibuffersource$buffersource);

        multibuffersource$buffersource.endBatch();
        posestack.popPose();
    }



    @OnlyIn(Dist.CLIENT)
    public static final class Provider implements ParticleProvider<LightningParticleOptions> {

        @Override
        public Particle createParticle(LightningParticleOptions typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            LightningParticle particle;
            particle = new LightningParticle(worldIn, x, y, z, (float)xSpeed, (float)ySpeed, (float)zSpeed, typeIn.getR(), typeIn.getG(), typeIn.getB());

            return particle;
        }
    }
}
