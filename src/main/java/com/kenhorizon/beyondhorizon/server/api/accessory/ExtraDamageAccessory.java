package com.kenhorizon.beyondhorizon.server.api.accessory;

import com.kenhorizon.beyondhorizon.server.level.CombatUtil;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageHandler;
import com.kenhorizon.beyondhorizon.server.util.Constant;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class ExtraDamageAccessory extends AccessorySkill {
    @FunctionalInterface
    public interface DamageTypeFunction {
        public float calculate(float magnitude, float level, MobType mobType, float damageDealt, DamageSource source, LivingEntity attacker, LivingEntity target);
    }
    public static final ExtraDamageAccessory.DamageTypeFunction CURRENT_HEALTH = ((magnitude, level, mobType, damageDealt, source, attacker, target) -> {

        float finalDamage = damageDealt + (target.getHealth() * (magnitude * level));
        if (target instanceof WitherBoss ||  target instanceof Warden) {
            return Math.min(finalDamage, Constant.PENALTY_DAMAGE);
        }
        return finalDamage;
    });
    public static final ExtraDamageAccessory.DamageTypeFunction MAX_HEALTH = ((magnitude, level, mobType, damageDealt, source, attacker, target) -> {
        float finalDamage = damageDealt + (target.getMaxHealth() * (magnitude * level));
        if (target instanceof WitherBoss ||  target instanceof Warden) {
            return Math.min(finalDamage, Constant.PENALTY_DAMAGE);
        }
        return finalDamage;
    });

    public static final ExtraDamageAccessory.DamageTypeFunction USER_MISSING_HEALTH = ((magnitude, level, mobType, damageDealt, source, attacker, target) -> {
        float damageMultiplier = (attacker.getMaxHealth() - attacker.getHealth() / attacker.getMaxHealth());
        float amplifier = 0.0F;
        if ((magnitude * level) != 0) {
            for (int i = 0; i < (100 * magnitude); ++i) {
                if (i % magnitude == 0) {
                    amplifier++;
                    float perDamage = (amplifier / 100.0F);
                    return DamageHandler.multiplier(damageDealt, perDamage);
                }
            }
        }
        return DamageHandler.multiplier(damageDealt, damageMultiplier);
    });
    public static final ExtraDamageAccessory.DamageTypeFunction TARGET_MISSING_HEALTH = ((magnitude, level, mobType, damageDealt, source, attacker, target) -> {
        float damageMultiplier = (target.getMaxHealth() - target.getHealth() / target.getMaxHealth());
        float amplifier = 0.0F;
        if ((magnitude * level) != 0) {
            for (int i = 0; i < (100 * magnitude); ++i) {
                if (i % magnitude == 0) {
                    amplifier++;
                    float perDamage = (amplifier / 100.0F);
                    return DamageHandler.multiplier (damageDealt, perDamage);
                }
            }
        }
        return DamageHandler.multiplier(damageDealt, damageMultiplier);
    });

    public static final ExtraDamageAccessory.DamageTypeFunction BONUS_DAMAGE = ((magnitude, level, mobType, damageDealt, source, attacker, target) -> {
        if (mobType != null && mobType == target.getMobType()) {
            return DamageHandler.multiplier(damageDealt, magnitude * level);
        }
        return DamageHandler.multiplier(damageDealt, magnitude * level);
    });

    public static final ExtraDamageAccessory.DamageTypeFunction KINETIC_WEAPON = ((magnitude, level, mobType, damageDealt, source, attacker, target) -> {
        Entity entity = attacker;
        if (!(entity instanceof Player) && attacker.isPassenger()) {
            entity = attacker.getVehicle();
        }
        if (entity instanceof LivingEntity) {
            Vec3 vec3 = entity.getDeltaMovement().scale(20.0F);
            return DamageHandler.multiplier(damageDealt, (float) Maths.perValue(vec3.length(), (magnitude * level), magnitude));
        }
        return damageDealt;
    });

    private final ExtraDamageAccessory.DamageTypeFunction damageFunction;
    private MobType mobType;

    public ExtraDamageAccessory(float magnitude, int level, MobType mobType, ExtraDamageAccessory.DamageTypeFunction damageFunction) {
        super(magnitude, level);
        this.damageFunction = damageFunction;
        this.setMagnitude(magnitude);
        this.setLevel(level);
        this.mobType = mobType;
    }

    public ExtraDamageAccessory(float magnitude, ExtraDamageAccessory.DamageTypeFunction damageTypeFunction) {
        this(magnitude, 1, null, damageTypeFunction);
    }

    public ExtraDamageAccessory(float magnitude, MobType mobType, ExtraDamageAccessory.DamageTypeFunction damageTypeFunction) {
        this(magnitude, 1, mobType, damageTypeFunction);
    }

    @Override
    protected MutableComponent tooltipDescription(ItemStack itemStack) {
        if (this.getMagnitude() > 0.0F && this.getLevel() > 0.0F) {
            return Component.translatable(this.createId(), Maths.format0(this.getMagnitude()), this.getLevel());
        } else {
            return Component.translatable(this.createId(), Maths.format0(this.getMagnitude()));
        }
    }

    @Override
    public float preMigitationDamage(float damageDealt, DamageSource source, LivingEntity attacker, LivingEntity target) {
        if (attacker == null || target == null) return damageDealt;
        return this.damageFunction.calculate(this.getMagnitude(), this.getLevel(), this.mobType, damageDealt, source, attacker, target);
    }
}
