package com.kenhorizon.beyondhorizon.server.api.accessory.ability;

import com.kenhorizon.beyondhorizon.server.api.accessory.AccessoryPassiveSkill;
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
    private float damageReduce;
    private DamageReductionType damageReductionType;

    public DamageReductionAccessory(float damageReduce, DamageReductionType damageReductionType) {
        this.damageReduce = damageReduce;
        this.damageReductionType = damageReductionType;
    }
    @Override
    protected MutableComponent makeTooltip(ItemStack itemStack) {
        return Component.translatable(this.createId(), Maths.format0(this.damageReduce));
    }

    @Override
    public float damageTaken(float damageDealt, DamageSource source, LivingEntity entity) {
        if (entity == null) return damageDealt;
        float reducedDamage = damageDealt * this.damageReduce;
        switch (this.damageReductionType) {
            case BASIC_ATTACK -> {
                if (source.is(DamageTypeTags.IS_EXPLOSION) || source.is(DamageTypeTags.IS_FIRE) || source.is(DamageTypeTags.IS_PROJECTILE)) {
                    return damageDealt;
                }
                if (source.getDirectEntity() == source.getEntity() && source.getEntity() instanceof LivingEntity) {
                    return damageDealt - reducedDamage;
                }
            }
            case ALL -> {
                return damageDealt - reducedDamage;
            }
            default -> {
                return damageDealt;
            }
        }
        return damageDealt;
    }
}
