package com.kenhorizon.beyondhorizon.client.render.entity.ability;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.render.util.ColorUtil;
import com.kenhorizon.beyondhorizon.client.render.AnimatedAbilityRenderer;
import com.kenhorizon.beyondhorizon.client.render.BHRenderTypes;
import com.kenhorizon.beyondhorizon.client.render.tools.BeamRenderer;
import com.kenhorizon.beyondhorizon.server.entity.ability.FlameStrikeAbility;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class FlameStrikeRenderer extends AnimatedAbilityRenderer<FlameStrikeAbility> {

    private static final float TEXTURE_WIDTH = 128;
    private static final float TEXTURE_HEIGHT = 128;
    private static final float TEXTURE_SIZE = 128;
    private static final ResourceLocation WARNING_INDICATOR = BeyondHorizon.resource("textures/entity/effect/warning_indicator.png");
    private static final ResourceLocation TEXTURE = BeyondHorizon.resource("textures/entity/effect/flame_strike/flame_strike.png");
    public FlameStrikeRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected int getBlockLightLevel(FlameStrikeAbility entity, BlockPos blockPos) {
        return 15;
    }

    @Override
    public void render(FlameStrikeAbility entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        float alpha = 1.0F;
        float height = 1.0F ;
        float minTextureX = TEXTURE_SIZE / TEXTURE_WIDTH;
        float maxTextureX = minTextureX + TEXTURE_SIZE / TEXTURE_WIDTH;
        float minTextureY = TEXTURE_SIZE / TEXTURE_HEIGHT;
        float maxTextureY = minTextureY + TEXTURE_SIZE / TEXTURE_HEIGHT;
        RenderSystem.disableBlend();
        renderInner(entity, poseStack, buffer, packedLight, height, minTextureX, maxTextureX, minTextureY, maxTextureY);
        renderOuter(entity, poseStack, buffer, packedLight, height, alpha, minTextureX, maxTextureX, minTextureY, maxTextureY);
        BeamRenderer.renderBeam(poseStack, buffer, partialTicks, entity.level().getGameTime(), entity, 1.0F, 5.0F, ColorUtil.combineRGB(255, 0, 0));
    }

    private void renderOuter(FlameStrikeAbility entity, PoseStack poseStack, MultiBufferSource buffer, int packedLight, float height, float alpha, float minTextureX, float maxTextureX, float minTextureY, float maxTextureY) {
        poseStack.pushPose();
        float radius = 0.05F * 12.85F;
        VertexConsumer outer = buffer.getBuffer(BHRenderTypes.glowing(TEXTURE));
        RenderSystem.setShader(GameRenderer::getRendertypeEntityTranslucentShader);
        poseStack.scale((1.0F + entity.getRadius()), 1.0F, (1.0F + entity.getRadius()) );
        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F - entity.getYRot()));
        poseStack.translate(0.0D, -0.95D, 0.0D);
        renderParts(poseStack, outer, radius, height, alpha, minTextureX, maxTextureX, minTextureY, maxTextureY, packedLight);
        poseStack.popPose();
    }

    private void renderInner(FlameStrikeAbility entity, PoseStack poseStack, MultiBufferSource buffer, int packedLight, float height, float minTextureX, float maxTextureX, float minTextureY, float maxTextureY) {
        poseStack.pushPose();
        float radius = 0.05F * 12.85F;
        VertexConsumer inner = buffer.getBuffer(BHRenderTypes.glowing(WARNING_INDICATOR));
        RenderSystem.setShader(GameRenderer::getRendertypeEntityTranslucentShader);
        float factor = Mth.clamp((float) entity.getLifeTime() / entity.getDuration() + entity.getDelay(), 0, 1);
        poseStack.scale((1.0F + entity.getRadius()) * factor, 1.0F, (1.0F + entity.getRadius()) * factor);
        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F - entity.getYRot()));
        poseStack.translate(0.0D, -0.95D, 0.0D);
        renderParts(poseStack, inner, radius, height, 0.25F, minTextureX, maxTextureX, minTextureY, maxTextureY, packedLight);
        poseStack.popPose();
    }

    @Override
    public String getTextureLocation() {
        return "textures/entity/effect/flame_strike/flame_strike";
    }

    @Override
    public int numberOfFrames() {
        return 10;
    }
}
