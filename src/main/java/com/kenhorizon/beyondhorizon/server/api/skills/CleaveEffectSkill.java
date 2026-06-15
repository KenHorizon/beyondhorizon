package com.kenhorizon.beyondhorizon.server.api.skills;

import com.kenhorizon.beyondhorizon.server.entity.ability.CleaveAbility;
import com.kenhorizon.beyondhorizon.server.entity.ability.CleaveConeAbility;
import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import com.kenhorizon.beyondhorizon.server.util.MathUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public abstract class CleaveEffectSkill extends WeaponSkills {
    private float cleaveRange;
    protected CleaveAbility.Type type = CleaveAbility.Type.CIRCLE;
    public CleaveEffectSkill(float magnitude, float range, CleaveAbility.Type type) {
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

    @Override
    protected MutableComponent tooltipDescription(ItemStack itemStack) {
        return Component.translatable(this.createId(), MathUtils.format(this.getMagnitude() * 100.0F), MathUtils.format(this.getCleaveRange() * 100.0F));
    }

    @Override
    public void onHitAttack(DamageSource damageSource, ItemStack itemStack, LivingEntity target, LivingEntity attacker, float damageDealt) {
        target.invulnerableTime = 0;
        target.hurt(BHDamageTypes.physicalDamage(attacker, null), this.dealDamage(target, attacker, damageDealt, itemStack));
        target.invulnerableTime = 0;
        if (this.type == CleaveAbility.Type.CONE) {
            this.attackCleave(itemStack, target, attacker, damageDealt);
        } else {
            this.attackCleave(itemStack, target, attacker, damageDealt);
//            CleaveAbility.spawn(attacker.level(), target , attacker, this.dealDamage(target, attacker, damageDealt, itemStack), this.getCleaveRange());
        }
    }

    public abstract boolean coneAtTarget();

    public abstract float dealDamage(LivingEntity target, LivingEntity attacker, float damageDealt, ItemStack itemStack);

    public void attackCleave(ItemStack itemStack, LivingEntity target, LivingEntity attacker, float damageDealt) {

    }
}
