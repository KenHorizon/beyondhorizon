package com.kenhorizon.beyondhorizon.client.render.entity.ability;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.render.AnimatedAbilityRenderer;
import com.kenhorizon.beyondhorizon.server.entity.ability.EruptionAbility;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class EruptionRenderer extends AnimatedAbilityRenderer<EruptionAbility> {
    public EruptionRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public String getTextureLocation() {
        return "textures/entity/effect/eruption/eruption";
    }

    @Override
    public int numberOfFrames() {
        return 5;
    }
}
