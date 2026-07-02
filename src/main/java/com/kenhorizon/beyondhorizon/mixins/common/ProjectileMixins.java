package com.kenhorizon.beyondhorizon.mixins.common;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;

@Mixin(Projectile.class)
public class ProjectileMixins extends EntityMixins {
    @Shadow
    public Entity getOwner() {
        throw new IllegalStateException("Mixin failed to shadow the \"ProjectileMixin.getOwner()\" method!");
    }
}
