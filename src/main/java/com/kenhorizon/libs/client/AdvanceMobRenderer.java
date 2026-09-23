package com.kenhorizon.libs.client;

import com.kenhorizon.beyondhorizon.server.level.entity.BHLibEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;

public abstract class AdvanceMobRenderer<T extends Mob, E extends EntityModel<T>> extends MobRenderer<T, E> {

    public AdvanceMobRenderer(EntityRendererProvider.Context context, E model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Override
    protected float getFlipDegrees(T entity) {
        if (entity instanceof BHLibEntity moddedEntity && moddedEntity.getAnimationDeath() != 0) {
            return 0;
        } else {
            return super.getFlipDegrees(entity);
        }
    }
}
