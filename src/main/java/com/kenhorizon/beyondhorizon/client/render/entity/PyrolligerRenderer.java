package com.kenhorizon.beyondhorizon.client.render.entity;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.model.entity.PyrolligerModel;
import com.kenhorizon.beyondhorizon.client.render.BHModelLayers;
import com.kenhorizon.beyondhorizon.client.render.BHRenderTypes;
import com.kenhorizon.beyondhorizon.client.render.RenderUtils;
import com.kenhorizon.beyondhorizon.server.world.entity.boss.pyrolliger.Pyrolliger;
import com.kenhorizon.libs.client.AdvanceMobRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class PyrolligerRenderer extends AdvanceMobRenderer<Pyrolliger, PyrolligerModel> {
    public static final ResourceLocation TEXTURE = BeyondHorizon.resource("textures/entity/illager/pryolliger/pyrolliger.png");
    public static final ResourceLocation TEXTURE_AURA = BeyondHorizon.resource("textures/entity/illager/pryolliger/aura_effect.png");
    public static final RenderType AURA = BHRenderTypes.beam(TEXTURE_AURA);

    public PyrolligerRenderer(EntityRendererProvider.Context context) {
        super(context, new PyrolligerModel(context.bakeLayer(BHModelLayers.PYROLLIGER)), 0.5F);
    }

    @Override
    protected int getBlockLightLevel(Pyrolliger entity, BlockPos blockPos) {
        return entity.isOnFire() ? 15 : 0;
    }

    @Override
    public void render(Pyrolliger entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, yaw, partialTicks, poseStack, buffer, packedLight);
        float ageInTicks = this.getBob(entity, partialTicks);
        if (entity.isSecondPhase()) {
            poseStack.pushPose();
            float rotation = (float) entity.tickCount + partialTicks;
            poseStack.mulPose(Axis.YP.rotationDegrees(rotation * 2.25F - 180.0F));
            RenderUtils.circleOutline(poseStack, buffer.getBuffer(BHRenderTypes.beam(TEXTURE_AURA, this.xOffset(ageInTicks) % 1.0F, 0)), 1, 32, 1.0F, 0, 0, 1.0F);
            poseStack.popPose();
        }
    }

    protected float xOffset(float tickCount) {
        return tickCount * 0.5F;
    }

    @Override
    public ResourceLocation getTextureLocation(Pyrolliger entity) {
        return TEXTURE;
    }
}
