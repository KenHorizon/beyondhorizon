package com.kenhorizon.beyondhorizon.client.render.entity.ability;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.entity.ability.AbstractDeathRayAbility;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class BlazingInfernoRayRenderer extends AbstractLaserBeamRenderer {
    private static final ResourceLocation TEXTURE = BeyondHorizon.resource("textures/entity/effect/blazing_inferno_ray.png");

    public BlazingInfernoRayRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(AbstractDeathRayAbility ability) {
        return TEXTURE;
    }
}
