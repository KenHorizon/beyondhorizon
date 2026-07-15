package com.kenhorizon.beyondhorizon.server.api.armor_ability;

import com.kenhorizon.beyondhorizon.server.entity.ability.CleaveAbility;
import com.kenhorizon.beyondhorizon.server.entity.util.EntityUtils;
import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.util.Constant;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class WildfireArmorAbility extends ArmorAbilityMagnitude {
    private static final UUID WILDFIRE_INCREASED_DAMAGE = UUID.fromString("3a4ae5af-4b46-4c57-9b3b-2ca40be2c89a");
    private float increasedDamage;
    public WildfireArmorAbility(float shockwaveDamage, float increaseDamage) {
        this.increasedDamage = increaseDamage;
        this.setMagnitude(shockwaveDamage);
    }

    public void setIncreasedDamage(float increasedDamage) {
        this.increasedDamage = increasedDamage;
    }

    public float getIncreasedDamage() {
        return increasedDamage;
    }

    @Override
    public void applyBonus(LivingEntity entity) {
        super.applyBonus(entity);
        AttributeInstance instance = entity.getAttribute(BHAttributes.DAMAGE_DEALT.get());
        if (instance != null && instance.getModifier(WILDFIRE_INCREASED_DAMAGE) == null) {
            instance.addTransientModifier(new AttributeModifier(WILDFIRE_INCREASED_DAMAGE, "Wildfire Bonuses", this.getIncreasedDamage(), AttributeModifier.Operation.MULTIPLY_TOTAL));
        }
    }

    @Override
    public void removeBonus(LivingEntity entity) {
        super.removeBonus(entity);
        AttributeInstance instance = entity.getAttribute(BHAttributes.DAMAGE_DEALT.get());
        if (instance != null) instance.removeModifier(WILDFIRE_INCREASED_DAMAGE);
    }

    @Override
    public float damageTaken(float damageDealt, DamageSource source, LivingEntity entity) {
        if (entity == null) return damageDealt;

        float totalDamage = (float) EntityUtils.getAttackDamage(entity);
        float dealDamage = this.getLevel() + (totalDamage * this.getMagnitude());
        CleaveAbility.spawn(entity.level(), entity, entity, dealDamage, 8.0F);

        return dealDamage;
    }


    @Override
    public void onHitAttack(DamageSource damageSource, ItemStack itemStack, LivingEntity target, LivingEntity attacker, float damageDealt) {
        target.setSecondsOnFire(Constant.FIRE_EFFECT);
    }
}
