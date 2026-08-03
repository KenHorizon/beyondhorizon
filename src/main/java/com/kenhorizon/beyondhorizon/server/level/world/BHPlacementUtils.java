package com.kenhorizon.beyondhorizon.server.level.world;

import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class BHPlacementUtils {
    private static List<PlacementModifier> placementFeatures(PlacementModifier countperchunk, PlacementModifier modifier) {
        return List.of(countperchunk, InSquarePlacement.spread(), modifier, BiomeFilter.biome());
    }

    public static List<PlacementModifier> commonOrePlacement(int countperchunk, PlacementModifier modifier) {
        return placementFeatures(CountPlacement.of(countperchunk), modifier);
    }

    public static List<PlacementModifier> rareOrePlacement(int countperchunk, PlacementModifier modifier) {
        return placementFeatures(RarityFilter.onAverageOnceEvery(countperchunk), modifier);
    }
}
