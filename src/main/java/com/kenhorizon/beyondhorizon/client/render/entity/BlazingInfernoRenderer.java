package com.kenhorizon.beyondhorizon.client.render.entity;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.model.entity.BlazingInfernoModel;
import com.kenhorizon.beyondhorizon.client.render.BHModelLayers;
import com.kenhorizon.beyondhorizon.client.render.BHRenderTypes;
import com.kenhorizon.beyondhorizon.server.entity.boss.blazing_inferno.BlazingInferno;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import com.mojang.math.Axis;

@OnlyIn(Dist.CLIENT)
public class BlazingInfernoRenderer extends MobRenderer<BlazingInferno, BlazingInfernoModel> {
    public static final ResourceLocation TEXTURE = BeyondHorizon.resource("textures/entity/blazing_inferno/blazing_inferno.png");
    public static final ResourceLocation TEXTURE_ENRAGED = BeyondHorizon.resource("textures/entity/blazing_inferno/blazing_inferno_enraged.png");
    public static final ResourceLocation TEXTURE_INACTIVE = BeyondHorizon.resource("textures/entity/blazing_inferno/blazing_inferno_inactive.png");
    public static final ResourceLocation EXPLOSION = BeyondHorizon.resource("textures/entity/blazing_inferno/blazing_inferno_explosion.png");
    private static final RenderType DECAL = RenderType.entityDecal(TEXTURE);
    private static final float HALF_SQRT_3 = (float) (Math.sqrt(3.0D) / 2.0D);

    public BlazingInfernoRenderer(EntityRendererProvider.Context context) {
        super(context, new BlazingInfernoModel(context.bakeLayer(BHModelLayers.BLAZING_INFERNO)), 0.5F);
    }

    @Override
    protected int getBlockLightLevel(BlazingInferno entity, BlockPos blockPos) {
        return 15;
    }

    @Override
    public void render(BlazingInferno entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        float yBodyRotation = Mth.rotLerp(partialTicks, entity.yBodyRotO, entity.yBodyRot);
        float yHeadRotation = Mth.rotLerp(partialTicks, entity.yHeadRotO, entity.yHeadRot);
        float rotation = yHeadRotation - yBodyRotation;
        float ageInTicks = this.getBob(entity, partialTicks);

        this.setupRotations(entity, poseStack, ageInTicks, yBodyRotation, partialTicks);
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        this.scale(entity, poseStack, partialTicks);
        poseStack.translate(0.0F, -1.501F, 0.0F);

        float entityWalkSpeed = 0.0F;
        float entityWalkPosition = 0.0F;

        if (entity.isAlive()) {
            entityWalkSpeed = entity.walkAnimation.speed(partialTicks);
            entityWalkPosition = entity.walkAnimation.position(partialTicks);
            if (entityWalkSpeed > 1.0F) {
                entityWalkSpeed = 1.0F;
            }
        }

        float entityXRotation = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
        if (isEntityUpsideDown(entity)) {
            entityXRotation *= -1.0F;
            yHeadRotation *= -1.0F;
        }
        this.model.prepareMobModel(entity, entityWalkPosition, entityWalkSpeed, partialTicks);
        this.model.setupAnim(entity, entityWalkPosition, entityWalkSpeed, ageInTicks, rotation, entityXRotation);
        boolean flag = entity.hurtTime > 0;
        if (entity.deathTime > 0) {
            float alpha = ((float) entity.deathTime / 200.0F);
            VertexConsumer renderModelExplosion = buffer.getBuffer(BHRenderTypes.explosionDeathEntity(EXPLOSION));
            this.model.renderToBuffer(poseStack, renderModelExplosion, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, alpha);
            VertexConsumer renderModelDecal = buffer.getBuffer(entityDecal(entity));
            this.model.renderToBuffer(poseStack, renderModelDecal, packedLight, OverlayTexture.pack(0.0F, flag), 1.0F, 1.0F, 1.0F, 1.0F);
        } else {
            float enragedProgress = entity.getEnragedProgress(partialTicks);
            float awakenProgress = entity.getAwakenProgress(partialTicks);

            VertexConsumer renderModel = buffer.getBuffer(RenderType.entityCutoutNoCull(this.getTextureLocation(entity)));
            this.model.renderToBuffer(poseStack, renderModel, packedLight, OverlayTexture.pack(0.0F, flag), 1.0F, 1.0F, 1.0F, 1.0F);
            if (awakenProgress != 1.0F) {
                VertexConsumer renderModelExplosion = buffer.getBuffer(RenderType.entityTranslucent(TEXTURE_INACTIVE));
                this.model.renderToBuffer(poseStack, renderModelExplosion, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F - awakenProgress);
            }
            if (enragedProgress != 1.0F) {
                VertexConsumer renderModelExplosion = buffer.getBuffer(RenderType.entityTranslucent(TEXTURE_ENRAGED));
                this.model.renderToBuffer(poseStack, renderModelExplosion, packedLight, OverlayTexture.pack(0.0F, flag), 1.0F, 1.0F, 1.0F, enragedProgress);
            }
        }
        if (entity.deathTime > 0 && entity.isEnraged()) {
            float f1 = ((float) entity.deathTime + partialTicks) / 200.0F;
            float f2 = Math.min(f1 > 0.8F ? (f1 - 0.8F) / 0.2F : 0.0F, 1.0F);
            RandomSource randomsource = RandomSource.create(432L);
            VertexConsumer vertexconsumer2 = buffer.getBuffer(RenderType.lightning());
            poseStack.pushPose();
            poseStack.translate(0.0F, 0.0F, 0.0F);

            for (int i = 0; (float) i < (f1 + f1 * f1) / 2.0F * 60.0F; ++i) {
                poseStack.mulPose(Axis.XP.rotationDegrees(randomsource.nextFloat() * 360.0F));
                poseStack.mulPose(Axis.YP.rotationDegrees(randomsource.nextFloat() * 360.0F));
                poseStack.mulPose(Axis.ZP.rotationDegrees(randomsource.nextFloat() * 360.0F));
                poseStack.mulPose(Axis.XP.rotationDegrees(randomsource.nextFloat() * 360.0F));
                poseStack.mulPose(Axis.YP.rotationDegrees(randomsource.nextFloat() * 360.0F));
                poseStack.mulPose(Axis.ZP.rotationDegrees(randomsource.nextFloat() * 360.0F + f1 * 90.0F));
                float f3 = randomsource.nextFloat() * 10.0F + 5.0F + f2 * 5.0F;
                float f4 = randomsource.nextFloat() * 2.0F + 1.0F + f2 * 2.0F;
                Matrix4f matrix4f = poseStack.last().pose();
                int j = (int) (255.0F * (1.0F - f2));
                vertex01(vertexconsumer2, matrix4f, j);
                vertex2(vertexconsumer2, matrix4f, f3, f4);
                vertex3(vertexconsumer2, matrix4f, f3, f4);
                vertex01(vertexconsumer2, matrix4f, j);
                vertex3(vertexconsumer2, matrix4f, f3, f4);
                vertex4(vertexconsumer2, matrix4f, f3, f4);
                vertex01(vertexconsumer2, matrix4f, j);
                vertex4(vertexconsumer2, matrix4f, f3, f4);
                vertex2(vertexconsumer2, matrix4f, f3, f4);
            }
            poseStack.popPose();
        }
        if (!entity.isSpectator()) {
            for (RenderLayer<BlazingInferno, BlazingInfernoModel> renderlayer : this.layers) {
                renderlayer.render(poseStack, buffer, packedLight, entity, entityWalkPosition, entityWalkSpeed, partialTicks, rotation, yHeadRotation, entityXRotation);
            }
        }
        poseStack.popPose();
    }

    @Override
    protected float getFlipDegrees(BlazingInferno entity) {
        return 0;
    }

    @Override
    public ResourceLocation getTextureLocation(BlazingInferno entity) {
        if (entity.isSleep()) {
            return TEXTURE_INACTIVE;
        } else {
            return TEXTURE;
        }
    }

    public RenderType entityDecal(BlazingInferno entity) {
        return RenderType.entityDecal(getTextureLocation(entity));
    }
    private void vertex01(VertexConsumer consumer, Matrix4f matrix4f, int alpha) {
        consumer.vertex(matrix4f, 0.0F, 0.0F, 0.0F).color(255, 255, 255, alpha).endVertex();
    }

    private void vertex2(VertexConsumer consumer, Matrix4f matrix4f, float uvX, float uvZ) {
        consumer.vertex(matrix4f, -HALF_SQRT_3 * uvZ, uvX, -0.5F * uvZ).color(255, 106, 0, 0).endVertex();
    }

    private void vertex3(VertexConsumer consumer, Matrix4f matrix4f, float uvX, float uvZ) {
        consumer.vertex(matrix4f, HALF_SQRT_3 * uvZ, uvX, -0.5F * uvZ).color(255, 106, 0, 0).endVertex();
    }

    private void vertex4(VertexConsumer consumer, Matrix4f matrix4f, float uvX, float uvZ) {
        consumer.vertex(matrix4f, 0.0F, uvX, 1.0F * uvZ).color(255, 106, 0, 0).endVertex();
    }

    private void renderGlowingParts(PoseStack poseStack, VertexConsumer vertexConsumer, float alpha, float red, float green, float blue, int packedLight) {
        PoseStack.Pose pose = poseStack.last();
        Matrix4f matrix4f = pose.pose();
        Matrix3f matrix3f = pose.normal();
        vertexGlowing(vertexConsumer, matrix4f, matrix3f, alpha ,red, green, blue, packedLight, 0.0F, 0, 0, 1);
        vertexGlowing(vertexConsumer, matrix4f, matrix3f, alpha ,red, green, blue, packedLight, 1.0F, 0, 1, 1);
        vertexGlowing(vertexConsumer, matrix4f, matrix3f, alpha ,red, green, blue, packedLight, 1.0F, 1, 1, 0);
        vertexGlowing(vertexConsumer, matrix4f, matrix3f, alpha ,red, green, blue, packedLight, 0.0F, 1, 0, 0);
    }
    private void vertexGlowing(VertexConsumer consumer, Matrix4f poseStack, Matrix3f normal, float alpha, float red, float green, float blue, int packedLight, float x, int y, int u, int v) {
        consumer.vertex(poseStack, x - 0.5F, (float)y - 0.25F, 0.0F).color(red, green, blue, alpha).uv((float)u, (float)v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
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
}
