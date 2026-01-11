package com.kenhorizon.beyondhorizon.client.render.blockentity;

import com.kenhorizon.beyondhorizon.server.block.spawner.data.BHBaseSpawner;
import com.kenhorizon.beyondhorizon.server.block.spawner.data.BaseSpawnerData;
import com.kenhorizon.beyondhorizon.server.block.entity.BaseSpawnerBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class BaseSpawnerRenderer implements BlockEntityRenderer<BaseSpawnerBlockEntity> {

    private final EntityRenderDispatcher entityRenderer;

    public BaseSpawnerRenderer(BlockEntityRendererProvider.Context context) {
        this.entityRenderer = context.getEntityRenderer();
    }

    @Override
    public void render(BaseSpawnerBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource pBuffer, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        poseStack.translate(0.5F, 0.0F, 0.5F);
        Level level = blockEntity.getLevel();
        if (level != null) {
            BHBaseSpawner spawner = blockEntity.getVoidSpawner();
            BaseSpawnerData data = spawner.getData();
            Entity entity = data.getOrCreateDisplayEntity(spawner, level, spawner.getState());
            if (entity != null) {
                float f = 0.53125F;
                float f1 = Math.max(entity.getBbWidth(), entity.getBbHeight());
                if ((double)f1 > 1.0D) {
                    f /= f1;
                }

                poseStack.translate(0.0F, 0.4F, 0.0F);
                poseStack.mulPose(Axis.YP.rotationDegrees((float) Mth.lerp((double) partialTick, data.getOSpin(), data.getSpin()) * 10.0F));
                poseStack.translate(0.0F, -0.2F, 0.0F);
                poseStack.mulPose(Axis.XP.rotationDegrees(-30.0F));
                poseStack.scale(f, f, f);
                this.entityRenderer.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, partialTick, poseStack, pBuffer, packedLight);
            }

        }
        poseStack.popPose();
    }
}
