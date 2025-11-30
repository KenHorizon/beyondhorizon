package com.kenhorizon.beyondhorizon.server.init;

import com.kenhorizon.beyondhorizon.server.item.BasicItem;
import com.kenhorizon.beyondhorizon.server.item.materials.MeleeWeaponMaterials;
import com.kenhorizon.beyondhorizon.server.item.base.WeaponBuilder;
import com.kenhorizon.libs.client.model.item.ItemModels;
import com.kenhorizon.libs.registry.RegistryEntries;
import com.kenhorizon.libs.registry.RegistryItems;
import com.kenhorizon.libs.registry.RegistryTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

public class BHItems {
    public static final RegistryObject<Item> RAW_ADAMANTITE = RegistryItems
            .register("raw_adamantite", BasicItem::new)
            .tab(RegistryTabs.Category.INGREDIENTS)
            .model(ItemModels.GENERATED)
            .register()
            .build();

    public static final RegistryObject<Item> RAW_COBALT = RegistryItems
            .register("raw_cobalt", BasicItem::new)
            .tab(RegistryTabs.Category.INGREDIENTS)
            .model(ItemModels.GENERATED)
            .register()
            .build();

    public static final RegistryObject<Item> RAW_HELLSTONE = RegistryItems
            .register("raw_hellstone", BasicItem::new)
            .tab(RegistryTabs.Category.INGREDIENTS)
            .model(ItemModels.GENERATED)
            .register()
            .build();

    public static final RegistryObject<Item> RAW_EMBED_HELLSTONE = RegistryItems
            .register("raw_embed_hellstone", BasicItem::new)
            .tab(RegistryTabs.Category.INGREDIENTS)
            .model(ItemModels.GENERATED)
            .register()
            .build();

    public static final RegistryObject<Item> RAW_PALLADIUM = RegistryItems
            .register("raw_palladium", BasicItem::new)
            .tab(RegistryTabs.Category.INGREDIENTS)
            .model(ItemModels.GENERATED)
            .register()
            .build();

    public static final RegistryObject<Item> RAW_SILVER = RegistryItems
            .register("raw_silver", BasicItem::new)
            .tab(RegistryTabs.Category.INGREDIENTS)
            .model(ItemModels.GENERATED)
            .register()
            .build();

    public static final RegistryObject<Item> RAW_TITANIUM = RegistryItems
            .register("raw_titanium", BasicItem::new)
            .tab(RegistryTabs.Category.INGREDIENTS)
            .model(ItemModels.GENERATED)
            .register()
            .build();
    public static final RegistryObject<Item> SILVER_INGOT = RegistryItems
            .register("silver_ingot", BasicItem::new)
            .tab(RegistryTabs.Category.INGREDIENTS)
            .model(ItemModels.GENERATED)
            .register()
            .build();

    public static final RegistryObject<Item> ADAMANTITE_INGOT = RegistryItems
            .register("adamantite_ingot", BasicItem::new)
            .tab(RegistryTabs.Category.INGREDIENTS)
            .model(ItemModels.GENERATED)
            .register()
            .build();
    public static final RegistryObject<Item> COBALT_INGOT = RegistryItems
            .register("cobalt_ingot", BasicItem::new)
            .tab(RegistryTabs.Category.INGREDIENTS)
            .model(ItemModels.GENERATED)
            .register()
            .build();

    public static final RegistryObject<Item> HELLSTONE_INGOT = RegistryItems
            .register("hellstone_ingot", BasicItem::new)
            .tab(RegistryTabs.Category.INGREDIENTS)
            .model(ItemModels.GENERATED)
            .register()
            .build();

    public static final RegistryObject<Item> PALLADIUM_INGOT = RegistryItems
            .register("palladium_ingot", BasicItem::new)
            .tab(RegistryTabs.Category.INGREDIENTS)
            .model(ItemModels.GENERATED)
            .register()
            .build();
    public static final RegistryObject<Item> TITANIUM_INGOT = RegistryItems
            .register("titanium_ingot", BasicItem::new)
            .tab(RegistryTabs.Category.INGREDIENTS)
            .model(ItemModels.GENERATED)
            .register()
            .build();
    public static final RegistryObject<Item> IRON_PLATE = RegistryItems
            .register("iron_plate", BasicItem::new)
            .tab(RegistryTabs.Category.INGREDIENTS)
            .model(ItemModels.GENERATED)
            .register()
            .build();
    public static final RegistryObject<Item> CHAIN_PLATE = RegistryItems
            .register("chain_plate", BasicItem::new)
            .tab(RegistryTabs.Category.INGREDIENTS)
            .model(ItemModels.GENERATED)
            .register()
            .build();
    public static final RegistryObject<Item> BLADE_OF_THE_ENDERLORD = RegistryItems
            .register("blade_of_the_enderlord", item -> WeaponBuilder.BLADE_OF_THE_ENDERLORD.create(MeleeWeaponMaterials.BLADE_OF_THE_ENDERLORD, new Item.Properties().rarity(BHRarity.MYTHICAL)))
            .tab(RegistryTabs.Category.COMBAT)
            .model(ItemModels.BIG_HANDHELD)
            .register()
            .build();
    public static final RegistryObject<Item> ZENITH = RegistryItems
            .register("zenith", item -> WeaponBuilder.ZENITH.create(MeleeWeaponMaterials.ZENITH, new Item.Properties().rarity(BHRarity.MYTHICAL)))
            .tab(RegistryTabs.Category.COMBAT)
            .model(ItemModels.BIG_HANDHELD)
            .register()
            .build();

    public static void register(IEventBus eventBus) {
        RegistryEntries.ITEMS.register(eventBus);
    }
}
