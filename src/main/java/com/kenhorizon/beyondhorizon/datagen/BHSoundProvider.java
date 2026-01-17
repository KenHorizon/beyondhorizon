package com.kenhorizon.beyondhorizon.datagen;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.init.BHSounds;
import com.kenhorizon.libs.datagen.SoundDefinitionFactory;
import com.kenhorizon.libs.registry.RegistryHelper;
import com.kenhorizon.libs.registry.RegistryLanguage;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;
import net.minecraftforge.registries.RegistryObject;

import java.util.*;
import java.util.function.Supplier;

public class BHSoundProvider extends SoundDefinitionFactory {

    public BHSoundProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, BeyondHorizon.ID, helper);
    }
    @Override
    public void registerSounds() {
        this.add(BHSounds.HEAVY_ATTACK, "generic/heavy_hit/heavyhit", 3);

        this.add(BHSounds.BLAZING_INFERNO_SHOOT, "mob/blazing_inferno/shoot/shoot", 2);
        this.add(BHSounds.BLAZING_INFERNO_SHOCKWAVE, "mob/blazing_inferno/shockwave/shockwave", 3);
        this.add(BHSounds.BLAZING_INFERNO_GROWL, "mob/blazing_inferno/growl/growl", 4);
        this.add(BHSounds.BLAZING_INFERNO_HURT, "mob/blazing_inferno/hurt/hurt", 3);
        this.add(BHSounds.BLAZING_INFERNO_IDLE, "mob/blazing_inferno/idle/idle", 4);
        this.add(BHSounds.BLAZING_INFERNO_STEP, "mob/blazing_inferno/scream");
        this.add(BHSounds.BLAZING_INFERNO_DEATH, "mob/blazing_inferno/death/death", 3);
        this.add(BHSounds.BLAZING_INFERNO_CHARGE, "mob/blazing_inferno/charging/charging", 4);
        this.add(BHSounds.BLAZING_INFERNO_EXPLOSION, "mob/blazing_inferno/explosion");
        this.add(BHSounds.BLAZING_INFERNO_DEATH_RAY, "mob/blazing_inferno/death_ray");
        this.add(BHSounds.BLAZING_INFERNO_DEATH_RAY_CHARGING, "mob/blazing_inferno/death_ray_charging");

        this.add(BHSounds.INFERNO_SHIELD_BREAK, "mob/inferno_shield/break", 3);

        this.add(BHSounds.SPAWNER_HIT, "block/spawner_block/break", 3);
        this.add(BHSounds.SPAWNER_BREAK, "block/spawner_block/break", 3);
        this.add(BHSounds.SPAWNER_FALL, "block/spawner_block/step", 5);
        this.add(BHSounds.SPAWNER_STEP, "block/spawner_block/step", 5);
        this.add(BHSounds.SPAWNER_PLACE, "block/spawner_block/place", 3);
        this.add(BHSounds.SPAWNER_AMBIENT, "block/spawner_block/ambient", 5);
        this.add(BHSounds.SPAWNER_AMBIENT_OMINOUS, "block/spawner_block/ambient_ominous", 5);
        this.add(BHSounds.SPAWNER_ABOUT_TO_SPAWN,"block/spawner_block/about_to_spawn_item");
        this.add(BHSounds.SPAWNER_SPAWN,"block/spawner_block/spawn", 4);
        this.add(BHSounds.SPAWNER_SPAWN_ITEM_BEGIN,"block/spawner_block/spawn_item_begin", 3);
        this.add(BHSounds.SPAWNER_SPAWN_ITEM,"block/spawner_block/spawn_item", 3);
        this.add(BHSounds.SPAWNER_EJECT_ITEM,"block/spawner_block/eject_item");
        this.add(BHSounds.SPAWNER_OMINOUS_ACTIVATE, "block/spawner_block/ominous_activate");
        this.add(BHSounds.SPAWNER_OPEN_SHUTTER, "block/spawner_block/open_shutter");
        this.add(BHSounds.SPAWNER_CLOSE_SHUTTER, "block/spawner_block/close_shutter");
        this.add(BHSounds.SPAWNER_DETECT_PLAYER, "block/spawner_block/detect_player", 3);
    }
}
