package com.kenhorizon.beyondhorizon.mixins.common;

import com.kenhorizon.beyondhorizon.server.enchantment.*;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Optional;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixins implements IAttributeEnchantment, IAdditionalEnchantment {
    @Unique
    @Override
    public Optional<IAdditionalEnchantment> enchantmentCallback() {
        return Optional.of(this);
    }
}
