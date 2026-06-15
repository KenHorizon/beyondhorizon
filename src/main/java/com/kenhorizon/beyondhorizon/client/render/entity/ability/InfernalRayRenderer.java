package com.kenhorizon.beyondhorizon.client.render.entity.ability;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.entity.ability.AbstractDeathRayAbility;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class InfernalRayRenderer extends AbstractLaserBeamRenderer {
    private static final ResourceLocation TEXTURE = BeyondHorizon.resource("textures/entity/effect/infernal_ray.png");

    public InfernalRayRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(AbstractDeathRayAbility ability) {
        return TEXTURE;
    }
}
