package com.kenhorizon.beyondhorizon.server.init;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

public class BHDamageTypes {

    public static final ResourceKey<DamageType> BLEED = createKey("bleed");
    public static final ResourceKey<DamageType> TRUE_DAMAGE = createKey("true_damage");
    public static final ResourceKey<DamageType> LETHALITY = createKey("lethality");
    public static final ResourceKey<DamageType> ARMOR_PENETRATION = createKey("armor_penetration");
    public static final ResourceKey<DamageType> MAGIC_PENETRATION = createKey("magic_penetration");

    private static Registry<DamageType> damageTypes;

    public static void bootstrap(BootstapContext<DamageType> context) {
        context.register(BLEED, new DamageType("bleed", 0.1F));
        context.register(TRUE_DAMAGE, new DamageType("true_damage", 0.1F));
        context.register(LETHALITY, new DamageType("lethality", 0.1F));
        context.register(MAGIC_PENETRATION, new DamageType("magic_penetration", 0.1F));
        context.register(ARMOR_PENETRATION, new DamageType("armor_penetration", 0.1F));
    }
    public static void init(RegistryAccess registryAccess) {
        damageTypes = registryAccess.registryOrThrow(Registries.DAMAGE_TYPE);
    }
    private static DamageSource source(ResourceKey<DamageType> damageType) {
        return new DamageSource(BHDamageTypes.damageTypes.getHolderOrThrow(damageType));
    }

    private static DamageSource source(ResourceKey<DamageType> damageType, @Nullable Entity entity) {
        return new DamageSource(BHDamageTypes.damageTypes.getHolderOrThrow(damageType), entity);
    }

    private static DamageSource source(ResourceKey<DamageType> damageType, @Nullable Entity causingEntity, @Nullable Entity directEntity) {
        return new DamageSource(BHDamageTypes.damageTypes.getHolderOrThrow(damageType), causingEntity, directEntity);
    }
    public static DamageSource bleed() {
        return source(BLEED);
    }

    public static DamageSource armorPenetration(Entity source) {
        return source(ARMOR_PENETRATION, source);
    }

    public static DamageSource magicPenetration(Entity source) {
        return source(ARMOR_PENETRATION, source);
    }

    public static DamageSource lethality(Entity source) {
        return source(LETHALITY, source);
    }

    public static DamageSource trueDamage(Entity source, Entity target) {
        return source(TRUE_DAMAGE, source, target);
    }

    private static ResourceKey<DamageType> createKey(String keyName) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, BeyondHorizon.resource(keyName));
    }
}
