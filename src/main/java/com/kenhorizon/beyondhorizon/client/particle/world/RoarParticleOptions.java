package com.kenhorizon.beyondhorizon.client.particle.world;

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

public class RoarParticleOptions implements ParticleOptions {
    public static final Deserializer<RoarParticleOptions> DESERIALIZER = new Deserializer<RoarParticleOptions>() {
        public RoarParticleOptions fromCommand(ParticleType<RoarParticleOptions> particleTypeIn, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            int duration = reader.readInt();
            reader.expect(' ');
            int r = reader.readInt();
            reader.expect(' ');
            int g = reader.readInt();
            reader.expect(' ');
            int b = reader.readInt();
            reader.expect(' ');
            float a = reader.readFloat();
            reader.expect(' ');
            float start = reader.readFloat();
            reader.expect(' ');
            float increase = reader.readFloat();
            reader.expect(' ');
            float end = reader.readFloat();
            return new RoarParticleOptions(duration, r, g, b, a, start, increase, end);
        }

        public RoarParticleOptions fromNetwork(ParticleType<RoarParticleOptions> particleType, FriendlyByteBuf buf) {
            return new RoarParticleOptions(buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(),buf.readFloat() ,buf.readFloat(),buf.readFloat(),buf.readFloat());
        }
    };

    private final int duration;
    private final int r;
    private final int g;
    private final int b;
    private final float a;
    private final float startsize;
    private final float increase;
    private final float endsize;
    public RoarParticleOptions(int duration, int r, int g, int b, float a, float startsize,float increase, float endsize) {
        this.duration = duration;
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        this.startsize = startsize;
        this.increase = increase;
        this.endsize = endsize;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buff) {
        buff.writeInt(this.duration);
        buff.writeInt(this.r);
        buff.writeInt(this.g);
        buff.writeInt(this.b);
        buff.writeFloat(this.a);
        buff.writeFloat(this.startsize);
        buff.writeFloat(this.increase);
        buff.writeFloat(this.endsize);
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %d %d %d %d %.2f %.2f %.2f %.2f", BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()),
                this.duration, this.r, this.g, this.b, this.a, this.startsize, this.increase, this.endsize);
    }

    @Override
    public ParticleType<RoarParticleOptions> getType() {
        return BHParticle.ROAR.get();
    }

    @OnlyIn(Dist.CLIENT)
    public int getDuration() {
        return this.duration;
    }

    @OnlyIn(Dist.CLIENT)
    public int getR() {
        return this.r;
    }


    @OnlyIn(Dist.CLIENT)
    public int getG() {
        return this.g;
    }


    @OnlyIn(Dist.CLIENT)
    public int getB() {
        return this.b;
    }


    @OnlyIn(Dist.CLIENT)
    public float getA() {
        return this.a;
    }

    @OnlyIn(Dist.CLIENT)
    public float getStart() {
        return this.startsize;
    }

    @OnlyIn(Dist.CLIENT)
    public float getIncrease() {
        return this.increase;
    }

    @OnlyIn(Dist.CLIENT)
    public float getEnd() {
        return this.endsize;
    }


    public static Codec<RoarParticleOptions> CODEC = RecordCodecBuilder.create((codecBuilder) -> codecBuilder.group(
                Codec.INT.fieldOf("duration").forGetter(RoarParticleOptions::getDuration),
                Codec.INT.fieldOf("r").forGetter(RoarParticleOptions::getR),
                Codec.INT.fieldOf("g").forGetter(RoarParticleOptions::getG),
                Codec.INT.fieldOf("b").forGetter(RoarParticleOptions::getB),
                Codec.FLOAT.fieldOf("a").forGetter(RoarParticleOptions::getA),
                Codec.FLOAT.fieldOf("start").forGetter(RoarParticleOptions::getStart),
                Codec.FLOAT.fieldOf("increase").forGetter(RoarParticleOptions::getIncrease),
                Codec.FLOAT.fieldOf("end").forGetter(RoarParticleOptions::getEnd)
        ).apply(codecBuilder, RoarParticleOptions::new));
}
