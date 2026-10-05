package com.kenhorizon.beyondhorizon.compat;

import net.minecraftforge.fml.ModList;

public class ModLists {
    public static final String NEAT = "neat";
    public static final String ANVIL_FIX = "anvilfix";
    public static final String ATTRIBUTE_FIX = "attributefix";
    public static final String ALEX_CAVES = "alexscaves";

    private static boolean neatLoaded;
    private static boolean anvilFixLoaded;
    private static boolean attributeFixLoaded;
    private static boolean alexCavesLoaded;

    public static void afterAllModsLoaded() {
        neatLoaded = ModList.get().isLoaded(NEAT);
        anvilFixLoaded = ModList.get().isLoaded(ANVIL_FIX);
        attributeFixLoaded = ModList.get().isLoaded(ATTRIBUTE_FIX);
        alexCavesLoaded = ModList.get().isLoaded(ALEX_CAVES);
    }

    public static boolean isAlexCavesLoaded() {
        return alexCavesLoaded;
    }

    public static boolean isAnvilFixLoaded() {
        return anvilFixLoaded;
    }

    public static boolean isAttributeFixLoaded() {
        return attributeFixLoaded;
    }

    public static boolean isNeatLoaded() {
        return neatLoaded;
    }
}
