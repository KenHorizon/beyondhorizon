package com.kenhorizon.beyondhorizon.server.api.classes.classes;

import com.kenhorizon.beyondhorizon.server.api.classes.RoleClass;
import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import com.kenhorizon.beyondhorizon.server.util.Constant;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class VanguardRole extends RoleClass {

    @Override
    public void onHitAttack(DamageSource damageSource, ItemStack itemStack, LivingEntity target, LivingEntity attacker, float damageDealt) {
        if (target == null || attacker == null) return;
        if (!attacker.level().isClientSide() && attacker instanceof Player player) {
            float armor = (float) player.getAttributeValue(Attributes.ARMOR);
            float bonus = Constant.VANGUARD_EXTRA_DAMAGE;
            target.hurt(BHDamageTypes.trueDamage(player, target), armor * bonus);
        }
    }
}
