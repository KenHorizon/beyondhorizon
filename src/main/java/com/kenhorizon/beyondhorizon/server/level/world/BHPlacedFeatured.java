package com.kenhorizon.beyondhorizon.server.level.world;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class BHPlacedFeatured {

    public static final ResourceKey<PlacedFeature> BLACK_IRON_ORES_FEATURES = createKey("black_iron_ores_features");
    public static final ResourceKey<PlacedFeature> CRIMSNITE_ORES_FEATURES = createKey("crimsnite_ores_features");
    public static final ResourceKey<PlacedFeature> LUMINITE_GEODE_FEATURES = createKey("luminite_geode_features");
    public static final ResourceKey<PlacedFeature> STARITE_GEODE_FEATURES = createKey("starite_geode_features");

    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
        HolderGetter<PlacedFeature> placeFeatures = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<Feature<?>> features = context.lookup(Registries.FEATURE);

        register(context, BLACK_IRON_ORES_FEATURES, configuredFeatures.getOrThrow(BHConfiguredFeatures.OW_BLACK_IRON_FEATURE),
                BHPlacementUtils.commonOrePlacement(16, // veins per chunk
                        HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(-64), VerticalAnchor.absolute(16))));

        register(context, CRIMSNITE_ORES_FEATURES, configuredFeatures.getOrThrow(BHConfiguredFeatures.OW_CRIMSNITE_FEATURE),
                BHPlacementUtils.commonOrePlacement(16, // veins per chunk
                        HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(-64), VerticalAnchor.absolute(16))));

        register(context, LUMINITE_GEODE_FEATURES, configuredFeatures.getOrThrow(BHConfiguredFeatures.OW_LUMINITE_GEODE_FEATURE),
                List.of(RarityFilter.onAverageOnceEvery(25), InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(6), VerticalAnchor.absolute(50)),
                        BiomeFilter.biome()));

        register(context, STARITE_GEODE_FEATURES, configuredFeatures.getOrThrow(BHConfiguredFeatures.OW_STARITE_GEODE_FEATURE),
                List.of(RarityFilter.onAverageOnceEvery(25), InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(6), VerticalAnchor.absolute(50)),
                        BiomeFilter.biome()));
    }


    private static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key,
                                 Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }

    private static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key,
                                 Holder<ConfiguredFeature<?, ?>> configuration,
                                 PlacementModifier... modifiers) {
        register(context, key, configuration, List.of(modifiers));
    }

    private static ResourceKey<PlacedFeature> createKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, BeyondHorizon.resource(name));
    }
}
