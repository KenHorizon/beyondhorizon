package com.kenhorizon.beyondhorizon.server.api.accessory;

import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import com.kenhorizon.beyondhorizon.server.init.BHEffects;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageHandler;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

public class ThornsAccessory extends AccessorySkill {
    float baseDamage;
    public ThornsAccessory(float baseDamage, float magnitude) {
        this.setMagnitude(magnitude);
        this.baseDamage = baseDamage;
    }

    @Override
    protected MutableComponent tooltipDescription(ItemStack itemStack) {
        return Component.translatable(this.createId(), this.baseDamage, Maths.format0(this.getMagnitude()));
    }

    @Override
    public float damageTaken(float damageDealt, DamageSource source, LivingEntity entity) {
        if (entity == null) return damageDealt;
        if (source.getDirectEntity() == source.getEntity() && source.getEntity() instanceof LivingEntity attacker) {
            double bonusArmor = entity.getArmorValue() - entity.getAttributeBaseValue(Attributes.ARMOR);
            double damageInflict = bonusArmor * (this.getMagnitude() * this.getLevel());
            double baseDamage = this.baseDamage + damageInflict;
            if (DamageHandler.damage(attacker, true, BHDamageTypes.magicDamage(entity, attacker), (float) baseDamage)) {
                attacker.addEffect(new MobEffectInstance(BHEffects.WOUNDED.get(), Maths.sec(3)));
            }
        }
        return damageDealt;
    }
}
