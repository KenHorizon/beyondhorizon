package com.kenhorizon.beyondhorizon.client.render.entity;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.model.entity.FayeFlaresModel;
import com.kenhorizon.beyondhorizon.client.model.entity.PyrolligerModel;
import com.kenhorizon.beyondhorizon.client.render.BHModelLayers;
import com.kenhorizon.beyondhorizon.server.entity.boss.pyrolliger.Pyrolliger;
import com.kenhorizon.beyondhorizon.server.entity.mobs.FayeFlares;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class PyrolligerRenderer extends MobRenderer<Pyrolliger, PyrolligerModel> {
    public static final ResourceLocation TEXTURE = BeyondHorizon.resource("textures/entity/illager/pryolliger/pyrolliger.png");

    public PyrolligerRenderer(EntityRendererProvider.Context context) {
        super(context, new PyrolligerModel(context.bakeLayer(BHModelLayers.PYROLLIGER)), 0.5F);
    }

    @Override
    protected int getBlockLightLevel(Pyrolliger entity, BlockPos blockPos) {
        return 15;
    }

    @Override
    public ResourceLocation getTextureLocation(Pyrolliger entity) {
        return TEXTURE;
    }
}
