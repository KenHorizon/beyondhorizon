package com.kenhorizon.beyondhorizon.server.api;

import com.kenhorizon.beyondhorizon.server.util.DamageContext;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IAttack {

    /**
     * Handle customizing the post migitation damage modifications
     *
     * @param context Amount of damage taken recevied
     * @param source  The source of damage recevied
     * @param entity  Living Target of Attacker
     *
     */
    default float damageTaken(DamageContext context, DamageSource source, LivingEntity entity) {
        return context.damage();
    }

    /**
     * Handle customizing the pre migitation damage
     * {@code Pre-Mitigation the damage after all the modification and reduction applied}
     * @param context Damage value is stored
     * @param source The source of damage recevied
     * @param attacker The entity who's causing
     * @param target Living Target of Attacker
     * */
    default float preMigitationDamage(final DamageContext context, DamageSource source, LivingEntity attacker, LivingEntity target) {
        return context.damage();
    }

    /**
     * Handle customizing the post migitation damage modifications
     * @param damageDealt Amount of damage taken recevied
     * @param source The source of damage recevied
     * @param attacker The entity who's causing
     * @param target Living Target of Attacker
     * */
    default float postMigitationDamage(DamageContext context, DamageSource source, LivingEntity attacker, LivingEntity target) {
        return context.damage();
    }

    /**
     * Handle if target is being hit by any source
     * @param damageDealt Amount of damage taken recevied
     * @param source The source of damage recevied
     * @param attacker The entity who's causing
     * @param target Living Target of Attacker
     * */
    default void onHitAttack(DamageSource source, ItemStack itemStack, LivingEntity target, LivingEntity attacker, final DamageContext context) {}

    /**
     * Handle if player is using attack keys
     * */
    default boolean onLeftClickProperties(ItemStack itemStack, Player player) {
        return false;
    }

    /**
     * Handle if player is using attack keys
     * */
    default void onLeftClick(ItemStack itemStack, Player player) {}

    /**
     * Handle if player is using attack keys and a success hit is landed
     * */
    default boolean onLeftClickEntity(ItemStack itemStack, Player player, Entity entity) {
        return false;
    }

    /**
     * Handle if target is killed on the action
     * @param source The source of damage recevied
     * @param attacker The entity who's causing
     * @param target Living Target of Attacker
     * */
    default void onEntityKilled(DamageSource source, LivingEntity attacker, LivingEntity target) {}

    /**
     * Handle modification of amount of exp drops/values of exp orbs
     * */
    default int expDrop(int dropExperience, int originalAmount, LivingEntity target, Player player) {
        return dropExperience;
    }

    /**
     * Check if the player's attack cooldown is ready to use
     * */
    default boolean attackFullyCharged(Player player, ItemStack itemStack) {
        return player.getAttackStrengthScale(0.5F) > 0.9F;
    }

    /**
     * Handle if attack is killed on the action
     * @param source The source of damage recevied
     * @param player The entity who's causing
     * @param target Living Target of Attacker
     * */
    default boolean canEntiyReceiveDamage(Player player, LivingEntity target, DamageSource source) {
        return false;
    }

    /**
     * Handle if holder is killed on the action
     * @param itemStack The source of damage recevied
     * @param entity The entity who's causing
     * */
    default boolean onEntityDeath(LivingEntity entity, ItemStack itemStack) {
        return false;
    }

    /**
     * Handle item duration use
     * @param itemStack Item being used for
     * @param duration The total duration of item use
     * @return Item used in duration otherwise if negative value will not be used
     * */

    default int onItemUseItem(ItemStack itemStack, int duration) {
        return 0;
    }
}
