package com.kenhorizon.beyondhorizon.mixins.server;

import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import com.kenhorizon.beyondhorizon.server.item.ICustomHitSound;
import com.kenhorizon.beyondhorizon.server.item.ICustomSweepParticle;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixins extends LivingEntityMixins  {

    @Redirect(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getSweepingDamageRatio(Lnet/minecraft/world/entity/LivingEntity;)F"))
    private float getSweepingDamageRatio(LivingEntity entityIn) {
        AttributeInstance instance = entityIn.getAttribute(BHAttributes.SWEEP_DAMAGE.get());
        float damageRatio;
        if (instance == null) {
            damageRatio = EnchantmentHelper.getSweepingDamageRatio(entityIn);
        } else {
            float damage = (float)entityIn.getAttributeValue(Attributes.ATTACK_DAMAGE);
            float resultDamage = (float) (damage * instance.getValue());
            damageRatio = (float) (((resultDamage - 1.0F) / resultDamage) * instance.getValue());
        }
        return damageRatio;
    }
    @Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/player/Player;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V", ordinal = 3), cancellable = true)
    private void getAttackSoundHitParticle(Entity entity, CallbackInfo ci) {
        ItemStack itemStack = getMainHandItem();
        if (entity instanceof LivingEntity target) {
            if (itemStack.getItem() instanceof ICustomHitSound hitSoundItem) {
                if (hitSoundItem.hitSound(level(), _this(), target)) {
                    ci.cancel();
                }
            }
        }
    }
    @Inject(method = "sweepAttack", at = @At(value = "HEAD"), cancellable = true)
    private void getAttackCustomSweepParticles(CallbackInfo ci) {
        ItemStack itemStack = getMainHandItem();
        if (itemStack.getItem() instanceof ICustomSweepParticle sweepParticleItem) {
            if (level() instanceof ServerLevel) {
                if (sweepParticleItem.sweepParticles(_this())) {
                    ci.cancel();
                }
            }
        }
    }

    @Unique
    protected Player _this() {
        return (Player) (Object) this;
    }
}
