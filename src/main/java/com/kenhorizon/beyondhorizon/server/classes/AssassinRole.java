package com.kenhorizon.beyondhorizon.server.classes;

import com.kenhorizon.beyondhorizon.server.level.CombatUtil;
import com.kenhorizon.beyondhorizon.server.util.Constant;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class AssassinRole extends RoleClass {

    public AssassinRole(Player player) {
        super(player);
    }

    @Override
    public float preMigitationDamage(float damageDealt, DamageSource source, LivingEntity attacker, LivingEntity target) {
        if (target == null || attacker == null) return damageDealt;
        return CombatUtil.damageAtBack(Constant.ASSASSIN_DAMAGE_MULTIPLIER_AT_THE_BACK, damageDealt, source, attacker, target);
    }
}
