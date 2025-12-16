package com.kenhorizon.beyondhorizon.server.api.classes.classes;

import com.kenhorizon.beyondhorizon.server.api.classes.RoleClass;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class StrikerRole extends RoleClass {

    @Override
    public float preMigitationDamage(float damageDealt, DamageSource source, LivingEntity attacker, LivingEntity target) {
        if (target == null || attacker == null) return damageDealt;
        if (attacker instanceof Player player) {

        }
        return damageDealt;
    }
}
