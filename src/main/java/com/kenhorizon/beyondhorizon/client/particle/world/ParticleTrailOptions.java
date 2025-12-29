package com.kenhorizon.beyondhorizon.client.particle.world;

import com.kenhorizon.beyondhorizon.client.particle.TrailParticles;
import com.kenhorizon.beyondhorizon.server.init.BHParticle;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Locale;

public class ParticleTrailOptions implements ParticleOptions {
    public static final Deserializer<ParticleTrailOptions> DESERIALIZER = new Deserializer<ParticleTrailOptions>() {
        public ParticleTrailOptions fromCommand(ParticleType<ParticleTrailOptions> particleTypeIn, StringReader reader) throws CommandSyntaxException {
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
            reader.expect(' ');
            boolean facesCamera = reader.readBoolean();
            reader.expect(' ');
            int mode = reader.readInt();
            reader.expect(' ');
            float targetX = reader.readFloat();
            reader.expect(' ');
            float targetY = reader.readFloat();
            reader.expect(' ');
            float targetZ = reader.readFloat();
            return new ParticleTrailOptions(yaw, pitch, duration, r, g, b, a, scale, facesCamera, TrailParticles.Behavior.values()[mode], new Vec3(targetX, targetY, targetZ));
        }

        public ParticleTrailOptions fromNetwork(ParticleType<ParticleTrailOptions> particleTypeIn, FriendlyByteBuf buffer) {
            return new ParticleTrailOptions(buffer.readFloat(), buffer.readFloat(), buffer.readInt(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readBoolean(), TrailParticles.Behavior.values()[buffer.readInt()], new Vec3(buffer.readFloat(), buffer.readFloat(), buffer.readFloat()));
        }
    };

    private final float yaw;
    private final float pitch;
    private final float r;
    private final float g;
    private final float b;
    private final float a;
    private final float scale;
    private final int duration;
    private final boolean facesCamera;
    private final Vec3 target;
    private final TrailParticles.Behavior behavior;

    public ParticleTrailOptions(float yaw, float pitch, int duration, float r, float g, float b, float a, float scale, boolean facesCamera, TrailParticles.Behavior behavior, Vec3 target) {
        this.target = target;
        this.yaw = yaw;
        this.pitch = pitch;
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        this.scale = scale;
        this.duration = duration;
        this.facesCamera = facesCamera;
        this.behavior = behavior;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeFloat(this.yaw);
        buffer.writeFloat(this.pitch);
        buffer.writeFloat(this.r);
        buffer.writeFloat(this.g);
        buffer.writeFloat(this.b);
        buffer.writeFloat(this.a);
        buffer.writeFloat(this.scale);
        buffer.writeInt(this.duration);
        buffer.writeBoolean(this.facesCamera);
        buffer.writeInt(this.behavior.ordinal());
        buffer.writeDouble(this.target.x);
        buffer.writeDouble(this.target.y);
        buffer.writeDouble(this.target.z);
    }

    @SuppressWarnings("deprecation")
    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %.2f %.2f %.2f %d %b", BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()),
                this.yaw, this.pitch, this.r, this.g, this.b, this.scale, this.a, this.duration, this.facesCamera);
    }

    @Override
    public ParticleType<ParticleTrailOptions> getType() {
        return BHParticle.TRAILS.get();
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
    public boolean getFacesCamera() {
        return this.facesCamera;
    }

    @OnlyIn(Dist.CLIENT)
    public TrailParticles.Behavior getBehavior() {
        return this.behavior;
    }

    @OnlyIn(Dist.CLIENT)
    public Vec3 getTarget() {
        return target;
    }

    public static Codec<ParticleTrailOptions> CODEC = RecordCodecBuilder.create((codecBuilder) -> codecBuilder.group(
                        Codec.FLOAT.fieldOf("yaw").forGetter(ParticleTrailOptions::getYaw),
                        Codec.FLOAT.fieldOf("pitch").forGetter(ParticleTrailOptions::getPitch),
                        Codec.FLOAT.fieldOf("r").forGetter(ParticleTrailOptions::getR),
                        Codec.FLOAT.fieldOf("g").forGetter(ParticleTrailOptions::getG),
                        Codec.FLOAT.fieldOf("b").forGetter(ParticleTrailOptions::getB),
                        Codec.FLOAT.fieldOf("a").forGetter(ParticleTrailOptions::getA),
                        Codec.FLOAT.fieldOf("scale").forGetter(ParticleTrailOptions::getScale),
                        Codec.INT.fieldOf("duration").forGetter(ParticleTrailOptions::getDuration),
                        Codec.BOOL.fieldOf("facesCamera").forGetter(ParticleTrailOptions::getFacesCamera),
                        Codec.STRING.fieldOf("behavior").forGetter((ParticleTrailOptions) -> ParticleTrailOptions.getBehavior().toString())
                ).apply(codecBuilder, (yaw, pitch, r, g, b, a, scale, duration, facesCamera, behavior) ->
                        new ParticleTrailOptions(yaw, pitch, duration, r, g, b, a, scale, facesCamera, TrailParticles.Behavior.valueOf(behavior), null))
        );

    public static void add(Level level, TrailParticles.Behavior behavior, double x, double y, double z, double motionX, double motionY, double motionZ, float yaw, float pitch, float scale, float a, float r, float g, float b, boolean faceCamera, int duration, Vec3 destination) {
        level.addParticle(new ParticleTrailOptions(yaw, pitch, duration,  a,  r,  g, b,  scale, faceCamera, behavior, destination), x, y, z, motionX, motionY, motionZ);
    }
}