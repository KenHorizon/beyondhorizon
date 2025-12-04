package com.kenhorizon.beyondhorizon.server.accessory;

import com.kenhorizon.beyondhorizon.server.capability.CapabilityCaller;
import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import com.kenhorizon.beyondhorizon.server.level.damagesource.IDamageInfo;
import com.kenhorizon.beyondhorizon.server.tags.BHDamageTypeTags;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import com.kenhorizon.beyondhorizon.client.level.tooltips.Tooltips;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class BleedingEffectAccessory extends AccessorySkill {
    private int duration = 0;
    private boolean activatedEffect;
    public BleedingEffectAccessory(double modifier) {
        this.setMagnitude((float) modifier);
    }

    @Override
    protected MutableComponent tooltipDescription(ItemStack itemStack) {
        return Component.translatable(this.createId(), Maths.format(this.getMagnitude()));
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
