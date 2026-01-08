package com.kenhorizon.beyondhorizon.server.init;

import com.kenhorizon.beyondhorizon.client.particle.world.*;
import com.kenhorizon.libs.registry.RegistryEntries;
import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

public class BHParticle {
    public static final RegistryObject<SimpleParticleType> HELLFIRE_ORB_TRAIL = RegistryEntries.PARTICLE.register("hellfire_orb_trail", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> HELLFIRE_ORB_EXPLOSION = RegistryEntries.PARTICLE.register("hellfire_orb_explosion", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> BLEED = RegistryEntries.PARTICLE.register("bleed", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> STUN_PARTICLES = RegistryEntries.PARTICLE.register("stun_particles", () -> new SimpleParticleType(true));
    public static final RegistryObject<ParticleType<DamageIndicatorOptions>> DAMAGE_INDICATOR = RegistryEntries.PARTICLE.register("damage_indicator", () -> new ParticleType<DamageIndicatorOptions>(false, DamageIndicatorOptions.DESERIALIZER) {
        @Override
        public Codec<DamageIndicatorOptions> codec() {
            return DamageIndicatorOptions.CODEC;
        }
    });
    public static final RegistryObject<ParticleType<IndicatorRingParticleOptions>> INDICATOR = RegistryEntries.PARTICLE.register("indicator", () -> new ParticleType<IndicatorRingParticleOptions>(false, IndicatorRingParticleOptions.DESERIALIZER) {
        @Override
        public Codec<IndicatorRingParticleOptions> codec() {
            return IndicatorRingParticleOptions.CODEC;
        }
    });
    public static final RegistryObject<ParticleType<RingParticleOptions>> RING = RegistryEntries.PARTICLE.register("ring", () -> new ParticleType<RingParticleOptions>(false, RingParticleOptions.DESERIALIZER) {
        @Override
        public Codec<RingParticleOptions> codec() {
            return RingParticleOptions.CODEC;
        }
    });
    public static final RegistryObject<ParticleType<RingParticleOptions>> RING_BIG = RegistryEntries.PARTICLE.register("ring_big", () -> new ParticleType<RingParticleOptions>(false, RingParticleOptions.DESERIALIZER) {
        @Override
        public Codec<RingParticleOptions> codec() {
            return RingParticleOptions.CODEC;
        }
    });
    public static final RegistryObject<ParticleType<ParticleTrailOptions>> TRAILS = RegistryEntries.PARTICLE.register("trails", () -> new ParticleType<ParticleTrailOptions>(false, ParticleTrailOptions.DESERIALIZER) {
        @Override
        public Codec<ParticleTrailOptions> codec() {
            return ParticleTrailOptions.CODEC;
        }
    });
    public static final RegistryObject<ParticleType<RoarParticleOptions>> ROAR = RegistryEntries.PARTICLE.register("roar", () -> new ParticleType<RoarParticleOptions>(false, RoarParticleOptions.DESERIALIZER) {
        @Override
        public Codec<RoarParticleOptions> codec() {
            return RoarParticleOptions.CODEC;
        }
    });
    public static void register(IEventBus eventBus) {
        RegistryEntries.PARTICLE.register(eventBus);
    }
}
