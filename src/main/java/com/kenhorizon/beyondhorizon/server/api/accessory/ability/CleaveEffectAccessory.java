package com.kenhorizon.beyondhorizon.server.api.accessory.ability;

import com.kenhorizon.beyondhorizon.server.api.accessory.AccessoryPassiveSkill;
import com.kenhorizon.beyondhorizon.server.entity.ability.CleaveAbility;
import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import com.kenhorizon.beyondhorizon.server.util.DamageContext;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public abstract class CleaveEffectAccessory extends AccessoryPassiveSkill {
    public static final int CLEAVE_DURATION = 15;
    private float cleaveRange;
    protected CleaveAbility.Type type;
    public CleaveEffectAccessory(float magnitude, float range, CleaveAbility.Type type) {
        this.setMagnitude(magnitude);
        this.cleaveRange = range;
        this.type = type;
    }

    public void setCleaveRange(float cleaveRange) {
        this.cleaveRange = cleaveRange;
    }

    public float getCleaveRange() {
        return cleaveRange;
    }

    public CleaveAbility.Type getCleaveType() {
        return this.type;
    }

    public void setType(CleaveAbility.Type type) {
        this.type = type;
    }

    @Override
    protected MutableComponent makeTooltip(ItemStack itemStack) {
        return Component.translatable(this.createId(), Maths.format(this.getMagnitude() * 100.0F), Maths.format(this.getCleaveRange() * 100.0F));
    }

    @Override
    public void onHitAttack(DamageSource source, ItemStack itemStack, LivingEntity target, LivingEntity attacker, DamageContext context) {
        target.invulnerableTime = 0;
        target.hurt(BHDamageTypes.physicalDamage(attacker, null), this.dealDamage(target, attacker, context.damage(), itemStack));
        target.invulnerableTime = 0;
        this.attackCleave(itemStack, target, attacker, context.damage());
    }

    public abstract boolean coneAtTarget();

    public abstract float dealDamage(LivingEntity target, LivingEntity attacker, float damageDealt, ItemStack itemStack);

    public void attackCleave(ItemStack itemStack, LivingEntity target, LivingEntity attacker, float damageDealt) {

    }
}
