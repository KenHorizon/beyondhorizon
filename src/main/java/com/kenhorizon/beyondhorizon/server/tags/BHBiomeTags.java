package com.kenhorizon.beyondhorizon.server.tags;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class BHBiomeTags {
    public static final TagKey<Biome> BRUTE_MAMMOTH_SPAWN = create("brute_mammoth_spawn");
    public static final TagKey<Biome> TRENCH_IGNORES_STONE_IN = create("trench_ignores_stone_in");
    public static final TagKey<Biome> IS_UNDERGROUND_JUNGLE = create("is_undeground_jungle");
    public static final TagKey<Biome> DRAGON_CHASM = create("dragon_chasm");

    public static TagKey<Biome> create(ResourceLocation loc) {
        return TagKey.create(Registries.BIOME, loc);
    }

    public static TagKey<Biome> create(String namespace, String path) {
        return create(ResourceLocation.fromNamespaceAndPath(namespace, path));
    }

    public static TagKey<Biome> create(String path) {
        return create(BeyondHorizon.resource(path));
    }
}
