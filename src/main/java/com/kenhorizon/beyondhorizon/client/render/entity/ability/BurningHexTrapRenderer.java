package com.kenhorizon.beyondhorizon.client.render.entity.ability;

import com.kenhorizon.beyondhorizon.client.render.AnimatedAbilityRenderer;
import com.kenhorizon.beyondhorizon.client.render.BHRenderTypes;
import com.kenhorizon.beyondhorizon.server.entity.ability.BurningHexTrapAbility;
import com.kenhorizon.beyondhorizon.server.entity.ability.EruptionAbility;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;

public class BurningHexTrapRenderer extends AnimatedAbilityRenderer<BurningHexTrapAbility> {
    public BurningHexTrapRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected int getBlockLightLevel(BurningHexTrapAbility entity, BlockPos blockPos) {
        return 15;
    }

    @Override
    public String getTextureLocation() {
        return "textures/entity/effect/burning_hex_trap";
    }

    @Override
    public int numberOfFrames() {
        return 1;
    }

    @Override
    public void render(BurningHexTrapAbility entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        RenderSystem.disableBlend();
        float radius = 0.05F * 12.85F;
        float rotation = (float) entity.tickCount + partialTicks;
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation * 2.25F - 90.0F));
        VertexConsumer vertexConsumer = buffer.getBuffer(BHRenderTypes.glowing(this.getTexture()));
        RenderSystem.setShader(GameRenderer::getRendertypeEntityTranslucentShader);
        float factor = ((float) entity.getLifeTime() / (entity.getDuration() + entity.getDelay()));
        float scale = (entity.getRadius() * (1.0F - factor));
        poseStack.scale(1.0F + scale, 1.0F, 1.0F + scale);
        RenderSystem.setShaderColor(1, 1, 1, 1.0F - factor);
        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F - entity.getYRot()));
        poseStack.translate(0.0D, -0.95D, 0.0D);
        renderParts(poseStack, vertexConsumer, radius, height, alpha, minTextureX, maxTextureX, minTextureY, maxTextureY, packedLight);
        poseStack.popPose();
    }
}
