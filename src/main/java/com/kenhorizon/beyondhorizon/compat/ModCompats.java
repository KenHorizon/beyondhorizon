package com.kenhorizon.beyondhorizon.compat;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.util.LoaderExceptionModCrash;

public class ModCompats {
    public static final String LEGENDARY_TOOLTIP = "legendarytooltips";
    public static final String ENCHANTMENT_DESCRIPTION = "enchdesc";
    public static final String ANVIL_FIX = "anvilfix";
    public static final String ATTRIBUTE_FIX = "attributefix";
    public static final String ALEX_CAVES = "alexscaves";
    public static final String SPARTANWEAPONRY = "spartanweaponry";
    public static final String ICENFIRE = "iceandfire";
    protected static String modId = "";

    public ModCompats() {
    }

    public static ModCompats getMod(String string) {
        modId = string;
        return new ModCompats();
    }

    public boolean isModLoaded() {
        return ModList.get().isLoaded(modId);
    }

    public void throwException(String text, String runTimeException) {
        throw new LoaderExceptionModCrash(text, new RuntimeException(runTimeException));
    }
}
