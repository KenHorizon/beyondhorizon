package com.kenhorizon.beyondhorizon.client.render.entity;

import com.kenhorizon.beyondhorizon.server.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.model.entity.FayeFlaresModel;
import com.kenhorizon.beyondhorizon.client.render.BHModelLayers;
import com.kenhorizon.beyondhorizon.server.entity.mobs.FayeFlares;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

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
