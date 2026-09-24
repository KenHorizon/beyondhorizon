package com.kenhorizon.beyondhorizon.client.render;

import com.kenhorizon.beyondhorizon.server.BeyondHorizon;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class RenderUtils {

    private static final ResourceLocation TEXTURE = BeyondHorizon.resource("textures/entity/effect/sheet.png");
    public static final RenderType SHEET = BHRenderTypes.beam(TEXTURE);

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
