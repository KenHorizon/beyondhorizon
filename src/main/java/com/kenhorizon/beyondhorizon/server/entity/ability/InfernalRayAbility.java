package com.kenhorizon.beyondhorizon.server.entity.ability;

import com.kenhorizon.beyondhorizon.client.particle.RingParticles;
import com.kenhorizon.beyondhorizon.client.particle.world.RingParticleOptions;
import com.kenhorizon.beyondhorizon.client.particle.world.SlashParticleOptions;
import com.kenhorizon.beyondhorizon.client.render.util.ColorUtil;
import com.kenhorizon.beyondhorizon.server.entity.mobs.FayeWildfire;
import com.kenhorizon.beyondhorizon.server.init.BHEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class InfernalRayAbility extends AbstractDeathRayAbility {
    public InfernalRayAbility(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.setColor(255, 116, 0);
        this.setScale(0.5F);
    }

    public InfernalRayAbility(Level world, LivingEntity caster, double x, double y, double z, float yaw, float pitch, int duration) {
        super(BHEntity.INFERNAL_RAY.get(), world, caster, x, y, z, yaw, pitch, duration);
        this.setColor(255, 116, 0);
        this.setScale(0.5F);
    }

    public InfernalRayAbility(Level world, LivingEntity caster, double x, double y, double z, float yaw, float pitch, int duration, float range) {
        super(BHEntity.INFERNAL_RAY.get(), world, caster, x, y, z, yaw, pitch, duration, range);
        this.setColor(255, 116, 0);
        this.setScale(0.5F);
    }

    @Override
    protected void updateWithMob() {
        if (this.caster instanceof FayeWildfire) {
            this.setYaw((float) ((caster.yHeadRot + 90) * Math.PI / 180.0D));
            this.setPitch((float) (-caster.getXRot() * Math.PI / 180.0D));
            Vec3 vecOffset1 = new Vec3(0, 0, 0.6).yRot((float) Math.toRadians(-caster.getYRot()));
            Vec3 vecOffset2 = new Vec3(1.2, 0, 0).yRot(-getYaw()).xRot(getPitch());
            this.setPos(caster.getX() + vecOffset1.x() + vecOffset2.x(), caster.getY() + (caster.getBbHeight() / 2) + vecOffset1.y() + vecOffset2.y(), caster.getZ() + vecOffset1.z() + vecOffset2.z());

        } else {
            super.updateWithMob();
        }
    }
}
