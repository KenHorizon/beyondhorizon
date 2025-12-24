package com.kenhorizon.beyondhorizon.server.datagen;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.init.BHSounds;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class BHSoundProvider extends SoundDefinitionsProvider {
    /**
     * Creates a new instance of this data provider.
     *
     * @param output The {@linkplain PackOutput} instance provided by the data generator.
     * @param helper The existing file helper provided by the event you are initializing this provider in.
     */
    public BHSoundProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, BeyondHorizon.ID, helper);
    }
    @Override
    public void registerSounds() {
        this.add(BHSounds.HEAVY_ATTACK,
                this.addSounds("generic/heavy_hit/heavyhit1"),
                this.addSounds("generic/heavy_hit/heavyhit2"),
                this.addSounds("generic/heavy_hit/heavyhit3")
        );
        this.add(BHSounds.BLAZING_INFERNO_SHOOT,
                this.addSounds("mob/blazing_inferno/shoot/shoot1"),
                this.addSounds("mob/blazing_inferno/shoot/shoot2")
        );
        this.add(BHSounds.BLAZING_INFERNO_SHOCKWAVE,
                this.addSounds("mob/blazing_inferno/shockwave/shockwave1"),
                this.addSounds("mob/blazing_inferno/shockwave/shockwave2"),
                this.addSounds("mob/blazing_inferno/shockwave/shockwave3")
        );
        this.add(BHSounds.BLAZING_INFERNO_GROWL,
                this.addSounds("mob/blazing_inferno/growl/growl1"),
                this.addSounds("mob/blazing_inferno/growl/growl2"),
                this.addSounds("mob/blazing_inferno/growl/growl3"),
                this.addSounds("mob/blazing_inferno/growl/growl4")
        );
        this.add(BHSounds.BLAZING_INFERNO_HURT,
                this.addSounds("mob/blazing_inferno/hurt/hurt1"),
                this.addSounds("mob/blazing_inferno/hurt/hurt2"),
                this.addSounds("mob/blazing_inferno/hurt/hurt3")
        );
        this.add(BHSounds.BLAZING_INFERNO_IDLE,
                this.addSounds("mob/blazing_inferno/idle/idle1"),
                this.addSounds("mob/blazing_inferno/idle/idle2"),
                this.addSounds("mob/blazing_inferno/idle/idle3"),
                this.addSounds("mob/blazing_inferno/idle/idle4")
        );
        this.add(BHSounds.BLAZING_INFERNO_SCREAM,
                this.addSounds("mob/blazing_inferno/scream")
        );
        this.add(BHSounds.BLAZING_INFERNO_STEP,
                this.addSounds("mob/blazing_inferno/step/step1"),
                this.addSounds("mob/blazing_inferno/step/step2"),
                this.addSounds("mob/blazing_inferno/step/step3"),
                this.addSounds("mob/blazing_inferno/step/step4")
        );
        this.add(BHSounds.BLAZING_INFERNO_DEATH,
                this.addSounds("mob/blazing_inferno/death/death1"),
                this.addSounds("mob/blazing_inferno/death/death2"),
                this.addSounds("mob/blazing_inferno/death/death3")
        );
        this.add(BHSounds.BLAZING_INFERNO_CHARGE,
                this.addSounds("mob/blazing_inferno/charging/charging1"),
                this.addSounds("mob/blazing_inferno/charging/charging2"),
                this.addSounds("mob/blazing_inferno/charging/charging3"),
                this.addSounds("mob/blazing_inferno/charging/charging4")
        );
        this.add(BHSounds.BLAZING_INFERNO_EXPLOSION,
                this.addSounds("mob/blazing_inferno/explosion")
        );
        this.add(BHSounds.BLAZING_INFERNO_DEATH_RAY,
                this.addSounds("mob/blazing_inferno/death_ray")
        );
        this.add(BHSounds.BLAZING_INFERNO_DEATH_RAY_CHARGING,
                this.addSounds("mob/blazing_inferno/death_ray_charging")
        );
    }

    protected void add(final Supplier<SoundEvent> soundEvent, final SoundDefinition.Sound... definition) {
        this.add(soundEvent.get(), subtitle(soundEvent).with(definition));
    }

    private SoundDefinition subtitle(Supplier<SoundEvent> subtile) {
        return SoundDefinition.definition().subtitle(String.format("subtitle.%s.%s", BeyondHorizon.ID, subtile.get().getLocation().getPath()));
    }

    private SoundDefinition.Sound addSounds(String sound) {
        return addSounds(sound, SoundDefinition.SoundType.SOUND);
    }

    private SoundDefinition.Sound addSounds(String sound, SoundDefinition.SoundType type) {
        return SoundDefinition.Sound.sound(BeyondHorizon.resource(sound), type);
    }
}
