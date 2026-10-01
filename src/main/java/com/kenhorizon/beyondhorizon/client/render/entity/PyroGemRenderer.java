package com.kenhorizon.beyondhorizon.client.render.entity;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.model.entity.InfernoShieldModel;
import com.kenhorizon.beyondhorizon.client.model.entity.PyroGemModel;
import com.kenhorizon.beyondhorizon.client.render.BHModelLayers;
import com.kenhorizon.beyondhorizon.client.render.RenderUtils;
import com.kenhorizon.beyondhorizon.server.world.entity.boss.blazing_inferno.InfernoShield;
import com.kenhorizon.beyondhorizon.server.world.entity.boss.pyrolliger.PyroGem;
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
public class PyroGemRenderer extends EntityRenderer<PyroGem> {
    private final PyroGemModel model;
    public static final ResourceLocation TEXTURE = BeyondHorizon.resource("textures/entity/illager/pryolliger/pyrogem.png");

    public PyroGemRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new PyroGemModel(context.bakeLayer(BHModelLayers.PYROGEM));
    }

    @Override
    protected int getBlockLightLevel(PyroGem entity, BlockPos blockPos) {
        return 15;
    }

    @Override
    public void render(PyroGem entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(0.0D, 1.25D, 0.0D);
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        float rotation = (float) entity.tickCount + partialTicks;
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation * 2.25F - 45.0F));
        VertexConsumer builder = buffer.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(entity)));
        model.renderToBuffer(poseStack, builder, 240, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(PyroGem entity) {
        return TEXTURE;
    }
}
