package com.kenhorizon.beyondhorizon.server.classes;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class StrikerRole extends RoleClass {

    public StrikerRole(Player player) {
        super(player);
    }

    @Override
    public float preMigitationDamage(float damageDealt, DamageSource source, LivingEntity attacker, LivingEntity target) {
        if (target == null || attacker == null) return damageDealt;
        double bonusDamage = attacker.getAttributeBaseValue(Attributes.ATTACK_DAMAGE) - attacker.getAttributeValue(Attributes.ATTACK_DAMAGE);
        boolean doDoubleAttack = attacker.getRandom().nextDouble() >= (bonusDamage * 0.05D);
        return doDoubleAttack ? (float) (damageDealt + (damageDealt * 0.5D)) : damageDealt;
    }
}
