package com.kenhorizon.beyondhorizon.server.level.world;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.init.BHBlocks;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GeodeBlockSettings;
import net.minecraft.world.level.levelgen.GeodeCrackSettings;
import net.minecraft.world.level.levelgen.GeodeLayerSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class BHConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> OW_BLACK_IRON_FEATURE = registerKey("overworld_black_iron_ore_feature");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OW_CRIMSNITE_FEATURE = registerKey("overworld_crimsnite_ore_feature");

    public static final ResourceKey<ConfiguredFeature<?, ?>> OW_LUMINITE_GEODE_FEATURE = registerKey("overworld_luminite_geode_feature");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OW_STARITE_GEODE_FEATURE = registerKey("overworld_starite_geode_feature");

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        HolderGetter<Block> holdergetter = context.lookup(Registries.BLOCK);
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatureGetter = context.lookup(Registries.CONFIGURED_FEATURE);
        RuleTest stoneOreReplaceables = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateOreReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        RuleTest stoneBaseOverworld = new TagMatchTest(BlockTags.BASE_STONE_OVERWORLD);
        RuleTest netherrackReplaceabeles = new BlockMatchTest(Blocks.NETHERRACK);
        RuleTest endReplaceabeles = new BlockMatchTest(Blocks.END_STONE);

        List<OreConfiguration.TargetBlockState> oreBlackIron = List.of(
                OreConfiguration.target(deepslateOreReplaceables, BHBlocks.BLACK_IRON_ORE.get().defaultBlockState()));
        List<OreConfiguration.TargetBlockState> oreCrimsnite = List.of(
                OreConfiguration.target(deepslateOreReplaceables, BHBlocks.CRIMSNITE_ORE.get().defaultBlockState()));

        register(context, OW_BLACK_IRON_FEATURE, Feature.ORE, new OreConfiguration(oreBlackIron, 9));
        register(context, OW_CRIMSNITE_FEATURE, Feature.ORE, new OreConfiguration(oreCrimsnite, 7));

        register(context, OW_STARITE_GEODE_FEATURE, Feature.GEODE,
                new GeodeConfiguration(new GeodeBlockSettings(BlockStateProvider.simple(Blocks.AIR),
                        BlockStateProvider.simple(BHBlocks.STARITE_ROCKS.get()),
                        BlockStateProvider.simple(BHBlocks.STARITE_ORE.get()),
                        BlockStateProvider.simple(Blocks.CALCITE),
                        BlockStateProvider.simple(BHBlocks.STARITE_ROCKS.get()),
                        List.of(BHBlocks.STARITE_ORE.get().defaultBlockState()),
                        BlockTags.FEATURES_CANNOT_REPLACE, BlockTags.GEODE_INVALID_BLOCKS),
                        new GeodeLayerSettings(1.7D, 2.2D, 3.2D, 4.2D),
                        new GeodeCrackSettings(0.95D, 2.0D, 2), 0.35D, 0.083D,
                        true, UniformInt.of(4, 6),
                        UniformInt.of(3, 4), UniformInt.of(1, 2),
                        -16, 16, 0.05D, 1));

        register(context, OW_LUMINITE_GEODE_FEATURE, Feature.GEODE,
                new GeodeConfiguration(new GeodeBlockSettings(BlockStateProvider.simple(Blocks.AIR),
                        BlockStateProvider.simple(BHBlocks.LUMINITE_ROCKS.get()),
                        BlockStateProvider.simple(BHBlocks.LUMINITE_ORE.get()),
                        BlockStateProvider.simple(Blocks.CALCITE),
                        BlockStateProvider.simple(BHBlocks.LUMINITE_ROCKS.get()),
                        List.of(BHBlocks.LUMINITE_ORE.get().defaultBlockState()),
                        BlockTags.FEATURES_CANNOT_REPLACE, BlockTags.GEODE_INVALID_BLOCKS),
                        new GeodeLayerSettings(1.7D, 2.2D, 3.2D, 4.2D),
                        new GeodeCrackSettings(0.95D, 2.0D, 2), 0.35D, 0.083D,
                        true, UniformInt.of(4, 6),
                        UniformInt.of(3, 4), UniformInt.of(1, 2),
                        -16, 16, 0.05D, 1));

//        FeatureUtils.register(pContext, AMETHYST_GEODE, Feature.GEODE,
//                new GeodeConfiguration(new GeodeBlockSettings(BlockStateProvider.simple(Blocks.AIR),
//                        BlockStateProvider.simple(Blocks.AMETHYST_BLOCK),
//                        BlockStateProvider.simple(Blocks.BUDDING_AMETHYST),
//                        BlockStateProvider.simple(Blocks.CALCITE),
//                        BlockStateProvider.simple(Blocks.SMOOTH_BASALT),
//                        List.of(Blocks.SMALL_AMETHYST_BUD.defaultBlockState(),
//                                Blocks.MEDIUM_AMETHYST_BUD.defaultBlockState(),
//                                Blocks.LARGE_AMETHYST_BUD.defaultBlockState(),
//                                Blocks.AMETHYST_CLUSTER.defaultBlockState()),
//                        BlockTags.FEATURES_CANNOT_REPLACE, BlockTags.GEODE_INVALID_BLOCKS),
//                        new GeodeLayerSettings(1.7D, 2.2D, 3.2D, 4.2D),
//                        new GeodeCrackSettings(0.95D, 2.0D, 2), 0.35D, 0.083D, true,
//                        UniformInt.of(4, 6), UniformInt.of(3, 4),
//                        UniformInt.of(1, 2), -16, 16, 0.05D, 1));


    }

    private static OreConfiguration.TargetBlockState createTarget(RuleTest replaceables, Block blocks) {
        return OreConfiguration.target(replaceables, blocks.defaultBlockState());
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC config) {
        context.register(key, new ConfiguredFeature<>(feature, config));
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, BeyondHorizon.resource(name));
    }
}
