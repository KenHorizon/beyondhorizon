package com.kenhorizon.beyondhorizon.server.api.accessory;

import com.kenhorizon.beyondhorizon.server.entity.ability.CleaveAbility;
import com.kenhorizon.beyondhorizon.server.entity.ability.CleaveConeAbility;
import com.kenhorizon.beyondhorizon.server.util.Constant;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class TitanicCrescentAccessory extends CleaveEffectAccessory {

    public TitanicCrescentAccessory() {
        super(Constant.TITANIC_CRESCENT, 0, CleaveAbility.Type.CONE);
    }

    @Override
    protected MutableComponent tooltipDescription(ItemStack itemStack) {
        return Component.translatable(this.createId(), Maths.format(this.getMagnitude() * 100.0F), Maths.format(this.getMagnitude() * 100.0F));
    }

    @Override
    public boolean coneAtTarget() {
        return true;
    }

    @Override
    public float dealDamage(LivingEntity target, LivingEntity attacker, float damageDealt, ItemStack itemStack) {
        return (attacker.getMaxHealth() * this.getMagnitude());
    }

    @Override
    public void attackCleave(ItemStack itemStack, LivingEntity target, LivingEntity attacker, float damageDealt) {
        CleaveConeAbility.spawn(attacker.level(), target , attacker, this.dealDamage(target, attacker, damageDealt, itemStack), this.coneAtTarget());
    }
}
