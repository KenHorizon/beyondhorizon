package com.kenhorizon.beyondhorizon.server.accessory;

import com.kenhorizon.beyondhorizon.server.data.IAttack;
import com.kenhorizon.beyondhorizon.server.data.IItemGeneric;
import com.kenhorizon.beyondhorizon.server.util.Constant;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class AccessorySkill extends Accessory implements IItemGeneric, IAttack {

    public AccessorySkill(float magnitude, int level) {
        super(magnitude, level);
    }

    public AccessorySkill() {
        super(0, 0);
    }

    @Override
    public Optional<IItemGeneric> IItemGeneric() {
        return Optional.of(this);
    }

    @Override
    public Optional<IAttack> IAttackCallback() {
        return Optional.of(this);
    }
    @Override
    protected MutableComponent tooltipDescription(ItemStack itemStack) {
        if (this.getMagnitude() > 0.0F && this.getLevel() > 0.0F) {
            return Component.translatable(this.createId(), Maths.format0(this.getMagnitude()), this.getLevel());
        } else {
            return Component.translatable(this.createId(), Maths.format0(this.getMagnitude()));
        }
    }

    @Override
    public void onEntityUpdate(LivingEntity entity, ItemStack itemStack) {
        if (this == Accessories.FEATHER_FEET.get()) {
            entity.fallDistance = -1;
        }
    }

    @Override
    public void onHitAttack(DamageSource damageSource, ItemStack itemStack, LivingEntity target, LivingEntity attacker, float damageDealt) {
        if (target == null || attacker == null) return;
        if (this == Accessories.BURN_EFFECT.get()) {
            target.setSecondsOnFire(Constant.FIRE_EFFECT);
        }
    }
}
