package com.kenhorizon.beyondhorizon.client.sound;

import com.kenhorizon.beyondhorizon.server.entity.ability.AbstractDeathRayAbility;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public class DeathRaySound extends AbstractTickableSoundInstance {
    private final AbstractDeathRayAbility laserBeam;
    boolean endLocation = false;

    public DeathRaySound(AbstractDeathRayAbility laserBeam, SoundEvent sounds, boolean endLocation) {
        super(sounds, SoundSource.NEUTRAL, SoundInstance.createUnseededRandom());
        this.laserBeam = laserBeam;
        this.volume = 1.0F;
        this.pitch = 1.0F;
        this.endLocation = endLocation;
    }

    @Override
    public void tick() {
        if (!this.laserBeam.isAlive()) {
            this.stop();
        }
        if (this.endLocation && this.laserBeam.hasDoneRaytrace()) {
            this.x = (float) this.laserBeam.collidePosX;
            this.y = (float) this.laserBeam.collidePosY;
            this.z = (float) this.laserBeam.collidePosZ;
        }
        else {
            this.x = (float) this.laserBeam.getX();
            this.y = (float) this.laserBeam.getY();
            this.z = (float) this.laserBeam.getZ();
        }
    }
}
