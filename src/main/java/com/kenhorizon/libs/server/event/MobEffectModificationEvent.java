package com.kenhorizon.libs.server.event;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.NotNull;

public class MobEffectModificationEvent extends LivingEvent {
    private MobEffectInstance effectInstance;
    public MobEffectModificationEvent(LivingEntity entity, @NotNull MobEffectInstance effectInstance) {
        super(entity);
        this.setEffectInstance(effectInstance);
    }

    public void setEffectInstance(MobEffectInstance effectInstance) {
        this.effectInstance = effectInstance;
    }

    public MobEffectInstance getEffectInstance() {
        return effectInstance;
    }

}
