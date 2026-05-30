package com.kenhorizon.beyondhorizon.server.api.accessory;

import com.kenhorizon.beyondhorizon.server.util.MathUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class DamageEffectivenessAccessory extends AccessorySkill {

    public DamageEffectivenessAccessory(float magnitude) {
        super(magnitude, 1);
    }

    @Override
    protected MutableComponent tooltipDescription(ItemStack itemStack) {
        return Component.translatable(this.createId(), MathUtils.format(this.getMagnitude() * 100.0F));
    }

    @Override
    public float postMigitationDamage(float damageDealt, DamageSource source, LivingEntity attacker, LivingEntity target) {
        if (attacker == null || target == null) return damageDealt;
        return damageDealt * this.getMagnitude();
    }
}
