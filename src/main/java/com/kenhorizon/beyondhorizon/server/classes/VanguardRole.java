package com.kenhorizon.beyondhorizon.server.classes;

import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import com.kenhorizon.beyondhorizon.server.tags.BHDamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class VanguardRole extends RoleClass {

    public VanguardRole(Player player) {
        super(player);
    }

    @Override
    public void onHitAttack(DamageSource damageSource, ItemStack itemStack, LivingEntity target, LivingEntity attacker, float damageDealt) {
        float armor = attacker.getArmorValue();
        float extraDamage = armor * 0.10F;
        target.hurt(BHDamageTypes.trueDamage(attacker), extraDamage);
    }
}
