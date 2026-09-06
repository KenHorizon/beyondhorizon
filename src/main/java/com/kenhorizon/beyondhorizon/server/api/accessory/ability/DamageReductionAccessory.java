package com.kenhorizon.beyondhorizon.server.api.accessory.ability;

import com.kenhorizon.beyondhorizon.server.api.accessory.AccessoryPassiveSkill;
import com.kenhorizon.beyondhorizon.server.util.DamageContext;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class DamageReductionAccessory extends AccessoryPassiveSkill {
    public enum DamageReductionType {
        ALL,
        BASIC_ATTACK
    }
    private DamageReductionType damageReductionType;

    public DamageReductionAccessory(float damageReduce, DamageReductionType damageReductionType) {
        super(damageReduce);
        this.damageReductionType = damageReductionType;
    }
    @Override
    protected MutableComponent makeTooltip(ItemStack itemStack) {
        return Component.translatable(this.createId(), Maths.format(100.0F * this.getMagnitude()));
    }

    @Override
    public float damageTaken(DamageContext context, DamageSource source, LivingEntity entity) {
        if (entity == null) return context.damage();
        float reducedDamage = context.multiply(this.getMagnitude());
        switch (this.damageReductionType) {
            case BASIC_ATTACK -> {
                if (source.is(DamageTypeTags.IS_EXPLOSION) || source.is(DamageTypeTags.IS_FIRE) || source.is(DamageTypeTags.IS_PROJECTILE)) {
                    return context.damage();
                }
                if (source.getDirectEntity() == source.getEntity() && source.getEntity() instanceof LivingEntity) {
                    return context.sub(reducedDamage);
                }
            }
            case ALL -> {
                return context.sub(reducedDamage);
            }
            default -> {
                return context.damage();
            }
        }
        return context.damage();
    }
}
