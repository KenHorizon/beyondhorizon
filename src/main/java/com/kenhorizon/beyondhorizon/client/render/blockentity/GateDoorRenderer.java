package com.kenhorizon.beyondhorizon.client.render.blockentity;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.model.blockentity.GateDoorModel;
import com.kenhorizon.beyondhorizon.client.render.BHModelLayers;
import com.kenhorizon.beyondhorizon.server.block.entity.GateBlockBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelData;

@OnlyIn(Dist.CLIENT)
public class GateDoorRenderer implements BlockEntityRenderer<GateBlockBlockEntity> {
    private final BlockRenderDispatcher dispatcher;
    private static final ResourceLocation TEXTURE = BeyondHorizon.resource("textures/block/gate.png");
    private final GateDoorModel model;
    public GateDoorRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new GateDoorModel(context.bakeLayer(BHModelLayers.GATE_DOOR));
        this.dispatcher = context.getBlockRenderDispatcher();
    }

    @Override
    public int getViewDistance() {
        return 256;
    }

    @Override
    public void render(GateBlockBlockEntity entity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int overlay) {

        poseStack.pushPose();
        poseStack.translate(0.5F, 1.5F, 0.5F);
        poseStack.mulPose(Axis.ZN.rotationDegrees(180.0F));
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        model.modelAnimations(entity, partialTick);
        model.renderToBuffer(poseStack, vertexConsumer, packedLight, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
        BlockState blockstate = entity.getBaseBlock();
        Level level = entity.getLevel();
        if (level != null && blockstate.getRenderShape() != RenderShape.INVISIBLE) {
            poseStack.pushPose();
            poseStack.translate(0.5D, 0.0D, 0.5D);
            BlockPos blockpos = entity.getBaseBlockPos();
            poseStack.translate(-0.5D, 0.0D, -0.5D);
            var model = this.dispatcher.getBlockModel(blockstate);
            for (var renderType : model.getRenderTypes(blockstate, RandomSource.create(blockstate.getSeed(entity.getBaseBlockPos())), ModelData.EMPTY)) {
                this.dispatcher.getModelRenderer().tesselateBlock(level, model, blockstate, blockpos, poseStack, buffer.getBuffer(renderType), false, RandomSource.create(), blockstate.getSeed(entity.getBaseBlockPos()), OverlayTexture.NO_OVERLAY, ModelData.EMPTY, renderType);
            }
            poseStack.popPose();
        }
    }
}
