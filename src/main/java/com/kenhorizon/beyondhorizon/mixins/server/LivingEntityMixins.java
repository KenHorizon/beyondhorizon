package com.kenhorizon.beyondhorizon.mixins.server;

import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.tags.BHDamageTypeTags;
import net.minecraft.core.Holder;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixins extends EntityMixins  {

    @Inject(method = "decreaseAirSupply", at = @At("RETURN"), cancellable = true)
    private void modifiedDecreaseAirSupply(int currentAir, CallbackInfoReturnable<Integer> cir) {
        AttributeInstance respiration = _this().getAttribute(BHAttributes.OXYGEN_BONUS.get());
        float respirationBonus = EnchantmentHelper.getRespiration(_this());
        float bonusOxygen;
        if (respiration != null) {
            bonusOxygen = (float) (respirationBonus + respiration.getValue());
        } else {
            bonusOxygen = 0.0F;
        }
        int air = bonusOxygen > 0 && _this().getRandom().nextDouble() >= (double) 1.0F / (bonusOxygen + (double) 1.0F) ? currentAir : currentAir - 1;
        cir.setReturnValue(air);
    }

    @Inject(method = "calculateFallDamage", at = @At("RETURN"), cancellable = true)
    private void modifiedCalculateFallDamage(float fallDistance, float damageMultiplier, CallbackInfoReturnable<Integer> cir) {
        if (_this().getType().is(EntityTypeTags.FALL_DAMAGE_IMMUNE)) {
            cir.setReturnValue(0);
        } else {
            int damage = 0;
            MobEffectInstance instance = _this().getEffect(MobEffects.JUMP);
            float distance = instance == null ? 0.0F : (float) (instance.getAmplifier() + 1);
            double baseDamage = fallDistance - 3 - distance;
            damage = Mth.floor(baseDamage * damageMultiplier * _this().getAttributeValue(BHAttributes.FALLDAMAGE_MULTIPLIER.get()));
            cir.setReturnValue(damage);

        }
    }
    @Inject(at = @At("HEAD"), method = "getDamageAfterArmorAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F", cancellable = true)
    private void beyondHorizon$getDamageAfterArmorAbsorb(DamageSource damageSource, float amount, CallbackInfoReturnable<Float> cir) {
        if (!damageSource.is(DamageTypeTags.BYPASSES_ARMOR)) {
            this.hurtArmor(damageSource, amount);
            float armorPenetration = (float) getAttributeValue(BHAttributes.ARMOR_PENETRATION.get());
            float lethality = (float) this.getAttributeValue(BHAttributes.LETHALITY.get());
            float toughness = (float) this.getAttributeValue(Attributes.ARMOR_TOUGHNESS);
            float totalDamage = (amount * armorPenetration) + lethality;
            float damageTaken = amount - totalDamage;
            float reducedDamage = CombatRules.getDamageAfterAbsorb(damageTaken, this.getArmorValue(), toughness);
            float resultDamage = totalDamage + reducedDamage;
            cir.setReturnValue(resultDamage);
        }
        if (damageSource.is(BHDamageTypeTags.IS_TRUE_DAMAGE)) {
            this.hurtArmor(damageSource, amount);
            float trueDamage = 0.0F;
            if (damageSource.is(DamageTypes.INDIRECT_MAGIC)) {
                trueDamage = 0;
            } else {
                trueDamage += 1.0F;
            }
            float toughness = (float) getAttributeValue(Attributes.ARMOR_TOUGHNESS);
            float reducedDamage = CombatRules.getDamageAfterAbsorb(amount, (float) getArmorValue(), toughness);
            float resultDamage = trueDamage + reducedDamage;
            cir.setReturnValue(resultDamage);
        }
    }

    @Unique
    private LivingEntity _this() {
        return (LivingEntity) (Object) this;
    }


    @Shadow
    public void setHealth(float newHealth) {
        throw new IllegalStateException("Mixin failed to shadow the \"setHealth.hurtArmor(newHealth)\" method!");
    }

    @Shadow
    protected void hurtArmor(DamageSource source, float damage) {
        throw new IllegalStateException("Mixin failed to shadow the \"LivingEntity.hurtArmor(float)\" method!");
    }
    @Shadow
    public MobEffectInstance getEffect(MobEffect effect) {
        throw new IllegalStateException("Mixin failed to shadow the \"LivingEntity.getEffect(float)\" method!");
    }

    @Shadow
    public int getArmorValue() {
        throw new IllegalStateException("Mixin failed to shadow the \"LivingEntity.getArmorValue()\" method!");
    }

    @Shadow
    public double getAttributeValue(Attribute attribute) {
        throw new IllegalStateException("Mixin failed to shadow the \"LivingEntity.getAttributeValue(Attribute)\" method!");
    }

    @Shadow
    public ItemStack getItemInHand(InteractionHand hand) {
        throw new IllegalStateException("Mixin failed to shadow the \"LivingEntity.getItemInHand(InteractionHand)\" method!");
    }

    @Shadow
    private ItemStack getLastHandItem(EquipmentSlot slot) {
        throw new IllegalStateException("Mixin failed to shadow the \"LivingEntity.getLastHandItem(EquipmentSlot slot)\" method!");
    }

    @Shadow
    private ItemStack getLastArmorItem(EquipmentSlot slot) {
        throw new IllegalStateException("Mixin failed to shadow the \"LivingEntity.getLastArmorItem(EquipmentSlot slot)\" method!");
    }

    @Shadow
    protected void playBlockFallSound() {
        throw new IllegalStateException("Mixin failed to shadow the \"LivingEntity.playBlockFallSound()\" method!");
    }

    @Shadow
    private static EntityDataAccessor<Float> DATA_HEALTH_ID;

    @Shadow
    public float getHealth() {
        throw new IllegalStateException("Mixin failed to shadow the \"LivingEntity.getHealth()\" method!");
    }

    @Shadow
    public float getMaxHealth() {
        throw new IllegalStateException("Mixin failed to shadow the \"LivingEntity.getMaxHealth()\" method!");
    }

    @Shadow
    public boolean hasEffect(MobEffect effect) {
        throw new IllegalStateException("Mixin failed to shadow the \"LivingEntity.hasEffect(effect)\" method!");
    }

    @Shadow
    public double getAttributeValue(Holder<Attribute> attribute) {
        throw new IllegalStateException("Mixin failed to shadow the \"LivingEntity.getAttributeValue(attribute)\" method!");
    }

    @Shadow @Nullable public AttributeInstance getAttribute(Attribute attribute) {
        throw new IllegalStateException("Mixin failed to shadow the \"LivingEntity.getAttribute(Attribute attribute)\" method!");
    }

    @Shadow
    public RandomSource getRandom() {
        throw new IllegalStateException("Mixin failed to shadow the \"LivingEntity.getRandom()\" method!");
    }
}
