package com.kenhorizon.beyondhorizon.client.particle;

import com.kenhorizon.beyondhorizon.client.particle.world.CircleLightningParticleOptions;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector4f;

import java.util.Random;

public class CircleLightningParticle extends Particle {
    private int r ;
    private int g ;
    private int b ;
    private float toX;
    private float toY;
    private float toZ;
    private LightningRenderer lightningRender = new LightningRenderer();

    public CircleLightningParticle(ClientLevel level, double x, double y, double z, float size, float xSpeed, float ySpeed, float zSpeed, int r, int g, int b) {
        super(level, x, y, z);
        this.setSize(6.0F, 6.0F);
        this.x = x;
        this.y = y;
        this.z = z;
        this.gravity = 0.0F;
        this.toX = xSpeed;
        this.toY = ySpeed;
        this.toZ = zSpeed;
        this.r = r;
        this.g = g;
        this.b = b;
        this.lifetime = 10;
        Vec3 lightningTo = new Vec3(xd - x, yd - y, zd - z);
        LightningBoltData.BoltRenderInfo boltData = new LightningBoltData.BoltRenderInfo(
                0.5F, 0.1F, 0.5F, 0.85F,
                new Vector4f(rCol /255, gCol /255, bCol /255, 0.8F), 0.1F);
        LightningBoltData bolt = new LightningBoltData(boltData, Vec3.ZERO, lightningTo, 5)
                .size(size)
                .lifespan(this.lifetime)
                .spawn(LightningBoltData.SpawnFunction.CONSECUTIVE);

        lightningRender.update(this, bolt, 1.0F);

    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }
    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.move(this.xd, this.yd, this.zd);
        }

    }
    public void render(VertexConsumer vertexConsumer, Camera camera, float partialTick) {
        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        Vec3 cameraPos = camera.getPosition();
        float x = (float) (Mth.lerp((double) partialTick, this.xo, this.x));
        float y = (float) (Mth.lerp((double) partialTick, this.yo, this.y));
        float z = (float) (Mth.lerp((double) partialTick, this.zo, this.z));
        PoseStack posestack = new PoseStack();
        posestack.pushPose();
        posestack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        posestack.translate(x, y, z);
        lightningRender.render(partialTick, posestack, multibuffersource$buffersource);
        multibuffersource$buffersource.endBatch();
        posestack.popPose();
    }



    @OnlyIn(Dist.CLIENT)
    public static final class Provider implements ParticleProvider<CircleLightningParticleOptions> {

        @Override
        public Particle createParticle(CircleLightningParticleOptions typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            CircleLightningParticle particle;
            particle = new CircleLightningParticle(worldIn, x, y, z, typeIn.getSize(), (float)xSpeed, (float)ySpeed, (float)zSpeed, typeIn.getR(), typeIn.getG(), typeIn.getB());

            return particle;
        }
    }
}
