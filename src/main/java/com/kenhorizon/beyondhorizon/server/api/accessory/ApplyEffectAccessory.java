package com.kenhorizon.beyondhorizon.server.api.accessory;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ApplyEffectAccessory extends AccessorySkill {
    protected int seconds = 0;
    protected int amplifier = 0;
    protected int maxStack = 3;
    protected double effectOccured = 1.0D;
    protected boolean isAmbient = false;
    protected boolean isVisible = true;
    protected boolean showIcon = true;
    protected boolean self = false;
    protected boolean onKilled = false;
    protected boolean stacking = false;
    protected MobEffect[] mobEffect;
    protected Random random = new Random();

    public ApplyEffectAccessory(int seconds, int amplifier, MobEffect... mobEffect) {
        this.mobEffect = mobEffect;
        this.seconds = seconds;
        this.amplifier = amplifier;
    }

    public ApplyEffectAccessory(int seconds, MobEffect... mobEffect) {
        this(seconds, 0, mobEffect);
    }

    public int getSeconds() {
        return seconds;
    }

    public int getAmplifier() {
        return amplifier;
    }

    public boolean isAmbient() {
        return isAmbient;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public boolean isShowIcon() {
        return showIcon;
    }

    public boolean isOnKilled() {
        return onKilled;
    }

    public boolean isSelf() {
        return self;
    }

    public boolean isStacking() {
        return this.stacking;
    }

    public double getEffectOccured() {
        return effectOccured;
    }

    public ApplyEffectAccessory showIcon(boolean showIcon) {
        this.showIcon = showIcon;
        return this;
    }
    public ApplyEffectAccessory visible(boolean isVisible) {
        this.isVisible = isVisible;
        return this;
    }
    public ApplyEffectAccessory ambient(boolean isAmbient) {
        this.isAmbient = isAmbient;
        return this;
    }
    public ApplyEffectAccessory onKilled(boolean onKilled) {
        this.onKilled = onKilled;
        return this;
    }
    public ApplyEffectAccessory self(boolean onSelf) {
        this.self = onSelf;
        return this;
    }
    public ApplyEffectAccessory isStacking(int maxStack) {
        this.stacking = true;
        this.maxStack = maxStack;
        return this;
    }
    public ApplyEffectAccessory chances(double chances) {
        this.effectOccured = Math.min(1.0D, chances);
        return this;
    }

    @Override
    protected MutableComponent tooltipDescription(ItemStack itemStack) {
        if (this.effectOccured == 1.0D) {
            return Component.translatable(String.format("%s.desc", this.getDescriptionId()), getSeconds());
        } else {
            return Component.translatable(String.format("%s.desc", this.getDescriptionId()), Maths.percentIntoDecimal(getEffectOccured()), getSeconds());
        }
    }

    private MobEffect getMobEffects() {
        List<MobEffect> randomEffect = Arrays.stream(this.mobEffect).toList();
        int index = this.random.nextInt(randomEffect.size());
        return randomEffect.get(index);
    }

    @Override
    public void onHitAttack(DamageSource damageSource, ItemStack itemStack, LivingEntity target, LivingEntity attacker, float damageDealt) {
        if (attacker == null || target == null) return;
        BeyondHorizon.LOGGER.info("List of effects {} and got {}", this.mobEffect, this.getMobEffects().getDescriptionId());
        if (this.isSelf() && !this.isOnKilled()) {
            BeyondHorizon.LOGGER.info("Apply effect on attacker");
            if (attacker.level().getRandom().nextDouble() <= getEffectOccured()) {
                if (this.stacking) {
                    this.stackingEffect(attacker);
                } else {
                    attacker.addEffect(new MobEffectInstance(this.getMobEffects(), Maths.sec(this.seconds), this.getAmplifier(), this.isAmbient(), this.isVisible(), this.isShowIcon()));
                }
            }
        } else {
            BeyondHorizon.LOGGER.info("Apply effect on target");

            if (target.level().getRandom().nextDouble() <= getEffectOccured()) {
                if (this.stacking) {
                    this.stackingEffect(target);
                } else {
                    target.addEffect(new MobEffectInstance(this.getMobEffects(), Maths.sec(this.seconds), this.getAmplifier(), this.isAmbient(), this.isVisible(), this.isShowIcon()));
                }
            }
        }
    }

    @Override
    public void onEntityKilled(DamageSource damageSource, LivingEntity attacker, LivingEntity target) {
        if (this.isSelf() && !this.isOnKilled()) {
            BeyondHorizon.LOGGER.info("Apply effect on attacker on killed");
            if (attacker.level().getRandom().nextDouble() <= this.effectOccured) {
                if (this.stacking) {
                    this.stackingEffect(attacker);
                } else {
                    attacker.addEffect(new MobEffectInstance(this.getMobEffects(), Maths.sec(this.seconds), this.amplifier, isAmbient(), isVisible(), isShowIcon()));
                }
            }
        } else {
            BeyondHorizon.LOGGER.info("Apply effect on target on killed");
            if (target.level().getRandom().nextDouble() <= this.effectOccured) {
                if (this.stacking) {
                    this.stackingEffect(target);
                } else {
                    target.addEffect(new MobEffectInstance(this.getMobEffects(), Maths.sec(this.seconds), this.amplifier, isAmbient(), isVisible(), isShowIcon()));
                }
            }
        }
    }

    private void stackingEffect(LivingEntity target) {
        MobEffectInstance targetEffect = target.getEffect(this.getMobEffects());
        int stackingAmplifier = this.amplifier;
        if (targetEffect != null) {
            stackingAmplifier = Math.min(stackingAmplifier + targetEffect.getAmplifier(), this.maxStack);
            target.removeEffectNoUpdate(this.getMobEffects());
        } else {
            --stackingAmplifier;
        }
        MobEffectInstance effectInstance = new MobEffectInstance(this.getMobEffects(), Maths.sec(this.seconds), stackingAmplifier, this.isAmbient, this.isVisible);
        target.addEffect(effectInstance);
    }
}
