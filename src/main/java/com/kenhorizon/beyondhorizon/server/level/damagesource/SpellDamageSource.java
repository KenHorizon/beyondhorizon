package com.kenhorizon.beyondhorizon.server.level.damagesource;

import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public class SpellDamageSource extends DamageSource {
    private final DamageTags damageTags;
    public SpellDamageSource(Holder<DamageType> holder, @Nullable Entity entity, DamageTags damageTags) {
        super(holder, entity);
        this.damageTags = damageTags;
    }

    public SpellDamageSource(Holder<DamageType> holder, @Nullable Entity direct, @Nullable Entity cause, DamageTags damageTags) {
        super(holder, direct, cause);
        this.damageTags = damageTags;
    }

    public SpellDamageSource(Holder<DamageType> holder, DamageTags damageTags) {
        super(holder);
        this.damageTags = damageTags;
    }

    public DamageTags getDamageTags() {
        return damageTags;
    }
}
