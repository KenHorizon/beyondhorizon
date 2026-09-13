package com.kenhorizon.beyondhorizon.client.render.entity.ability;

import com.kenhorizon.beyondhorizon.server.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.model.entity.ability.EntityCrossModel;
import com.kenhorizon.beyondhorizon.client.render.BHModelLayers;
import com.kenhorizon.beyondhorizon.client.render.BHRenderTypes;
import com.kenhorizon.beyondhorizon.server.entity.ability.LightningStrikeAbility;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.joml.Matrix4f;

public class LightningStrikeRenderer extends EntityRenderer<LightningStrikeAbility> {
    private final EntityCrossModel model;
    private static final float HALF_SQRT_3 = (float) (Math.sqrt(3.0D) / 2.0D);
    private static final ResourceLocation TEXTURE = BeyondHorizon.resource("textures/entity/effect/lightning_strike.png");
    public LightningStrikeRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new EntityCrossModel(context.bakeLayer(BHModelLayers.ENTITY_CROSS));
    }

    @Override
    public void render(LightningStrikeAbility entity,float entitYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        VertexConsumer builder = buffer.getBuffer(BHRenderTypes.beam(TEXTURE));
        int frames = 32;
        float flickerSpeed = 2.02F;
        float flicker = (float) ((entity.getLifeTime() * flickerSpeed % frames) / frames);
        float flickerEffect = Mth.clamp(flicker, 0.0F, 1.0F);
        float rotation = (float) entity.getLifeTime() + partialTicks;
        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F - (rotation * 2.25F)));
        poseStack.translate(0, -0.25F, 0);
        this.model.renderToBuffer(poseStack, builder, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, flickerEffect);
        poseStack.popPose();

        if (entity.hasStriked(10)) {
            float[] afloat = new float[8];
            float[] afloat1 = new float[8];
            float f = 0.0F;
            float f1 = 0.0F;
            RandomSource random1 = RandomSource.create(entity.seed);

            for (int i = 7; i >= 0; --i) {
                afloat[i] = f;
                afloat1[i] = f1;
                f += (float) (random1.nextInt(11) - 5);
                f1 += (float) (random1.nextInt(11) - 5);
            }
            poseStack.pushPose();
            VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.lightning());
            Matrix4f matrix4f = poseStack.last().pose();
            for (int j = 0; j < 4; ++j) {
                RandomSource random2 = RandomSource.create(entity.seed);

                for (int k = 0; k < 3; ++k) {
                    int l = 7;
                    int i1 = 0;
                    if (k > 0) {
                        l = 7 - k;
                    }

                    if (k > 0) {
                        i1 = l - 2;
                    }

                    float x1 = afloat[l] - f;
                    float z1 = afloat1[l] - f1;

                    for (int index = l; index >= i1; --index) {
                        float x2 = x1;
                        float z2 = z1;
                        if (k == 0) {
                            x1 += (float) (random2.nextInt(11) - 5);
                            z1 += (float) (random2.nextInt(11) - 5);
                        } else {
                            x1 += (float) (random2.nextInt(31) - 15);
                            z1 += (float) (random2.nextInt(31) - 15);
                        }
                        float f10 = 0.1F + (float) j * 0.2F;
                        if (k == 0) {
                            f10 *= (float) index * 0.1F + 0.5F;
                        }

                        float f11 = 0.1F + (float) j * 0.2F;
                        if (k == 0) {
                            f11 *= ((float) index - 0.5F) * 0.1F + 0.5F;
                        }

                        quad(matrix4f, vertexConsumer, x1, z1, index, x2, z2, 0.45F, 0.45F, 0.5F, f10, f11, false, false, true, false);
                        quad(matrix4f, vertexConsumer, x1, z1, index, x2, z2, 0.45F, 0.45F, 0.5F, f10, f11, true, false, true, true);
                        quad(matrix4f, vertexConsumer, x1, z1, index, x2, z2, 0.45F, 0.45F, 0.5F, f10, f11, true, true, false, true);
                        quad(matrix4f, vertexConsumer, x1, z1, index, x2, z2, 0.45F, 0.45F, 0.5F, f10, f11, false, true, false, false);
                    }
                }
            }
            poseStack.popPose();
        }
    }

    private void sparkEffect(LightningStrikeAbility entity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer) {
        float factor = ((float) entity.getLifeTime() + partialTicks) / entity.getDuration();
        float minimalSpinDegree = Math.min(factor > 0.8F ? (factor - 0.8F) / 0.2F : 0.0F, 1.0F);
        float spinFactor = 45.0F;
        RandomSource randoms = RandomSource.create(432L);
        VertexConsumer consumer = buffer.getBuffer(RenderType.lightning());
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.0F, 0.0F);

        for (int i = 0; (float) i < (factor + factor * factor) / 2.0F * 2.0F; ++i) {
            poseStack.mulPose(Axis.XP.rotationDegrees(randoms.nextFloat() * spinFactor));
            poseStack.mulPose(Axis.YP.rotationDegrees(randoms.nextFloat() * spinFactor));
            poseStack.mulPose(Axis.ZP.rotationDegrees(randoms.nextFloat() * spinFactor));
            poseStack.mulPose(Axis.XP.rotationDegrees(randoms.nextFloat() * spinFactor));
            poseStack.mulPose(Axis.YP.rotationDegrees(randoms.nextFloat() * spinFactor));
            poseStack.mulPose(Axis.ZP.rotationDegrees(randoms.nextFloat() * spinFactor + factor * 90.0F));
            float sizeX = randoms.nextFloat() * 2.0F + 1.0F + minimalSpinDegree * 1.11F;
            float sizeZ = randoms.nextFloat() * 2.0F + 1.0F + minimalSpinDegree * 2.0F;
            Matrix4f matrix4f = poseStack.last().pose();
            int j = (int) (255.0F * (1.0F - minimalSpinDegree));
            vertex01(consumer, matrix4f, j);
            vertex2(consumer, matrix4f, sizeX, sizeZ);
            vertex3(consumer, matrix4f, sizeX, sizeZ);
            vertex01(consumer, matrix4f, j);
            vertex3(consumer, matrix4f, sizeX, sizeZ);
            vertex4(consumer, matrix4f, sizeX, sizeZ);
            vertex01(consumer, matrix4f, j);
            vertex4(consumer, matrix4f, sizeX, sizeZ);
            vertex2(consumer, matrix4f, sizeX, sizeZ);
        }
        poseStack.popPose();
    }

    private void quad (Matrix4f matrix4f, VertexConsumer consumer,float x1, float z1, int index, float x2, float z2,
                       float red, float green, float blue, float p_115283_, float p_115284_, boolean p_115285_, boolean p_115286_,
                       boolean p_115287_, boolean p_115288_){
        consumer.vertex(matrix4f, x1 + (p_115285_ ? p_115284_ : -p_115284_), (float) (index * 8), z1 + (p_115286_ ? p_115284_ : -p_115284_)).color(red, green, blue, 0.3F).endVertex();
        consumer.vertex(matrix4f, x2 + (p_115285_ ? p_115283_ : -p_115283_), (float) ((index + 1) * 8), z2 + (p_115286_ ? p_115283_ : -p_115283_)).color(red, green, blue, 0.3F).endVertex();
        consumer.vertex(matrix4f, x2 + (p_115287_ ? p_115283_ : -p_115283_), (float) ((index + 1) * 8), z2 + (p_115288_ ? p_115283_ : -p_115283_)).color(red, green, blue, 0.3F).endVertex();
        consumer.vertex(matrix4f, x1 + (p_115287_ ? p_115284_ : -p_115284_), (float) (index * 8), z1 + (p_115288_ ? p_115284_ : -p_115284_)).color(red, green, blue, 0.3F).endVertex();
    }

    private void vertex01(VertexConsumer consumer, Matrix4f matrix4f, int alpha) {
        consumer.vertex(matrix4f, 0.0F, 0.0F, 0.0F).color(255, 255, 255, alpha).endVertex();
    }

    private void vertex2(VertexConsumer consumer, Matrix4f matrix4f, float uvX, float uvZ) {
        consumer.vertex(matrix4f, -0.5F * uvZ, uvX, -HALF_SQRT_3 * uvZ).color(255, 106, 0, 0).endVertex();
    }

    private void vertex3(VertexConsumer consumer, Matrix4f matrix4f, float uvX, float uvZ) {
        consumer.vertex(matrix4f, -0.5F * uvZ, uvX, HALF_SQRT_3 * uvZ).color(255, 106, 0, 0).endVertex();
    }

    private void vertex4(VertexConsumer consumer, Matrix4f matrix4f, float uvX, float uvZ) {
        consumer.vertex(matrix4f, 1.0F * uvZ, uvX, 0.0F).color(255, 106, 0, 0).endVertex();
    }

    @Override
    public ResourceLocation getTextureLocation (LightningStrikeAbility entity){
        return TextureAtlas.LOCATION_BLOCKS;
    }
}