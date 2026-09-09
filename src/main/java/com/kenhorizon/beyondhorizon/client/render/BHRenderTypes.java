package com.kenhorizon.beyondhorizon.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.BiFunction;
import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public class BHRenderTypes extends RenderType {
    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_TRAIL_SHADER = new RenderStateShard.ShaderStateShard(BHInternalShaders::getRenderTypeTrailShader);
    protected static final RenderStateShard.ShaderStateShard POSITION_MARKER = new RenderStateShard.ShaderStateShard(BHInternalShaders::getPositionMarker);

    public BHRenderTypes(String pName, VertexFormat pFormat, VertexFormat.Mode pMode, int pBufferSize, boolean pAffectsCrumbling, boolean pSortOnUpload, Runnable pSetupState, Runnable pClearState) {
        super(pName, pFormat, pMode, pBufferSize, pAffectsCrumbling, pSortOnUpload, pSetupState, pClearState);
    }

    public static RenderType dustedEffect(ResourceLocation resourceLocation) {
        return DUSTED_EFFECT.apply(resourceLocation);
    }

    public static RenderType beam(ResourceLocation resourceLocation) {
        return BEAM.apply(resourceLocation);
    }

    public static RenderType flickering(ResourceLocation resourceLocation) {
        return FLICKERING.apply(resourceLocation);
    }

    public static RenderType glowing(ResourceLocation resourceLocation) {
        return GLOWING_EFFECT.apply(resourceLocation);
    }

    public static RenderType positionMarker(ResourceLocation resourceLocation) {
        return MARKER.apply(resourceLocation);
    }

    public static RenderType swril(ResourceLocation resourceLocation, float pU, float pV) {
        RenderType.CompositeState state = RenderType.CompositeState.builder().setShaderState(RENDERTYPE_ENERGY_SWIRL_SHADER).setTextureState(new RenderStateShard.TextureStateShard(resourceLocation, false, false)).setTexturingState(new RenderStateShard.OffsetTexturingStateShard(pU, pV)).setTransparencyState(ADDITIVE_TRANSPARENCY).setCullState(NO_CULL).setLightmapState(LIGHTMAP).setOverlayState(OVERLAY).createCompositeState(false);
        return RenderType.create("swirl", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, false, state);
    }

    public static RenderType movingTexture(ResourceLocation resourceLocation, float pU, float pV) {
        RenderType.CompositeState state = RenderType.CompositeState.builder().setShaderState(RENDERTYPE_ENERGY_SWIRL_SHADER).setTextureState(new RenderStateShard.TextureStateShard(resourceLocation, false, false)).setTexturingState(new RenderStateShard.OffsetTexturingStateShard(pU, pV)).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setCullState(NO_CULL).setLightmapState(LIGHTMAP).setOutputState(RenderStateShard.ITEM_ENTITY_TARGET).setOverlayState(OVERLAY).setWriteMaskState(RenderStateShard.COLOR_DEPTH_WRITE).createCompositeState(true);
        return RenderType.create("moving_texture", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, false, state);
    }


    public static RenderType getTrailEffect(ResourceLocation resourceLocation) {
        return TRAIL_EFFECT.apply(resourceLocation);
    }


    public static RenderType getEntityTranslucentEmissive(ResourceLocation resourceLocation) {
        return ENTITY_TRANSLUCENT_EMISSIVE.apply(resourceLocation);
    }

    private static final Function<ResourceLocation, RenderType> ENTITY_TRANSLUCENT_EMISSIVE = Util.memoize(resourceLocation -> {
        RenderType.CompositeState state = RenderType.CompositeState.builder()
                .setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_EMISSIVE_SHADER)
                .setTextureState(new RenderStateShard.TextureStateShard(resourceLocation, false, false))
                .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                .setCullState(NO_CULL)
                .setWriteMaskState(COLOR_WRITE)
                .setOverlayState(OVERLAY)
                .createCompositeState(false);
        return create("entity_translucent_emissive", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, state);
    });

    private static final Function<ResourceLocation, RenderType> DUSTED_EFFECT = Util.memoize(resourceLocation -> {
                RenderType.CompositeState state = RenderType.CompositeState.builder().setShaderState(RENDERTYPE_ENTITY_ALPHA_SHADER)
                        .setTextureState(new RenderStateShard.TextureStateShard(resourceLocation, false, false))
                        .setCullState(NO_CULL)
                        .createCompositeState(true);
                return RenderType.create("dusted_effect", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, false, state);
            }
    );
    private static final Function<ResourceLocation, RenderType> BEAM = Util.memoize(resourceLocation -> {
        RenderType.CompositeState state = RenderType.CompositeState.builder()
                .setShaderState(RENDERTYPE_BEACON_BEAM_SHADER)
                .setTextureState(new RenderStateShard.TextureStateShard(resourceLocation, false, false))
                .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                .setWriteMaskState(RenderStateShard.COLOR_WRITE)
                .setCullState(NO_CULL).setOverlayState(OVERLAY)
                .createCompositeState(false);
        return RenderType.create("beam", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, state);
            }
    );
    private static final Function<ResourceLocation, RenderType> GLOWING_EFFECT = Util.memoize(resourceLocation -> {
                RenderType.CompositeState state = RenderType.CompositeState.builder()
                        .setTextureState(new TextureStateShard(resourceLocation, false, false))
                        .setShaderState(RENDERTYPE_BEACON_BEAM_SHADER)
                        .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                        .setCullState(NO_CULL)
                        .setOverlayState(OVERLAY)
                        .setWriteMaskState(COLOR_WRITE)
                        .createCompositeState(false);
                return RenderType.create("glowing", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256,true,true, state);
            }
    );

    private static final Function<ResourceLocation, RenderType> FLICKERING = Util.memoize(
            p_286169_ -> {
                RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder()
                        .setTextureState(new RenderStateShard.TextureStateShard(p_286169_, false, false))
                        .setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_CULL_SHADER)
                        .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                        .setCullState(NO_CULL)
                        .setLightmapState(LIGHTMAP)
                        .setOverlayState(OVERLAY)
                        .createCompositeState(false);


                return create("flickering", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256,false,true, rendertype$compositestate);
            }
    );
    private static final Function<ResourceLocation, RenderType> MARKER = Util.memoize(resourceLocation -> {
        RenderType.CompositeState state = RenderType.CompositeState.builder()
                .setShaderState(POSITION_MARKER)
                .setTextureState(new TextureStateShard(resourceLocation, false, false))
                .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                .setDepthTestState(NO_DEPTH_TEST)
                .setCullState(NO_CULL)
                .setLightmapState(LIGHTMAP)
                .setOutputState(RenderStateShard.OUTLINE_TARGET)
                .setOverlayState(OVERLAY)
                .setWriteMaskState(RenderStateShard.COLOR_DEPTH_WRITE)
                .createCompositeState(true);
        return RenderType.create("marker", BHVertextFormat.POSITION_MARKER, VertexFormat.Mode.QUADS, 256, false, false, state);
    });

    private static final Function<ResourceLocation, RenderType> TRAIL_EFFECT = Util.memoize(resourceLocation -> {
        CompositeState state = CompositeState.builder()
                .setShaderState(RENDERTYPE_ITEM_ENTITY_TRANSLUCENT_CULL_SHADER)
                .setTextureState(new TextureStateShard(resourceLocation, false, false))
                .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                .setOutputState(ITEM_ENTITY_TARGET)
                .setLightmapState(LIGHTMAP)
                .setCullState(NO_CULL)
                .setOverlayState(OVERLAY)
                .setWriteMaskState(RenderStateShard.COLOR_DEPTH_WRITE)
                .createCompositeState(true);
        return RenderType.create("entity_trail_effect", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 1536, true, true, state);
    });
}