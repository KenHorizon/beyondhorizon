package com.kenhorizon.beyondhorizon.server.entity.projectiles;


import com.kenhorizon.beyondhorizon.server.init.BHEntity;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

public class Pyrolance extends HomingProjectile {

    public Pyrolance(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

    public Pyrolance(EntityType<? extends Projectile> entityType, Level level, double x, double y, double z, double dx, double dy, double dz) {
        super(BHEntity.PYRO_LANCE.get(), level, x, y, z, dx, dy, dz);
    }

    public Pyrolance(Level level, DamageType damageType, LivingEntity owner, float damage, double dx, double dy, double dz, boolean crit) {
        super(BHEntity.PYRO_LANCE.get(), level, damageType, owner, damage, dx, dy, dz, crit);
    }

    public Pyrolance(Level level, LivingEntity shooter) {
        super(BHEntity.PYRO_LANCE.get(), level, shooter);
    }
}