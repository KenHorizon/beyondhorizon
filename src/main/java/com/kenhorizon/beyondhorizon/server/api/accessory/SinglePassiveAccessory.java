package com.kenhorizon.beyondhorizon.server.api.accessory;

import com.kenhorizon.beyondhorizon.server.util.Constant;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class SinglePassiveAccessory extends AccessorySkill {

    public SinglePassiveAccessory() {
        super(0, 1);
    }

    public SinglePassiveAccessory(float magnitude, int level) {
        super(magnitude, level);
    }

    public SinglePassiveAccessory(float magnitude) {
        super(magnitude, 1);
    }

    @Override
    public void onEntityUpdate(LivingEntity entity, ItemStack itemStack) {
        if (this == Accessories.FEATHER_FEET.get()) {
            entity.fallDistance = -1;
        }
        if (this == Accessories.JUMP_BOOST.get()) {
            entity.fallDistance -= entity.getMaxFallDistance() + (entity.getMaxFallDistance() * this.getMagnitude() * this.getLevel());
        }
    }

    @Override
    public void onEntityJump(LivingEntity entity, ItemStack itemStack) {
        if (this == Accessories.JUMP_BOOST.get()) {
            entity.fallDistance -= entity.getMaxFallDistance() + (entity.getMaxFallDistance() * this.getMagnitude() * this.getLevel());
            Vec3 vec3 = entity.getDeltaMovement();
            entity.setDeltaMovement(vec3.x, this.getJumpPower(entity) + (this.getJumpPower(entity) * (this.getMagnitude() * this.getLevel())), vec3.z);
        }
    }

    //TODO: copied from class LivingEntity.getJumpPower()
    protected float getJumpPower(LivingEntity entity) {
        return 0.42F * this.getBlockJumpFactor(entity, entity.level()) + entity.getJumpBoostPower();
    }

    protected float getBlockJumpFactor(LivingEntity entity, Level level) {
        float jumpFactor0 = level.getBlockState(entity.blockPosition()).getBlock().getJumpFactor();
        float jumpFactor1 = level.getBlockState(this.getOnPos(entity,0.500001F)).getBlock().getJumpFactor();
        return (double)jumpFactor0 == 1.0D ? jumpFactor1 : jumpFactor0;
    }
    protected BlockPos getOnPos(LivingEntity entity, float yOffset) {
        if (entity.mainSupportingBlockPos.isPresent()) {
            BlockPos blockpos = entity.mainSupportingBlockPos.get();
            if (!(yOffset > 1.0E-5F)) {
                return blockpos;
            } else {
                BlockState blockstate = entity.level().getBlockState(blockpos);
                return (!((double)yOffset <= 0.5D) || !blockstate.collisionExtendsVertically(entity.level(), blockpos, entity)) ? blockpos.atY(Mth.floor(entity.position().y - (double)yOffset)) : blockpos;
            }
        } else {
            int i = Mth.floor(entity.position().x);
            int j = Mth.floor(entity.position().y - (double)yOffset);
            int k = Mth.floor(entity.position().z);
            return new BlockPos(i, j, k);
        }
    }

    @Override
    public void onHitAttack(DamageSource damageSource, ItemStack itemStack, LivingEntity target, LivingEntity attacker, float damageDealt) {
        if (target == null || attacker == null) return;
        if (this == Accessories.BURN_EFFECT.get()) {
            target.setSecondsOnFire(Constant.FIRE_EFFECT);
        }
    }

    @Override
    public boolean canEntiyReceiveDamage(Player player, LivingEntity target, DamageSource source) {
        if (this == Accessories.FIRE_IMMUNITY.get() && source.is(DamageTypeTags.IS_FIRE)) {
            return source.is(DamageTypes.HOT_FLOOR);
        }
        return false;
    }
}
