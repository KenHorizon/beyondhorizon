package com.kenhorizon.beyondhorizon.server.effect;

import com.kenhorizon.beyondhorizon.server.init.BHEffects;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class BHMobEffect extends MobEffect {
    public BHMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }
    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (this == BHEffects.RECOVERY.get() && entity.getHealth() < entity.getMaxHealth()) {
            float missingHealth = (entity.getMaxHealth() - entity.getHealth()) / entity.getMaxHealth();
            float healingFactor = 0.015F * entity.getMaxHealth() * (entity.getMaxHealth() * missingHealth);
            entity.heal(healingFactor);
        }
        if (this == BHEffects.DRAGON_FLAME.get()) {
            if (entity.getHealth() > 0) {
                float damageOutput = 1.0F + (amplifier * 0.5F);
                if (entity.isInWaterOrRain() || entity.isInWaterOrBubble()) {
                    damageOutput *= 1.5F;
                } else if (entity.isOnFire()) {
                    damageOutput *= 2.0F;
                } else if (entity.isFallFlying() || entity instanceof Player player && player.getAbilities().flying) {
                    damageOutput *= 1.25F;
                }
                entity.hurt(entity.level().damageSources().magic(), damageOutput);
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
        }
        super.applyEffectTick(entity, amplifier);
    }
    private void bleedSpecialEffect(LivingEntity user, int level) {
        double rangeLevel = 32.0D * level;
        if (user instanceof ServerPlayer player) {
            AABB range = player.getBoundingBox().inflate(rangeLevel);
            for (LivingEntity targetZombies : user.level().getEntitiesOfClass(LivingEntity.class, range)) {
                if (targetZombies instanceof Zombie zombie) {
                    if (zombie.getTarget() == null) {
                        zombie.getMoveControl().setWantedPosition(player.getX(), player.getY(), player.getZ(), 1.0D);
                    }
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
                BHEffects.CURSED.get(),
                BHEffects.ARMOR_BREAK.get(),
                BHEffects.RAPID_HEALING.get(),
                BHEffects.FEAR.get(),
                BHEffects.STUN.get(),
                BHEffects.DRAGON_FLAME.get()
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
        if (this == BHEffects.RECOVERY.get()) {
            int tick = 20 >> amplifier;
            if (tick > 0) {
                return duration % tick == 0;
            } else {
                return true;
            }
        }
        if (this == BHEffects.DRAGON_FLAME.get()) {
            int tick = 20;
            return duration % tick == 0;
        }
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
        return true;
    }
}
