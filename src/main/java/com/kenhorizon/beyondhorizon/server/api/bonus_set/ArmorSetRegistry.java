package com.kenhorizon.beyondhorizon.server.api.bonus_set;

import java.util.ArrayList;
import java.util.List;

public class ArmorSetRegistry {
    private static final List<ArmorBonusSet> SETS = new ArrayList<>();

    public static void register(ArmorBonusSet set) {
        SETS.add(set);
    }

    public static List<ArmorBonusSet> getAll() {
        return SETS;
    }
}
