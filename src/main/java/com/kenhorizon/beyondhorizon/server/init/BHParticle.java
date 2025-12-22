package com.kenhorizon.beyondhorizon.server.init;

import com.kenhorizon.beyondhorizon.client.particle.RingParticles;
import com.kenhorizon.beyondhorizon.client.particle.world.DamageIndicatorOptions;
import com.kenhorizon.beyondhorizon.client.particle.world.RingParticleOptions;
import com.kenhorizon.libs.registry.RegistryEntries;
import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

public class BHParticle {
    public static final RegistryObject<SimpleParticleType> BLEED = RegistryEntries.PARTICLE.register("bleed", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> STUN_PARTICLES = RegistryEntries.PARTICLE.register("stun_particles", () -> new SimpleParticleType(true));
    public static final RegistryObject<ParticleType<DamageIndicatorOptions>> DAMAGE_INDICATOR = RegistryEntries.PARTICLE.register("damage_indicator", () -> new ParticleType<DamageIndicatorOptions>(false, DamageIndicatorOptions.DESERIALIZER) {
        @Override
        public Codec<DamageIndicatorOptions> codec() {
            return DamageIndicatorOptions.CODEC;
        }
    });
    public static final RegistryObject<ParticleType<RingParticleOptions>> RING = RegistryEntries.PARTICLE.register("ring", () -> new ParticleType<RingParticleOptions>(false, RingParticleOptions.DESERIALIZER) {
        @Override
        public Codec<RingParticleOptions> codec() {
            return RingParticleOptions.CODEC;
        }
    });

    public static void register(IEventBus eventBus) {
        RegistryEntries.PARTICLE.register(eventBus);
    }
}
