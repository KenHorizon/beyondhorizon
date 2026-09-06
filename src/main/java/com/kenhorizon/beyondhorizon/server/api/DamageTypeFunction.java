package com.kenhorizon.beyondhorizon.server.api;

import com.kenhorizon.beyondhorizon.server.api.entity.player.PlayerData;
import com.kenhorizon.beyondhorizon.server.api.skills.Skill;
import com.kenhorizon.beyondhorizon.server.capability.Capabilities;
import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageInfo;
import com.kenhorizon.beyondhorizon.server.tags.BHDamageTypeTags;
import com.kenhorizon.beyondhorizon.server.util.Constant;
import com.kenhorizon.beyondhorizon.server.util.DamageContext;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public interface DamageTypeFunction {
    public float calculate(float magnitude, float level, MobType mobType, DamageContext context, DamageSource source, LivingEntity attacker, LivingEntity target);
    public static final DamageTypeFunction TARGET_CURRENT_HEALTH = ((magnitude, level, mobType, context, source, attacker, target) -> {

        float finalDamage = DamageInfo.getCurrentHealth(target, context, magnitude);
        if (target instanceof WitherBoss ||  target instanceof Warden) {
            return Math.min(finalDamage, Constant.PENALTY_DAMAGE);
        }
        return finalDamage;
    });

    public static final DamageTypeFunction TARGET_MAX_HEALTH = ((magnitude, level, mobType, context, source, attacker, target) -> {

        float finalDamage = DamageInfo.getMaxHealth(target, context, magnitude);
        if (target instanceof WitherBoss ||  target instanceof Warden) {
            return Math.min(finalDamage, Constant.PENALTY_DAMAGE);
        }
        return finalDamage;
    });
    public static final DamageTypeFunction TARGET_MISSING_HEALTH = ((magnitude, level, mobType, context, source, attacker, target) -> {
        return DamageInfo.getMissingHealth(target, context, magnitude);
    });
    public static final DamageTypeFunction USER_CURRENT_HEALTH = ((magnitude, level, mobType, context, source, attacker, target) -> {
        return DamageInfo.getCurrentHealth(attacker, context, magnitude);
    });

    public static final DamageTypeFunction USER_MAX_HEALTH = ((magnitude, level, mobType, context, source, attacker, target) -> {
        return DamageInfo.getMaxHealth(attacker, context, magnitude);
    });

    public static final DamageTypeFunction USER_MISSING_HEALTH = ((magnitude, level, mobType, context, source, attacker, target) -> {
        return DamageInfo.getMissingHealth(target, context, magnitude);
    });
    public static final DamageTypeFunction BONUS_DAMAGE = ((magnitude, level, mobType, context, source, attacker, target) -> {
        float bonusDamage = context.add(magnitude * level);
        if (mobType != null && mobType == target.getMobType()) {
            return bonusDamage;
        }
        return bonusDamage;
    });

    public static final DamageTypeFunction ARMORED_DAMAGE = ((magnitude, level, mobType, context, source, attacker, target) -> {
        if (source.is(BHDamageTypeTags.PHYSICAL_DAMAGE)) {
            float targetArmor = target.getArmorValue();
            float bonusDamage = level + (targetArmor * magnitude);
            return target.getArmorCoverPercentage() > 0 ? context.multiply((1.0F + bonusDamage)) : context.damage();
        }
        return context.damage();
    });

    public static final DamageTypeFunction PERFECTION = ((magnitude, level, mobType, context, source, attacker, target) -> {
        if (attacker instanceof Player player) {
            PlayerData playerData = Capabilities.data(player);
            if (playerData.isCrit()) {
                target.hurt(BHDamageTypes.trueDamage(attacker, target), context.multiply(magnitude));
            }
        }
        return context.damage();
    });

    public static final DamageTypeFunction KINETIC_WEAPON = ((magnitude, level, mobType, context, source, attacker, target) -> {
        Entity entity = attacker;
        if (!(entity instanceof Player) && attacker.isPassenger()) {
            entity = attacker.getVehicle();
        }
        if (entity instanceof LivingEntity) {
            Vec3 vec3 = entity.getDeltaMovement().scale(20.0F);
            float extraDamage = context.multiply(Maths.perValue(vec3.length(), (magnitude * level), magnitude));
            return context.add(extraDamage);
        }
        return context.damage();
    });

}
