package com.kenhorizon.beyondhorizon.server.level.damagesource;

import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import com.kenhorizon.beyondhorizon.server.level.utils.AttributeUtils;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;

public enum DamageType {
    ADAPTIVE_DAMAGE,
    PHYSICAL_DAMAGE,
    MAGIC_DAMAGE,
    TRUE_DAMAGE;

    public boolean dealDamage(LivingEntity target, LivingEntity attacker, float damage, boolean noKnockback) {
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
            case ADAPTIVE_DAMAGE -> {
                double AD = AttributeUtils.getBonus(attacker, Attributes.ATTACK_DAMAGE);
                double AP = attacker.getAttributeValue(BHAttributes.ABILITY_POWER.get());
                if (AD == AP) {
                    double BAD = attacker.getAttributeBaseValue(Attributes.ATTACK_DAMAGE);
                    double BAP = attacker.getAttributeBaseValue(BHAttributes.ABILITY_POWER.get());
                    if (BAD > BAP) {
                        return target.hurt(BHDamageTypes.physicalDamage(attacker, noKnockback), damage);
                    } else {
                        return target.hurt(BHDamageTypes.magicDamage(attacker, noKnockback), damage);
                    }
                } else if (AD > AP) {
                    return target.hurt(BHDamageTypes.physicalDamage(attacker, noKnockback), damage);
                } else {
                    return target.hurt(BHDamageTypes.magicDamage(attacker, noKnockback), damage);
                }
            }
            default -> {
                return false;
            }
        }
    }

    private boolean dealOnEffectsDamage(LivingEntity target, LivingEntity attacker, float damage, boolean noKnockback) {
        switch (this) {
            case PHYSICAL_DAMAGE -> {
                DamageSource source = BHDamageTypes.physicalDamage(attacker, noKnockback);
                OnHitEffectHandler.add(source, damage);
                return target.hurt(source, damage);
            }
            case MAGIC_DAMAGE -> {
                DamageSource source = BHDamageTypes.magicDamage(attacker, noKnockback);
                OnHitEffectHandler.add(source, damage);
                return target.hurt(source, damage);
            }
            case TRUE_DAMAGE -> {
                DamageSource source = BHDamageTypes.trueDamage(attacker, noKnockback);
                OnHitEffectHandler.add(source, damage);
                return target.hurt(source, damage);
            }
            case ADAPTIVE_DAMAGE -> {
                double AD = AttributeUtils.getBonus(attacker, Attributes.ATTACK_DAMAGE);
                double AP = attacker.getAttributeValue(BHAttributes.ABILITY_POWER.get());
                if (AD == AP) {
                    double BAD = attacker.getAttributeBaseValue(Attributes.ATTACK_DAMAGE);
                    double BAP = attacker.getAttributeBaseValue(BHAttributes.ABILITY_POWER.get());
                    if (BAD > BAP) {
                        DamageSource source = BHDamageTypes.physicalDamage(attacker, noKnockback);
                        OnHitEffectHandler.add(source, damage);
                        return target.hurt(source, damage);
                    } else {
                        DamageSource source = BHDamageTypes.magicDamage(attacker, noKnockback);
                        OnHitEffectHandler.add(source, damage);
                        return target.hurt(source, damage);
                    }
                } else if (AD > AP) {
                    DamageSource source = BHDamageTypes.physicalDamage(attacker, noKnockback);
                    OnHitEffectHandler.add(source, damage);
                    return target.hurt(source, damage);
                } else {
                    DamageSource source = BHDamageTypes.magicDamage(attacker, noKnockback);
                    OnHitEffectHandler.add(source, damage);
                    return target.hurt(source, damage);
                }
            }
            default -> {
                return false;
            }
        }
    }
    public boolean dealAOEDamage(LivingEntity target, LivingEntity attacker, float damage) {
        switch (this) {
            case PHYSICAL_DAMAGE -> {
                return target.hurt(BHDamageTypes.AOEphysicalDamage(attacker, null), damage);
            }
            case MAGIC_DAMAGE -> {
                return target.hurt(BHDamageTypes.AOEmagicDamage(attacker, null), damage);
            }
            case TRUE_DAMAGE -> {
                return target.hurt(BHDamageTypes.AOEtrueDamage(attacker, null), damage);
            }
            case ADAPTIVE_DAMAGE -> {
                double AD = AttributeUtils.getBonus(attacker, Attributes.ATTACK_DAMAGE);
                double AP = attacker.getAttributeValue(BHAttributes.ABILITY_POWER.get());
                if (AD == AP) {
                    double BAD = attacker.getAttributeBaseValue(Attributes.ATTACK_DAMAGE);
                    double BAP = attacker.getAttributeBaseValue(BHAttributes.ABILITY_POWER.get());
                    if (BAD > BAP) {
                        return target.hurt(BHDamageTypes.AOEphysicalDamage(attacker, null), damage);
                    } else {
                        return target.hurt(BHDamageTypes.AOEmagicDamage(attacker, null), damage);
                    }
                } else if (AD > AP) {
                    return target.hurt(BHDamageTypes.AOEphysicalDamage(attacker, null), damage);
                } else {
                    return target.hurt(BHDamageTypes.AOEmagicDamage(attacker, null), damage);
                }
            }
            default -> {
                return false;
            }
        }
    }
    public boolean dealDamage(LivingEntity target, Entity source, LivingEntity attacker, float damage, boolean noKnockback) {
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
            case ADAPTIVE_DAMAGE -> {
                double AD = AttributeUtils.getBonus(attacker, Attributes.ATTACK_DAMAGE);
                double AP = attacker.getAttributeValue(BHAttributes.ABILITY_POWER.get());
                if (AD == AP) {
                    double BAD = attacker.getAttributeBaseValue(Attributes.ATTACK_DAMAGE);
                    double BAP = attacker.getAttributeBaseValue(BHAttributes.ABILITY_POWER.get());
                    if (BAD > BAP) {
                        return target.hurt(BHDamageTypes.physicalDamage(source, attacker, noKnockback), damage);
                    } else {
                        return target.hurt(BHDamageTypes.magicDamage(source, attacker, noKnockback), damage);
                    }
                } else if (AD > AP) {
                    return target.hurt(BHDamageTypes.physicalDamage(attacker, noKnockback), damage);
                } else {
                    return target.hurt(BHDamageTypes.magicDamage(source, attacker, noKnockback), damage);
                }
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
        return this.dealOnEffectsDamage(target, attacker, damage, noKnockback);
    }

    public boolean onHit(LivingEntity target, LivingEntity attacker, float damage) {
        return this.onHit(target, attacker, damage, false);
    }

    public boolean dealDamage(LivingEntity target, Entity source, LivingEntity attacker, float damage) {
        return this.dealDamage(target, source, attacker, damage, true);
    }
}
