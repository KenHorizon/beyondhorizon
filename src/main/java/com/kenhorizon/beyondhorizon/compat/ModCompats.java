package com.kenhorizon.beyondhorizon.compat;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.util.LoaderExceptionModCrash;

public class ModCompats {
    protected String modId = "";

    public ModCompats(String modId) {
        this.modId = modId;
    }

    public static ModCompats getMod(String string) {
        return new ModCompats(string);
    }

    public ResourceLocation getRL(String rl) {
        return ResourceLocation.fromNamespaceAndPath(this.modId, rl);
    }

    public boolean isModLoaded() {
        if (modId.isEmpty() || modId.isBlank()) {
            return false;
        } else {
            return ModList.get().isLoaded(modId);
        }
    }

    public void throwException(String text, String runTimeException) {
        throw new LoaderExceptionModCrash(text, new RuntimeException(runTimeException));
    }
}
