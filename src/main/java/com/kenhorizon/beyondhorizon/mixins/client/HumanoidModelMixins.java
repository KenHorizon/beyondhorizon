package com.kenhorizon.beyondhorizon.mixins.client;

import com.kenhorizon.libs.client.event.PlayerModelEvent;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixins extends Model {
    public HumanoidModelMixins(Function<ResourceLocation, RenderType> render) {
        super(render);
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At(value = "TAIL"))
    private void setupAnimations(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float yaw, float pitch, CallbackInfo ci) {
        PlayerModelEvent event = new PlayerModelEvent(entity, (HumanoidModel<?>) ((Model) this), limbSwing, limbSwingAmount, ageInTicks, yaw, pitch);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.getResult() == Event.Result.ALLOW) {
            ci.cancel();
        }
    }
}