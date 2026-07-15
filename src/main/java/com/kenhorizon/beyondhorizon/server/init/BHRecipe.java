package com.kenhorizon.beyondhorizon.server.init;

import com.kenhorizon.beyondhorizon.server.item.recipe.WorkbenchRecipe;
import com.kenhorizon.libs.registry.RegistryEntries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

public class BHRecipe {
    public static final RegistryObject<RecipeSerializer<WorkbenchRecipe>> WORKBENCH_RECIPES = RegistryEntries.RECIPE_SERIALIZERS.register("workbench", WorkbenchRecipe.Serializer::new);
    public static final RegistryObject<RecipeType<WorkbenchRecipe>> WORKBENCH_RECIPE_TYPES = RegistryEntries.RECIPE_TYPES.register("workbench_type", WorkbenchRecipe.Type::new);

    public static void register(IEventBus eventBus) {
        RegistryEntries.RECIPE_SERIALIZERS.register(eventBus);
        RegistryEntries.RECIPE_TYPES.register(eventBus);
    }
}
