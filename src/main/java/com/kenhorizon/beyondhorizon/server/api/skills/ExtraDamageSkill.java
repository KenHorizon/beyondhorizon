package com.kenhorizon.beyondhorizon.server.api.skills;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.api.entity.player.PlayerData;
import com.kenhorizon.beyondhorizon.server.capability.CapabilityCaller;
import com.kenhorizon.beyondhorizon.server.entity.IEntityDamageCap;
import com.kenhorizon.beyondhorizon.server.entity.projectiles.BlazingRod;
import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import com.kenhorizon.beyondhorizon.server.tags.BHDamageTypeTags;
import com.kenhorizon.beyondhorizon.server.util.Constant;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ExtraDamageSkill extends WeaponSkills {
    @FunctionalInterface
    public interface DamageTypeFunction {
        public float calculate(float magnitude, float level, MobType mobType, float damageDealt, DamageSource source, LivingEntity attacker, LivingEntity target);
    }
    public static final DamageTypeFunction CURRENT_HEALTH = ((magnitude, level, mobType, damageDealt, source, attacker, target) -> {

        float finalDamage = damageDealt + (target.getHealth() * (magnitude * level));
        if (target instanceof WitherBoss ||  target instanceof Warden) {
            return Math.min(finalDamage, Constant.PENALTY_DAMAGE);
        }
        if (target instanceof IEntityDamageCap cap) {
            return Math.min(finalDamage, cap.getDamageCap());
        }
        return finalDamage;
    });
    public static final DamageTypeFunction MAX_HEALTH = ((magnitude, level, mobType, damageDealt, source, attacker, target) -> {
        float finalDamage = damageDealt + (target.getMaxHealth() * (magnitude * level));
        if (target instanceof WitherBoss ||  target instanceof Warden) {
            return Math.min(finalDamage, Constant.PENALTY_DAMAGE);
        }
        if (target instanceof IEntityDamageCap cap) {
            return Math.min(finalDamage, cap.getDamageCap());
        }
        return finalDamage;
    });

    public static final DamageTypeFunction USER_MISSING_HEALTH = ((magnitude, level, mobType, damageDealt, source, attacker, target) -> {
        float damageMultiplier = (attacker.getMaxHealth() - attacker.getHealth() / attacker.getMaxHealth());
        float amplifier = 0.0F;
        if ((magnitude * level) != 0) {
            for (int i = 0; i < (100 * magnitude); ++i) {
                if (i % magnitude == 0) {
                    amplifier++;
                    float perDamage = (amplifier / 100.0F);
                    return (1.0F + perDamage) * damageDealt;
                }
            }
        }
        return (float) (damageDealt * (1.0F + (Mth.clamp(damageMultiplier, 0.0F, 1.0F))));
    });
    public static final DamageTypeFunction TARGET_MISSING_HEALTH = ((magnitude, level, mobType, damageDealt, source, attacker, target) -> {
        float damageMultiplier = (target.getMaxHealth() - target.getHealth() / target.getMaxHealth());
        float amplifier = 0.0F;
        if ((magnitude * level) != 0) {
            for (int i = 0; i < (100 * magnitude); ++i) {
                if (i % magnitude == 0) {
                    amplifier++;
                    float perDamage = (amplifier / 100.0F);
                    return (1.0F + perDamage) * damageDealt;
                }
            }
        }
        return (float) (damageDealt * (1.0F + (Mth.clamp(damageMultiplier, 0.0F, 1.0F))));
    });

    public static final DamageTypeFunction BONUS_DAMAGE = ((magnitude, level, mobType, damageDealt, source, attacker, target) -> {
        if (mobType != null && mobType == target.getMobType()) {
            return damageDealt + (magnitude * level);
        }
        return damageDealt + (magnitude * level);
    });

    public static final DamageTypeFunction ARMORED_DAMAGE = ((magnitude, level, mobType, damageDealt, source, attacker, target) -> {
        if (source.is(BHDamageTypeTags.PHYSICAL_DAMAGE)) {
            float targetArmor = target.getArmorValue();
            float baseDamage = level;
            float bonusDamage = baseDamage + (targetArmor * magnitude);
            return target.getArmorCoverPercentage() > 0 ? damageDealt * (1.0F + bonusDamage) : damageDealt;
        }
        return damageDealt;
    });

    public static final DamageTypeFunction PERFECTION = ((magnitude, level, mobType, damageDealt, source, attacker, target) -> {
        if (attacker instanceof Player player) {
            PlayerData playerData = CapabilityCaller.data(player);
            if (playerData.isCrit()) {
                float lethalStrikeDamage = damageDealt * magnitude;
                target.hurt(BHDamageTypes.trueDamage(attacker, target), lethalStrikeDamage);
            }
        }
        return damageDealt;
    });

    public static final DamageTypeFunction KINETIC_WEAPON = ((magnitude, level, mobType, damageDealt, source, attacker, target) -> {
        Entity entity = attacker;
        if (!(entity instanceof Player) && attacker.isPassenger()) {
            entity = attacker.getVehicle();
        }
        if (entity instanceof LivingEntity) {
            Vec3 vec3 = entity.getDeltaMovement().scale(20.0F);
            float extraDamage = damageDealt * (float) Maths.perValue(vec3.length(), (magnitude * level), magnitude);
            return damageDealt + extraDamage;
        }
        return damageDealt;
    });

    private final DamageTypeFunction damageFunction;
    private MobType mobType;

    public ExtraDamageSkill(float magnitude, float level, MobType mobType, DamageTypeFunction damageFunction) {
        this.damageFunction = damageFunction;
        this.setMagnitude(magnitude);
        this.setLevel(level);
        this.mobType = mobType;
    }
    public ExtraDamageSkill(float magnitude, float level, DamageTypeFunction damageTypeFunction) {
        this(magnitude, level, null, damageTypeFunction);
    }
    public ExtraDamageSkill(float magnitude, DamageTypeFunction damageTypeFunction) {
        this(magnitude, 1.0F, null, damageTypeFunction);
    }

    public ExtraDamageSkill(float magnitude, MobType mobType, DamageTypeFunction damageTypeFunction) {
        this(magnitude, 1.0F, mobType, damageTypeFunction);
    }



    @Override
    public float preMigitationDamage(float damageDealt, DamageSource source, LivingEntity attacker, LivingEntity target) {
        if (attacker == null || target == null) return damageDealt;
        return this.damageFunction.calculate(this.getMagnitude(), this.getLevel(), this.mobType, damageDealt, source, attacker, target);
    }
}

