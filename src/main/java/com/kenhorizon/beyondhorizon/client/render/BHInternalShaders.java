package com.kenhorizon.beyondhorizon.client.render;

import net.minecraft.client.renderer.ShaderInstance;

import javax.annotation.Nullable;

public class BHInternalShaders {
    private static ShaderInstance renderTypeTrailShader;
    private static ShaderInstance positionMarker;

    @Nullable
    public static ShaderInstance getRenderTypeTrailShader() {
        return renderTypeTrailShader;
    }

    @Nullable
    public static ShaderInstance getPositionMarker() {
        return positionMarker;
    }

    public static void setRenderTypeTrailShader(ShaderInstance instance) {
        renderTypeTrailShader = instance;
    }
    public static void setPositionMarker(ShaderInstance instance) {
        positionMarker = instance;
    }
}
