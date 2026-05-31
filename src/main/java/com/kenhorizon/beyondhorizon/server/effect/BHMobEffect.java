package com.kenhorizon.beyondhorizon.server.effect;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.capability.CapabilityCaller;
import com.kenhorizon.beyondhorizon.server.capability.DamageInfoCap;
import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import com.kenhorizon.beyondhorizon.server.init.BHEffects;
import com.kenhorizon.beyondhorizon.server.init.BHParticle;
import com.kenhorizon.beyondhorizon.server.level.ICombatCore;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageInfo;
import com.kenhorizon.beyondhorizon.server.level.damagesource.IDamageInfo;
import com.kenhorizon.beyondhorizon.server.util.Constant;
import com.mojang.realmsclient.dto.PlayerInfo;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class BHMobEffect extends MobEffect {
    private int rapidHealingRate = 20;
    private int rapidHealingMinusRate = 1;
    private final int rapidHealingDefaultRate = 20;
    private final int rapidHealingLimitRate = 5;

    public BHMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }
    public BHMobEffect(MobEffectCategory category) {
        this(category, 0x00000000);
    }
    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (this == BHEffects.GHOUL_WILL.get()) {
            if (entity instanceof Player player) {
                player.getFoodData().setExhaustion(-999.0F);
                if (player.tickCount % 40 == 0) {
                    player.heal(0.50F);
                    player.getFoodData().eat(1, 0);
                }
            }
        }
        if (this == BHEffects.INFLAME.get()) {
            entity.hurt(BHDamageTypes.burnMagic(), 0.1F + (0.1F * amplifier));
            Level level = entity.level();
            if (level instanceof ServerLevel sLevel) {
                sLevel.sendParticles(ParticleTypes.FLAME, entity.getRandomX(0.50D), entity.getRandomY(), entity.getRandomZ(0.50D), 5, 0,0,0, 0.05D);
            }
        }
        if (this == BHEffects.TORMENT.get()) {
            entity.hurt(BHDamageTypes.burnMagic(), entity.getMaxHealth() * Constant.TORMENT_EFFECT);
            Level level = entity.level();
            if (level instanceof ServerLevel sLevel) {
                sLevel.sendParticles(ParticleTypes.FLAME, entity.getRandomX(0.50D), entity.getRandomY(), entity.getRandomZ(0.50D), 5, 0,0,0, 0.0D);
            }
        }

        if (this == BHEffects.ACID.get()) {
            entity.hurt(BHDamageTypes.bleed(), 0.1F + (0.1F * amplifier));
            Level level = entity.level();
            if (level instanceof ServerLevel sLevel) {
                sLevel.sendParticles(BHParticle.RED_SKULL.get(), entity.getRandomX(0.50D), entity.getRandomY(), entity.getRandomZ(0.50D), 5, 0,0,0, 0.0D);
            }
        }

        if (this == BHEffects.DRAGONIC_FLAME.get()) {
            if (entity.getHealth() > 0) {
                float damageOutput = 1.0F + (amplifier * 0.5F);
                if (entity.isInWaterOrRain() || entity.isInWaterOrBubble()) {
                    damageOutput *= 1.5F;
                } else if (entity.isOnFire()) {
                    damageOutput *= 2.0F;
                } else if (entity.isFallFlying() || entity instanceof Player player && player.getAbilities().flying) {
                    damageOutput *= 1.25F;
                }
                if (entity.level() instanceof ServerLevel sLevel) {
                    sLevel.sendParticles(BHParticle.DRAGONIC_FLAME.get(), entity.getRandomX(0.50D), entity.getRandomY(), entity.getRandomZ(0.50D), 5, 0,0,0, 0.0D);
                }
                entity.hurt(BHDamageTypes.burnMagic(), 2 * damageOutput);
            }
        }
        if (this == BHEffects.RAPID_HEALING.get()) {
            ICombatCore combatCore = CapabilityCaller.combat(entity);
            boolean cancelHeal = combatCore.OnCombat();
            if (!cancelHeal && entity.getHealth() < entity.getMaxHealth()) {
                if (entity.tickCount % this.rapidHealingRate == 0) {
                    BeyondHorizon.LOGGER.debug("Rapid Healing Debug: Rate:{} Limit:{} Minus:{}", this.rapidHealingRate, this.rapidHealingLimitRate, this.rapidHealingMinusRate);
                    entity.heal(0.5F);
                    if (this.rapidHealingRate > this.rapidHealingLimitRate) {
                        this.rapidHealingRate -= this.rapidHealingMinusRate;
                    } else {
                        this.rapidHealingRate = this.rapidHealingLimitRate;
                    }
                }
            }
            if (cancelHeal) {
                this.rapidHealingRate = this.rapidHealingDefaultRate;
            }
            if (entity.level() instanceof ServerLevel sLevel) {
                sLevel.sendParticles(ParticleTypes.HEART, entity.getRandomX(0.50D), entity.getRandomY(), entity.getRandomZ(0.50D), 2, 0,0,0, 0.10D);
            }
        }
        if (this == BHEffects.BLEED.get()) {
            if (!entity.level().isClientSide()) {
                this.bleedSpecialEffect(entity, amplifier);
            }
        }
        if (this == BHEffects.PARALYZE.get()) {
            Vec3 vec3 = entity.getDeltaMovement();
            entity.setDeltaMovement(0, vec3.y() > 0 ? 0 : vec3.y(), 0);
        }
        if (this == BHEffects.LETHAL_POISON.get()) {
            int level = amplifier / 2;
            if (entity.getHealth() > 0.0F) {
                entity.hurt(entity.damageSources().magic(), 0.5F + level);
            }
            if (entity.level() instanceof ServerLevel sLevel) {
                sLevel.sendParticles(BHParticle.RED_SKULL.get(), entity.getRandomX(0.5D), entity.getRandomY(), entity.getRandomZ(0.5D), 2, 0,0,0, 0.005D);
            }
        }
        super.applyEffectTick(entity, amplifier);
    }
    private void bleedSpecialEffect(LivingEntity user, int level) {
        double rangeLevel = 32.0D * level;
        if (user instanceof ServerPlayer player) {
            AABB range = player.getBoundingBox().inflate(rangeLevel);
            for (LivingEntity targetZombies : user.level().getEntitiesOfClass(LivingEntity.class, range)) {
                if (targetZombies instanceof Zombie zombie) {
                    zombie.getMoveControl().setWantedPosition(player.getX(), player.getY(), player.getZ(), 1.0D);
                }
            }
        }
    }
    private void hungerBoostRange(LivingEntity user, int level) {
        AABB range = user.getBoundingBox().inflate(16.0D);
        for (LivingEntity target : user.level().getEntitiesOfClass(LivingEntity.class, range)) {
            if (target.isAlive() && !target.isInvulnerable() && target != user) {
                if (target.getHealth() < user.getHealth()) {
                    target.addEffect(new MobEffectInstance(BHEffects.VULNERABLE.get(), 20, level, true, false, true));
                } else {
                    target.addEffect(new MobEffectInstance(MobEffects.GLOWING, 20, level, true, false, false));
                }
            }
        }
    }
    @Override
    public List<ItemStack> getCurativeItems() {
        return removeCurative(
                BHEffects.LETHAL_PROTECTION.get(),
                BHEffects.LETHAL_PROTECTION_COOLDOWN.get(),
                BHEffects.RAPID_HEALING.get(),
                BHEffects.CURSED.get(),
                BHEffects.ARMOR_BREAK.get(),
                BHEffects.RAPID_HEALING.get(),
                BHEffects.FEAR.get(),
                BHEffects.STUN.get(),
                BHEffects.INFLAME.get(),
                BHEffects.DRAGONIC_FLAME.get()
        );
    }

    public List<ItemStack> removeCurative(MobEffect... effect) {
        for (MobEffect getEffects : effect) {
            if (getEffects == this) {
                return List.of();
            }
        }
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        ret.add(new ItemStack(Items.MILK_BUCKET));
        return ret;
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        if (this == BHEffects.LETHAL_POISON.get()) {
            int tick = 25 >> amplifier;
            if (tick > 0) {
                return duration % tick == 0;
            } else {
                return true;
            }
        }
        if (this == BHEffects.LIGHTNING.get()) {
            int tick = 75 >> amplifier;
            if (tick > 0) {
                return duration % tick == 0;
            } else {
                return true;
            }
        }
        if (this == BHEffects.ACID.get()) {
            return duration % 5 == 0;
        }
        if (this == BHEffects.INFLAME.get()) {
            return duration % 10 == 0;
        }
        if (this == BHEffects.DRAGONIC_FLAME.get()) {
            return duration % 10 == 0;
        }
        if (this == BHEffects.TORMENT.get()) {
            return duration % 10 == 0;
        }
        return true;
    }
}
