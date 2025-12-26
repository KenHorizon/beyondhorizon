package com.kenhorizon.beyondhorizon.client.render.entity.ability;

import com.kenhorizon.beyondhorizon.client.render.BHRenderTypes;
import com.kenhorizon.beyondhorizon.server.entity.ability.AbstractDeathRayAbility;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public abstract class AbstractLaserBeamRenderer extends EntityRenderer<AbstractDeathRayAbility> {
    private static final float TEXTURE_WIDTH = 256;
    private static final float TEXTURE_HEIGHT = 32;
    private static final float START_RADIUS = 1.3f;
    private static final float BEAM_RADIUS = 1;
    private float startRadius = START_RADIUS;
    private float beamSize = BEAM_RADIUS;
    private boolean clearerView = false;

    public AbstractLaserBeamRenderer(EntityRendererProvider.Context context, float beamSize, float startRadius) {
        super(context);
        this.startRadius = startRadius;
        this.beamSize = beamSize;
    }

    public AbstractLaserBeamRenderer(EntityRendererProvider.Context context) {
        this(context, BEAM_RADIUS, START_RADIUS);
    }

    @Override
    public void render(AbstractDeathRayAbility ability, float entityYaw, float delta, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
        clearerView = ability.caster instanceof Player && Minecraft.getInstance().player == ability.caster && Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON;
        double collidePosX = ability.prevCollidePosX + (ability.collidePosX - ability.prevCollidePosX) * delta;
        double collidePosY = ability.prevCollidePosY + (ability.collidePosY - ability.prevCollidePosY) * delta;
        double collidePosZ = ability.prevCollidePosZ + (ability.collidePosZ - ability.prevCollidePosZ) * delta;
        double posX = ability.xo + (ability.getX() - ability.xo) * delta;
        double posY = ability.yo + (ability.getY() - ability.yo) * delta;
        double posZ = ability.zo + (ability.getZ() - ability.zo) * delta;
        float yaw = ability.prevYaw + (ability.renderYaw - ability.prevYaw) * delta;
        float pitch = ability.prevPitch + (ability.renderPitch - ability.prevPitch) * delta;

        float length = (float) Math.sqrt(Math.pow(collidePosX - posX, 2) + Math.pow(collidePosY - posY, 2) + Math.pow(collidePosZ - posZ, 2));
        int frame = Mth.floor((ability.appear.getTimer() - 1 + delta) * 2);
        if (frame < 0) {
            frame = 6;
        }
        VertexConsumer ivertexbuilder = bufferIn.getBuffer(BHRenderTypes.glowing(getTextureLocation(ability)));
        renderStart(frame, poseStack, ivertexbuilder, packedLightIn);
        renderBeam(length, 180f / (float) Math.PI * yaw, 180f / (float) Math.PI * pitch, frame, poseStack, ivertexbuilder, packedLightIn);
        poseStack.pushPose();
        poseStack.translate(collidePosX - posX, collidePosY - posY, collidePosZ - posZ);
        renderEnd(frame, ability.blockSide, poseStack, ivertexbuilder, packedLightIn);
        poseStack.popPose();
    }

    private void renderFlatQuad(int frame, PoseStack matrixStackIn, VertexConsumer builder, int packedLightIn) {
        float minU = 0 + 16F / TEXTURE_WIDTH * frame;
        float minV = 0;
        float maxU = minU + 16F / TEXTURE_WIDTH;
        float maxV = minV + 16F / TEXTURE_HEIGHT;
        PoseStack.Pose matrixstack$entry = matrixStackIn.last();
        Matrix4f matrix4f = matrixstack$entry.pose();
        Matrix3f matrix3f = matrixstack$entry.normal();
        drawVertex(matrix4f, matrix3f, builder, -this.getStartRadius(), -this.getStartRadius(), 0, minU, minV, 1, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, -this.getStartRadius(), this.getStartRadius(), 0, minU, maxV, 1, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, this.getStartRadius(), this.getStartRadius(), 0, maxU, maxV, 1, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, this.getStartRadius(), -this.getStartRadius(), 0, maxU, minV, 1, packedLightIn);
    }

    private void renderStart(int frame, PoseStack matrixStackIn, VertexConsumer builder, int packedLightIn) {
        if (clearerView) {
            return;
        }
        matrixStackIn.pushPose();
        Quaternionf quat = this.entityRenderDispatcher.cameraOrientation();
        matrixStackIn.mulPose(quat);
        renderFlatQuad(frame, matrixStackIn, builder, packedLightIn);
        matrixStackIn.popPose();
    }

    private void renderEnd(int frame, Direction side, PoseStack matrixStackIn, VertexConsumer builder, int packedLightIn) {
        matrixStackIn.pushPose();
        Quaternionf quat = this.entityRenderDispatcher.cameraOrientation();
        matrixStackIn.mulPose(quat);
        renderFlatQuad(frame, matrixStackIn, builder, packedLightIn);
        matrixStackIn.popPose();
        if (side == null) {
            return;
        }
        matrixStackIn.pushPose();
        Quaternionf sideQuat = side.getRotation();
        sideQuat.mul(quatFromRotationXYZ(90, 0, 0, true));
        matrixStackIn.mulPose(sideQuat);
        matrixStackIn.translate(0, 0, -0.01f);
        renderFlatQuad(frame, matrixStackIn, builder, packedLightIn);
        matrixStackIn.popPose();
    }

    private void drawBeam(float length, int frame, PoseStack matrixStackIn, VertexConsumer builder, int packedLightIn) {
        float minU = 0;
        float minV = 16 / TEXTURE_HEIGHT + 1 / TEXTURE_HEIGHT * frame;
        float maxU = minU + 20 / TEXTURE_WIDTH;
        float maxV = minV + 1 / TEXTURE_HEIGHT;
        PoseStack.Pose matrixstack$entry = matrixStackIn.last();
        Matrix4f matrix4f = matrixstack$entry.pose();
        Matrix3f matrix3f = matrixstack$entry.normal();
        float offset = clearerView ? -1 : 0;
        drawVertex(matrix4f, matrix3f, builder, -this.getBeamSize(), offset, 0, minU, minV, 1, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, -this.getBeamSize(), length, 0, minU, maxV, 1, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, this.getBeamSize(), length, 0, maxU, maxV, 1, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, this.getBeamSize(), offset, 0, maxU, minV, 1, packedLightIn);
    }

    private void renderBeam(float length, float yaw, float pitch, int frame,  PoseStack matrixStackIn, VertexConsumer builder, int packedLightIn) {
        matrixStackIn.pushPose();
        matrixStackIn.mulPose(quatFromRotationXYZ(90, 0, 0, true));
        matrixStackIn.mulPose(quatFromRotationXYZ(0, 0, yaw - 90f, true));
        matrixStackIn.mulPose(quatFromRotationXYZ(-pitch, 0, 0, true));
        matrixStackIn.pushPose();
        if (!clearerView) {
            matrixStackIn.mulPose(quatFromRotationXYZ(0, Minecraft.getInstance().gameRenderer.getMainCamera().getXRot() + 90, 0, true));
        }
        drawBeam(length, frame, matrixStackIn, builder, packedLightIn);
        matrixStackIn.popPose();

        if (!clearerView) {
            matrixStackIn.pushPose();
            matrixStackIn.mulPose(quatFromRotationXYZ(0, -Minecraft.getInstance().gameRenderer.getMainCamera().getXRot() - 90, 0, true));
            drawBeam(length, frame, matrixStackIn, builder, packedLightIn);
            matrixStackIn.popPose();
        }
        matrixStackIn.popPose();
    }

    public void drawVertex(Matrix4f matrix, Matrix3f normals, VertexConsumer vertexBuilder, float offsetX, float offsetY, float offsetZ, float textureX, float textureY, float alpha, int packedLightIn) {
        vertexBuilder.vertex(matrix, offsetX, offsetY, offsetZ).color(1, 1, 1, 1 * alpha).uv(textureX, textureY).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLightIn).normal(normals, 0.0F, 1.0F, 0.0F).endVertex();
    }
    public Quaternionf quatFromRotationXYZ(float x, float y, float z, boolean degrees) {
        if (degrees) {
            x *= ((float)Math.PI / 180F);
            y *= ((float)Math.PI / 180F);
            z *= ((float)Math.PI / 180F);
        }
        return (new Quaternionf()).rotationXYZ(x, y, z);
    }

    public float getBeamSize() {
        return this.beamSize;
    }

    public float getStartRadius() {
        return this.startRadius;
    }

    public abstract ResourceLocation getTexture();

    @Override
    public ResourceLocation getTextureLocation(AbstractDeathRayAbility entity) {
        return this.getTexture();
    }
}
