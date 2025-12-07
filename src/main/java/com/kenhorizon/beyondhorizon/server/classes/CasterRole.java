package com.kenhorizon.beyondhorizon.server.classes;

import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.tags.BHDamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class CasterRole extends RoleClass {

    public CasterRole(Player player) {
        super(RoleClassTypes.CASTER, player);
    }
    @Override
    public float preMigitationDamage(float damageDealt, DamageSource source, LivingEntity attacker, LivingEntity target) {
        if (target == null || attacker == null) return damageDealt;
        if (source.is(BHDamageTypeTags.MAGIC_DAMAGE)) {
            float bonusDamage = damageDealt * (float) (attacker.getAttributeValue(BHAttributes.MAX_MANA.get()) * 0.5D);
            return damageDealt + bonusDamage;
        }
        return damageDealt;
    }
}
