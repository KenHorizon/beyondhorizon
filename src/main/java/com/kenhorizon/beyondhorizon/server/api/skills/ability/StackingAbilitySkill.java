package com.kenhorizon.beyondhorizon.server.api.skills.ability;

import com.kenhorizon.beyondhorizon.client.api.IStackIconOverlay;
import com.kenhorizon.beyondhorizon.server.api.skills.WeaponPassiveSkills;
import com.kenhorizon.beyondhorizon.server.api.stackable_tags.StackableTags;
import com.kenhorizon.beyondhorizon.server.util.DamageContext;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public abstract class StackingAbilitySkill extends WeaponPassiveSkills implements IStackIconOverlay {
    public enum StackType {
        HIT,
        KILL
    }

    protected StackType stackType = StackType.HIT;
    private final StackableTags stackableTags;
    public StackingAbilitySkill(StackType stackType, StackableTags stackableTags) {
        this.stackType = stackType;
        this.stackableTags = stackableTags;
    }

    public StackableTags getStackableTags() {
        return stackableTags;
    }

    @Override
    public StackableTags getStacks() {
        return this.getStackableTags();
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
            this.onKill(source, attacker.getMainHandItem(), target, attacker);
        }
    }

    @Override
    public float preMigitationDamage(DamageContext context, DamageSource source, LivingEntity attacker, LivingEntity target) {
        if (attacker == null || target == null) return context.damage();
        return this.preDamage(source, target, attacker, context);
    }

    @Override
    public float postMigitationDamage(DamageContext context, DamageSource source, LivingEntity attacker, LivingEntity target) {
        if (attacker == null || target == null) return context.damage();
        return this.postDamage(source, target, attacker, context);
    }

    public void onKill(DamageSource damageSource, ItemStack itemStack, LivingEntity target, LivingEntity attacker) {

    }

    public void onHitAttacks(DamageSource damageSource, ItemStack itemStack, LivingEntity target, LivingEntity attacker, DamageContext context) {

    }

    public float preDamage(DamageSource damageSource, LivingEntity target, LivingEntity attacker, DamageContext context) {
        return context.damage();
    }

    public float postDamage(DamageSource damageSource, LivingEntity target, LivingEntity attacker, DamageContext context) {
        return context.damage();
    }
}
