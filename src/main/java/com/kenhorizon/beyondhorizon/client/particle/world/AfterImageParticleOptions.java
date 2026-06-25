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

public class AfterImageParticleOptions implements ParticleOptions {
    public static final Deserializer<AfterImageParticleOptions> DESERIALIZER = new Deserializer<AfterImageParticleOptions>() {
        public AfterImageParticleOptions fromCommand(ParticleType<AfterImageParticleOptions> particleTypeIn, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            int entityId = reader.readInt();
            reader.expect(' ');
            int r = reader.readInt();
            reader.expect(' ');
            int g = reader.readInt();
            reader.expect(' ');
            int b = reader.readInt();
            reader.expect(' ');
            int duration = reader.readInt();
            return new AfterImageParticleOptions(entityId, r, g, b, duration);
        }

        public AfterImageParticleOptions fromNetwork(ParticleType<AfterImageParticleOptions> particleType, FriendlyByteBuf buf) {
            return new AfterImageParticleOptions(buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt());
        }
    };

    private final int duration;
    private final int entityId;
    private final int r;
    private final int g;
    private final int b;
    public AfterImageParticleOptions(int entityId, int r, int g, int b, int duration) {
        this.duration = duration;
        this.entityId = entityId;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buff) {
        buff.writeInt(this.entityId);
        buff.writeInt(this.r);
        buff.writeInt(this.g);
        buff.writeInt(this.b);
        buff.writeInt(this.duration);

    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %d %d %d %s", BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()),
                this.duration, this.r, this.g, this.b, this.entityId);
    }

    @Override
    public ParticleType<AfterImageParticleOptions> getType() {
        return BHParticle.AFTERIMAGE.get();
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
    public int getEntityId() {
        return this.entityId;
    }


    public static Codec<AfterImageParticleOptions> CODEC = RecordCodecBuilder.create((codecBuilder) -> codecBuilder.group(
                Codec.INT.fieldOf("entity").forGetter(AfterImageParticleOptions::getEntityId),
                Codec.INT.fieldOf("r").forGetter(AfterImageParticleOptions::getR),
                Codec.INT.fieldOf("g").forGetter(AfterImageParticleOptions::getG),
                Codec.INT.fieldOf("b").forGetter(AfterImageParticleOptions::getB),
                Codec.INT.fieldOf("duration").forGetter(AfterImageParticleOptions::getDuration)
        ).apply(codecBuilder, AfterImageParticleOptions::new));
}
