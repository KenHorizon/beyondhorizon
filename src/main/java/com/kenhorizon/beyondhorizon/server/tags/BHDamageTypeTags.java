package com.kenhorizon.beyondhorizon.server.tags;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

public class BHDamageTypeTags {
    public static final TagKey<DamageType> IS_TRUE_DAMAGE = create("is_true_damage");
    public static final TagKey<DamageType> IS_ARMOR_PENETRATION = create("is_armor_penetration");
    public static final TagKey<DamageType> IS_MAGIC_PENETRATION = create("is_magic_penetration");
    public static final TagKey<DamageType> MAGIC_DAMAGE = create("magic_damage");
    public static final TagKey<DamageType> PHYSICAL_DAMAGE = create("physical_damage");
    public static final TagKey<DamageType> TRUE_DAMAGE = create("true_damage");
    public static final TagKey<DamageType> RANGED_DAMAGE = create("ranged_damage");
    public static final TagKey<DamageType> CANT_STORE_DAMAGE = create("cant_store_damage");

    public static TagKey<DamageType> create(ResourceLocation loc) {
        return TagKey.create(Registries.DAMAGE_TYPE, loc);
    }

    public static TagKey<DamageType> create(String namespace, String path) {
        return create(ResourceLocation.fromNamespaceAndPath(namespace, path));
    }

    public static TagKey<DamageType> create(String path) {
        return create(BeyondHorizon.resource(path));
    }
}
