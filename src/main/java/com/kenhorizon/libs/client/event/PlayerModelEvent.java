package com.kenhorizon.libs.client.event;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.Event;

@OnlyIn(Dist.CLIENT)
@Event.HasResult
public class PlayerModelEvent extends Event {
    private LivingEntity entityIn;
    private EntityModel model;
    private float limbSwing;
    private float limbSwingAmount;
    private float ageInTicks;
    private float yaw;
    private float pitch;

    public PlayerModelEvent(LivingEntity entityIn, HumanoidModel model, float limbSwing, float limbSwingAmount, float ageInTicks, float yaw, float pitch) {
        this.entityIn = entityIn;
        this.model = model;
        this.limbSwing = limbSwing;
        this.limbSwingAmount = limbSwingAmount;
        this.ageInTicks = ageInTicks;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public float getAgeInTicks() {
        return ageInTicks;
    }

    public float getLimbSwing() {
        return limbSwing;
    }

    public float getLimbSwingAmount() {
        return limbSwingAmount;
    }

    public Entity getEntityIn() {
        return entityIn;
    }

    public EntityModel getModel() {
        return model;
    }
}