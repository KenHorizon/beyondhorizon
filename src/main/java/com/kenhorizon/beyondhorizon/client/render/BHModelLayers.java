package com.kenhorizon.beyondhorizon.client.render;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.model.blockentity.GateDoorModel;
import com.kenhorizon.beyondhorizon.client.model.entity.*;
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
    public static final ModelLayerLocation FAYE_FLARES = createLocation("faye_flares");
    public static final ModelLayerLocation FAYE_WILDFIRE = createLocation("faye_wildfire");
    public static final ModelLayerLocation GATE_DOOR = createLocation("gate_door");
    public static final ModelLayerLocation BASE_SPEAR = createLocation("base_spear");
    public static final ModelLayerLocation PYROLLIGER = createLocation("pyrolliger");

    public static void register(final EntityRenderersEvent.RegisterLayerDefinitions event) {
        BeyondHorizon.LOGGER.info("Registering Model Layers...");
        event.registerLayerDefinition(BLAZING_INFERNO, BlazingInfernoModel::createBodyLayer);
        event.registerLayerDefinition(INFERNO_SHIELD, InfernoShieldModel::createBodyLayer);
        event.registerLayerDefinition(BLAZING_SPEAR, BlazingSpearModel::createBodyLayer);
        event.registerLayerDefinition(FAYE_FLARES, FayeFlaresModel::createBodyLayer);
        event.registerLayerDefinition(FAYE_WILDFIRE, FayeWildfireModel::createBodyLayer);
        event.registerLayerDefinition(GATE_DOOR, GateDoorModel::createBodyLayer);
        event.registerLayerDefinition(BASE_SPEAR, BaseSpearModel::createBodyLayer);
        event.registerLayerDefinition(PYROLLIGER, PyrolligerModel::createBodyLayer);
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
