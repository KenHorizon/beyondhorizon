package com.kenhorizon.beyondhorizon.server.api.skills;

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
    public void onHitAttack(DamageSource damageSource, ItemStack itemStack, LivingEntity target, LivingEntity attacker, float damageDealt) {
        if (this.stackType == StackType.HIT) {

        }

        this.onHitAttacks(damageSource, itemStack, target, attacker, damageDealt);
    }

    @Override
    public void onEntityKilled(DamageSource damageSource, LivingEntity attacker, LivingEntity target) {
        if (this.stackType == StackType.KILL) {

        }

    }

    @Override
    public float preMigitationDamage(float damageDealt, DamageSource source, LivingEntity attacker, LivingEntity target) {
        if (attacker == null || target == null) return damageDealt;
        return this.preDamage(source, target, attacker, damageDealt);
    }

    @Override
    public float postMigitationDamage(float damageDealt, DamageSource source, LivingEntity attacker, LivingEntity target) {
        if (attacker == null || target == null) return damageDealt;
        return this.postDamage(source, target, attacker, damageDealt);
    }

    public abstract String tagName();

    public abstract void onHitAttacks(DamageSource damageSource, ItemStack itemStack, LivingEntity target, LivingEntity attacker, float damageDealt);
    public abstract float preDamage(DamageSource damageSource, LivingEntity target, LivingEntity attacker, float damageDealt);
    public abstract float postDamage(DamageSource damageSource, LivingEntity target, LivingEntity attacker, float damageDealt);
}
