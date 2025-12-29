package com.kenhorizon.beyondhorizon.server.entity.projectiles;

import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;

public class HellfireOrb extends ExtendedThrowableProjectile {
    public double prevDeltaMovementX;
    public double prevDeltaMovementY;
    public double prevDeltaMovementZ;


    public HellfireOrb(EntityType<? extends ThrowableProjectile> entityType, Level level) {
        super(entityType, level);
        this.setBaseDamage(4);
    }

    @Override
    public void tick() {
        super.tick();
        this.prevDeltaMovementX = getDeltaMovement().x;
        this.prevDeltaMovementY = getDeltaMovement().y;
        this.prevDeltaMovementZ = getDeltaMovement().z;
    }

    @Override
    public DamageSource setDamageSource(LivingEntity entity) {
        return BHDamageTypes.magicDamage(this, entity);
    }
}
