package com.kenhorizon.beyondhorizon.mixins.server;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Entity.class)
public abstract class EntityMixins {

    @Shadow public Vec3 position() {
        throw new IllegalStateException("Mixin failed to shadow the \"Entity.position()\" method!");
    }

    @Shadow public final float getBbHeight() {
        throw new IllegalStateException("Mixin failed to shadow the \"Entity.getBbHeight()\" method!");
    }

    @Shadow public boolean isRemoved() {
        throw new IllegalStateException("Mixin failed to shadow the \"Entity.isRemoved()\" method!");
    }

    @Shadow public boolean isInvulnerableTo(DamageSource source) {
        throw new IllegalStateException("Mixin failed to shadow the \"Entity.isInvulnerableTo(DamageSource source)\" method!");
    }

    @Shadow public  double getRandomZ(double scale)  {
        throw new IllegalStateException("Mixin failed to shadow the \"Entity.getRandomZ(double scale)\" method!");
    }

    @Shadow public double getRandomY() {
        throw new IllegalStateException("Mixin failed to shadow the \"Entity.getRandomY()\" method!");
    }

    @Shadow public double getRandomX(double scale) {
        throw new IllegalStateException("Mixin failed to shadow the \"Entity.getRandomX(double scale)\" method!");
    }

    @Shadow @Final protected RandomSource random;

    @Shadow public boolean fireImmune()  {
        throw new IllegalStateException("Mixin failed to shadow the \"Entity.fireImmune()\" method!");
    }

    @Shadow public boolean isInvisible()  {
        throw new IllegalStateException("Mixin failed to shadow the \"Entity.isInvisible()\" method!");
    }

    @Shadow @Final protected SynchedEntityData entityData;

    @Shadow
    protected boolean getSharedFlag(int flag) {
        throw new IllegalStateException("Mixin failed to shadow the \"Entity.getSharedFlag()\" method!");
    }

    @Shadow
    public boolean isSpectator() {
        throw new IllegalStateException("Mixin failed to shadow the \"Entity.isSpectator()\" method!");
    }

    @Shadow
    public Level level() {
        throw new IllegalStateException("Mixin failed to shadow the \"Entity.level()\" method!");
    }

    @Shadow
    public float getYRot() {
        throw new IllegalStateException("Mixin failed to shadow the \"Entity.getYRot()\" method!");
    }

    @Shadow
    public final double getX(double random) {
        throw new IllegalStateException("Mixin failed to shadow the \"Entity.getY()\" method!");
    }

    @Shadow
    public final double getY(double random) {
        throw new IllegalStateException("Mixin failed to shadow the \"Entity.getY()\" method!");
    }

    @Shadow
    public final double getZ(double random) {
        throw new IllegalStateException("Mixin failed to shadow the \"Entity.getZ()\" method!");
    }
}
