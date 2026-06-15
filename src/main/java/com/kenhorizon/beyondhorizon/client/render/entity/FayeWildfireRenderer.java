package com.kenhorizon.beyondhorizon.client.render.entity;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.model.entity.FayeFlaresModel;
import com.kenhorizon.beyondhorizon.client.model.entity.FayeWildfireModel;
import com.kenhorizon.beyondhorizon.client.render.BHModelLayers;
import com.kenhorizon.beyondhorizon.client.render.entity.layer.GenericEmissiveLayer;
import com.kenhorizon.beyondhorizon.server.entity.mobs.FayeFlares;
import com.kenhorizon.beyondhorizon.server.entity.mobs.FayeWildfire;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class FayeWildfireRenderer extends MobRenderer<FayeWildfire, FayeWildfireModel> {
    public static final ResourceLocation TEXTURE = BeyondHorizon.resource("textures/entity/flares/faye_wildfire.png");
    public static final ResourceLocation LAYER_LOCATION = BeyondHorizon.resource("textures/entity/flares/faye_wildfire_skull.png");

    public FayeWildfireRenderer(EntityRendererProvider.Context context) {
        super(context, new FayeWildfireModel(context.bakeLayer(BHModelLayers.FAYE_WILDFIRE)), 0.0F);
        this.addLayer(new GenericEmissiveLayer<>(this, LAYER_LOCATION));
    }

    @Override
    protected int getBlockLightLevel(FayeWildfire entity, BlockPos blockPos) {
        return 15;
    }

    @Override
    public ResourceLocation getTextureLocation(FayeWildfire entity) {
        return TEXTURE;
    }
}
