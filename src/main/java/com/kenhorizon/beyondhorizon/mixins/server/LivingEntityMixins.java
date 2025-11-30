package com.kenhorizon.beyondhorizon.mixins.server;

import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.tags.BHDamageTypeTags;
import net.minecraft.core.Holder;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixins extends EntityMixins {

    @Inject(at = @At("HEAD"), method = "getDamageAfterArmorAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F", cancellable = true)
    private void beyondHorizon$getDamageAfterArmorAbsorb(DamageSource damageSource, float amount, CallbackInfoReturnable<Float> cir) {
        if (damageSource.is(BHDamageTypeTags.IS_MAGIC_PENETRATION) && !damageSource.is(DamageTypeTags.BYPASSES_ARMOR) && damageSource.is(DamageTypes.MAGIC)) {
            hurtArmor(damageSource, amount);
            float armorPenetration = (float) this.getAttributeValue(BHAttributes.MAGIC_PENETRATION.get()); // Attributes
            float armorValue = (float) this.getAttributeValue(BHAttributes.MAGIC_RESISTANCE.get());
            float totalDamage = amount * armorPenetration;
            float damageTaken = amount - totalDamage;
            float reducedDamage = CombatRules.getDamageAfterAbsorb(damageTaken, armorValue, 1.0F);
            float resultDamage = totalDamage + reducedDamage;
            cir.setReturnValue(resultDamage);
        }
        if (damageSource.is(BHDamageTypeTags.IS_ARMOR_PENETRATION) && !damageSource.is(DamageTypeTags.BYPASSES_ARMOR)) {
            hurtArmor(damageSource, amount);
            float armorPenetration = (float) getAttributeValue(BHAttributes.MAGIC_PENETRATION.get()); // Attributes
            float lethality = (float) this.getAttributeValue(BHAttributes.LETHALITY.get());
            float toughness = (float) this.getAttributeValue(Attributes.ARMOR_TOUGHNESS);
            float lethalityArmor = this.getArmorValue() - lethality;
            float totalDamage = amount * armorPenetration;
            float damageTaken = amount - totalDamage;
            float reducedDamage = CombatRules.getDamageAfterAbsorb(damageTaken, lethalityArmor, toughness);
            float resultDamage = totalDamage + reducedDamage;
            cir.setReturnValue(resultDamage);
        }
        if (damageSource.is(BHDamageTypeTags.IS_TRUE_DAMAGE)) {
            hurtArmor(damageSource, amount);
            float trueDamage = 0.0F;
            if (damageSource.is(DamageTypes.INDIRECT_MAGIC)) {
                trueDamage = 0;
            } else {
                trueDamage += 1.0F; // Damage Source
            }
            float toughness = (float) getAttributeValue(Attributes.ARMOR_TOUGHNESS);
            float reducedDamage = CombatRules.getDamageAfterAbsorb(amount, (float) getArmorValue(), toughness);
            float resultDamage = trueDamage + reducedDamage;
            //BeyondHorizon.loggers().debug("Damage: {} True Damage: {} Target's Armor: {} Damage Reduce: {}", amount, trueDamage, toughness, reducedDamage);
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
