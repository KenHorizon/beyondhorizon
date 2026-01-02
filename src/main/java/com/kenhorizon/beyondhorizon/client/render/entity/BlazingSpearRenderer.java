package com.kenhorizon.beyondhorizon.client.render.entity;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.model.entity.BlazingSpearModel;
import com.kenhorizon.beyondhorizon.client.render.BHModelLayers;
import com.kenhorizon.beyondhorizon.server.entity.boss.blazing_inferno.BlazingSpear;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class BlazingSpearRenderer extends EntityRenderer<BlazingSpear> {
    private final BlazingSpearModel model;
    public static final ResourceLocation TEXTURE = BeyondHorizon.resource("textures/entity/blazing_inferno/blazing_inferno_spear.png");

    public BlazingSpearRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new BlazingSpearModel(context.bakeLayer(BHModelLayers.BLAZING_SPEAR));
    }

    @Override
    protected int getBlockLightLevel(BlazingSpear entity, BlockPos blockPos) {
        return 15;
    }

    @Override
    public void render(BlazingSpear entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0f));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot()) + 90.0f));
        VertexConsumer builder = buffer.getBuffer(RenderType.entityTranslucent(getTextureLocation(entity)));
        float getTransparency = entity.getFade();
        float alpha = Mth.clamp(getTransparency, 0, 1);
        model.renderToBuffer(poseStack, builder, 240, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, alpha);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(BlazingSpear entity) {
        return TEXTURE;
    }
}
