package com.kenhorizon.beyondhorizon.server.api.bonus_set;

import java.util.ArrayList;
import java.util.List;

public class ArmorSetRegistry {
    private static final List<ArmorSet> SETS = new ArrayList<>();

    public static void register(ArmorSet set) {
        SETS.add(set);
    }

    public static List<ArmorSet> getAll() {
        return SETS;
    }
}
