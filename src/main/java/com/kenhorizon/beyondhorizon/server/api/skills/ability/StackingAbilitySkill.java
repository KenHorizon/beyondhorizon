package com.kenhorizon.beyondhorizon.server.api.skills.ability;

import com.kenhorizon.beyondhorizon.server.api.skills.WeaponPassiveSkills;
import com.kenhorizon.beyondhorizon.server.util.DamageContext;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public abstract class StackingAbilitySkill extends WeaponPassiveSkills {
    public enum StackType {
        HIT,
        KILL
    }

    protected StackType stackType = StackType.HIT;
    public StackingAbilitySkill(StackType stackType) {
        this.stackType = stackType;
    }


    @Override
    public void onHitAttack(DamageSource source, ItemStack itemStack, LivingEntity target, LivingEntity attacker, DamageContext context) {
        if (this.stackType == StackType.HIT) {
            this.onHitAttacks(source, itemStack, target, attacker, context);
        }
    }

    @Override
    public void onEntityKilled(DamageSource source, LivingEntity attacker, LivingEntity target) {
        if (this.stackType == StackType.KILL) {

        }
    }

    @Override
    public float postMigitationDamage(DamageContext context, DamageSource source, LivingEntity attacker, LivingEntity target) {
        if (attacker == null || target == null) return context.damage();
        return this.postDamage(source, target, attacker, context);
    }

    public abstract String tagName();

    public abstract void onHitAttacks(DamageSource damageSource, ItemStack itemStack, LivingEntity target, LivingEntity attacker, DamageContext context);
    public abstract float preDamage(DamageSource damageSource, LivingEntity target, LivingEntity attacker, DamageContext context);
    public abstract float postDamage(DamageSource damageSource, LivingEntity target, LivingEntity attacker, DamageContext context);
}
