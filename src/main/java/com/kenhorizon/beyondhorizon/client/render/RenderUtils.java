package com.kenhorizon.beyondhorizon.client.render;

import com.kenhorizon.beyondhorizon.server.BeyondHorizon;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class RenderUtils {

    private static final ResourceLocation FILLED_TEXTURE = BeyondHorizon.resource("textures/entity/effect/sheet.png");
    private static final ResourceLocation OUTLINE_TEXTURE = BeyondHorizon.resource("textures/entity/effect/beam.png");
    public static final RenderType SHEET = BHRenderTypes.beam(FILLED_TEXTURE);
    public static final RenderType OUTLINE = BHRenderTypes.beam(OUTLINE_TEXTURE);



    public static void circleOutline(PoseStack poseStack, VertexConsumer vertex, float radius, int segments,
                                     float r, float g, float b, float a) {
        poseStack.pushPose();
        PoseStack.Pose pose = poseStack.last();
        Matrix4f matrix = pose.pose();
        Matrix3f normal = pose.normal();
        float angleStep = (float) (Math.PI * 2.0 / segments);
        float height = 3.01F;
        float texture = 1.0F;
        float v1 = 0.0F;
        float v2 = texture * 0.5F;
        for (int i = 0; i < segments; i++) {
            float angle1 = i * angleStep;
            float angle2 = (i + 1) * angleStep;
            float x1 = (float) (Math.cos(angle1) * radius);
            float x2 = (float) (Math.cos(angle2) * radius);
            float z1 = (float) (Math.sin(angle1) * radius);
            float z2 = (float) (Math.sin(angle2) * radius);
            float u1 = (float) i / segments;
            float u2 = (float) (i + 1) / segments;
            float normX = (x1 + x2) * 0.5F;
            float normZ = (z1 + z2) * 0.5F;

            draw(matrix, normal, vertex, x1, 0.0F, z1, u1, v2, normX, 0.0F, normZ, r, g, b, a);
            draw(matrix, normal, vertex, x2, 0.0F, z2, u2, v2, normX, 0.0F, normZ, r, g, b, a);
            draw(matrix, normal, vertex, x2, height, z2, u2, v1, normX, 0.0F, normZ, r, g, b, a);
            draw(matrix, normal, vertex, x1, height, z1, u1, v1, normX, 0.0F, normZ, r, g, b, a);
        }
        poseStack.popPose();

    }

    public static void addQuads(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer vertexConsumer,
                                   float radius, float height, float r, float g, float b, float a) {
        draw(matrix4f, matrix3f, vertexConsumer, -radius, height, -radius, 1, 0, 0, 1.0F ,0, r, g, b, a);
        draw(matrix4f, matrix3f, vertexConsumer, -radius, height, radius, 0, 1, 0, 1.0F ,0, r, g, b, a);
        draw(matrix4f, matrix3f, vertexConsumer, radius, height, radius, 1, 1, 0, 1.0F ,0, r, g, b, a);
        draw(matrix4f, matrix3f, vertexConsumer, radius, height, -radius, 0, 0, 0, 1.0F ,0, r, g, b, a);
    }

    public static void circle(PoseStack poseStack, VertexConsumer vertex, float radius, int segments,
                                     float r, float g, float b, float a) {
        poseStack.pushPose();
        PoseStack.Pose pose = poseStack.last();
        Matrix4f matrix = pose.pose();
        Matrix3f normal = pose.normal();
        float angleStep = (float) (Math.PI * 2.0 / segments);
        for (int i = 0; i < segments; i++) {
            double angle1 = i * angleStep;
            double angle2 = (i + 1) * angleStep;
            draw(matrix, normal, vertex, 0, 0.01F, 0, 0, 0 ,0, 1.0F, 0, r, g, b, a);
            float x1 = (float) (Math.cos(angle1) * radius);
            float z1 = (float) (Math.sin(angle1) * radius);

            draw(matrix, normal, vertex, x1, 0.01F, z1, 1, 0 ,0, 1.0F ,0, r, g, b, a);
            float x2 = (float) (Math.cos(angle2) * radius);
            float z2 = (float) (Math.sin(angle2) * radius);
            draw(matrix, normal, vertex, x2, 0.01F, z2, 0, 1 ,0, 1.0F,0, r, g, b, a);
            draw(matrix, normal, vertex, x2, 0.01F, z2, 0, 1 ,0, 1.0F,0, r, g, b, a);
        }
        poseStack.popPose();
    }

    protected static void draw(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer consumer,
                               float x, float y, float z, float textureX, float textureY,
                               float nX, float nY, float nZ, float r, float g, float b, float alpha) {
        consumer.vertex(matrix4f,  x, y, z)
                .color(r, g, b, alpha)
                .uv(textureX, textureY)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(LightTexture.FULL_BRIGHT)
                .normal(matrix3f, nX, nY, nZ)
                .endVertex();
    }
}
