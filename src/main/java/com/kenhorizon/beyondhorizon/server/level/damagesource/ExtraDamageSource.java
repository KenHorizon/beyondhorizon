package com.kenhorizon.beyondhorizon.server.level.damagesource;

import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class ExtraDamageSource extends DamageSource implements IDamageSource {
    public ExtraDamageSource(Holder<DamageType> type, @Nullable Entity directEntity, @Nullable Entity attacker, @Nullable Vec3 damageSourcePosition) {
        super(type, directEntity, attacker, damageSourcePosition);
    }

    public ExtraDamageSource(Holder<DamageType> type, @Nullable Entity directEntity, @Nullable Entity attacker) {
        super(type, directEntity, attacker);
    }

    public ExtraDamageSource(Holder<DamageType> type, Vec3 damageSourcePosition) {
        super(type, damageSourcePosition);
    }

    public ExtraDamageSource(Holder<DamageType> type, @Nullable Entity entity) {
        super(type, entity);
    }

    public ExtraDamageSource(Holder<DamageType> type) {
        super(type);
    }
}
