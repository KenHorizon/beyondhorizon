package com.kenhorizon.libs.registry;

import com.kenhorizon.libs.client.data.ItemModelGenerator;
import com.kenhorizon.libs.client.model.item.ItemModelDefinition;
import com.kenhorizon.libs.client.model.item.ItemModels;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public record RegistryItemModels(ItemModelProvider itemModelProvider) {
    public enum Type {
        BUILTIN_ENTITY,
        SPAWN_EGG,
        THROWING,
        HANDHELD,
        HOLD,
        CROSSBOW_HOLD,
        BOW_HOLD
    }

    public static Map<Supplier<? extends Item>, ItemModelDefinition> MODEL = new HashMap<>();

    public static void register(Supplier<? extends Item> item, ItemModels category) {
        for (ItemModels register : ItemModels.values()) {
            if (category == register) {
                RegistryItemModels.MODEL.put(item, register.getItemModel());
            }
        }
    }

    public static void register(Supplier<? extends Item> item, ItemModelDefinition model) {
        RegistryItemModels.MODEL.put(item, model);
    }

    public void registerModels() {
        final ItemModelGenerator itemModelGenerator = new ItemModelGenerator(this.itemModelProvider);
        RegistryItemModels.MODEL.forEach(itemModelGenerator::createItemModels);
    }
}
