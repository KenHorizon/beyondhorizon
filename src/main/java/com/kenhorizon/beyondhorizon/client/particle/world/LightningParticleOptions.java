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

public class LightningParticleOptions implements ParticleOptions {
    public static final Deserializer<LightningParticleOptions> DESERIALIZER = new Deserializer<LightningParticleOptions>() {
        public LightningParticleOptions fromCommand(ParticleType<LightningParticleOptions> particleTypeIn, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            int r = reader.readInt();
            reader.expect(' ');
            int g = reader.readInt();
            reader.expect(' ');
            int b = reader.readInt();
            return new LightningParticleOptions(r, g, b);
        }

        public LightningParticleOptions fromNetwork(ParticleType<LightningParticleOptions> particleTypeIn, FriendlyByteBuf buffer) {
            return new LightningParticleOptions(buffer.readInt(), buffer.readInt(), buffer.readInt());
        }
    };

    private final int r;
    private final int g;
    private final int b;



    public LightningParticleOptions(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;


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
    public ParticleType<LightningParticleOptions> getType() {
        return BHParticle.LIGHTNING.get();
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


//    public static Codec<LightningParticleOptions> CODEC(ParticleType<LightningParticleOptions> particleType) {
//        return RecordCodecBuilder.create((codecBuilder) -> codecBuilder.group(
//                        Codec.INT.fieldOf("r").forGetter(LightningParticleOptions::getR),
//                        Codec.INT.fieldOf("g").forGetter(LightningParticleOptions::getG),
//                        Codec.INT.fieldOf("b").forGetter(LightningParticleOptions::getB)
//                ).apply(codecBuilder, LightningParticleOptions::new)
//        );
//    }
    public static Codec<LightningParticleOptions> CODEC = RecordCodecBuilder.create((codecBuilder) -> codecBuilder.group(
                        Codec.INT.fieldOf("r").forGetter(LightningParticleOptions::getR),
                        Codec.INT.fieldOf("g").forGetter(LightningParticleOptions::getG),
                        Codec.INT.fieldOf("b").forGetter(LightningParticleOptions::getB)
                ).apply(codecBuilder, LightningParticleOptions::new)
        );
}