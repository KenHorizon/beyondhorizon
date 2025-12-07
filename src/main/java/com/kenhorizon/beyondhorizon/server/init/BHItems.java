package com.kenhorizon.beyondhorizon.server.init;

import com.kenhorizon.beyondhorizon.server.accessory.AccessoryBuilder;
import com.kenhorizon.beyondhorizon.server.accessory.AccessoryItemGroup;
import com.kenhorizon.beyondhorizon.server.item.BasicItem;
import com.kenhorizon.beyondhorizon.server.item.base.AccessoryItem;
import com.kenhorizon.beyondhorizon.server.item.debug_items.DebugHealItems;
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
    //
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
    public static final RegistryObject<Item> DEBUG2 = RegistryItems
            .register("debug2", DebugHealItems::new)
            .itemName("Debug: Max Health Heal")
            .tab(RegistryTabs.Category.DEBUGS)
            .model(ItemModels.GENERATED)
            .register()
            .build();
    public static final RegistryObject<Item> DEBUG3 = RegistryItems
            .register("debug3", DebugHealItems::new)
            .itemName("Debug: Role Changer")
            .tab(RegistryTabs.Category.DEBUGS)
            .model(ItemModels.GENERATED)
            .register()
            .build();
    //
    public static final RegistryObject<Item> HANDLE = RegistryItems
            .register("handle", BasicItem::new)
            .tab(RegistryTabs.Category.INGREDIENTS)
            .model(ItemModels.GENERATED)
            .register()
            .build();
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
    public static final RegistryObject<Item> RUBY = RegistryItems
            .register("ruby", BasicItem::new)
            .tab(RegistryTabs.Category.INGREDIENTS)
            .model(ItemModels.GENERATED)
            .register()
            .build();
    public static final RegistryObject<Item> AMPLIFLYING_TOME = RegistryItems
            .register("ampliflying_tome", BasicItem::new)
            .tab(RegistryTabs.Category.INGREDIENTS)
            .model(ItemModels.GENERATED)
            .register()
            .build();
    //
    public static final RegistryObject<Item> TOUGH_CLOTH = RegistryItems
            .register("tough_cloth", item -> new AccessoryItem(item, AccessoryBuilder.ARMOR_0))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register()
            .build();
    public static final RegistryObject<Item> CHAIN_VEST = RegistryItems
            .register("chain_vest", item -> new AccessoryItem(item, AccessoryBuilder.ARMOR_1))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register()
            .build();
    public static final RegistryObject<Item> NULL_MAGIC_RUNE = RegistryItems
            .register("null_magic_rune", item -> new AccessoryItem(item, AccessoryBuilder.MAGIC_RESIST_0))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register()
            .build();
    public static final RegistryObject<Item> FIREFLY_FAYE = RegistryItems
            .register("firefly_faye", item -> new AccessoryItem(item, AccessoryBuilder.REGEN_0))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register()
            .build();
    public static final RegistryObject<Item> SAPPHIRE_CRYSTAL = RegistryItems
            .register("sapphire_crystal", item -> new AccessoryItem(item, AccessoryBuilder.MAX_MANA_0))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register()
            .build();
    public static final RegistryObject<Item> RUMINATIVE_BEADS = RegistryItems
            .register("ruminative_beads", item -> new AccessoryItem(item, AccessoryBuilder.REGEN_0))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register()
            .build();
    public static final RegistryObject<Item> SPECTRAL_CLOAK = RegistryItems
            .register("spectral_cloak", item -> new AccessoryItem(item, AccessoryBuilder.SPECTRAL_CLOAK))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register()
            .build();
    public static final RegistryObject<Item> UNSTABLE_RUNIC_TOME = RegistryItems
            .register("unstable_runic_tome", item -> new AccessoryItem(item, AccessoryBuilder.UNSTABLE_RUNIC_TOME))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register()
            .build();
    public static final RegistryObject<Item> BROKEN_HERO_SWORD = RegistryItems
            .register("broken_hero_sword", item -> new AccessoryItem(item, AccessoryBuilder.BROKEN_HERO_SWORD))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register()
            .build();
    public static final RegistryObject<Item> DARK_ESSESNCE_CRYSTAL = RegistryItems
            .register("dark_essence_crystal", item -> new AccessoryItem(item, AccessoryBuilder.MAGICAL_OPS))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register()
            .build();

    public static final RegistryObject<Item> BOOTS = RegistryItems
            .register("boots", item -> new AccessoryItem(AccessoryItemGroup.BOOTS, item, AccessoryBuilder.BOOTS_0))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register()
            .build();

    public static final RegistryObject<Item> GOLDEN_BOOTS = RegistryItems
            .register("golden_boots", item -> new AccessoryItem(AccessoryItemGroup.BOOTS, item, AccessoryBuilder.BOOTS_0))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register()
            .build();

    public static final RegistryObject<Item> BERSERKER_BOOTS = RegistryItems
            .register("berserker_boots", item -> new AccessoryItem(AccessoryItemGroup.BOOTS, item, AccessoryBuilder.BERSERKER_BOOTS))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register()
            .build();

    public static final RegistryObject<Item> IRON_PLATED_BOOTS = RegistryItems
            .register("iron_plated_boots", item -> new AccessoryItem(AccessoryItemGroup.BOOTS, item, AccessoryBuilder.IRON_PLATED_BOOTS))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register()
            .build();

    public static final RegistryObject<Item> MINER_BOOTS = RegistryItems
            .register("miner_boots", item -> new AccessoryItem(AccessoryItemGroup.BOOTS, item, AccessoryBuilder.MINING_BOOTS))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register()
            .build();

    public static final RegistryObject<Item> ANCIENT_PICKAXE = RegistryItems
            .register("ancient_pickaxe", item -> new AccessoryItem(item, AccessoryBuilder.ANCIENT_PICKAXE))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register()
            .build();
    public static final RegistryObject<Item> ANCIENT_CHISEL = RegistryItems
            .register("ancient_chisel", item -> new AccessoryItem(item, AccessoryBuilder.ANCIENT_CHISEL))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register()
            .build();

    public static final RegistryObject<Item> LUCKY_HORSE_SHOE = RegistryItems
            .register("lucky_horse_shoe", item -> new AccessoryItem(item, AccessoryBuilder.NEGATE_FALL_DAMAGE))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register()
            .build();

    public static final RegistryObject<Item> POWER_GLOVES = RegistryItems
            .register("power_gloves", item -> new AccessoryItem(item, AccessoryBuilder.POWER_GLOVES))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register()
            .build();

    public static final RegistryObject<Item> SWIFT_DAGGER = RegistryItems
            .register("swift_dagger", item -> new AccessoryItem(item, AccessoryBuilder.SWIFT_DAGGER))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register()
            .build();

    public static final RegistryObject<Item> AETHER_WISP = RegistryItems
            .register("aether_wisp", item -> new AccessoryItem(item, AccessoryBuilder.AETHER_WISP))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register()
            .build();

    public static final RegistryObject<Item> HEART_OF_THE_TREE = RegistryItems
            .register("heart_of_the_tree", item -> new AccessoryItem(item, AccessoryBuilder.OVERGROWTH))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register()
            .build();

    public static final RegistryObject<Item> RECTRIX = RegistryItems
            .register("rectrix", item -> new AccessoryItem(item, AccessoryBuilder.RECTRIX))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register()
            .build();

    public static final RegistryObject<Item> FORTUNE_SHIKIGAMI = RegistryItems
            .register("fortune_shikigami", item -> new AccessoryItem(item, AccessoryBuilder.FORTUNE_SHIKIGAMI))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register()
            .build();

    public static final RegistryObject<Item> LEATHER_AGILITY = RegistryItems
            .register("leather_agility", item -> new AccessoryItem(item, AccessoryBuilder.BASIC_CRIT_0))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register()
            .build();

    public static final RegistryObject<Item> AGILE_DAGGER = RegistryItems
            .register("agile_dagger", item -> new AccessoryItem(item, AccessoryBuilder.AGILE_DAGGER))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register()
            .build();

    public static final RegistryObject<Item> MASK_OF_AGONY = RegistryItems
            .register("mask_of_agony", item -> new AccessoryItem(item, AccessoryBuilder.DESPAIR_AND_DEFY))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register()
            .build();

    public static final RegistryObject<Item> VITALITY_STONE = RegistryItems
            .register("vitality_stone", item -> new AccessoryItem(item, AccessoryBuilder.MAX_HEALTH_0))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register()
            .build();

    public static final RegistryObject<Item> CINDER_STONE = RegistryItems
            .register("cinder_stone", item -> new AccessoryItem(item, AccessoryBuilder.CINDER_STONE))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register()
            .build();

    public static final RegistryObject<Item> CRYSTALLIZED_PLATE = RegistryItems
            .register("crystallized_plate", item -> new AccessoryItem(item, AccessoryBuilder.CRYSTALLIZED_PLATE))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register()
            .build();

    //
    public static final RegistryObject<Item> SILVER_SWORD = RegistryItems
            .register("silver_sword", item -> WeaponBuilder.SWORD.create(MeleeWeaponMaterials.SILVER, item))
            .tab(RegistryTabs.Category.COMBAT)
            .model(ItemModels.HANDHELD)
            .register()
            .build();
    public static final RegistryObject<Item> HELLSTONE_SWORD = RegistryItems
            .register("hellstone_sword", item -> WeaponBuilder.SWORD.create(MeleeWeaponMaterials.HELLSTONE, item))
            .tab(RegistryTabs.Category.COMBAT)
            .model(ItemModels.HANDHELD)
            .register()
            .build();
    public static final RegistryObject<Item> COBALT_SWORD = RegistryItems
            .register("cobalt_sword", item -> WeaponBuilder.SWORD.create(MeleeWeaponMaterials.COBALT, item))
            .tab(RegistryTabs.Category.COMBAT)
            .model(ItemModels.HANDHELD)
            .register()
            .build();
    public static final RegistryObject<Item> ADAMANTITE_SWORD = RegistryItems
            .register("adamantite_sword", item -> WeaponBuilder.SWORD.create(MeleeWeaponMaterials.ADAMANTITE, item))
            .tab(RegistryTabs.Category.COMBAT)
            .model(ItemModels.HANDHELD)
            .register()
            .build();
    public static final RegistryObject<Item> TITANIUM_SWORD = RegistryItems
            .register("titanium_sword", item -> WeaponBuilder.SWORD.create(MeleeWeaponMaterials.TITANIUM, item))
            .tab(RegistryTabs.Category.COMBAT)
            .model(ItemModels.HANDHELD)
            .register()
            .build();

    public static final RegistryObject<Item> SILVER_AXE = RegistryItems
            .register("silver_axe", item -> WeaponBuilder.AXE.create(MeleeWeaponMaterials.SILVER, item))
            .tab(RegistryTabs.Category.COMBAT, RegistryTabs.Category.AXE, RegistryTabs.Category.TOOLS)
            .model(ItemModels.HANDHELD)
            .register()
            .build();
    public static final RegistryObject<Item> COBALT_AXE = RegistryItems
            .register("cobalt_axe", item -> WeaponBuilder.AXE.create(MeleeWeaponMaterials.COBALT, item))
            .tab(RegistryTabs.Category.COMBAT, RegistryTabs.Category.AXE, RegistryTabs.Category.TOOLS)
            .model(ItemModels.HANDHELD)
            .register()
            .build();
    public static final RegistryObject<Item> HELLSTONE_AXE = RegistryItems
            .register("hellstone_axe", item -> WeaponBuilder.AXE.create(MeleeWeaponMaterials.HELLSTONE, item))
            .tab(RegistryTabs.Category.COMBAT, RegistryTabs.Category.AXE, RegistryTabs.Category.TOOLS)
            .model(ItemModels.HANDHELD)
            .register()
            .build();
    public static final RegistryObject<Item> ADAMANTITE_AXE = RegistryItems
            .register("adamantite_axe", item -> WeaponBuilder.AXE.create(MeleeWeaponMaterials.ADAMANTITE, item))
            .tab(RegistryTabs.Category.COMBAT, RegistryTabs.Category.AXE, RegistryTabs.Category.TOOLS)
            .model(ItemModels.HANDHELD)
            .register()
            .build();
    public static final RegistryObject<Item> TITANIUM_AXE = RegistryItems
            .register("titanium_axe", item -> WeaponBuilder.AXE.create(MeleeWeaponMaterials.TITANIUM, item))
            .tab(RegistryTabs.Category.COMBAT, RegistryTabs.Category.AXE, RegistryTabs.Category.TOOLS)
            .model(ItemModels.HANDHELD)
            .register()
            .build();

    public static final RegistryObject<Item> SILVER_PICKAXE = RegistryItems
            .register("silver_pickaxe", item -> WeaponBuilder.PICKAXE.create(MeleeWeaponMaterials.SILVER, item))
            .tab(RegistryTabs.Category.TOOLS)
            .model(ItemModels.HANDHELD)
            .register()
            .build();
    public static final RegistryObject<Item> COBALT_PICKAXE = RegistryItems
            .register("cobalt_pickaxe", item -> WeaponBuilder.PICKAXE.create(MeleeWeaponMaterials.COBALT, item))
            .tab(RegistryTabs.Category.TOOLS)
            .model(ItemModels.HANDHELD)
            .register()
            .build();
    public static final RegistryObject<Item> HELLSTONE_PICKAXE = RegistryItems
            .register("hellstone_pickaxe", item -> WeaponBuilder.PICKAXE.create(MeleeWeaponMaterials.HELLSTONE, item))
            .tab(RegistryTabs.Category.TOOLS)
            .model(ItemModels.HANDHELD)
            .register()
            .build();
    public static final RegistryObject<Item> ADAMANTITE_PICKAXE = RegistryItems
            .register("adamantite_pickaxe", item -> WeaponBuilder.PICKAXE.create(MeleeWeaponMaterials.ADAMANTITE, item))
            .tab(RegistryTabs.Category.TOOLS)
            .model(ItemModels.HANDHELD)
            .register()
            .build();
    public static final RegistryObject<Item> TITANIUM_PICKAXE = RegistryItems
            .register("titanium_pickaxe", item -> WeaponBuilder.PICKAXE.create(MeleeWeaponMaterials.TITANIUM, item))
            .tab(RegistryTabs.Category.TOOLS)
            .model(ItemModels.HANDHELD)
            .register()
            .build();
    public static final RegistryObject<Item> SILVER_SHOVEL = RegistryItems
            .register("silver_shovel", item -> WeaponBuilder.SHOVEL.create(MeleeWeaponMaterials.SILVER, item))
            .tab(RegistryTabs.Category.TOOLS)
            .model(ItemModels.HANDHELD)
            .register()
            .build();
    public static final RegistryObject<Item> COBALT_SHOVEL = RegistryItems
            .register("cobalt_shovel", item -> WeaponBuilder.SHOVEL.create(MeleeWeaponMaterials.COBALT, item))
            .tab(RegistryTabs.Category.TOOLS)
            .model(ItemModels.HANDHELD)
            .register()
            .build();
    public static final RegistryObject<Item> HELLSTONE_SHOVEL = RegistryItems
            .register("hellstone_shovel", item -> WeaponBuilder.SHOVEL.create(MeleeWeaponMaterials.HELLSTONE, item))
            .tab(RegistryTabs.Category.TOOLS)
            .model(ItemModels.HANDHELD)
            .register()
            .build();
    public static final RegistryObject<Item> ADAMANTITE_SHOVEL = RegistryItems
            .register("adamantite_shovel", item -> WeaponBuilder.SHOVEL.create(MeleeWeaponMaterials.ADAMANTITE, item))
            .tab(RegistryTabs.Category.TOOLS)
            .model(ItemModels.HANDHELD)
            .register()
            .build();
    public static final RegistryObject<Item> TITANIUM_SHOVEL = RegistryItems
            .register("titanium_shovel", item -> WeaponBuilder.SHOVEL.create(MeleeWeaponMaterials.TITANIUM, item))
            .tab(RegistryTabs.Category.TOOLS)
            .model(ItemModels.HANDHELD)
            .register()
            .build();
    public static final RegistryObject<Item> COBALT_HOE = RegistryItems
            .register("cobalt_hoe", item -> WeaponBuilder.HOE.create(MeleeWeaponMaterials.COBALT, item))
            .tab(RegistryTabs.Category.TOOLS)
            .model(ItemModels.HANDHELD)
            .register()
            .build();
    public static final RegistryObject<Item> HELLSTONE_HOE = RegistryItems
            .register("hellstone_hoe", item -> WeaponBuilder.HOE.create(MeleeWeaponMaterials.HELLSTONE, item))
            .tab(RegistryTabs.Category.TOOLS)
            .model(ItemModels.HANDHELD)
            .register()
            .build();
    public static final RegistryObject<Item> ADAMANTITE_HOE = RegistryItems
            .register("adamantite_hoe", item -> WeaponBuilder.HOE.create(MeleeWeaponMaterials.ADAMANTITE, item))
            .tab(RegistryTabs.Category.TOOLS)
            .model(ItemModels.HANDHELD)
            .register()
            .build();
    public static final RegistryObject<Item> TITANIUM_HOE = RegistryItems
            .register("titanium_hoe", item -> WeaponBuilder.HOE.create(MeleeWeaponMaterials.TITANIUM, item))
            .tab(RegistryTabs.Category.TOOLS)
            .model(ItemModels.HANDHELD)
            .register()
            .build();

    public static final RegistryObject<Item> BLADE_OF_THE_ENDERLORD = RegistryItems
            .register("blade_of_the_enderlord", item -> WeaponBuilder.BLADE_OF_THE_ENDERLORD.create(MeleeWeaponMaterials.TIER_ONE, item))
            .properties(p -> p.rarity(BHRarity.MYTHICAL))
            .tab(RegistryTabs.Category.COMBAT)
            .model(ItemModels.BIG_HANDHELD)
            .register()
            .build();
    public static final RegistryObject<Item> GIANT_SLAYER_SWORD = RegistryItems
            .register("giant_slayer_sword", item -> WeaponBuilder.GIANT_SLAYER_SWORD.create(MeleeWeaponMaterials.TIER_ONE, item))
            .properties(p -> p.rarity(BHRarity.MYTHICAL))
            .tab(RegistryTabs.Category.COMBAT)
            .model(ItemModels.BIG_HANDHELD)
            .register()
            .build();
    public static final RegistryObject<Item> ZENITH = RegistryItems
            .register("zenith", item -> WeaponBuilder.ZENITH.create(MeleeWeaponMaterials.TIER_ONE, item))
            .properties(p -> p.rarity(BHRarity.MYTHICAL))
            .tab(RegistryTabs.Category.COMBAT)
            .model(ItemModels.BIG_HANDHELD)
            .register()
            .build();

    public static void register(IEventBus eventBus) {
        RegistryEntries.ITEMS.register(eventBus);
    }
}
