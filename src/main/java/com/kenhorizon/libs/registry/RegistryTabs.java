package com.kenhorizon.libs.registry;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.IExtensibleEnum;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class RegistryTabs {
    public static List<RegistryObject<?>> VANILLA_BUILDING_BLOCKS = new ArrayList<>();
    public static List<RegistryObject<?>> VANILLA_COLORED_BLOCKS = new ArrayList<>();
    public static List<RegistryObject<?>> VANILLA_NATURAL_BLOCKS = new ArrayList<>();
    public static List<RegistryObject<?>> VANILLA_FUNCTIONAL_BLOCKS = new ArrayList<>();
    public static List<RegistryObject<?>> VANILLA_REDSTONE_BLOCKS = new ArrayList<>();
    public static List<RegistryObject<?>> VANILLA_TOOLS_AND_UTILITIES = new ArrayList<>();
    public static List<RegistryObject<?>> VANILLA_COMBAT = new ArrayList<>();
    public static List<RegistryObject<?>> VANILLA_FOOD_AND_DRINKS = new ArrayList<>();
    public static List<RegistryObject<?>> VANILLA_INGREDIENTS = new ArrayList<>();
    public static List<RegistryObject<?>> VANILLA_SPAWN_EGGS = new ArrayList<>();
    public static List<RegistryObject<?>> VANILLA_OP_BLOCKS = new ArrayList<>();

    public static List<RegistryObject<?>> ACCESSORY_LIST = new ArrayList<>();
    public static List<RegistryObject<?>> FOOD_LIST = new ArrayList<>();
    public static List<RegistryObject<?>> WEAPONS_LIST = new ArrayList<>();
    public static List<RegistryObject<?>> TOOLS_LIST = new ArrayList<>();
    public static List<RegistryObject<?>> MISC_LIST = new ArrayList<>();
    public static List<RegistryObject<?>> INGREDIENTS_LIST = new ArrayList<>();
    public static List<RegistryObject<?>> SPAWN_EGG_LIST = new ArrayList<>();
    public static List<RegistryObject<?>> BLOCKS_LIST = new ArrayList<>();
    public static List<RegistryObject<?>> SWORD_LIST = new ArrayList<>();
    public static List<RegistryObject<?>> AXE_LIST = new ArrayList<>();
    public static List<RegistryObject<?>> REFINED_SWORD_LIST = new ArrayList<>();
    public static List<RegistryObject<?>> DEBUGS = new ArrayList<>();

    public static RegistryObject<CreativeModeTab> registerCreativeTabs(String name, Supplier<CreativeModeTab> supplier) {
        return RegistryEntries.CREATIVE_MODE_TABS.register(name, supplier);
    }


    public static void register(RegistryObject<?> registryObject, Category... category) {
        for (Category getSelected : category) {
            for (Category register : Category.values()) {
                if (getSelected == register) {
                    register.getRegistry().add(registryObject);
                }
            }
        }
    }
    public static void register(RegistryObject<?> registryObject, ResourceKey<CreativeModeTab> category) {
        if (category == CreativeModeTabs.BUILDING_BLOCKS) {
            VANILLA_BUILDING_BLOCKS.add(registryObject);
        }
        if (category == CreativeModeTabs.COLORED_BLOCKS) {
            VANILLA_COLORED_BLOCKS.add(registryObject);
        }
        if (category == CreativeModeTabs.NATURAL_BLOCKS) {
            VANILLA_NATURAL_BLOCKS.add(registryObject);
        }
        if (category == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            VANILLA_FUNCTIONAL_BLOCKS.add(registryObject);
        }
        if (category == CreativeModeTabs.REDSTONE_BLOCKS) {
            VANILLA_REDSTONE_BLOCKS.add(registryObject);
        }
        if (category == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            VANILLA_TOOLS_AND_UTILITIES.add(registryObject);
        }
        if (category == CreativeModeTabs.COMBAT) {
            VANILLA_COMBAT.add(registryObject);
        }
        if (category == CreativeModeTabs.FOOD_AND_DRINKS) {
            VANILLA_FOOD_AND_DRINKS.add(registryObject);
        }
        if (category == CreativeModeTabs.INGREDIENTS) {
            VANILLA_INGREDIENTS.add(registryObject);
        }
        if (category == CreativeModeTabs.SPAWN_EGGS) {
            VANILLA_SPAWN_EGGS.add(registryObject);
        }
        if (category == CreativeModeTabs.OP_BLOCKS) {
            VANILLA_OP_BLOCKS.add(registryObject);
        }

    }
    private static void onVanillaCreativeTabsRegister(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            VANILLA_BUILDING_BLOCKS.forEach(itemLike -> event.accept((ItemLike) itemLike.get()));
        }
        if (event.getTabKey() == CreativeModeTabs.COLORED_BLOCKS) {
            VANILLA_COLORED_BLOCKS.forEach(itemLike -> event.accept((ItemLike) itemLike.get()));
        }
        if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            VANILLA_NATURAL_BLOCKS.forEach(itemLike -> event.accept((ItemLike) itemLike.get()));
        }
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            VANILLA_FUNCTIONAL_BLOCKS.forEach(itemLike -> event.accept((ItemLike) itemLike.get()));
        }
        if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) {
            VANILLA_REDSTONE_BLOCKS.forEach(itemLike -> event.accept((ItemLike) itemLike.get()));
        }
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            VANILLA_TOOLS_AND_UTILITIES.forEach(itemLike -> event.accept((ItemLike) itemLike.get()));
        }
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            VANILLA_COMBAT.forEach(itemLike -> event.accept((ItemLike) itemLike.get()));
        }
        if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            VANILLA_FOOD_AND_DRINKS.forEach(itemLike -> event.accept((ItemLike) itemLike.get()));
        }
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            VANILLA_INGREDIENTS.forEach(itemLike -> event.accept((ItemLike) itemLike.get()));
        }
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            VANILLA_SPAWN_EGGS.forEach(itemLike -> event.accept((ItemLike) itemLike.get()));
        }
        if (event.getTabKey() == CreativeModeTabs.OP_BLOCKS) {
            VANILLA_OP_BLOCKS.forEach(itemLike -> event.accept((ItemLike) itemLike.get()));
        }
    }

    public enum Category implements IExtensibleEnum {
        ACCESSORY(ACCESSORY_LIST),
        MISC(MISC_LIST),
        FOOD(FOOD_LIST),
        INGREDIENTS(INGREDIENTS_LIST),
        BLOCKS(BLOCKS_LIST),
        SPAWN_EGG(SPAWN_EGG_LIST),
        COMBAT(WEAPONS_LIST),
        SWORDS(SWORD_LIST),
        AXE(AXE_LIST),
        TOOLS(TOOLS_LIST),
        REFINED_SWORD(REFINED_SWORD_LIST),
        DEBUGS(RegistryTabs.DEBUGS);
        private final List<RegistryObject<?>> objects;
        private Category(List<RegistryObject<?>> objects) {
            this.objects = objects;
        }

        public List<RegistryObject<?>> getRegistry() {
            return objects;
        }

        public static Category create(String name, List<RegistryObject<?>> registryObjects) {
            throw new IllegalStateException("Enum not extended");
        }
    }
}
