package com.kenhorizon.beyondhorizon.server.init;

import com.kenhorizon.libs.registry.RegistryEntries;
import com.kenhorizon.libs.registry.RegistryHelper;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

public class BHSounds {
    public static RegistryObject<SoundEvent> BLAZING_INFERNO_HURT = RegistryHelper.registerSounds("mob.blazing_inferno.hurt", "Blazing Inferno Hurt");
    public static RegistryObject<SoundEvent> BLAZING_INFERNO_IDLE = RegistryHelper.registerSounds("mob.blazing_inferno.idle", "Blazing Inferno Idle");
    public static RegistryObject<SoundEvent> BLAZING_INFERNO_SCREAM = RegistryHelper.registerSounds("mob.blazing_inferno.scream", "Blazing Inferno Screma");
    public static RegistryObject<SoundEvent> BLAZING_INFERNO_STEP = RegistryHelper.registerSounds("mob.blazing_inferno.step", "Blazing Inferno Step");
    public static RegistryObject<SoundEvent> BLAZING_INFERNO_GROWL = RegistryHelper.registerSounds("mob.blazing_inferno.growl", "Blazing Inferno Growling");
    public static RegistryObject<SoundEvent> BLAZING_INFERNO_SHOOT = RegistryHelper.registerSounds("mob.blazing_inferno.shoot", "Blazing Inferno Shoot");
    public static RegistryObject<SoundEvent> BLAZING_INFERNO_CHARGE = RegistryHelper.registerSounds("mob.blazing_inferno.charge", "Blazing Inferno Charging");
    public static RegistryObject<SoundEvent> BLAZING_INFERNO_SHOCKWAVE = RegistryHelper.registerSounds("mob.blazing_inferno.shockwave", "Blazing Inferno Shockwave");
    public static RegistryObject<SoundEvent> BLAZING_INFERNO_EXPLOSION = RegistryHelper.registerSounds("mob.blazing_inferno.explosion", "Blazing Inferno Explosding");
    public static RegistryObject<SoundEvent> BLAZING_INFERNO_DEATH_RAY = RegistryHelper.registerSounds("mob.blazing_inferno.death_ray", "Death Ray Blast");
    public static RegistryObject<SoundEvent> BLAZING_INFERNO_DEATH_RAY_CHARGING = RegistryHelper.registerSounds("mob.blazing_inferno.death_ray_charging", "Death Ray Charging");
    public static RegistryObject<SoundEvent> BLAZING_INFERNO_DEATH = RegistryHelper.registerSounds("mob.blazing_inferno.death", "Blazing Inferno Death");

    public static RegistryObject<SoundEvent> HEAVY_ATTACK = RegistryHelper.registerSounds("generic.heavy_attack", "Heavy Attack");


    public static void register(IEventBus eventBus) {
        RegistryEntries.SOUND_EVENTS.register(eventBus);
    }
}
