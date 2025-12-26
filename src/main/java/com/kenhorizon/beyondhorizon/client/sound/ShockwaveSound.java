package com.kenhorizon.beyondhorizon.client.sound;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ShockwaveSound extends AbstractTickableSoundInstance {

    private final Entity entity;
    private float maxDistance;

    public ShockwaveSound(Entity entity, SoundEvent sound, float maxDistance) {
        super(sound, SoundSource.NEUTRAL, SoundInstance.createUnseededRandom());
        this.entity = entity;
        this.maxDistance = maxDistance;
        this.x = (double)((float)this.entity.getX());
        this.y = (double)((float)this.entity.getY());
        this.z = (double)((float)this.entity.getZ());
        this.pitch = 1.0F;
        this.delay = 0;
    }

    @Override
    public void tick() {
        Player player = BeyondHorizon.PROXY.clientPlayer();
        float distance = (float) player.distanceTo(this.entity);
        this.volume = Mth.clamp(1.0F - (distance / this.maxDistance), 0.0F, 1.0F);
    }
}
