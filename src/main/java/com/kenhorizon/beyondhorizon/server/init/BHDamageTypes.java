package com.kenhorizon.beyondhorizon.server.init;

import com.kenhorizon.beyondhorizon.server.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.level.damagesource.AdvanceDamageSource;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageInfoTypes;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageTags;
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

    public static final ResourceKey<DamageType> TRUE_DAMAGE_BURNING = createKey("true_damage_burning");
    public static final ResourceKey<DamageType> PHYSICAL_BURNING = createKey("phyiscal_burning");
    public static final ResourceKey<DamageType> MAGIC_BURNING = createKey("magic_burning");
    public static final ResourceKey<DamageType> BLEED = createKey("bleed");
    public static final ResourceKey<DamageType> IGNORE_ENCHANTMENT_PROTECTION = createKey("ignore_enchantment_protection");
    public static final ResourceKey<DamageType> BLAZING_ROD = createKey("blazing_rod");
    public static final ResourceKey<DamageType> PHYSICAL_DAMAGE = createKey("physical_damage");
    public static final ResourceKey<DamageType> MAGIC_DAMAGE = createKey("magic_damage");
    public static final ResourceKey<DamageType> TRUE_DAMAGE = createKey("true_damage");
    public static final ResourceKey<DamageType> NO_KNOCKBACK_PHYSICAL_DAMAGE = createKey("no_knockback_physical_damage");
    public static final ResourceKey<DamageType> NO_KNOCKBACK_MAGIC_DAMAGE = createKey("no_knockback_magic_damage");
    public static final ResourceKey<DamageType> NO_KNOCKBACK_TRUE_DAMAGE = createKey("no_knockback_true_damage");

    private static Registry<DamageType> damageTypes;

    public static void bootstrap(BootstapContext<DamageType> context) {
        context.register(NO_KNOCKBACK_PHYSICAL_DAMAGE, new DamageType("no_knockback_physical_damage", 0.1F));
        context.register(NO_KNOCKBACK_MAGIC_DAMAGE, new DamageType("no_knockback_magic_damage", 0.1F));
        context.register(NO_KNOCKBACK_TRUE_DAMAGE, new DamageType("no_knockback_true_damage", 0.1F));
        context.register(TRUE_DAMAGE_BURNING, new DamageType("true_burning", 0.1F));
        context.register(PHYSICAL_BURNING, new DamageType("physical_burning", 0.1F));
        context.register(MAGIC_BURNING, new DamageType("magic_burning", 0.1F));
        context.register(IGNORE_ENCHANTMENT_PROTECTION, new DamageType("ignore_enchantment_protection", 0.1F));
        context.register(BLAZING_ROD, new DamageType("blazing_rod", 0.1F));
        context.register(BLEED, new DamageType("bleed", 0.1F));
        context.register(PHYSICAL_DAMAGE, new DamageType("physical_damage", 0.1F));
        context.register(MAGIC_DAMAGE, new DamageType("magic_damage", 0.1F));
        context.register(TRUE_DAMAGE, new DamageType("true_damage", 0.1F));
    }
    public static void init(RegistryAccess registryAccess) {
        damageTypes = registryAccess.registryOrThrow(Registries.DAMAGE_TYPE);
    }

    private static DamageSource source(ResourceKey<DamageType> damageType, DamageTags damageTags) {
        return new AdvanceDamageSource(BHDamageTypes.damageTypes.getHolderOrThrow(damageType), damageTags);
    }

    private static DamageSource source(ResourceKey<DamageType> damageType, @Nullable Entity entity, DamageTags damageTags) {
        return new AdvanceDamageSource(BHDamageTypes.damageTypes.getHolderOrThrow(damageType), entity, damageTags);
    }

    private static DamageSource source(ResourceKey<DamageType> damageType, @Nullable Entity causingEntity, @Nullable Entity directEntity, DamageTags damageTags) {
        return new AdvanceDamageSource(BHDamageTypes.damageTypes.getHolderOrThrow(damageType), causingEntity, directEntity, damageTags);
    }
    private static DamageSource source(ResourceKey<DamageType> damageType) {
        return source(damageType, DamageTags.DEFAULT);
    }

    private static DamageSource source(ResourceKey<DamageType> damageType, @Nullable Entity entity) {
        return source(damageType, entity, DamageTags.DEFAULT);
    }

    private static DamageSource source(ResourceKey<DamageType> damageType, @Nullable Entity causingEntity, @Nullable Entity directEntity) {
        return source(damageType, causingEntity, directEntity, DamageTags.DEFAULT);
    }

    public static DamageSource applyDamage(DamageInfoTypes damageInfoTypes, DamageTags damageTags, Entity source, Entity cause, boolean noKnockback) {
        if (damageInfoTypes == DamageInfoTypes.MAGIC_DAMAGE) {
            var damageTypes = noKnockback ? NO_KNOCKBACK_MAGIC_DAMAGE : MAGIC_DAMAGE;
            return source(damageTypes, source ,cause, damageTags);
        } else if (damageInfoTypes == DamageInfoTypes.TRUE_DAMAGE) {
            var damageTypes = noKnockback ? NO_KNOCKBACK_TRUE_DAMAGE : TRUE_DAMAGE;
            return source(damageTypes, source ,cause, damageTags);
        } else {
            var damageTypes = noKnockback ? NO_KNOCKBACK_PHYSICAL_DAMAGE : PHYSICAL_DAMAGE;
            return source(damageTypes, source ,cause, damageTags);
        }
    }

    public static DamageSource noKnockbackApplyDamage(DamageInfoTypes DamageInfoTypes, DamageTags damageTags, Entity source, Entity cause) {
        return applyDamage(DamageInfoTypes, damageTags, source, cause, true);
    }

    public static DamageSource noKnockbackApplyDamage(DamageInfoTypes DamageInfoTypes, DamageTags damageTags, Entity source) {
        return applyDamage(DamageInfoTypes, damageTags, source, source, true);
    }

    public static DamageSource noKnockbackApplyDamage(DamageInfoTypes DamageInfoTypes, Entity source, Entity cause) {
        return applyDamage(DamageInfoTypes, DamageTags.DEFAULT, source, cause, true);
    }

    public static DamageSource noKnockbackApplyDamage(DamageInfoTypes DamageInfoTypes, Entity source) {
        return applyDamage(DamageInfoTypes, DamageTags.DEFAULT, source, source, true);
    }

    public static DamageSource applyDamage(DamageInfoTypes DamageInfoTypes, DamageTags damageTags, Entity source, boolean noKnockback) {
        return applyDamage(DamageInfoTypes, damageTags, source, source, noKnockback);
    }

    public static DamageSource applyDamage(DamageInfoTypes DamageInfoTypes, DamageTags damageTags, Entity source, Entity cause) {
        return applyDamage(DamageInfoTypes, damageTags, source, cause, false);
    }

    public static DamageSource applyDamage(DamageInfoTypes DamageInfoTypes, Entity source) {
        return applyDamage(DamageInfoTypes, DamageTags.DEFAULT, source, source, false);
    }

    public static DamageSource applyDamage(DamageInfoTypes DamageInfoTypes, Entity source, Entity cause) {
        return applyDamage(DamageInfoTypes, DamageTags.DEFAULT, source, cause, false);
    }

    public static DamageSource bleed() {
        return source(BLEED);
    }

    public static DamageSource burnTrueDamage() {
        return source(TRUE_DAMAGE_BURNING);
    }

    public static DamageSource burnPhysical() {
        return source(PHYSICAL_BURNING);
    }

    public static DamageSource burnMagic() {
        return source(MAGIC_BURNING);
    }

    public static DamageSource blazingRod(Entity source, Entity cause) {
        return source(BLAZING_ROD, source, cause);
    }

    public static DamageSource nullify(Entity source, Entity target) {
        return source(IGNORE_ENCHANTMENT_PROTECTION, source, target);
    }
    public static DamageSource nullify(Entity source) {
        return source(IGNORE_ENCHANTMENT_PROTECTION, source);
    }

    private static ResourceKey<DamageType> createKey(String keyName) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, BeyondHorizon.resource(keyName));
    }
}
