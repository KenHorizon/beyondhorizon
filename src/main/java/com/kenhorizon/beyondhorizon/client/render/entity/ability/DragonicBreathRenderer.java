package com.kenhorizon.beyondhorizon.client.render.entity.ability;

import com.kenhorizon.beyondhorizon.server.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.level.entity.ability.beam.AbstractDeathRayAbility;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class DragonicBreathRenderer extends AbstractLaserBeamRenderer {
    private static final ResourceLocation TEXTURE = BeyondHorizon.resource("textures/entity/effect/dragonic_breath.png");

    public DragonicBreathRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public float getBeamSize() {
        return 3.45F;
    }

    @Override
    public float getStartRadius() {
        return 3.32F;
    }

    @Override
    public ResourceLocation getTextureLocation(AbstractDeathRayAbility ability) {
        return TEXTURE;
    }
}
