package com.kenhorizon.beyondhorizon.client.api.event;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@OnlyIn(Dist.CLIENT)
@Event.HasResult
@Cancelable
public class PotionEffectParticleEvent extends Event {
    private LivingEntity entity;
    private ParticleOptions particle;
    private double x;
    private double y;
    private double z;
    private double dx;
    private double dy;
    private double dz;
    public PotionEffectParticleEvent(LivingEntity entity, ParticleOptions particle, double x, double y, double z, double dx, double dy, double dz) {
        this.entity = entity;
        this.particle = particle;
        this.x = x;
        this.y = y;
        this.z = z;
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
    }

    public void setEntity(LivingEntity entity) {
        this.entity = entity;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public void setDz(double dz) {
        this.dz = dz;
    }

    public double getDz() {
        return dz;
    }

    public double getDy() {
        return dy;
    }

    public double getDx() {
        return dx;
    }

    public ParticleOptions getParticle() {
        return particle;
    }

    public void setParticle(ParticleOptions particle) {
        this.particle = particle;
    }
}
