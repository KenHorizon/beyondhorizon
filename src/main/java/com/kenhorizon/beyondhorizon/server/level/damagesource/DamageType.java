package com.kenhorizon.beyondhorizon.server.level.damagesource;

import com.kenhorizon.beyondhorizon.server.entity.ability.AbstractDeathRayAbility;
import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import com.kenhorizon.beyondhorizon.server.tags.BHDamageTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;

public enum DamageType {
    PHYSICAL_DAMAGE,
    MAGIC_DAMAGE,
    TRUE_DAMAGE;

    private boolean dealDamage(LivingEntity target, LivingEntity attacker, float damage, boolean noKnockback) {
        switch (this) {
            case PHYSICAL_DAMAGE -> {
                return target.hurt(BHDamageTypes.physicalDamage(attacker, noKnockback), damage);
            }
            case MAGIC_DAMAGE -> {
                return target.hurt(BHDamageTypes.magicDamage(attacker, noKnockback), damage);
            }
            case TRUE_DAMAGE -> {
                return target.hurt(BHDamageTypes.trueDamage(attacker, noKnockback), damage);
            }
            default -> {
                return false;
            }
        }
    }
    private boolean dealDamage(LivingEntity target, Entity source, LivingEntity attacker, float damage, boolean noKnockback) {
        switch (this) {
            case PHYSICAL_DAMAGE -> {
                return target.hurt(BHDamageTypes.physicalDamage(source, attacker, noKnockback), damage);
            }
            case MAGIC_DAMAGE -> {
                return target.hurt(BHDamageTypes.magicDamage(source, attacker, noKnockback), damage);
            }
            case TRUE_DAMAGE -> {
                return target.hurt(BHDamageTypes.trueDamage(source, attacker, noKnockback), damage);
            }
            default -> {
                return false;
            }
        }
    }
    public boolean dealDamage(LivingEntity target, LivingEntity attacker, float damage) {
        return this.dealDamage(target, attacker, damage, false);
    }

    public boolean onHit(LivingEntity target, LivingEntity attacker, float damage, boolean noKnockback) {
        target.invulnerableTime = 0;
        target.hurtTime = 0;
        return this.dealDamage(target, attacker, damage, noKnockback);
    }

    public boolean onHit(LivingEntity target, LivingEntity attacker, float damage) {
        return this.onHit(target, attacker, damage, false);
    }

    public boolean dealDamage(LivingEntity target, Entity source, LivingEntity attacker, float damage) {
        return this.dealDamage(target, source, attacker, damage, true);
    }
}
