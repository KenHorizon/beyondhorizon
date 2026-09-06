package com.kenhorizon.beyondhorizon.server.api.accessory.ability;

import com.kenhorizon.beyondhorizon.server.api.accessory.AccessoryPassiveSkill;
import com.kenhorizon.beyondhorizon.server.init.BHEffects;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageType;
import com.kenhorizon.beyondhorizon.server.util.DamageContext;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

public class ThornsAccessory extends AccessoryPassiveSkill {
    float baseDamage;
    public ThornsAccessory(float baseDamage, float magnitude) {
        this.setMagnitude(magnitude);
        this.baseDamage = baseDamage;
    }

    @Override
    protected MutableComponent makeTooltip(ItemStack itemStack) {
        return Component.translatable(this.createId(), this.baseDamage, Maths.format(100.0F * this.getMagnitude()));
    }

    @Override
    public float damageTaken(DamageContext context, DamageSource source, LivingEntity entity) {
        if (entity == null) return context.damage();
        if (source.getDirectEntity() == source.getEntity() && source.getEntity() instanceof LivingEntity attacker) {
            double bonusArmor = entity.getArmorValue() - entity.getAttributeBaseValue(Attributes.ARMOR);
            double damageInflict = bonusArmor * (this.getMagnitude() * this.getLevel());
            double baseDamage = this.baseDamage + damageInflict;
            if (DamageType.MAGIC_DAMAGE.dealDamage(attacker, entity, (float) baseDamage, false)) {
                attacker.addEffect(new MobEffectInstance(BHEffects.WOUNDED.get(), Maths.sec(3)));
            }
        }
        return context.damage();
    }
}
