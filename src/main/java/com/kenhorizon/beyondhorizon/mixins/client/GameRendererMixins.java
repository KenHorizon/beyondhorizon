package com.kenhorizon.beyondhorizon.mixins.client;

import com.kenhorizon.beyondhorizon.server.api.event.DamageTiltEvent;
import com.kenhorizon.beyondhorizon.server.api.event.HarvestBlockEvent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
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
            DamageTiltEvent event = new DamageTiltEvent(entity);
            MinecraftForge.EVENT_BUS.post(event);
            if (entity.invulnerableTime <= 0 || entity.hurtTime <= 0 || entity.hurtDuration <= 0 || event.isCanceled()) {
                ci.cancel();
            }
        }
    }
}
