package com.kenhorizon.beyondhorizon.server.init;

import com.kenhorizon.beyondhorizon.server.accessory.AccessoryBuilder;
import com.kenhorizon.beyondhorizon.server.accessory.AccessoryItemGroup;
import com.kenhorizon.beyondhorizon.server.item.BasicItem;
import com.kenhorizon.beyondhorizon.server.item.base.AccessoryItem;
import com.kenhorizon.beyondhorizon.server.item.debug_items.DebugHealthCheckerItems;
import com.kenhorizon.beyondhorizon.server.item.debug_items.DebugWeaponItems;
import com.kenhorizon.beyondhorizon.server.item.materials.MeleeWeaponMaterials;
import com.kenhorizon.beyondhorizon.server.item.base.WeaponBuilder;
import com.kenhorizon.beyondhorizon.server.tags.BHItemTags;
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
    public static final RegistryObject<Item> DEBUG0 = RegistryItems
            .register("debug0", DebugHealthCheckerItems::new)
            .itemName("Debug: Health Checker")
            .tab(RegistryTabs.Category.DEBUGS)
            .model(ItemModels.GENERATED)
            .register()
            .build();
    public static final RegistryObject<Item> DEBUG1 = RegistryItems
            .register("debug1", DebugWeaponItems::new)
            .itemName("Debug: One Tap One Kill")
            .tab(RegistryTabs.Category.DEBUGS)
            .model(ItemModels.GENERATED)
            .register()
            .build();
    //
    public static final RegistryObject<Item> BROKEN_HERO_SWORD = RegistryItems
            .register("broken_hero_sword", item -> new AccessoryItem(new Item.Properties(), AccessoryBuilder.BROKEN_HERO_SWORD))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register()
            .build();

    public static final RegistryObject<Item> BOOTS = RegistryItems
            .register("boots", item -> new AccessoryItem(AccessoryItemGroup.BOOTS, new Item.Properties(), AccessoryBuilder.BOOTS_0))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register()
            .build();

    public static final RegistryObject<Item> GOLDEN_BOOTS = RegistryItems
            .register("golden_boots", item -> new AccessoryItem(AccessoryItemGroup.BOOTS, new Item.Properties(), AccessoryBuilder.BOOTS_0))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register()
            .build();

    public static final RegistryObject<Item> BERSERKER_BOOTS = RegistryItems
            .register("berserker_boots", item -> new AccessoryItem(AccessoryItemGroup.BOOTS, new Item.Properties(), AccessoryBuilder.BERSERKER_BOOTS))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register()
            .build();

    public static final RegistryObject<Item> IRON_PLATED_BOOTS = RegistryItems
            .register("iron_plated_boots", item -> new AccessoryItem(AccessoryItemGroup.BOOTS, new Item.Properties(), AccessoryBuilder.IRON_PLATED_BOOTS))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register()
            .build();

    public static final RegistryObject<Item> MINER_BOOTS = RegistryItems
            .register("miner_boots", item -> new AccessoryItem(AccessoryItemGroup.BOOTS, new Item.Properties(), AccessoryBuilder.MINING_BOOTS))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register()
            .build();

    public static final RegistryObject<Item> ANCIENT_PICKAXE = RegistryItems
            .register("ancient_axe", item -> new AccessoryItem(new Item.Properties(), AccessoryBuilder.ANCIENT_PICKAXE))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register()
            .build();
    public static final RegistryObject<Item> ANCIENT_CHISEL = RegistryItems
            .register("ancient_chisel", item -> new AccessoryItem(new Item.Properties(), AccessoryBuilder.ANCIENT_CHISEL))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register()
            .build();

    public static final RegistryObject<Item> LUCKY_HORSE_SHOE = RegistryItems
            .register("lucky_horse_shoe", item -> new AccessoryItem(new Item.Properties(), AccessoryBuilder.NEGATE_FALL_DAMAGE))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register()
            .build();

    public static final RegistryObject<Item> HEART_OF_THE_TREE = RegistryItems
            .register("heart_of_the_tree", item -> new AccessoryItem(new Item.Properties(), AccessoryBuilder.OVERGROWTH))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register()
            .build();

    //
    public static final RegistryObject<Item> SILVER_SWORD = RegistryItems
            .register("silver_sword", item -> WeaponBuilder.SWORD.create(MeleeWeaponMaterials.SILVER, new Item.Properties()))
            .tab(RegistryTabs.Category.COMBAT)
            .model(ItemModels.HANDHELD)
            .register()
            .build();
    public static final RegistryObject<Item> HELLSTONE_SWORD = RegistryItems
            .register("hellstone_sword", item -> WeaponBuilder.SWORD.create(MeleeWeaponMaterials.HELLSTONE, new Item.Properties()))
            .tab(RegistryTabs.Category.COMBAT)
            .model(ItemModels.HANDHELD)
            .register()
            .build();
    public static final RegistryObject<Item> COBALT_SWORD = RegistryItems
            .register("cobalt_sword", item -> WeaponBuilder.SWORD.create(MeleeWeaponMaterials.COBALT, new Item.Properties()))
            .tab(RegistryTabs.Category.COMBAT)
            .model(ItemModels.HANDHELD)
            .register()
            .build();
    public static final RegistryObject<Item> ADAMANTITE_SWORD = RegistryItems
            .register("adamantite_sword", item -> WeaponBuilder.SWORD.create(MeleeWeaponMaterials.ADAMANTITE, new Item.Properties()))
            .tab(RegistryTabs.Category.COMBAT)
            .model(ItemModels.HANDHELD)
            .register()
            .build();
    public static final RegistryObject<Item> TITANIUM_SWORD = RegistryItems
            .register("titanium_sword", item -> WeaponBuilder.SWORD.create(MeleeWeaponMaterials.TITANIUM, new Item.Properties()))
            .tab(RegistryTabs.Category.COMBAT)
            .model(ItemModels.HANDHELD)
            .register()
            .build();

    public static final RegistryObject<Item> COBALT_AXE = RegistryItems
            .register("cobalt_axe", item -> WeaponBuilder.AXE.create(MeleeWeaponMaterials.COBALT, new Item.Properties()))
            .tab(RegistryTabs.Category.COMBAT)
            .model(ItemModels.HANDHELD)
            .register()
            .build();
    public static final RegistryObject<Item> HELLSTONE_AXE = RegistryItems
            .register("hellstone_axe", item -> WeaponBuilder.AXE.create(MeleeWeaponMaterials.HELLSTONE, new Item.Properties()))
            .tab(RegistryTabs.Category.COMBAT)
            .model(ItemModels.HANDHELD)
            .register()
            .build();
    public static final RegistryObject<Item> ADAMANTITE_AXE = RegistryItems
            .register("adamantite_axe", item -> WeaponBuilder.AXE.create(MeleeWeaponMaterials.ADAMANTITE, new Item.Properties()))
            .tab(RegistryTabs.Category.COMBAT)
            .model(ItemModels.HANDHELD)
            .register()
            .build();
    public static final RegistryObject<Item> TITANIUM_AXE = RegistryItems
            .register("titanium_axe", item -> WeaponBuilder.AXE.create(MeleeWeaponMaterials.TITANIUM, new Item.Properties()))
            .tab(RegistryTabs.Category.COMBAT)
            .model(ItemModels.HANDHELD)
            .register()
            .build();

    public static final RegistryObject<Item> COBALT_PICKAXE = RegistryItems
            .register("cobalt_pickaxe", item -> WeaponBuilder.PICKAXE.create(MeleeWeaponMaterials.COBALT, new Item.Properties()))
            .tab(RegistryTabs.Category.COMBAT)
            .model(ItemModels.HANDHELD)
            .register()
            .build();
    public static final RegistryObject<Item> HELLSTONE_PICKAXE = RegistryItems
            .register("hellstone_pickaxe", item -> WeaponBuilder.PICKAXE.create(MeleeWeaponMaterials.HELLSTONE, new Item.Properties()))
            .tab(RegistryTabs.Category.COMBAT)
            .model(ItemModels.HANDHELD)
            .register()
            .build();
    public static final RegistryObject<Item> ADAMANTITE_PICKAXE = RegistryItems
            .register("adamantite_pickaxe", item -> WeaponBuilder.PICKAXE.create(MeleeWeaponMaterials.ADAMANTITE, new Item.Properties()))
            .tab(RegistryTabs.Category.COMBAT)
            .model(ItemModels.HANDHELD)
            .register()
            .build();
    public static final RegistryObject<Item> TITANIUM_PICKAXE = RegistryItems
            .register("titanium_pickaxe", item -> WeaponBuilder.PICKAXE.create(MeleeWeaponMaterials.TITANIUM, new Item.Properties()))
            .tab(RegistryTabs.Category.COMBAT)
            .model(ItemModels.HANDHELD)
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
