package com.kenhorizon.beyondhorizon.mixins.common;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.api.accessory.AccessoryHelper;
import com.kenhorizon.libs.server.event.MobEffectModificationEvent;
import com.kenhorizon.beyondhorizon.server.entity.util.IBHDataEntity;
import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.tags.BHDamageTypeTags;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixins extends EntityMixins implements IBHDataEntity {
    @Unique
    private static final EntityDataAccessor<CompoundTag> DATA_BH_TAG_FLAGS = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.COMPOUND_TAG);
    @Unique
    private static final EntityDataAccessor<Byte> DATA_BH_SHARED_FLAGS = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.BYTE);

    @Shadow public abstract ItemStack getMainHandItem();

    @Inject(at = @At("TAIL"), method = "defineSynchedData()V")
    private void beyondhorizonRegisterData(CallbackInfo ci) {
        entityData.define(DATA_BH_TAG_FLAGS, new CompoundTag());
        entityData.define(DATA_BH_SHARED_FLAGS, (byte) 0);
    }

    @Inject(at = @At("TAIL"), method = "addAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V")
    private void beyondhorizonWriteAdditional(CompoundTag compoundNBT, CallbackInfo ci) {
        CompoundTag data = getEntityData();
        if (data != null) {
            compoundNBT.put("BeyondHorizonData", data);
        }
    }

    @Inject(at = @At("TAIL"), method = "readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V")
    private void beyondhorizonReadAdditional(CompoundTag compoundNBT, CallbackInfo ci) {
        if (compoundNBT.contains("BeyondHorizonData")) {
            setEntityData(compoundNBT.getCompound("BeyondHorizonData"));
        }
    }
    @Override
    public CompoundTag getEntityData() {
        return entityData.get(DATA_BH_TAG_FLAGS);
    }

    @Override
    public void setEntityData(CompoundTag nbt) {
        entityData.set(DATA_BH_TAG_FLAGS, nbt);
    }

    @Override
    public void setBHSharedFlags(int flag, boolean set) {
        byte b0 = this.entityData.get(DATA_BH_SHARED_FLAGS);
        if (set) {
            this.entityData.set(DATA_BH_SHARED_FLAGS, (byte)(b0 | 1 << flag));
        } else {
            this.entityData.set(DATA_BH_SHARED_FLAGS, (byte)(b0 & ~(1 << flag)));
        }
    }

    @Override
    public boolean getBHSharedFlags(int flag) {
        return (this.entityData.get(DATA_BH_SHARED_FLAGS) & 1 << flag) != 0;
    }

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

    @ModifyVariable(at = @At(value = "HEAD"), method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z",
            argsOnly = true, ordinal = 0)
    private MobEffectInstance beyondhorizonaddEffect(MobEffectInstance instance) {
        MobEffectModificationEvent event = new MobEffectModificationEvent(_this(), instance);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getEffectInstance();
    }


    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/DamageSource;is(Lnet/minecraft/tags/TagKey;)Z", ordinal = 7), method = "hurt")
    private boolean modifiedHurt(DamageSource instance, TagKey<DamageType> tag) {
        return instance.is(BHDamageTypeTags.NO_KNOCKBACK_DAMAGE) || instance.is(DamageTypeTags.IS_EXPLOSION);
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


    @SuppressWarnings("ConstantConditions")
    @Inject(at = @At("TAIL"), method = "canFreeze()Z", cancellable = true)
    public void curio$canFreeze(CallbackInfoReturnable<Boolean> cir) {
        if (_this() instanceof Player player && AccessoryHelper.isFreezeImmune(player)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "getBlockSpeedFactor", at = @At("RETURN"), cancellable = true)
    private void modifiedgetBlockSpeedFactor(CallbackInfoReturnable<Float> cir) {
        float newSpeedFactor = (float) Mth.lerp(_this().getAttributeValue(BHAttributes.MOVEMENT_EFFICIENCY.get()), cir.getReturnValue(), 1.0F);
        cir.setReturnValue(newSpeedFactor);
    }

    @Inject(at = @At("HEAD"), method = "getDamageAfterArmorAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F", cancellable = true)
    private void beyondHorizon$getDamageAfterArmorAbsorb(DamageSource damageSource, float amount, CallbackInfoReturnable<Float> cir) {
        float armorPenetration = 0;
        float lethality = 0;
        if (damageSource.getEntity() instanceof LivingEntity attacker) {
            armorPenetration = (float) attacker.getAttributeValue(BHAttributes.ARMOR_PENETRATION.get());
            lethality = (float) attacker.getAttributeValue(BHAttributes.LETHALITY.get());
        }
        if (!damageSource.is(DamageTypeTags.BYPASSES_ARMOR)) {
            this.hurtArmor(damageSource, amount);
            float toughness = (float) this.getAttributeValue(Attributes.ARMOR_TOUGHNESS);
            float armor = this.getArmorValue();
            float reduceArmor = (armor * (1.0F - armorPenetration)) - lethality;
            float resultDamage = CombatRules.getDamageAfterAbsorb(amount, reduceArmor, toughness);
            cir.setReturnValue(resultDamage);
        }
    }

    @Inject(at = @At("HEAD"), method = "getDamageAfterMagicAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F", cancellable = true)
    private void beyondHorizon$getDamageAfterMagicAbsorb(DamageSource damageSource, float amount, CallbackInfoReturnable<Float> cir) {
        float flatMagicPen = 0;
        float perMagicPen = 0;
        if (damageSource.getEntity() instanceof LivingEntity attacker) {
            flatMagicPen = (float) attacker.getAttributeValue(BHAttributes.FLAT_MAGIC_PENETRATION.get());
            perMagicPen = (float) attacker.getAttributeValue(BHAttributes.PERCENTAGE_MAGIC_PENETRATION.get());
        }
        if (damageSource.getEntity() instanceof LivingEntity entity && damageSource.getDirectEntity() == entity && damageSource.is(BHDamageTypeTags.MAGIC_DAMAGE)) {
            this.hurtArmor(damageSource, amount);
            float armor = (float) this.getAttributeValue(BHAttributes.MAGIC_RESISTANCE.get());
            float reduceArmor = (armor * (1.0F - perMagicPen)) - flatMagicPen;
            float resultDamage = CombatRules.getDamageAfterAbsorb(amount, reduceArmor, 0);
            cir.setReturnValue(resultDamage);
        }
    }
    @Unique
    protected LivingEntity _this() {
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
