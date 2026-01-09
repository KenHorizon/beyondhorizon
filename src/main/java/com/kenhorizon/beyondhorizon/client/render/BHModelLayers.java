package com.kenhorizon.beyondhorizon.client.render;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.model.entity.BlazingInfernoModel;
import com.kenhorizon.beyondhorizon.client.model.entity.BlazingSpearModel;
import com.kenhorizon.beyondhorizon.client.model.entity.InfernoShieldModel;
import com.kenhorizon.beyondhorizon.client.model.entity.ability.HellfireOrbModel;
import com.kenhorizon.beyondhorizon.client.model.entity.ability.HellfireRodModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;

public class BHModelLayers {
    public static final CubeDeformation OUTER_ARMOR_DEFORMATION = new CubeDeformation(1.0F);
    public static final CubeDeformation INNER_ARMOR_DEFORMATION = new CubeDeformation(0.5F);
    public static final ModelLayerLocation WINGS = new ModelLayerLocation(ResourceLocation.parse("minecraft:player"), "wings");
    public static final ModelLayerLocation BLAZING_INFERNO = createLocation("blazing_inferno");
    public static final ModelLayerLocation INFERNO_SHIELD = createLocation("inferno_shield");
    public static final ModelLayerLocation BLAZING_SPEAR = createLocation("blazing_spear");
    public static final ModelLayerLocation HELLFIRE_ORB = createLocation("hellfire_orb");
    public static final ModelLayerLocation HELLFIRE_ROD = createLocation("hellfire_rod");

    public static void register(final EntityRenderersEvent.RegisterLayerDefinitions event) {
        BeyondHorizon.LOGGER.info("Registering Model Layers...");
        event.registerLayerDefinition(BLAZING_INFERNO, BlazingInfernoModel::createBodyLayer);
        event.registerLayerDefinition(INFERNO_SHIELD, InfernoShieldModel::createBodyLayer);
        event.registerLayerDefinition(BLAZING_SPEAR, BlazingSpearModel::createBodyLayer);
        event.registerLayerDefinition(HELLFIRE_ORB, HellfireOrbModel::createBodyLayer);
        event.registerLayerDefinition(HELLFIRE_ROD, HellfireRodModel::createBodyLayer);
    }
    private static ModelLayerLocation createOuterArmor(String model) {
        return createLocation(model, "outer_armor");
    }
    private static ModelLayerLocation createInnerArmor(String model) {
        return createLocation(model, "inner_armor");
    }
    private static ModelLayerLocation createLocation(String model) {
        return createLocation(model, "main");
    }
    private static ModelLayerLocation createLocation(String model, String layer) {
        return new ModelLayerLocation(BeyondHorizon.resource(model), layer);
    }
}
