package com.kenhorizon.beyondhorizon.client.render.entity.misc;

import com.kenhorizon.beyondhorizon.server.entity.misc.BHFallingBlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

public class BHFallingBlocksRenderer extends EntityRenderer<BHFallingBlocks> {
//    private final BlockRenderDispatcher dispatcher;
    public BHFallingBlocksRenderer(EntityRendererProvider.Context context) {
        super(context);
//        this.dispatcher = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(BHFallingBlocks entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
        matrixStackIn.pushPose();
        if (entityIn.getMode() == BHFallingBlocks.FallingMoveType.OVERALL_MOVE) {
            matrixStackIn.translate(-0.5f, 0, -0.5f);
        } else {
            matrixStackIn.translate(0, 0.5f, 0);
            matrixStackIn.translate(0, Mth.lerp(partialTicks, entityIn.prevAnimY, entityIn.animY), 0);
            if (entityIn.getMode() == BHFallingBlocks.FallingMoveType.SIMULATE_RUPTURE) {
                matrixStackIn.mulPose(entityIn.getQuaternionf());
            }
            matrixStackIn.translate(0, -1, 0);
            matrixStackIn.translate(-0.5f, -0.5f, -0.5f);
        }
        dispatcher.renderSingleBlock(entityIn.getBlockState(), matrixStackIn, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY);
        matrixStackIn.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(BHFallingBlocks entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}