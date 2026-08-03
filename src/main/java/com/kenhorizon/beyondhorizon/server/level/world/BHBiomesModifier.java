package com.kenhorizon.beyondhorizon.server.level.world;


import com.kenhorizon.beyondhorizon.BeyondHorizon;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

public class BHBiomesModifier {
    public static final ResourceKey<BiomeModifier> BLACK_IRON_ORES = registerKey("add_black_iron_ores");
    public static final ResourceKey<BiomeModifier> CRIMSNITE_ORES = registerKey("add_crimsnite_ores");
    public static final ResourceKey<BiomeModifier> LUMINITE_GEODE = registerKey("add_luminite_geode");
    public static final ResourceKey<BiomeModifier> STARITE_GEODE = registerKey("add_starite_geode");

    public static void bootstrap(BootstapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        context.register(BLACK_IRON_ORES, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(BHPlacedFeatured.BLACK_IRON_ORES_FEATURES)),
                GenerationStep.Decoration.UNDERGROUND_ORES));

        context.register(CRIMSNITE_ORES, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(BHPlacedFeatured.CRIMSNITE_ORES_FEATURES)),
                GenerationStep.Decoration.UNDERGROUND_ORES));

        context.register(LUMINITE_GEODE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(BHPlacedFeatured.LUMINITE_GEODE_FEATURES)),
                GenerationStep.Decoration.LOCAL_MODIFICATIONS));

        context.register(STARITE_GEODE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(BHPlacedFeatured.STARITE_GEODE_FEATURES)),
                GenerationStep.Decoration.LOCAL_MODIFICATIONS));


    }

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, BeyondHorizon.resource(name));
    }
}
