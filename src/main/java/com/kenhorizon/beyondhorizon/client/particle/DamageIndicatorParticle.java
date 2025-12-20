package com.kenhorizon.beyondhorizon.client.particle;

import com.kenhorizon.beyondhorizon.client.particle.world.DamageIndicator;
import com.kenhorizon.beyondhorizon.mixins.client.accessor.FontAccessor;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

public class DamageIndicatorParticle extends TextureSheetParticle {
    private final Component text;
    private float factor = 0.02F;
    private float prevFactor = 0.02F;
    private int transparency = 0x11;
    private int prevTransparency = 0x11;
    private final boolean big;

    protected DamageIndicatorParticle(ClientLevel level, double x, double y, double z, Component damage, boolean big) {
        super(level, x, y, z);
        this.text = damage;
        this.lifetime = 30;
        this.big = big;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    @Override
    public void render(VertexConsumer vertexConsumer, Camera renderInfo, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();
        Vec3 camera = renderInfo.getPosition();
        double dx = this.x - camera.x;
        double dy = Mth.lerp(partialTicks, this.yo, this.y) - camera.y;
        double dz = this.z - camera.z;
        PoseStack poseStack = new PoseStack();
        poseStack.pushPose();
        poseStack.translate(dx, dy + (minecraft.font.lineHeight / 16.0D), dz);
        poseStack.mulPose(renderInfo.rotation());
        poseStack.mulPose(Axis.ZP.rotationDegrees(180));
        float newFactor = Mth.lerp(partialTicks, this.prevFactor, this.factor);
        poseStack.scale(newFactor, newFactor, newFactor);
        int width = minecraft.font.width(this.text);
        Matrix4f matrix = new Matrix4f(poseStack.last().pose());
        ((FontAccessor) (minecraft.font)).callRenderText(this.text.getVisualOrderText(),
                -width / 2f, 0, Mth.lerpInt(partialTicks, this.prevTransparency, this.transparency) << 24,
                false, matrix, bufferSource, Font.DisplayMode.NORMAL, 0, getLightColor(partialTicks));
        matrix.translate(0, 0, 0.03f);
        ((FontAccessor) (minecraft.font)).callRenderText(this.text.getVisualOrderText(),
                -width / 2f, 0, Mth.lerpInt(partialTicks, this.prevTransparency, this.transparency) << 24,
                true, matrix, bufferSource, Font.DisplayMode.NORMAL, 0, getLightColor(partialTicks));
        bufferSource.endBatch();
        poseStack.popPose();
    }

    @Override
    public void tick() {
        this.prevTransparency = this.transparency;
        this.prevFactor = this.factor;
        this.yo = this.y;
        if (this.age <= 4) {
            this.transparency = Math.min(this.transparency + 80, 255);
            this.factor = this.cubicBezier(this.age / 5f, 0, 1, 1, 1) * (this.big ? 0.06f : 0.04f);
        }
        float add = this.cubicBezier(this.age / (float)this.lifetime, 0,0.8F,1.0F,1) * 0.1F;

        float yOffset = 0.1f - add;
        this.y += yOffset;
        if(this.age >= this.lifetime - 3){
            this.transparency = Math.max(this.transparency - 60, 0);
        }
        if (this.age++ >= this.lifetime) {
            this.remove();
        }
    }
    public float cubicBezier(float t, float p0, float p1, float p2, float p3){
        float u = 1 - t;
        float tt = t * t;
        float uu = u * u;
        float uuu = uu * u;
        float ttt = tt * t;

        return uuu * p0 + 3 * uu * t * p1 + 3 * u * tt * p2 + ttt * p3;
    }
    @Override
    protected int getLightColor(float partialTick){
        return 15 << 20 | 15 << 4;
    }
    public static class Provider implements ParticleProvider<DamageIndicator> {
        @Nullable
        @Override
        public Particle createParticle(@NotNull DamageIndicator options, @NotNull ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed){
            return new DamageIndicatorParticle(pLevel, pX, pY, pZ, options.getDamage(), options.isBig());
        }
    }
}
