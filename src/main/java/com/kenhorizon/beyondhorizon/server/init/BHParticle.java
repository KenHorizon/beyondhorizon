package com.kenhorizon.beyondhorizon.server.init;

import com.kenhorizon.beyondhorizon.client.particle.world.DamageIndicator;
import com.kenhorizon.libs.registry.RegistryEntries;
import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

public class BHParticle {
    public static final RegistryObject<SimpleParticleType> BLEED = RegistryEntries.PARTICLE.register("bleed", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> STUN_PARTICLES = RegistryEntries.PARTICLE.register("stun_particles", () -> new SimpleParticleType(true));
    public static final RegistryObject<ParticleType<DamageIndicator>> DAMAGE_INDICATOR = RegistryEntries.PARTICLE.register("damage_indicator", () -> new ParticleType<DamageIndicator>(false, DamageIndicator.DESERIALIZER) {
        @Override
        public Codec<DamageIndicator> codec() {
            return DamageIndicator.CODEC;
        }
    });

    public static void register(IEventBus eventBus) {
        RegistryEntries.PARTICLE.register(eventBus);
    }
}
