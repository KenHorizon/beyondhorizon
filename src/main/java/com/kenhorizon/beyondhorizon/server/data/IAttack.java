package com.kenhorizon.beyondhorizon.server.data;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IAttack {

    default float damageTaken(float damageDealt, DamageSource source, LivingEntity entity) {
        return damageDealt;
    }

    default float preMigitationDamage(float damageDealt, DamageSource source, LivingEntity attacker, LivingEntity target) {
        return damageDealt;
    }

    default float postMigitationDamage(float damageDealt, DamageSource source, LivingEntity attacker, LivingEntity target) {
        return damageDealt;
    }

    default void onHitAttack(DamageSource damageSource, ItemStack itemStack, LivingEntity target, LivingEntity attacker, float damageDealt) {}

    default boolean onLeftClickProperties(ItemStack itemStack, Player player) {
        return false;
    }

    default void onLeftClick(ItemStack itemStack, Player player) {}

    default boolean onLeftClickEntity(ItemStack itemStack, Player player, Entity entity) {
        return false;
    }

    default void onEntityKilled(DamageSource damageSource, LivingEntity attacker, LivingEntity target) {}

    default int expDrop(int dropExperience, int originalAmount, LivingEntity target, Player player) {
        return dropExperience;
    }

    default boolean attackFullyCharged(Player player, ItemStack itemStack) {
        return player.getAttackStrengthScale(0.5F) > 0.9F;
    }

    default boolean canEntiyReceiveDamage(Player player, LivingEntity target, DamageSource source) {
        return false;
    }
}
