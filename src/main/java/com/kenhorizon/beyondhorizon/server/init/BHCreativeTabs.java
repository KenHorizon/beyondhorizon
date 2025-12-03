package com.kenhorizon.beyondhorizon.server.init;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.libs.registry.RegistryEntries;
import com.kenhorizon.libs.registry.RegistryTabs;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

public class BHCreativeTabs {
    public static final RegistryObject<CreativeModeTab> BH_INGREDIENTS = RegistryTabs.registerCreativeTabs("beyond_horizon_tab_ingredients",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(BHItems.RAW_COBALT.get()))
                    .title(Component.translatable(BeyondHorizon.ID + ".creative_tabs.ingredients"))
                    .displayItems((parameters, output) -> {
                        RegistryTabs.MISC_LIST.forEach(itemLike -> output.accept((ItemLike) itemLike.get()));
                        RegistryTabs.FOOD_LIST.forEach(itemLike -> output.accept((ItemLike) itemLike.get()));
                        RegistryTabs.INGREDIENTS_LIST.forEach(itemLike -> output.accept((ItemLike) itemLike.get()));
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> BH_TOOLS = RegistryTabs.registerCreativeTabs("beyond_horizon_tab_tools",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(BHItems.TITANIUM_PICKAXE.get()))
                    .withTabsBefore(BHCreativeTabs.BH_INGREDIENTS.getKey())
                    .title(Component.translatable(BeyondHorizon.ID + ".creative_tabs.tools"))
                    .displayItems((parameters, output) -> {
                        RegistryTabs.TOOLS_LIST.forEach(itemLike -> output.accept((ItemLike) itemLike.get()));
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> BH_ACCESSORY = RegistryTabs.registerCreativeTabs("beyond_horizon_tab_accessory",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(BHItems.POWER_GLOVES.get()))
                    .withTabsBefore(BHCreativeTabs.BH_TOOLS.getKey())
                    .title(Component.translatable(BeyondHorizon.ID + ".creative_tabs.accessory"))
                    .displayItems((parameters, output) -> {
                        RegistryTabs.ACCESSORY_LIST.forEach(itemLike -> output.accept((ItemLike) itemLike.get()));
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> BH_WEAPONS = RegistryTabs.registerCreativeTabs("beyond_horizon_tab_weapons",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(BHItems.BLADE_OF_THE_ENDERLORD.get()))
                    .withTabsBefore(BHCreativeTabs.BH_TOOLS.getKey())
                    .title(Component.translatable(BeyondHorizon.ID + ".creative_tabs.weapons"))
                    .displayItems((parameters, output) -> {
                        RegistryTabs.REFINED_SWORD_LIST.forEach(itemLike -> output.accept((ItemLike) itemLike.get()));
                        RegistryTabs.SWORD_LIST.forEach(itemLike -> output.accept((ItemLike) itemLike.get()));
                        RegistryTabs.AXE_LIST.forEach(itemLike -> output.accept((ItemLike) itemLike.get()));
                        RegistryTabs.WEAPONS_LIST.forEach(itemLike -> output.accept((ItemLike) itemLike.get()));
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> BH_BLOCKS = RegistryTabs.registerCreativeTabs("beyond_horizon_tabs_blocks",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(BHItems.COBALT_INGOT.get()))
                    .withTabsBefore(BHCreativeTabs.BH_WEAPONS.getKey())
                    .title(Component.translatable(BeyondHorizon.ID + ".creative_tabs.blocks"))
                    .displayItems((parameters, output) -> {
                        RegistryTabs.BLOCKS_LIST.forEach(itemLike -> output.accept((ItemLike) itemLike.get()));
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> BH_SPAWN_EGG = RegistryTabs.registerCreativeTabs("beyond_horizon_tabs_spawn_egg",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(BHItems.ADAMANTITE_INGOT.get()))
                    .withTabsBefore(BHCreativeTabs.BH_WEAPONS.getKey())
                    .title(Component.translatable(BeyondHorizon.ID + ".creative_tabs.spawn_eggs"))
                    .displayItems((parameters, output) -> {
                        RegistryTabs.SPAWN_EGG_LIST.forEach(itemLike -> output.accept((ItemLike) itemLike.get()));
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> BH_DEBUG_ITEMS = RegistryTabs.registerCreativeTabs("beyond_horizon_tabs_debug_items",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(BHItems.ADAMANTITE_INGOT.get()))
                    .withTabsBefore(BHCreativeTabs.BH_BLOCKS.getKey())
                    .title(Component.translatable(BeyondHorizon.ID + ".creative_tabs.debug_items"))
                    .displayItems((parameters, output) -> {
                        RegistryTabs.DEBUGS.forEach(itemLike -> output.accept((ItemLike) itemLike.get()));
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        RegistryEntries.CREATIVE_MODE_TABS.register(eventBus);
    }
}
