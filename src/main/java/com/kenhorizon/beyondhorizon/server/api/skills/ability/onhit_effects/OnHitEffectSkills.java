package com.kenhorizon.beyondhorizon.server.api.skills.ability.onhit_effects;

import com.kenhorizon.beyondhorizon.server.api.skills.WeaponPassiveSkills;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageType;
import com.kenhorizon.beyondhorizon.server.util.Constant;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.item.ItemStack;

public abstract class OnHitEffectSkills extends WeaponPassiveSkills {
    @FunctionalInterface
    public interface TypeFunction {
        public float calculate(float magnitude, float level, MobType mobType, float damageDealt, DamageSource source, LivingEntity attacker, LivingEntity target);
    }
    public static final TypeFunction RUINED_BLADE = ((magnitude, level, mobType,
                                                              damageDealt, source,
                                                              attacker, target) -> {

        float finalDamage = target.getHealth() * magnitude;
        if (target instanceof WitherBoss ||  target instanceof Warden) {
            return Math.min(finalDamage, Constant.PENALTY_DAMAGE);
        }
        return finalDamage;
    });

    private final TypeFunction typeFunction;
    private final MobType mobType;

    public OnHitEffectSkills(float magnitude, float level, MobType mobType, TypeFunction damageFunction) {
        super(magnitude, level);
        this.typeFunction = damageFunction;
        this.mobType = mobType;
    }

    @Override
    public void onHitAttack(DamageSource source, ItemStack itemStack, LivingEntity target, LivingEntity attacker, float damageDealt) {
        float damage = this.typeFunction.calculate(this.getMagnitude(), this.getLevel(), this.mobType, damageDealt, source, attacker, target);
        this.damageType().onHit(target, attacker, damage);
    }

    public abstract DamageType damageType();

    public static class RuinedBlade extends OnHitEffectSkills {

        public RuinedBlade(float magnitude) {
            super(magnitude, 1, null, RUINED_BLADE);
        }

        @Override
        public DamageType damageType() {
            return DamageType.PHYSICAL_DAMAGE;
        }
    }
}

