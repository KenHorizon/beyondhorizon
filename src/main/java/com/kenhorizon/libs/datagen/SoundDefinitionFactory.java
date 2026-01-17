package com.kenhorizon.libs.datagen;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.libs.registry.RegistryLanguage;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public abstract class SoundDefinitionFactory extends SoundDefinitionsProvider {
    public SoundDefinitionFactory(PackOutput output, String modId, ExistingFileHelper helper) {
        super(output, modId, helper);
    }


    protected void add(final RegistryObject<? extends SoundEvent> soundEvent, final String definition) {
        add(soundEvent, 1.0F, 1.0F, definition, 1);
    }

    protected void add(final RegistryObject<? extends SoundEvent> soundEvent, final String definition, int count) {
        add(soundEvent, 1.0F, 1.0F, definition, count);
    }

    protected void add(final RegistryObject<? extends SoundEvent> soundEvent, float volume, float pitch, final String definition, int count) {
        String localization = String.format("subtitle.%s.%s", BeyondHorizon.ID, soundEvent.get().getLocation().getPath());
        BeyondHorizon.LOGGER.debug("Check sound {}", localization);
        boolean check = !RegistryLanguage.ADD_SOUNDS_TRANSLATION.getOrDefault(localization, "").isBlank();
        SoundDefinition.Sound[] addSounds = new SoundDefinition.Sound[count];
        for (int i = 0; i < count; i++) {
            String str0 = definition + (1 + i);
            String str1 = definition;
            addSounds[i] = (this.sounds(count == 1 ? str1 : str0, volume, pitch));
        }
        if (check) {
            this.add(soundEvent.get(), subtitle(soundEvent).with(addSounds));
        } else {
            this.add(soundEvent.get(), SoundDefinition.definition().with(addSounds));
        }
    }

    private SoundDefinition subtitle(Supplier<? extends SoundEvent> subtile) {
        return SoundDefinition.definition().subtitle(String.format("subtitle.%s.%s", BeyondHorizon.ID, subtile.get().getLocation().getPath()));
    }

    private SoundDefinition.Sound sounds(String sound) {
        return this.sounds(sound, 1.0F, 1.0F, SoundDefinition.SoundType.SOUND);
    }

    private SoundDefinition.Sound sounds(String sound, float volume, float pitch) {
        return this.sounds(sound, volume, pitch, SoundDefinition.SoundType.SOUND);
    }

    private SoundDefinition.Sound sounds(String sound, double volume, double pitch) {
        return this.sounds(sound, volume, pitch, SoundDefinition.SoundType.SOUND);
    }

    private SoundDefinition.Sound sounds(String sound, float volume, float pitch, SoundDefinition.SoundType type) {
        return SoundDefinition.Sound.sound(BeyondHorizon.resource(sound), type).volume(volume).pitch(pitch);
    }

    private SoundDefinition.Sound sounds(String sound, double volume, double pitch, SoundDefinition.SoundType type) {
        return SoundDefinition.Sound.sound(BeyondHorizon.resource(sound), type).volume(volume).pitch(pitch);
    }
}