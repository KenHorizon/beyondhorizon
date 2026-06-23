package com.kenhorizon.beyondhorizon.client.render.entity.ability;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.model.entity.BlazingSpearModel;
import com.kenhorizon.beyondhorizon.client.model.entity.InfernalSlashModel;
import com.kenhorizon.beyondhorizon.client.render.AnimatedAbilityRenderer;
import com.kenhorizon.beyondhorizon.client.render.BHModelLayers;
import com.kenhorizon.beyondhorizon.server.entity.ability.EruptionAbility;
import com.kenhorizon.beyondhorizon.server.entity.ability.InfernalSlashAbility;
import com.kenhorizon.beyondhorizon.server.entity.projectiles.BlazingSpear;
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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class InfernalSlashRenderer extends EntityRenderer<InfernalSlashAbility> {
    private final InfernalSlashModel model;
    public static final ResourceLocation TEXTURE = BeyondHorizon.resource("textures/entity/effect/infernal_slash.png");
    public InfernalSlashRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new InfernalSlashModel(context.bakeLayer(BHModelLayers.INFERNAL_SLASH));
    }

    @Override
    protected int getBlockLightLevel(InfernalSlashAbility entity, BlockPos blockPos) {
        return 15;
    }
    @Override
    public void render(InfernalSlashAbility entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
//        float yRot = Mth.lerp(partialTicks, entity.yRotO, entity.getYRot());
//        float xRot = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
//
//        poseStack.mulPose(Axis.YP.rotationDegrees(yRot - 90.0F));
//        poseStack.mulPose(Axis.ZP.rotationDegrees(xRot + 90.0F));

        poseStack.mulPose(Axis.YP.rotationDegrees(entity.getYRot() + 180));
        VertexConsumer builder = buffer.getBuffer(RenderType.entityTranslucent(getTextureLocation(entity)));
        model.renderToBuffer(poseStack, builder, 240, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(InfernalSlashAbility entity) {
        return TEXTURE;
    }
}
