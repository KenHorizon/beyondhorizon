package com.kenhorizon.beyondhorizon.client.render.entity.ability;

import com.kenhorizon.beyondhorizon.client.level.util.ColorUtil;
import com.kenhorizon.beyondhorizon.client.render.AnimatedAbilityRenderer;
import com.kenhorizon.beyondhorizon.client.render.tools.BeamRenderer;
import com.kenhorizon.beyondhorizon.server.entity.ability.EruptionAbility;
import com.kenhorizon.beyondhorizon.server.entity.ability.FlameStrikeAbility;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class FlameStrikeRenderer extends AnimatedAbilityRenderer<FlameStrikeAbility> {
    public FlameStrikeRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public String getTextureLocation() {
        return "textures/entity/effect/flame_strike/flame_strike";
    }

    @Override
    public void render(FlameStrikeAbility entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, yaw, partialTicks, poseStack, buffer, packedLight);
        BeamRenderer.renderBeam(poseStack, buffer, partialTicks, entity.level().getGameTime(), entity, 1.0F, 5.0F, ColorUtil.combineRGB(255, 0, 0));
    }

    @Override
    public int numberOfFrames() {
        return 10;
    }
}
