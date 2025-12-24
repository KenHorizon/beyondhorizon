package com.kenhorizon.beyondhorizon.client.render.entity.ability;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.render.AnimatedEntityRenderer;
import com.kenhorizon.beyondhorizon.server.entity.ability.EruptionAbility;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class EruptionRenderer extends AnimatedEntityRenderer<EruptionAbility> {
    public EruptionRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation() {
        return BeyondHorizon.resource("textures/entity/effect/eruption/eruption");
    }

    @Override
    public int NumberOfFrames() {
        return 5;
    }
}
