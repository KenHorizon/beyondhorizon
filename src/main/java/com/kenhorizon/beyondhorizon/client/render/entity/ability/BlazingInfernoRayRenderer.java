package com.kenhorizon.beyondhorizon.client.render.entity.ability;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class BlazingInfernoRayRenderer extends AbstractLaserBeamRenderer {
    private final float beamSize;
    private final float startRadius;
    private static final ResourceLocation TEXTURE = BeyondHorizon.resource("textures/entity/effect/blazing_inferno_ray.png");
    public BlazingInfernoRayRenderer(EntityRendererProvider.Context context, float beamSize, float startRadius) {
        super(context);
        this.beamSize = beamSize;
        this.startRadius = startRadius;
    }

    public BlazingInfernoRayRenderer(EntityRendererProvider.Context context) {
        this(context, 1.0F, 1.3F);
    }

    @Override
    public ResourceLocation getTexture() {
        return BlazingInfernoRayRenderer.TEXTURE;
    }

    @Override
    public float getBeamSize() {
        return this.beamSize;
    }

    @Override
    public float getStartRadius() {
        return this.startRadius;
    }
}
