package com.kenhorizon.beyondhorizon.client.render.entity.ability;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class DeathLaserBeamRenderer extends AbstractLaserBeamRenderer {
    private static final ResourceLocation TEXTURE = BeyondHorizon.resource("textures/entity/laser_beam.png");

    public DeathLaserBeamRenderer(EntityRendererProvider.Context context, float beamSize, float startRadius) {
        super(context, beamSize, startRadius);
    }

    public DeathLaserBeamRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTexture() {
        return DeathLaserBeamRenderer.TEXTURE;
    }
}
