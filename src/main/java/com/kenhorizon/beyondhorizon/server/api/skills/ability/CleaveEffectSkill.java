package com.kenhorizon.beyondhorizon.server.api.skills.ability;

import com.kenhorizon.beyondhorizon.server.api.skills.WeaponPassiveSkills;
import com.kenhorizon.beyondhorizon.server.entity.ability.CleaveAbility;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageType;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public abstract class CleaveEffectSkill extends WeaponPassiveSkills {
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
    protected MutableComponent makeTooltip(ItemStack itemStack) {
        return Component.translatable(this.createId(), Maths.format(this.getMagnitude() * 100.0F), Maths.format(this.getCleaveRange() * 100.0F));
    }

    @Override
    public void onHitAttack(DamageSource damageSource, ItemStack itemStack, LivingEntity target, LivingEntity attacker, float damageDealt) {
        DamageType.PHYSICAL_DAMAGE.onHit(target, attacker, this.dealDamage(target, attacker, damageDealt, itemStack));
        this.attackCleave(itemStack, target, attacker, damageDealt);
    }

    public abstract boolean coneAtTarget();

    public abstract float dealDamage(LivingEntity target, LivingEntity attacker, float damageDealt, ItemStack itemStack);

    public void attackCleave(ItemStack itemStack, LivingEntity target, LivingEntity attacker, float damageDealt) {

    }
}
