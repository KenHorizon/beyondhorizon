package com.kenhorizon.beyondhorizon.mixins.common;

import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixins {
    @Shadow
    @Final
    public RandomSource random;
    @Shadow
    @Final
    public int remainingFireTicks;

    @Inject(method = "setSecondsOnFire", at = @At("RETURN"), cancellable = true)
    private void modifiedSetSecondOnFire(int seconds, CallbackInfo ci) {
        int i = seconds * 20;
        if (_this() instanceof LivingEntity entity) {
            float multiplier = (float) entity.getAttributeValue(BHAttributes.BURNING_TIME.get());
            i = ProtectionEnchantment.getFireAfterDampener(entity, i);
            i *= (int) multiplier;
        }
        if (this.remainingFireTicks < i) {
            this.setRemainingFireTicks(i);
        }
        ci.cancel();
    }
    @Unique
    private Entity _this() {
        return (Entity) (Object) this;
    }

    @Shadow public void setRemainingFireTicks(int remainingFireTicks) {
        throw new IllegalStateException("Mixin failed to shadow the \"Entity.setRemainingFireTicks()\" method!");
    }

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
