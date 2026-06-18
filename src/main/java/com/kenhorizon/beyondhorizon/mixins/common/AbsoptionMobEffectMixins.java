package com.kenhorizon.beyondhorizon.mixins.common;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import net.minecraft.world.effect.AbsoptionMobEffect;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.world.effect.AbsoptionMobEffect;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbsoptionMobEffect.class)
public abstract class AbsoptionMobEffectMixins {
    @Redirect(method = "addAttributeModifiers", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setAbsorptionAmount(F)V"))
    private void modifiedEffectAddAttrbiuteModifiers(LivingEntity instance, float absorptionAmount) {
        float value = (float) (absorptionAmount * instance.getAttributeValue(BHAttributes.SHIELDING.get()));
        BeyondHorizon.LOGGER.debug("Adding additional shield value! Base:{}, Final:{}", absorptionAmount, value);
        instance.setAbsorptionAmount((float) value);
    }
    @Redirect(method = "removeAttributeModifiers", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setAbsorptionAmount(F)V"))
    private void modifiedEffectRemoveAttrbiuteModifiers(LivingEntity instance, float absorptionAmount) {
        float value = (float) (absorptionAmount * instance.getAttributeValue(BHAttributes.SHIELDING.get()));
        BeyondHorizon.LOGGER.debug("removing additional shield value! Base:{}, Final:{}", absorptionAmount, value);
        instance.setAbsorptionAmount(instance.getAbsorptionAmount() - (float) value);
    }
}
