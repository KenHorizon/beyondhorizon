package com.kenhorizon.beyondhorizon.compat.alex_cave;

import com.kenhorizon.beyondhorizon.compat.ModCompats;
import com.kenhorizon.beyondhorizon.compat.ModLists;
import com.kenhorizon.libs.registry.RegistryHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.ForgeRegistries;

public class AlexCaveModHelper {
    public static final ResourceLocation IRRADIATED = alexCave("irradiated");

    public static ResourceLocation alexCave(String rl) {
        return ModCompats.getMod(ModLists.ALEX_CAVES).getRL(rl);
    }

    public static boolean isIrradiated(LivingEntity entity) {
        return entity.hasEffect(RegistryHelper.getValueOrThrow(ForgeRegistries.MOB_EFFECTS, IRRADIATED));
    }
}
