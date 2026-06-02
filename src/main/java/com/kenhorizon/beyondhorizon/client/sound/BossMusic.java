package com.kenhorizon.beyondhorizon.client.sound;

import com.kenhorizon.beyondhorizon.client.model.util.ControlledAnimation;
import com.kenhorizon.beyondhorizon.server.entity.BHBaseEntity;
import com.kenhorizon.beyondhorizon.server.entity.BHLibEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BossMusic<T extends BHBaseEntity> {
    protected T boss;
    protected SoundEvent soundEvent;
    protected BossMusicSound sound;
    protected boolean isPlaying;
    protected int ticksPlaying = 0;
    protected int timeUntilFade;
    ControlledAnimation volumeControl;

    public BossMusic(SoundEvent soundEvent) {
        this.soundEvent = soundEvent;
        timeUntilFade = 80;

        volumeControl = new ControlledAnimation(40);
        volumeControl.setTimer(20);
    }

    public void tick() {
        // If the music should stop playing
        if (this.boss == null || !this.boss.isAlive() || this.boss.isSilent() || this.boss.isRemoved()) {
            // If the boss is dead, skip the fade timer and fade out right away
            if (this.boss != null && !this.boss.isAlive()) this.timeUntilFade = 0;
            this.boss = null;
            if (this.timeUntilFade > 0) this.timeUntilFade--;
            else this.volumeControl.decreaseTimer();
        }
        // If the music should keep playing
        else {
            this.volumeControl.increaseTimer();
            this.timeUntilFade = 60;
        }

        if (this.volumeControl.getAnimationFraction() < 0.025) {
            stop();
        }
        if (this.ticksPlaying % 100 == 0) {
            Minecraft.getInstance().getMusicManager().stopPlaying();
        }
        this.ticksPlaying++;
    }

    public void play() {
        this.volumeControl.setTimer(20);
        this.isPlaying = true;
        this.ticksPlaying = 0;
        if (this.soundEvent != null) {
            this.sound = new BossMusicSound(this.soundEvent, getBoss(), this);
            Minecraft.getInstance().getSoundManager().play(this.sound);
        }
    }

    public void stop() {
        if (this.sound != null) this.sound.doStop();
        this.isPlaying = false;
        BossMusicPlayer.currentMusic = null;
        this.ticksPlaying = 0;
        this.sound = null;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public T getBoss() {
        return boss;
    }

    public void setBoss(T boss) {
        this.boss = boss;
    }

    public SoundEvent getSoundEvent() {
        return soundEvent;
    }
}
