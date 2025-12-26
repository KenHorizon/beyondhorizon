package com.kenhorizon.beyondhorizon.client.sound;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DeathRayChargingSound extends AbstractTickableSoundInstance {
    private final Entity entity;

    public DeathRayChargingSound(Entity entity, SoundEvent sounds) {
        super(sounds, SoundSource.NEUTRAL, SoundInstance.createUnseededRandom());
        this.entity = entity;
        this.volume = 2.0F;
        this.pitch = 1.0F;
        this.attenuation = Attenuation.LINEAR;
        this.x = (double)((float) this.entity.getX());
        this.y = (double)((float) this.entity.getY());
        this.z = (double)((float) this.entity.getZ());
        this.delay = 0;
    }

    @Override
    public void tick() {
        if (!this.entity.isAlive()) {
            stop();
        }
    }
}
