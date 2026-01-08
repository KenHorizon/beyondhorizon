package com.kenhorizon.beyondhorizon.server.enchantment;

import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.Event;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IAdditionalEnchantment {

    default Optional<IAdditionalEnchantment> enchantmentCallback() {
        return Optional.empty();
    }

    default float modifyDamageTaken(int level, float damageDealt, DamageSource source, LivingEntity entity) {
        return damageDealt;
    }

    default float preMigitationDamage(int level, float damageDealt, DamageSource source, LivingEntity attacker, LivingEntity target) {
        return damageDealt;
    }

    default float postMigitationDamage(int level, float damageDealt, DamageSource source, LivingEntity attacker, LivingEntity target) {
        return damageDealt;
    }

    default void onHitAttack(int level, DamageSource source, ItemStack itemStack, LivingEntity target, LivingEntity attacker, float damageDealt) {}

    default void onEntityUpdate(int level, LivingEntity entity) {}

    default boolean onHarvestDrop(int level, Player player, LevelAccessor levelAccessor, ItemStack itemStack, BlockPos blockPos, BlockState blockState, List<ItemStack> drops) {
        return true;
    }
}
