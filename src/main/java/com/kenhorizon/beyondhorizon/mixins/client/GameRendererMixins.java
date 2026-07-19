package com.kenhorizon.beyondhorizon.mixins.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixins {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(at = @At("HEAD"), method = "bobHurt", cancellable = true)
    private void modifiedbobHurt(PoseStack poseStack, float partialTicks, CallbackInfo ci) {
        if (this.minecraft.getCameraEntity() instanceof LivingEntity entity) {
            if (entity.hurtTime <= 0 || entity.invulnerableTime <= 0 || entity.hurtDuration <= 0) {
                ci.cancel();
            }
        }
    }
}
