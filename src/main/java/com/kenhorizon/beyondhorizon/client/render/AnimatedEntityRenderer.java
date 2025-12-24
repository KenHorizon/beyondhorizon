package com.kenhorizon.beyondhorizon.client.render;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.entity.ability.AbstractAbilityEntity;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public abstract class AnimatedEntityRenderer<T extends AbstractAbilityEntity> extends EntityRenderer<T> {
    private float alpha = 1.0F;
    private float height = 1.0F;
    private final float minTextureX;
    private final float maxTextureX;
    private final float minTextureY;
    private final float maxTextureY;
    protected final ResourceLocation[] TEXTURE_PROGRESS = new ResourceLocation[this.NumberOfFrames()];
    private T entity;
    public AnimatedEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.minTextureX = (float) this.textureSize() / this.textureWidth();
        this.maxTextureX = this.minTextureX + (float) this.textureSize() / this.textureWidth();
        this.minTextureY = (float) this.textureSize() / this.textureHeight();
        this.maxTextureY = this.minTextureY + (float) this.textureSize() / this.textureHeight();

        for (int i = 0; i < this.NumberOfFrames(); i++){
            TEXTURE_PROGRESS[i] = this.getTextureLocation().withSuffix(String.valueOf(i));
        }
    }

    @Override
    public void render(T entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        RenderSystem.disableBlend();
        float radius = 0.05F * 12.85F;
        VertexConsumer vertexConsumer = buffer.getBuffer(BHRenderTypes.glowing(getTextureLocation(entity)));
        RenderSystem.setShader(GameRenderer::getRendertypeEntityTranslucentShader);
        poseStack.scale(1.0F, 1.0F, 1.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F - entity.getYRot()));
        poseStack.translate(0.0D, -0.95D, 0.0D);
        renderParts(poseStack, vertexConsumer, radius, height, alpha, minTextureX, maxTextureX, minTextureY, maxTextureY, packedLight);
        poseStack.popPose();
        super.render(entity, yaw, partialTicks, poseStack, buffer, packedLight);
    }
    private void renderParts(PoseStack poseStack, VertexConsumer vertexConsumer, float radius, float height, float alpha, float minTextureX, float maxTextureX, float minTextureY, float maxTextureY, int packedLight) {
        addQuads(poseStack, vertexConsumer, 0, 1.0F, 0 ,radius, height, alpha, minTextureX, maxTextureX, minTextureY, maxTextureY, packedLight);
    }
    private void renderParts(PoseStack poseStack, VertexConsumer vertexConsumer, float nX, float nY, float nZ, float radius, float height, float alpha, float minTextureX, float maxTextureX, float minTextureY, float maxTextureY, int packedLight) {
        addQuads(poseStack, vertexConsumer, nX, nY, nZ, radius, height, alpha, minTextureX, maxTextureX, minTextureY, maxTextureY, packedLight);
    }
    private void addQuads(PoseStack poseStack, VertexConsumer vertexConsumer, float nX, float nY, float nZ, float radius, float height, float alpha, float minTextureX, float maxTextureX, float minTextureY, float maxTextureY, int packedLight) {
        PoseStack.Pose pose = poseStack.last();
        Matrix4f matrix4f = pose.pose();
        Matrix3f matrix3f = pose.normal();
        this.vertex(matrix4f, matrix3f, vertexConsumer, -radius, height, -radius, minTextureX, minTextureY, nX, nY, nZ, packedLight, alpha);
        this.vertex(matrix4f, matrix3f, vertexConsumer, -radius, height, radius, minTextureX, maxTextureY, nX, nY, nZ, packedLight, alpha);
        this.vertex(matrix4f, matrix3f, vertexConsumer, radius, height, radius, maxTextureX, maxTextureY, nX, nY, nZ, packedLight, alpha);
        this.vertex(matrix4f, matrix3f, vertexConsumer, radius, height, -radius, maxTextureX, minTextureY, nX, nY, nZ, packedLight, alpha);
    }
    public void vertex(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer consumer, float x, float y, float z, float textureX, float textureY, float nX, float nY, float nZ, int packedLight, float alpha) {
        consumer.vertex(matrix4f, (float) x, (float) y, (float) z).color(1.0F, 1.0F, 1.0F, alpha).uv(textureX, textureY).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(matrix3f, (float)nX, (float)nY, (float)nZ).endVertex();
    }

    public int textureWidth() {
        return 16;
    }

    public int textureHeight() {
        return 16;
    }

    public int textureSize() {
        return 16;
    }

    public abstract ResourceLocation getTextureLocation();

    public abstract int NumberOfFrames();

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return this.animatedTextureLocation(entity, entity.getLifeTime() * (TEXTURE_PROGRESS.length) / entity.getDuration());
    }

    public ResourceLocation animatedTextureLocation(T entity, int age) {
        return TEXTURE_PROGRESS[Mth.clamp(age, 0, (this.NumberOfFrames() - 1))];
    }
}
