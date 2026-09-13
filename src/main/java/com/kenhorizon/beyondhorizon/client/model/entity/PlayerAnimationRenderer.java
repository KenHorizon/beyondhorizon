package com.kenhorizon.beyondhorizon.client.model.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;

public class PlayerAnimationRenderer extends PlayerRenderer {
    public PlayerAnimationRenderer(EntityRendererProvider.Context context, boolean slim) {
        super(context, slim);
    }
}
