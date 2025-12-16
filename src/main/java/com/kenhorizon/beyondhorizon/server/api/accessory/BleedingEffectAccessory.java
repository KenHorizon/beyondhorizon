package com.kenhorizon.beyondhorizon.server.api.accessory;

import com.kenhorizon.beyondhorizon.server.capability.CapabilityCaller;
import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import com.kenhorizon.beyondhorizon.server.level.damagesource.IDamageInfo;
import com.kenhorizon.beyondhorizon.server.tags.BHDamageTypeTags;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class BleedingEffectAccessory extends AccessorySkill {
    private int duration = 0;
    private boolean activatedEffect;
    public BleedingEffectAccessory(double modifier) {
        this.setMagnitude((float) modifier);
    }

    @Override
    protected MutableComponent tooltipDescription(ItemStack itemStack) {
        return Component.translatable(this.createId(), Maths.format0(this.getMagnitude()));
    }


    @Override
    public float damageTaken(float damageDealt, DamageSource source, LivingEntity entity) {
        if (entity == null) return damageDealt;
        if (damageDealt == 0.0f || source.is(BHDamageTypeTags.PHYSICAL_DAMAGE) || !source.is(DamageTypeTags.IS_PROJECTILE) || !source.is(DamageTypeTags.IS_FIRE) || !source.is(DamageTypeTags.IS_EXPLOSION) || !(!source.getMsgId().equals("player") && !source.getMsgId().equals("mob"))) {
            float damageReduce = damageDealt * this.getMagnitude();
            this.activatedEffect = true;
            return damageDealt - damageReduce;
        } else {
            return damageDealt;
        }
    }

    @Override
    public void onEntityKilled(DamageSource damageSource, LivingEntity attacker, LivingEntity target) {
        IDamageInfo damageInfo = CapabilityCaller.damageInfo(attacker);
        attacker.heal(damageInfo.getPostStoredDamage());
    }

    @Override
    public void onEntityUpdate(LivingEntity entity, ItemStack itemStack) {
        IDamageInfo damageInfo = CapabilityCaller.damageInfo(entity);
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
