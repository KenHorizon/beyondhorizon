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

public class CircleLightningParticleOptions implements ParticleOptions {
    public static final Deserializer<CircleLightningParticleOptions> DESERIALIZER = new Deserializer<CircleLightningParticleOptions>() {
        public CircleLightningParticleOptions fromCommand(ParticleType<CircleLightningParticleOptions> particleTypeIn, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            float s = reader.readFloat();
            reader.expect(' ');
            int r = reader.readInt();
            reader.expect(' ');
            int g = reader.readInt();
            reader.expect(' ');
            int b = reader.readInt();
            return new CircleLightningParticleOptions(s, r, g, b);
        }

        public CircleLightningParticleOptions fromNetwork(ParticleType<CircleLightningParticleOptions> particleTypeIn, FriendlyByteBuf buffer) {
            return new CircleLightningParticleOptions(buffer.readFloat(), buffer.readInt(), buffer.readInt(), buffer.readInt());
        }
    };

    private final float size;
    private final int r;
    private final int g;
    private final int b;



    public CircleLightningParticleOptions(float size, int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.size = size;

    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeInt(this.r);
        buffer.writeInt(this.g);
        buffer.writeInt(this.b);
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %d %d %d", BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()),
                this.r, this.g, this.b);
    }

    @Override
    public ParticleType<CircleLightningParticleOptions> getType() {
        return BHParticle.CIRCLE_LIGHTNING.get();
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
    public float getSize() {
        return this.size;
    }

    public static Codec<CircleLightningParticleOptions> CODEC = RecordCodecBuilder.create((codecBuilder) -> codecBuilder.group(
            Codec.FLOAT.fieldOf("size").forGetter(CircleLightningParticleOptions::getSize),
            Codec.INT.fieldOf("r").forGetter(CircleLightningParticleOptions::getR),
                        Codec.INT.fieldOf("g").forGetter(CircleLightningParticleOptions::getG),
                        Codec.INT.fieldOf("b").forGetter(CircleLightningParticleOptions::getB)
                ).apply(codecBuilder, CircleLightningParticleOptions::new)
        );
}