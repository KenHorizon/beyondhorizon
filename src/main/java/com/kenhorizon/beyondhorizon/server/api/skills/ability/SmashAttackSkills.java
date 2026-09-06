package com.kenhorizon.beyondhorizon.server.api.skills.ability;

import com.kenhorizon.beyondhorizon.server.api.skills.WeaponPassiveSkills;
import com.kenhorizon.beyondhorizon.server.entity.util.ShockwaveUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SmashAttackSkills extends WeaponPassiveSkills {

    public SmashAttackSkills(float damage) {
        super(damage);
    }

    public boolean canSmashAttack(LivingEntity attacker) {
        return attacker.fallDistance > 1.5F && !attacker.isFallFlying();
    }

    @Override
    public float preMigitationDamage(float damageDealt, DamageSource source, LivingEntity attacker, LivingEntity target) {
        if (attacker == null || target == null) return damageDealt;
        if (this.canSmashAttack(attacker)) {
            for (LivingEntity targets : attacker.level().getEntitiesOfClass(LivingEntity.class, attacker.getBoundingBox().inflate(3.0D, 3.0D, 3.0D))) {
                if (targets.isAlive() && !targets.isInvulnerable() && targets != attacker) {
                    if (!target.level().isClientSide()) {
                        BlockPos targetPos = targets.getOnPosLegacy();
                        BlockState blockState = target.level().getBlockState(targetPos);
                        double d0 = target.getX();
                        double d1 = target.getY();
                        double d2 = target.getZ();
                        BlockPos attackerPos = attacker.blockPosition();
                        if (targetPos.getX() != attackerPos.getX() || targetPos.getZ() != attackerPos.getZ()) {
                            double d3 = d0 - (double)targetPos.getX() - 0.5D;
                            double d5 = d2 - (double)targetPos.getZ() - 0.5D;
                            double d6 = Math.max(Math.abs(d3), Math.abs(d5));
                            d0 = (double)targetPos.getX() + 0.5D + d3 / d6 * 0.5D;
                            d2 = (double)targetPos.getZ() + 0.5D + d5 / d6 * 0.5D;
                        }

                        float f = (float) Mth.ceil(attacker.fallDistance - 3.0F);
                        double d4 = Math.min((double)(0.2F + f / 15.0F), 2.5D);
                        int i = (int) (150.0D * d4);
                    }
                    targets.setDeltaMovement(targets.getDeltaMovement().with(Direction.Axis.Y, 0.009999999776482582));
                    targets.knockback(0.50F, Mth.sin(attacker.getYRot() * ((float) Math.PI / 180F)), (double) (-Mth.cos(attacker.getYRot() * ((float) Math.PI / 180F))));
                }
            }
            float damage = damageDealt;
            attacker.resetFallDistance();
            if (attacker.fallDistance > 3) {
                damage = damageDealt + (attacker.fallDistance * this.getMagnitude());
            }
            return damage;
        } else {
            return damageDealt;
        }
    }
}
