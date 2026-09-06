package com.kenhorizon.beyondhorizon.server.api.accessory.ability;

import com.kenhorizon.beyondhorizon.server.api.accessory.AccessoryPassiveSkill;
import com.kenhorizon.beyondhorizon.server.capability.Capabilities;
import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import com.kenhorizon.beyondhorizon.server.api.level.IDamageInfo;
import com.kenhorizon.beyondhorizon.server.tags.BHDamageTypeTags;
import com.kenhorizon.beyondhorizon.server.util.DamageContext;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class BleedingEffectAccessory extends AccessoryPassiveSkill {
    private int duration = 0;
    private boolean activatedEffect;
    public BleedingEffectAccessory(double modifier) {
        this.setMagnitude((float) modifier);
    }

    @Override
    protected MutableComponent makeTooltip(ItemStack itemStack) {
        return Component.translatable(this.createId(), Maths.format(100.0F * this.getMagnitude()));
    }


    @Override
    public float damageTaken(DamageContext context, DamageSource source, LivingEntity entity) {
        if (entity == null) return context.damage();
        if (context.damage() == 0.0f || source.is(BHDamageTypeTags.PHYSICAL_DAMAGE) || !source.is(DamageTypeTags.IS_PROJECTILE) || !source.is(DamageTypeTags.IS_FIRE) || !source.is(DamageTypeTags.IS_EXPLOSION) || !(!source.getMsgId().equals("player") && !source.getMsgId().equals("mob"))) {
            float damageReduce = context.multiply(this.getMagnitude());
            this.activatedEffect = true;
            return context.sub(damageReduce);
        } else {
            return context.damage();
        }
    }

    @Override
    public void onEntityKilled(DamageSource source, LivingEntity attacker, LivingEntity target) {
        IDamageInfo damageInfo = Capabilities.damageInfo(attacker);
        attacker.heal(damageInfo.getPostStoredDamage());
    }

    @Override
    public void onEntityUpdate(LivingEntity entity, ItemStack itemStack) {
        IDamageInfo damageInfo = Capabilities.damageInfo(entity);
        if (this.duration >= 100) {
            this.duration = 0;
            this.activatedEffect = false;
        }
        if (this.activatedEffect) {
            entity.hurt(BHDamageTypes.bleed(), damageInfo.getPostStoredDamage() * this.getMagnitude());
            this.duration++;
        }
    }
}
