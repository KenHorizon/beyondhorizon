package com.kenhorizon.beyondhorizon.server.api.skills.ability;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.particle.RingParticles;
import com.kenhorizon.beyondhorizon.client.particle.world.RingParticleOptions;
import com.kenhorizon.beyondhorizon.client.render.util.Colors;
import com.kenhorizon.beyondhorizon.server.api.skills.WeaponPassiveSkills;
import com.kenhorizon.beyondhorizon.server.entity.util.ShockwaveUtils;
import com.kenhorizon.beyondhorizon.server.init.BHEnchantments;
import com.kenhorizon.beyondhorizon.server.init.BHSounds;
import com.kenhorizon.beyondhorizon.server.util.DamageContext;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

public class SmashAttackSkills extends WeaponPassiveSkills {

    public SmashAttackSkills(float damage) {
        super(damage);
    }

    public boolean canSmashAttack(LivingEntity attacker) {
        return attacker.fallDistance > 1.5F && !attacker.isFallFlying();
    }
    @Override
    protected MutableComponent makeTooltip(ItemStack itemStack) {
        return Component.translatable(this.createId(), Maths.format(this.getMagnitude()));
    }

    @Override
    public float postMigitationDamage(DamageContext context, DamageSource source, LivingEntity attacker, LivingEntity target) {
        if (attacker == null || target == null) return context.damage();
        if (this.canSmashAttack(attacker)) {
            attacker.resetFallDistance();
        }
        return context.damage();
    }

    @Override
    public float preMigitationDamage(DamageContext context, DamageSource source, LivingEntity attacker, LivingEntity target) {
        if (attacker == null || target == null) return context.damage();
        if (this.canSmashAttack(attacker)) {
            double fallDistance = attacker.fallDistance;
            double damage;
            double damageBonus = (this.getMagnitude() + (0.5F * EnchantmentHelper.getEnchantmentLevel(BHEnchantments.HEAVY_SMASH.get(), attacker)));
            if (fallDistance <= 3) {
                damage = context.add(fallDistance * damageBonus);
            } else {
                damage = (damageBonus * 3.0F) + (fallDistance - 3.0F);
            }
            for (LivingEntity nearby : attacker.level().getEntitiesOfClass(LivingEntity.class, attacker.getBoundingBox().inflate(3.5D))) {
                if (nearby.isAlive() && !nearby.isInvulnerable() && nearby != attacker) {

                    Vec3 direction = nearby.position().subtract(attacker.position());
                    double knockbackPower = this.getKnockbackPower(attacker, nearby, direction);
                    Vec3 knockbackVector = direction.normalize().scale(knockbackPower);
                    if (knockbackPower > 0.0F) {
                        nearby.push(knockbackVector.x, 0.7F + (0.5F * EnchantmentHelper.getKnockbackBonus(attacker)), knockbackVector.z);
                    }
                    if (nearby == target) continue;
                    nearby.hurt(source, (float) damage);
                }
            }
            if (!target.level().isClientSide()) {
                attacker.level().playSound(null, attacker.blockPosition(), BHSounds.HEAVY_ATTACK.get(), SoundSource.PLAYERS, (attacker.fallDistance > 5.0F ? 2 : 1), 1.0F);
                ((ServerLevel) attacker.level()).sendParticles(new RingParticleOptions(0, (float) Math.PI / 2f, 33, Colors.WHITE, 110F * (attacker.fallDistance > 5.0F ? 1 : 0.5F), false, RingParticles.Behavior.GROW), target.getX(), target.getY(0.01D), target.getZ(), 1, 0,0, 0, 0);
                ShockwaveUtils.doRingShockwave(target, target.position(), 2.0F, -0.001F, 20);
            }
            return (float) damage;
        } else {
            return context.damage();
        }
    }

    private double getKnockbackPower(LivingEntity attacker, LivingEntity nearby, Vec3 direction) {
        return (3.5F - direction.length()) * (0.7F + (0.5F * EnchantmentHelper.getKnockbackBonus(attacker))) * (attacker.fallDistance > 5.0F ? 2 : 1) * (1.0F - nearby.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
    }
}
