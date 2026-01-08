package com.kenhorizon.beyondhorizon.client.particle.world;

import com.kenhorizon.beyondhorizon.client.particle.IndicatorRingParticles;
import com.kenhorizon.beyondhorizon.client.particle.RingParticles;
import com.kenhorizon.beyondhorizon.server.init.BHParticle;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Locale;

public class IndicatorRingParticleOptions implements ParticleOptions {
    public static final Deserializer<IndicatorRingParticleOptions> DESERIALIZER = new Deserializer<IndicatorRingParticleOptions>() {
        public IndicatorRingParticleOptions fromCommand(ParticleType<IndicatorRingParticleOptions> particleTypeIn, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            float yaw = (float) reader.readDouble();
            reader.expect(' ');
            float pitch = (float) reader.readDouble();
            reader.expect(' ');
            float r = (float) reader.readDouble();
            reader.expect(' ');
            float g = (float) reader.readDouble();
            reader.expect(' ');
            float b = (float) reader.readDouble();
            reader.expect(' ');
            float a = (float) reader.readDouble();
            reader.expect(' ');
            float scale = (float) reader.readDouble();
            reader.expect(' ');
            int duration = reader.readInt();
            return new IndicatorRingParticleOptions(yaw, pitch, duration, r, g, b, a, scale, IndicatorRingParticles.Behavior.GROW);
        }

        public IndicatorRingParticleOptions fromNetwork(ParticleType<IndicatorRingParticleOptions> particleTypeIn, FriendlyByteBuf buffer) {
            return new IndicatorRingParticleOptions(buffer.readFloat(), buffer.readFloat(), buffer.readInt(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), IndicatorRingParticles.Behavior.GROW);
        }
    };


    public static Codec<IndicatorRingParticleOptions> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                    Codec.FLOAT.fieldOf("yaw").forGetter(IndicatorRingParticleOptions::getYaw),
                    Codec.FLOAT.fieldOf("pitch").forGetter(IndicatorRingParticleOptions::getPitch),
                    Codec.FLOAT.fieldOf("r").forGetter(IndicatorRingParticleOptions::getR),
                    Codec.FLOAT.fieldOf("g").forGetter(IndicatorRingParticleOptions::getG),
                    Codec.FLOAT.fieldOf("b").forGetter(IndicatorRingParticleOptions::getB),
                    Codec.FLOAT.fieldOf("a").forGetter(IndicatorRingParticleOptions::getA),
                    Codec.FLOAT.fieldOf("scale").forGetter(IndicatorRingParticleOptions::getScale),
                    Codec.INT.fieldOf("duration").forGetter(IndicatorRingParticleOptions::getDuration),
                    Codec.STRING.fieldOf("behavior").forGetter((options) -> options.getBehavior().toString())
            ).apply(instance, (yaw, pitch, r, g, b, a, scale, duration, behavior) ->
                    new IndicatorRingParticleOptions(yaw, pitch, duration, r, g, b, a, scale, IndicatorRingParticles.Behavior.valueOf(behavior)))
    );
    private final float yaw;
    private final float pitch;
    private final float r;
    private final float g;
    private final float b;
    private final float a;
    private final float scale;
    private final int duration;
    private final IndicatorRingParticles.Behavior behavior;

    public IndicatorRingParticleOptions(float yaw, float pitch, int duration, float r, float g, float b, float a, float scale, IndicatorRingParticles.Behavior behavior) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        this.scale = scale;
        this.duration = duration;
        this.behavior = behavior;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeFloat(this.r);
        buffer.writeFloat(this.g);
        buffer.writeFloat(this.b);
        buffer.writeFloat(this.scale);
        buffer.writeInt(this.duration);
    }

    @SuppressWarnings("deprecation")
    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %.2f %.2f %.2f %d", BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()),
                this.yaw, this.pitch, this.r, this.g, this.b, this.scale, this.a, this.duration);
    }

    @Override
    public ParticleType<IndicatorRingParticleOptions> getType() {
        return BHParticle.INDICATOR.get();
    }

    @OnlyIn(Dist.CLIENT)
    public float getYaw() {
        return this.yaw;
    }

    @OnlyIn(Dist.CLIENT)
    public float getPitch() {
        return this.pitch;
    }

    @OnlyIn(Dist.CLIENT)
    public float getR() {
        return this.r;
    }

    @OnlyIn(Dist.CLIENT)
    public float getG() {
        return this.g;
    }

    @OnlyIn(Dist.CLIENT)
    public float getB() {
        return this.b;
    }

    @OnlyIn(Dist.CLIENT)
    public float getA() {
        return this.a;
    }

    @OnlyIn(Dist.CLIENT)
    public float getScale() {
        return this.scale;
    }

    @OnlyIn(Dist.CLIENT)
    public int getDuration() {
        return this.duration;
    }

    @OnlyIn(Dist.CLIENT)
    public IndicatorRingParticles.Behavior getBehavior() {
        return this.behavior;
    }
}