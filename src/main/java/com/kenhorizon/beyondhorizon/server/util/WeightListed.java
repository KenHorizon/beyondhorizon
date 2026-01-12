package com.kenhorizon.beyondhorizon.server.util;

import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.SpawnData;
import org.jetbrains.annotations.NotNull;

public class WeightListed {
    public static <T> @NotNull SimpleWeightedRandomList<T> of(T data) {
        return SimpleWeightedRandomList.<T>builder().add(data, 1).build();
    }
    public static <T> @NotNull SimpleWeightedRandomList<T> of(T data, int weight) {
        return SimpleWeightedRandomList.<T>builder().add(data, weight).build();
    }
}
