package com.kenhorizon.beyondhorizon.server.init;

import com.kenhorizon.libs.registry.RegistryEntries;
import com.kenhorizon.libs.registry.RegistryHelper;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

public class BHSounds {
    public static RegistryObject<SoundEvent> SPAWNER_BREAK = RegistryHelper.registerSounds("block.base_spawner.break", "Block broken");
    public static RegistryObject<SoundEvent> SPAWNER_PLACE = RegistryHelper.registerSounds("block.base_spawner.place", "Block placed");
    public static RegistryObject<SoundEvent> SPAWNER_STEP = RegistryHelper.registerSounds("block.base_spawner.step", "Block step");
    public static RegistryObject<SoundEvent> SPAWNER_FALL = RegistryHelper.registerSounds("block.base_spawner.fall", "Block fall");
    public static RegistryObject<SoundEvent> SPAWNER_HIT = RegistryHelper.registerSounds("block.base_spawner.hit", "Block breaking");
    public static RegistryObject<SoundEvent> SPAWNER_AMBIENT = RegistryHelper.registerSounds("block.base_spawner.ambient", "Spawner crackles");
    public static RegistryObject<SoundEvent> SPAWNER_AMBIENT_OMINOUS = RegistryHelper.registerSounds("block.base_spawner.ambient.ominous", "Ominous crackling");
    public static RegistryObject<SoundEvent> SPAWNER_ABOUT_TO_SPAWN = RegistryHelper.registerSounds("block.base_spawner.about_to_spawn", "Ominous item prepares");
    public static RegistryObject<SoundEvent> SPAWNER_SPAWN = RegistryHelper.registerSounds("block.base_spawner.spawn_mob", "Spawner spawns a mob");
    public static RegistryObject<SoundEvent> SPAWNER_SPAWN_ITEM_BEGIN = RegistryHelper.registerSounds("block.base_spawner.spawn_item.begin", "Ominous item appears");
    public static RegistryObject<SoundEvent> SPAWNER_SPAWN_ITEM = RegistryHelper.registerSounds("block.base_spawner.spawn_item", "Ominous item drops");
    public static RegistryObject<SoundEvent> SPAWNER_EJECT_ITEM = RegistryHelper.registerSounds("block.base_spawner.eject_item", "Spawner ejects items");
    public static RegistryObject<SoundEvent> SPAWNER_OMINOUS_ACTIVATE = RegistryHelper.registerSounds("block.base_spawner.ominous_activate", "Omen enguls spawner");
    public static RegistryObject<SoundEvent> SPAWNER_CLOSE_SHUTTER = RegistryHelper.registerSounds("block.base_spawner.close_shutter", "Spawner closes");
    public static RegistryObject<SoundEvent> SPAWNER_OPEN_SHUTTER = RegistryHelper.registerSounds("block.base_spawner.open_shutter", "Spawner opens");
    public static RegistryObject<SoundEvent> SPAWNER_DETECT_PLAYER = RegistryHelper.registerSounds("block.base_spawner.detect_player", "Spawner charges up");

    public static RegistryObject<SoundEvent> BLAZING_INFERNO_HURT = RegistryHelper.registerSounds("mob.blazing_inferno.hurt", "Blazing inferno hurt");
    public static RegistryObject<SoundEvent> BLAZING_INFERNO_IDLE = RegistryHelper.registerSounds("mob.blazing_inferno.idle", "Blazing inferno crackles");
    public static RegistryObject<SoundEvent> BLAZING_INFERNO_SCREAM = RegistryHelper.registerSounds("mob.blazing_inferno.scream", "Blazing inferno scream");
    public static RegistryObject<SoundEvent> BLAZING_INFERNO_STEP = RegistryHelper.registerSounds("mob.blazing_inferno.step", "Blazing inferno step");
    public static RegistryObject<SoundEvent> BLAZING_INFERNO_GROWL = RegistryHelper.registerSounds("mob.blazing_inferno.growl", "Blazing inferno growling");
    public static RegistryObject<SoundEvent> BLAZING_INFERNO_SHOOT = RegistryHelper.registerSounds("mob.blazing_inferno.shoot", "Blazing inferno shoot");
    public static RegistryObject<SoundEvent> BLAZING_INFERNO_CHARGE = RegistryHelper.registerSounds("mob.blazing_inferno.charge", "Blazing inferno charging");
    public static RegistryObject<SoundEvent> BLAZING_INFERNO_SHOCKWAVE = RegistryHelper.registerSounds("mob.blazing_inferno.shockwave", "Blazing inferno shockwave");
    public static RegistryObject<SoundEvent> BLAZING_INFERNO_EXPLOSION = RegistryHelper.registerSounds("mob.blazing_inferno.explosion", "Blazing inferno exploding");
    public static RegistryObject<SoundEvent> BLAZING_INFERNO_DEATH_RAY = RegistryHelper.registerSounds("mob.blazing_inferno.death_ray", "Death ray blast");
    public static RegistryObject<SoundEvent> BLAZING_INFERNO_DEATH_RAY_CHARGING = RegistryHelper.registerSounds("mob.blazing_inferno.death_ray_charging", "Death ray charging");
    public static RegistryObject<SoundEvent> BLAZING_INFERNO_DEATH = RegistryHelper.registerSounds("mob.blazing_inferno.death", "Blazing inferno death");
    public static RegistryObject<SoundEvent> INFERNO_SHIELD_BREAK = RegistryHelper.registerSounds("mob.inferno_shield.break", "Blazing inferno death");

    public static RegistryObject<SoundEvent> HEAVY_ATTACK = RegistryHelper.registerSounds("generic.heavy_attack", "Heavy attack");


    public static void register(IEventBus eventBus) {
        RegistryEntries.SOUND_EVENTS.register(eventBus);
    }
}
