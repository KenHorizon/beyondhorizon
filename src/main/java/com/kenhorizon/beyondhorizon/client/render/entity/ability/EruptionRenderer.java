package com.kenhorizon.beyondhorizon.client.render.entity.ability;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.level.util.ColorUtil;
import com.kenhorizon.beyondhorizon.client.render.AnimatedAbilityRenderer;
import com.kenhorizon.beyondhorizon.client.render.BHRenderTypes;
import com.kenhorizon.beyondhorizon.client.render.tools.BeamRenderer;
import com.kenhorizon.beyondhorizon.server.entity.ability.EruptionAbility;
import com.kenhorizon.beyondhorizon.server.entity.ability.FlameStrikeAbility;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class EruptionRenderer extends AnimatedAbilityRenderer<EruptionAbility> {
    public EruptionRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected int getBlockLightLevel(EruptionAbility entity, BlockPos blockPos) {
        return 15;
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
