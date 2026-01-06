package com.kenhorizon.beyondhorizon.client.render.tools;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.render.util.ColorUtil;
import com.kenhorizon.beyondhorizon.client.render.BHRenderTypes;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class BeamRenderer {
    public static final ResourceLocation DEFAULT_LOCATION = BeyondHorizon.resource("textures/entity/effect/beam.png");

    public static void renderBeam(PoseStack poseStack, MultiBufferSource buffer, float partialTicks, long worldTime, Entity entity, float radius, float height, int colors) {
        renderBeam(DEFAULT_LOCATION, poseStack, buffer, partialTicks, worldTime, entity, radius, height, colors);
    }

    public static void renderBeam(ResourceLocation texture, PoseStack poseStack, MultiBufferSource buffer, float partialTicks, long worldTime, Entity entity, float radius, float height, int colors) {
        RenderSystem.enableDepthTest();
        float beamAlpha = 0.85F;
        Minecraft minecraft = Minecraft.getInstance();
        if (entity != null) {
            if (minecraft.player != null && minecraft.player.distanceToSqr(entity) < 2f) {
                beamAlpha *= (float) minecraft.player.distanceToSqr(entity);
            }
        }
        float R = ColorUtil.getARGB(colors)[0] / 255f;
        float G = ColorUtil.getARGB(colors)[1] / 255f;
        float B = ColorUtil.getARGB(colors)[2] / 255f;

        float beamRadius = 0.05F * radius;
        float glowRadius = beamRadius + (beamRadius * 0.2F);
        float beamHeight = height;
        float yOffset = 0.0F;
        poseStack.pushPose();
        poseStack.pushPose();
        float rotation = (float) worldTime + partialTicks;
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation * 2.25F - 90.0F));
        poseStack.translate(0D, yOffset, 0D);
        poseStack.translate(0D, 1, 0D);
        poseStack.mulPose(Axis.XP.rotationDegrees(180));
        renderPart(poseStack, buffer.getBuffer(BHRenderTypes.beam(texture)), R, G, B, beamAlpha, beamHeight, 0.0F, beamRadius, beamRadius, 0.0F, -beamRadius, 0.0F, 0.0F, -beamRadius);
        poseStack.mulPose(Axis.XP.rotationDegrees(-180));
        renderPart(poseStack, buffer.getBuffer(BHRenderTypes.beam(texture)), R, G, B, beamAlpha, beamHeight, 0.0F, beamRadius, beamRadius, 0.0F, -beamRadius, 0.0F, 0.0F, -beamRadius);
        poseStack.popPose();

        //Render glow around main beam
        poseStack.translate(0D, yOffset, 0D);
        poseStack.translate(0D, 1, 0D);
        poseStack.mulPose(Axis.XP.rotationDegrees(180));
        renderPart(poseStack, buffer.getBuffer(BHRenderTypes.beam(texture)), R, G, B, beamAlpha * 0.4f, beamHeight, -glowRadius, -glowRadius, glowRadius, -glowRadius, -beamRadius, glowRadius, glowRadius, glowRadius);
        poseStack.mulPose(Axis.XP.rotationDegrees(-180));
        renderPart(poseStack, buffer.getBuffer(BHRenderTypes.beam(texture)), R, G, B, beamAlpha * 0.4f, beamHeight, -glowRadius, -glowRadius, glowRadius, -glowRadius, -beamRadius, glowRadius, glowRadius, glowRadius);
        poseStack.popPose();
    }

    public static void renderBeam(ResourceLocation texture, PoseStack poseStack, MultiBufferSource buffer, float partialTicks, long worldTime, BlockEntity entity, float radius, float height, int colors) {
        RenderSystem.enableDepthTest();
        float beamAlpha = 0.85F;
        float R = ColorUtil.getARGB(colors)[0] / 255f;
        float G = ColorUtil.getARGB(colors)[1] / 255f;
        float B = ColorUtil.getARGB(colors)[2] / 255f;
        float beamRadius = 0.05F * radius;
        float glowRadius = beamRadius + (beamRadius * 0.2F);
        float beamHeight = height;
        float yOffset = 0.0F;
        poseStack.pushPose();
        poseStack.pushPose();
        float rotation = (float) worldTime + partialTicks;
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation * 2.25F - 90.0F));
        poseStack.translate(0D, yOffset, 0D);
        poseStack.translate(0D, 1, 0D);
        poseStack.mulPose(Axis.XP.rotationDegrees(180));
        renderPart(poseStack, buffer.getBuffer(BHRenderTypes.beam(texture)), R, G, B, beamAlpha, beamHeight, 0.0F, beamRadius, beamRadius, 0.0F, -beamRadius, 0.0F, 0.0F, -beamRadius);
        poseStack.mulPose(Axis.XP.rotationDegrees(-180));
        renderPart(poseStack, buffer.getBuffer(BHRenderTypes.beam(texture)), R, G, B, beamAlpha, beamHeight, 0.0F, beamRadius, beamRadius, 0.0F, -beamRadius, 0.0F, 0.0F, -beamRadius);
        poseStack.popPose();

        //Render glow around main beam
        poseStack.translate(0D, yOffset, 0D);
        poseStack.translate(0D, 1, 0D);
        poseStack.mulPose(Axis.XP.rotationDegrees(180));
        renderPart(poseStack, buffer.getBuffer(BHRenderTypes.beam(texture)), R, G, B, beamAlpha * 0.4f, beamHeight, -glowRadius, -glowRadius, glowRadius, -glowRadius, -beamRadius, glowRadius, glowRadius, glowRadius);
        poseStack.mulPose(Axis.XP.rotationDegrees(-180));
        renderPart(poseStack, buffer.getBuffer(BHRenderTypes.beam(texture)), R, G, B, beamAlpha * 0.4f, beamHeight, -glowRadius, -glowRadius, glowRadius, -glowRadius, -beamRadius, glowRadius, glowRadius, glowRadius);
        poseStack.popPose();
    }
    private static void renderPart(PoseStack stack, VertexConsumer builder, float red, float green, float blue, float alpha, float height, float radius_1, float radius_2, float radius_3, float radius_4, float radius_5, float radius_6, float radius_7, float radius_8) {
        PoseStack.Pose matrixEntry = stack.last();
        Matrix4f matrix4f = matrixEntry.pose();
        Matrix3f matrix3f = matrixEntry.normal();
        renderQuad(matrix4f, matrix3f, builder, red, green, blue, alpha, height, radius_1, radius_2, radius_3, radius_4);
        renderQuad(matrix4f, matrix3f, builder, red, green, blue, alpha, height, radius_7, radius_8, radius_5, radius_6);
        renderQuad(matrix4f, matrix3f, builder, red, green, blue, alpha, height, radius_3, radius_4, radius_7, radius_8);
        renderQuad(matrix4f, matrix3f, builder, red, green, blue, alpha, height, radius_5, radius_6, radius_1, radius_2);
    }

    private static void renderQuad(Matrix4f pose, Matrix3f normal, VertexConsumer builder, float red, float green, float blue, float alpha, float y, float z1, float texu1, float z, float texu) {
        addVertex(pose, normal, builder, red, green, blue, alpha, y, z1, texu1, 1f, 0f);
        addVertex(pose, normal, builder, red, green, blue, alpha, 0f, z1, texu1, 1f, 1f);
        addVertex(pose, normal, builder, red, green, blue, alpha, 0f, z, texu, 0f, 1f);
        addVertex(pose, normal, builder, red, green, blue, alpha, y, z, texu, 0f, 0f);
    }

    private static void addVertex(Matrix4f pose, Matrix3f normal, VertexConsumer builder, float red, float green, float blue, float alpha, float y, float x, float z, float texu, float texv) {
        builder.vertex(pose, x, y, z).color(red, green, blue, alpha).uv(texu, texv).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
    }
}
