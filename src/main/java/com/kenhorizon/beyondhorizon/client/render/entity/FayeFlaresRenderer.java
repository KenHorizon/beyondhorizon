package com.kenhorizon.beyondhorizon.client.render.entity;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.model.entity.BlazingInfernoModel;
import com.kenhorizon.beyondhorizon.client.model.entity.FayeFlaresModel;
import com.kenhorizon.beyondhorizon.client.model.entity.InfernoShieldModel;
import com.kenhorizon.beyondhorizon.client.render.BHModelLayers;
import com.kenhorizon.beyondhorizon.server.entity.boss.blazing_inferno.InfernoShield;
import com.kenhorizon.beyondhorizon.server.entity.mobs.FayeFlares;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class FayeFlaresRenderer extends MobRenderer<FayeFlares, FayeFlaresModel> {
    public static final ResourceLocation TEXTURE = BeyondHorizon.resource("textures/entity/flares/faye_flares.png");

    public FayeFlaresRenderer(EntityRendererProvider.Context context) {
        super(context, new FayeFlaresModel(context.bakeLayer(BHModelLayers.FAYE_FLARES)), 0.0F);
    }

    @Override
    protected int getBlockLightLevel(FayeFlares entity, BlockPos blockPos) {
        return 15;
    }

    @Override
    public ResourceLocation getTextureLocation(FayeFlares entity) {
        return TEXTURE;
    }
}
