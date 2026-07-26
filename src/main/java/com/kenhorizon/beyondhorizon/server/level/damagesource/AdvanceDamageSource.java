package com.kenhorizon.beyondhorizon.server.level.damagesource;

import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class AdvanceDamageSource extends DamageSource {
    private final DamageTags damageTags;
    public AdvanceDamageSource(Holder<DamageType> holder, @Nullable Entity entity, DamageTags damageTags) {
        super(holder, entity);
        this.damageTags = damageTags;
    }

    public AdvanceDamageSource(Holder<DamageType> holder, @Nullable Entity direct, @Nullable Entity cause, DamageTags damageTags) {
        super(holder, direct, cause);
        this.damageTags = damageTags;
    }

    public AdvanceDamageSource(Holder<DamageType> holder, DamageTags damageTags) {
        super(holder);
        this.damageTags = damageTags;
    }

    public AdvanceDamageSource(Holder<DamageType> holder, @Nullable Entity entity) {
        this(holder, entity, DamageTags.DEFAULT);
    }

    public AdvanceDamageSource(Holder<DamageType> holder, @Nullable Entity direct, @Nullable Entity cause) {
        this(holder, direct, cause, DamageTags.DEFAULT);
    }

    public AdvanceDamageSource(Holder<DamageType> holder) {
        this(holder, DamageTags.DEFAULT);
    }

    public DamageTags getDamageTags() {
        return damageTags;
    }
}
