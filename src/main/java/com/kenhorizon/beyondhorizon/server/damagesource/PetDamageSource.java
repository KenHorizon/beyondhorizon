package com.kenhorizon.beyondhorizon.server.damagesource;

import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public class PetDamageSource extends DamageSource {
    public PetDamageSource(Holder<DamageType> holder, @Nullable Entity entity, DamageTags damageTags) {
        super(holder, entity);
    }

    public PetDamageSource(Holder<DamageType> holder, @Nullable Entity direct, @Nullable Entity cause, DamageTags damageTags) {
        super(holder, direct, cause);
    }

    public PetDamageSource(Holder<DamageType> holder, DamageTags damageTags) {
        super(holder);
    }

    @Override
    public String getMsgId() {
        return "pet." + super.getMsgId();
    }
}
