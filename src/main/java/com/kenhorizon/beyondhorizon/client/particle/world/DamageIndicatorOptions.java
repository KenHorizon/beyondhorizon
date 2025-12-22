package com.kenhorizon.beyondhorizon.client.particle.world;

import com.kenhorizon.beyondhorizon.server.init.BHParticle;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ExtraCodecs;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Locale;

public final class DamageIndicatorOptions implements ParticleOptions {
    private final Component damage;
    private final boolean big;
    public DamageIndicatorOptions(Component damage, boolean big) {
        this.damage = damage;
        this.big = big;
    }

    public Component getDamage() {
        return damage;
    }

    public boolean isBig() {
        return big;
    }

    @Override
    public ParticleType<?> getType() {
        return BHParticle.DAMAGE_INDICATOR.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        buf.writeComponent(this.damage);
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %s %d", ForgeRegistries.PARTICLE_TYPES.getKey(this.getType()), this.damage.getString(), this.big ? 1 : 0);
    }
    public static final Codec<DamageIndicatorOptions> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
                ExtraCodecs.COMPONENT.fieldOf("damage").forGetter(option -> option.damage),
                Codec.BOOL.fieldOf("bigText").forGetter(option -> option.big)
        ).apply(instance, DamageIndicatorOptions::new);
    });
    @SuppressWarnings("deprecation")
    public static final Deserializer<DamageIndicatorOptions> DESERIALIZER = new Deserializer<>() {
        @Override
        public DamageIndicatorOptions fromCommand(ParticleType<DamageIndicatorOptions> particleType, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            String text = reader.getString();
            reader.expect(' ');
            boolean big = reader.readBoolean();
            return new DamageIndicatorOptions(Component.literal(text), big);
        }

        @Override
        public DamageIndicatorOptions fromNetwork(ParticleType<DamageIndicatorOptions> particleType, FriendlyByteBuf buf) {
            return new DamageIndicatorOptions(buf.readComponent(), buf.readBoolean());
        }
    };
}
