package com.kenhorizon.beyondhorizon.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BHRenderTypes extends RenderType {
    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_TRAIL_SHADER = new RenderStateShard.ShaderStateShard(BHInternalShaders::getRenderTypeTrailShader);
    protected static final RenderStateShard.ShaderStateShard POSITION_MARKER = new RenderStateShard.ShaderStateShard(BHInternalShaders::getPositionMarker);

    public BHRenderTypes(String pName, VertexFormat pFormat, VertexFormat.Mode pMode, int pBufferSize, boolean pAffectsCrumbling, boolean pSortOnUpload, Runnable pSetupState, Runnable pClearState) {
        super(pName, pFormat, pMode, pBufferSize, pAffectsCrumbling, pSortOnUpload, pSetupState, pClearState);
    }

    public static RenderType explosionDeathEntity(ResourceLocation resourceLocation) {
        RenderType.CompositeState state = RenderType.CompositeState.builder().setShaderState(RENDERTYPE_ENTITY_ALPHA_SHADER).setTextureState(new RenderStateShard.TextureStateShard(resourceLocation, false, false)).setCullState(NO_CULL).createCompositeState(true);
        return RenderType.create("explosion_death_entity", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, false, state);
    }

    public static RenderType getEntityNoCull(ResourceLocation locationIn) {
        RenderType.CompositeState state = RenderType.CompositeState.builder().setShaderState(RENDERTYPE_TRAIL_SHADER).setTextureState(new RenderStateShard.TextureStateShard(locationIn, false, false)).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setCullState(NO_CULL).setLightmapState(LIGHTMAP).setOutputState(RenderStateShard.ITEM_ENTITY_TARGET).setOverlayState(OVERLAY).setWriteMaskState(RenderStateShard.COLOR_DEPTH_WRITE).createCompositeState(true);
        return RenderType.create("entity_no_cull", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, false, state);
    }

    public static RenderType beam(ResourceLocation resourceLocation) {
        RenderType.CompositeState state = RenderType.CompositeState.builder().setShaderState(RENDERTYPE_BEACON_BEAM_SHADER).setTextureState(new RenderStateShard.TextureStateShard(resourceLocation, false, false)).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setWriteMaskState(COLOR_DEPTH_WRITE).createCompositeState(true);
        return RenderType.create("beam", DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 256, false, false, state);
    }

    public static RenderType glowing(ResourceLocation resourceLocation) {
        RenderStateShard.TextureStateShard shard = new RenderStateShard.TextureStateShard(resourceLocation, false, false);
        RenderType.CompositeState state = RenderType.CompositeState.builder().setShaderState(RENDERTYPE_BEACON_BEAM_SHADER).setTextureState(shard).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setCullState(NO_CULL).setWriteMaskState(COLOR_WRITE).setOverlayState(OVERLAY).createCompositeState(false);
        return RenderType.create("glowing", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, state);
    }

    public static RenderType positionMarker(ResourceLocation resourceLocation) {
        RenderStateShard.TextureStateShard shard = new RenderStateShard.TextureStateShard(resourceLocation, false, false);
        RenderType.CompositeState state = RenderType.CompositeState.builder().setShaderState(POSITION_MARKER).setTextureState(shard).setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY).setDepthTestState(NO_DEPTH_TEST).setCullState(NO_CULL).setLightmapState(LIGHTMAP).setOutputState(RenderStateShard.OUTLINE_TARGET).setOverlayState(OVERLAY).setWriteMaskState(RenderStateShard.COLOR_DEPTH_WRITE).createCompositeState(true);
        return RenderType.create("position_marker", BHVertextFormat.POSITION_MARKER, VertexFormat.Mode.QUADS, 256, false, false, state);
    }

    public static RenderType swril(ResourceLocation resourceLocation, float pU, float pV) {
        RenderType.CompositeState state = RenderType.CompositeState.builder().setShaderState(RENDERTYPE_ENERGY_SWIRL_SHADER).setTextureState(new RenderStateShard.TextureStateShard(resourceLocation, false, false)).setTexturingState(new RenderStateShard.OffsetTexturingStateShard(pU, pV)).setTransparencyState(ADDITIVE_TRANSPARENCY).setCullState(NO_CULL).setLightmapState(LIGHTMAP).setOverlayState(OVERLAY).createCompositeState(false);
        return RenderType.create("swirl", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, false, state);
    }

    public static RenderType movingTexture(ResourceLocation resourceLocation, float pU, float pV) {
        RenderType.CompositeState state = RenderType.CompositeState.builder().setShaderState(RENDERTYPE_ENERGY_SWIRL_SHADER).setTextureState(new RenderStateShard.TextureStateShard(resourceLocation, false, false)).setTexturingState(new RenderStateShard.OffsetTexturingStateShard(pU, pV)).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setCullState(NO_CULL).setLightmapState(LIGHTMAP).setOutputState(RenderStateShard.ITEM_ENTITY_TARGET).setOverlayState(OVERLAY).setWriteMaskState(RenderStateShard.COLOR_DEPTH_WRITE).createCompositeState(true);
        return RenderType.create("moving_texture", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, false, state);
    }
}