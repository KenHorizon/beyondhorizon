package com.kenhorizon.beyondhorizon.client.render.entity;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.model.entity.DragonHornetModel;
import com.kenhorizon.beyondhorizon.client.model.entity.FayeFlaresModel;
import com.kenhorizon.beyondhorizon.client.render.BHModelLayers;
import com.kenhorizon.beyondhorizon.server.entity.mobs.DragonHornet;
import com.kenhorizon.beyondhorizon.server.entity.mobs.FayeFlares;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class DragonHornetRenderer extends MobRenderer<DragonHornet, DragonHornetModel> {
    public static final ResourceLocation TEXTURE = BeyondHorizon.resource("textures/entity/dragon_hornet.png");

    public DragonHornetRenderer(EntityRendererProvider.Context context) {
        super(context, new DragonHornetModel(context.bakeLayer(BHModelLayers.DRAGON_HORNET)), 0.50F);
    }

    @Override
    public ResourceLocation getTextureLocation(DragonHornet entity) {
        return TEXTURE;
    }
}
