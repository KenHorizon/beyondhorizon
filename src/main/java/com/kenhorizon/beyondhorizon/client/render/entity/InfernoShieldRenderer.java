package com.kenhorizon.beyondhorizon.client.render.entity;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.model.entity.InfernoShieldModel;
import com.kenhorizon.beyondhorizon.client.render.BHModelLayers;
import com.kenhorizon.beyondhorizon.server.entity.boss.blazing_inferno.InfernoShield;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class InfernoShieldRenderer extends EntityRenderer<InfernoShield> {
    private final InfernoShieldModel model;
    public static final ResourceLocation TEXTURE = BeyondHorizon.resource("textures/entity/blazing_inferno/blazing_inferno_shield.png");

    public InfernoShieldRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new InfernoShieldModel(context.bakeLayer(BHModelLayers.INFERNO_SHIELD));
    }

    @Override
    protected int getBlockLightLevel(InfernoShield entity, BlockPos blockPos) {
        return 15;
    }

    @Override
    public void render(InfernoShield entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(0.0D, 1.25D, 0.0D);
        Entity usingEntity = entity.getUsingEntity();
        if (usingEntity != null) {
            Vec3 usingEntityPosition = usingEntity.getPosition(partialTicks);
            Vec3 shieldPos = entity.getPosition(partialTicks);
            double d1 = usingEntityPosition.z - shieldPos.z;
            double d2 = usingEntityPosition.x - shieldPos.x;
            float f = (-((float) Mth.atan2(d2, d1)) * (180F / (float) Math.PI));
            poseStack.mulPose(Axis.YP.rotationDegrees(-f));
        }
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.mulPose(Axis.XN.rotationDegrees(entity.getViewXRot(partialTicks)));
        VertexConsumer builder = buffer.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(entity)));
        model.renderToBuffer(poseStack, builder, 240, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(InfernoShield entity) {
        return TEXTURE;
    }
}
