package com.kenhorizon.beyondhorizon.client.render.entity.ability;

import com.kenhorizon.beyondhorizon.server.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.entity.ability.beam.AbstractDeathRayAbility;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class TwilightRayRenderer extends AbstractLaserBeamRenderer {
    private static final ResourceLocation TEXTURE = BeyondHorizon.resource("textures/entity/effect/twilight_ray.png");

    public TwilightRayRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(AbstractDeathRayAbility ability) {
        return TEXTURE;
    }
}
