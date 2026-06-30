package com.kenhorizon.beyondhorizon.server.entity.projectiles;


import com.kenhorizon.beyondhorizon.client.particle.TrailParticles;
import com.kenhorizon.beyondhorizon.client.particle.world.TrailParticleOptions;
import com.kenhorizon.beyondhorizon.client.render.util.ColorUtil;
import com.kenhorizon.beyondhorizon.server.init.BHEffects;
import com.kenhorizon.beyondhorizon.server.init.BHEntity;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageType;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.UUID;

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