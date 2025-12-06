package com.kenhorizon.beyondhorizon.server.init;

import com.kenhorizon.beyondhorizon.server.recipe.WorkbenchRecipe;
import com.kenhorizon.libs.registry.RegistryEntries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

public class BHRecipe {
    public static final RegistryObject<RecipeSerializer<WorkbenchRecipe>> WORKBENCH_RECIPES = RegistryEntries.RECIPE_SERIALIZERS.register("workbench", WorkbenchRecipe.Serializer::getInstance);

    public static void register(IEventBus eventBus) {
        RegistryEntries.RECIPE_SERIALIZERS.register(eventBus);
    }
}
