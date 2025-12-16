package com.kenhorizon.beyondhorizon.server.api.skills;

import com.kenhorizon.beyondhorizon.server.api.accessory.Accessories;
import com.kenhorizon.beyondhorizon.server.api.accessory.AccessorySkill;
import com.kenhorizon.beyondhorizon.server.util.Constant;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class SinglePassiveAccessory extends AccessorySkill {

    @Override
    public void onEntityUpdate(LivingEntity entity, ItemStack itemStack) {
        if (this == Accessories.FEATHER_FEET.get()) {
            entity.fallDistance = -1;
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
