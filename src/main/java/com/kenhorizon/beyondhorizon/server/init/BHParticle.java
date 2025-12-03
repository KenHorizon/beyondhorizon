package com.kenhorizon.beyondhorizon.server.init;

import com.kenhorizon.libs.registry.RegistryEntries;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

public class BHParticle {
    public static final RegistryObject<SimpleParticleType> BLEED = RegistryEntries.PARTICLE.register("bleed", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> STUN_PARTICLES = RegistryEntries.PARTICLE.register("stun_particles", () -> new SimpleParticleType(true));

    public static void register(IEventBus eventBus) {
        RegistryEntries.PARTICLE.register(eventBus);
    }
}
