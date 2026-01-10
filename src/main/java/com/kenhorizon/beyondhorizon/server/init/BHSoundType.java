package com.kenhorizon.beyondhorizon.server.init;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.common.util.ForgeSoundType;

public class BHSoundType {
    public static final SoundType SPAWNER = new ForgeSoundType(1.0F, 1.0F, BHSounds.SPAWNER_BREAK, BHSounds.SPAWNER_STEP, BHSounds.SPAWNER_PLACE, BHSounds.SPAWNER_HIT, BHSounds.SPAWNER_FALL);

}
