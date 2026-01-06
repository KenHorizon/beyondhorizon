package com.kenhorizon.beyondhorizon.mixins.common;

import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.damagesource.DamageSources;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DamageSources.class)
public class DamageSourcesMixins {
    @Inject(method = "<init>", at = @At("RETURN"))
    private void beyondhorizonCustomDamageSources(RegistryAccess registryAccess, CallbackInfo ci) {
        BHDamageTypes.init(registryAccess);
    }
}
