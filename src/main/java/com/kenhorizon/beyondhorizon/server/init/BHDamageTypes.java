package com.kenhorizon.beyondhorizon.server.init;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.level.damagesource.AdvanceDamageSource;
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

    public static final ResourceKey<DamageType> SPELL_DAMAGE_TRUE_DAMAGE = createKey("spell_damage_true_damage");
    public static final ResourceKey<DamageType> SPELL_DAMAGE_MAGIC = createKey("spell_damage_magic");
    public static final ResourceKey<DamageType> SPELL_DAMAGE_PHYSICAL = createKey("spell_damage_physical");
    public static final ResourceKey<DamageType> PET_DAMAGE_TRUE_DAMAGE = createKey("pet_damage_true_damage");
    public static final ResourceKey<DamageType> PET_DAMAGE_MAGIC = createKey("pet_damage_magic");
    public static final ResourceKey<DamageType> PET_DAMAGE_PHYSICAL = createKey("pet_damage_physical");
    public static final ResourceKey<DamageType> BEAM = createKey("beam");
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
    public static final ResourceKey<DamageType> LETHALITY = createKey("lethality");
    public static final ResourceKey<DamageType> ARMOR_PENETRATION = createKey("armor_penetration");
    public static final ResourceKey<DamageType> MAGIC_PENETRATION = createKey("magic_penetration");

    private static Registry<DamageType> damageTypes;

    public static void bootstrap(BootstapContext<DamageType> context) {
        context.register(NO_KNOCKBACK_PHYSICAL_DAMAGE, new DamageType("no_knockback_physical_damage", 0.1F));
        context.register(NO_KNOCKBACK_MAGIC_DAMAGE, new DamageType("no_knockback_magic_damage", 0.1F));
        context.register(NO_KNOCKBACK_TRUE_DAMAGE, new DamageType("no_knockback_true_damage", 0.1F));
        context.register(SPELL_DAMAGE_MAGIC, new DamageType("spell_damage_magic", 0.1F));
        context.register(SPELL_DAMAGE_PHYSICAL, new DamageType("spell_damage_physical", 0.1F));
        context.register(PET_DAMAGE_TRUE_DAMAGE, new DamageType("pet_damage_true_damage", 0.1F));
        context.register(PET_DAMAGE_MAGIC, new DamageType("pet_damage_magic", 0.1F));
        context.register(PET_DAMAGE_PHYSICAL, new DamageType("pet_damage_physical", 0.1F));
        context.register(TRUE_DAMAGE_BURNING, new DamageType("true_burning", 0.1F));
        context.register(PHYSICAL_BURNING, new DamageType("physical_burning", 0.1F));
        context.register(MAGIC_BURNING, new DamageType("magic_burning", 0.1F));
        context.register(IGNORE_ENCHANTMENT_PROTECTION, new DamageType("ignore_enchantment_protection", 0.1F));
        context.register(BEAM, new DamageType("beam", 0.1F));
        context.register(BLAZING_ROD, new DamageType("blazing_rod", 0.1F));
        context.register(BLEED, new DamageType("bleed", 0.1F));
        context.register(PHYSICAL_DAMAGE, new DamageType("physical_damage", 0.1F));
        context.register(MAGIC_DAMAGE, new DamageType("magic_damage", 0.1F));
        context.register(TRUE_DAMAGE, new DamageType("true_damage", 0.1F));
        context.register(LETHALITY, new DamageType("lethality", 0.1F));
        context.register(MAGIC_PENETRATION, new DamageType("magic_penetration", 0.1F));
        context.register(ARMOR_PENETRATION, new DamageType("armor_penetration", 0.1F));
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

    public static DamageSource armorPenetration(Entity source) {
        return source(ARMOR_PENETRATION, source);
    }

    public static DamageSource magicPenetration(Entity source) {
        return source(ARMOR_PENETRATION, source);
    }

    public static DamageSource lethality(Entity source) {
        return source(LETHALITY, source);
    }

    public static DamageSource deathRay(Entity source, Entity cause) {
        return source(BEAM, source, cause);
    }

    public static DamageSource deathRay(Entity source) {
        return source(BEAM, source, source);
    }

    public static DamageSource petDamageTrue(Entity source, Entity cause) {
        return source(PET_DAMAGE_TRUE_DAMAGE, source, cause);
    }

    public static DamageSource petDamagePhysical(Entity source, Entity cause) {
        return source(PET_DAMAGE_PHYSICAL, source, cause);
    }

    public static DamageSource petDamageMagic(Entity source, Entity cause) {
        return source(PET_DAMAGE_MAGIC, source, cause);
    }

    public static DamageSource magicDamage(Entity source, Entity cause) {
        return source(MAGIC_DAMAGE, source, cause);
    }

    public static DamageSource magicDamage(Entity source) {
        return source(MAGIC_DAMAGE, source, (Entity) null);
    }

    public static DamageSource magicDamage(Entity source, boolean noKnocback) {
        return magicDamage(source, (Entity) null, noKnocback);
    }

    public static DamageSource AOEmagicDamage(Entity source, Entity cause) {
        return source(NO_KNOCKBACK_MAGIC_DAMAGE, source, cause, DamageTags.AOT);
    }

    public static DamageSource AOEtrueDamage(Entity source, Entity cause) {
        return source(NO_KNOCKBACK_TRUE_DAMAGE, source, cause, DamageTags.AOT);
    }

    public static DamageSource magicDamage(Entity source, Entity cause, boolean noKnocback) {
        return source(noKnocback ? NO_KNOCKBACK_MAGIC_DAMAGE : MAGIC_DAMAGE, source, cause);
    }

    public static DamageSource physicalDamage(Entity source, Entity cause) {
        return source(PHYSICAL_DAMAGE, source, cause);
    }

    public static DamageSource physicalDamage(Entity source) {
        return source(PHYSICAL_DAMAGE, source, (Entity) null);
    }

    public static DamageSource physicalDamage(Entity source, boolean noKnocback) {
        return physicalDamage(source, null, noKnocback);
    }


    public static DamageSource AOEphysicalDamage(Entity source, Entity cause) {
        return source(NO_KNOCKBACK_PHYSICAL_DAMAGE, source, cause, DamageTags.AOT);
    }

    public static DamageSource physicalDamage(Entity source, Entity cause, boolean noKnocback) {
        if (noKnocback) {
            return physicalDamageNoKnockback(source, cause, DamageTags.DEFAULT);
        }
        return source(PHYSICAL_DAMAGE, source, cause);
    }

    public static DamageSource physicalDamageNoKnockback(Entity source, Entity cause, DamageTags damageTags) {
        return source(NO_KNOCKBACK_PHYSICAL_DAMAGE, source, cause, damageTags);
    }

    public static DamageSource trueDamage(Entity source, Entity cause) {
        return source(TRUE_DAMAGE, source, cause);
    }

    public static DamageSource trueDamage(Entity source, Entity cause, boolean noKnocback) {
        return source(noKnocback ? NO_KNOCKBACK_TRUE_DAMAGE : TRUE_DAMAGE, source, cause);
    }

    public static DamageSource trueDamage(Entity source, boolean noKnocback) {
        return source(noKnocback ? NO_KNOCKBACK_TRUE_DAMAGE : TRUE_DAMAGE, source, (Entity) null);
    }

    public static DamageSource trueDamage(Entity source, boolean noKnocback, DamageTags damageTags) {
        return source(noKnocback ? NO_KNOCKBACK_TRUE_DAMAGE : TRUE_DAMAGE, source, null, damageTags);
    }

    public static DamageSource trueDamage(Entity source) {
        return source(TRUE_DAMAGE, source, source);
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
