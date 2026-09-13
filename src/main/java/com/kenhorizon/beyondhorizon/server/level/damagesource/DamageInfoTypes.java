package com.kenhorizon.beyondhorizon.server.level.damagesource;

import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public enum DamageInfoTypes {
    PHYSICAL_DAMAGE,
    MAGIC_DAMAGE,
    TRUE_DAMAGE;

    public boolean dealDamage(LivingEntity target, LivingEntity attacker, float damage, boolean noKnockback) {
        return target.hurt(BHDamageTypes.applyDamage(this, DamageTags.DEFAULT, attacker, noKnockback), damage);
    }

    private boolean dealOnEffectsDamage(LivingEntity target, LivingEntity attacker, float damage, boolean noKnockback) {
        DamageSource source = BHDamageTypes.applyDamage(this, DamageTags.DEFAULT, attacker, noKnockback);
        OnHitEffectHandler.add(source, damage);
        return target.hurt(source, damage);
    }

    public boolean dealAOEDamage(LivingEntity target, LivingEntity attacker, float damage) {
        return target.hurt(BHDamageTypes.applyDamage(this, DamageTags.AREA_OF_EFFECTS, attacker, null), damage);
    }

    public boolean dealDamage(LivingEntity target, Entity source, LivingEntity attacker, float damage, boolean noKnockback) {
        return target.hurt(BHDamageTypes.applyDamage(this, DamageTags.DEFAULT, source, attacker, noKnockback), damage);
    }

    public boolean dealDamage(LivingEntity target, LivingEntity attacker, float damage) {
        return this.dealDamage(target, attacker, damage, false);
    }

    public boolean onHit(LivingEntity target, LivingEntity attacker, float damage, boolean noKnockback) {
        target.invulnerableTime = 0;
        target.hurtTime = 0;
        return this.dealOnEffectsDamage(target, attacker, damage, noKnockback);
    }

    public boolean onHit(LivingEntity target, LivingEntity attacker, float damage) {
        return this.onHit(target, attacker, damage, false);
    }

    public boolean dealDamage(LivingEntity target, Entity source, LivingEntity attacker, float damage) {
        return this.dealDamage(target, source, attacker, damage, true);
    }
}
