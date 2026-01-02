package com.kenhorizon.beyondhorizon.server.init;

import com.kenhorizon.beyondhorizon.client.level.util.ColorUtil;
import com.kenhorizon.beyondhorizon.server.api.accessory.AccessoryBuilder;
import com.kenhorizon.beyondhorizon.server.api.accessory.AccessoryItemGroup;
import com.kenhorizon.beyondhorizon.server.item.BasicItem;
import com.kenhorizon.beyondhorizon.server.item.base.AccessoryItem;
import com.kenhorizon.beyondhorizon.server.item.base.BasicAccessoryItem;
import com.kenhorizon.beyondhorizon.server.item.debug_items.*;
import com.kenhorizon.beyondhorizon.server.item.materials.MeleeWeaponMaterials;
import com.kenhorizon.beyondhorizon.server.item.base.WeaponBuilder;
import com.kenhorizon.beyondhorizon.server.tags.BHItemTags;
import com.kenhorizon.libs.client.model.item.ItemModels;
import com.kenhorizon.libs.registry.RegistryEntries;
import com.kenhorizon.libs.registry.RegistryItems;
import com.kenhorizon.libs.registry.RegistryTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

public class BHItems {
    //
    public static final RegistryObject<Item> DEBUG0 = RegistryItems
            .register("debug0", DebugHealthCheckerItems::new)
            .itemName("Debug: Health Checker")
            .tab(RegistryTabs.Category.DEBUGS)
            .model(ItemModels.GENERATED)
            .register();
    public static final RegistryObject<Item> DEBUG1 = RegistryItems
            .register("debug1", DebugWeaponItems::new)
            .itemName("Debug: One Tap One Kill")
            .tab(RegistryTabs.Category.DEBUGS)
            .model(ItemModels.GENERATED)
            .register();
    public static final RegistryObject<Item> DEBUG2 = RegistryItems
            .register("debug2", DebugHealItems::new)
            .itemName("Debug: Max Health Heal")
            .tab(RegistryTabs.Category.DEBUGS)
            .model(ItemModels.GENERATED)
            .register();
    public static final RegistryObject<Item> DEBUG3 = RegistryItems
            .register("debug3", DebugRoleClassResetItems::new)
            .itemName("Debug: Role Class Reset")
            .tab(RegistryTabs.Category.DEBUGS)
            .model(ItemModels.GENERATED)
            .register();
    public static final RegistryObject<Item> DEBUG4 = RegistryItems
            .register("debug4", DebugBlazingInfernoItems::new)
            .itemName("Debug: Spawn Inactive Blazing Inferno")
            .tab(RegistryTabs.Category.DEBUGS)
            .model(ItemModels.GENERATED)
            .register();
    public static final RegistryObject<Item> DEBUG5 = RegistryItems
            .register("debug5", DebugGodModeItems::new)
            .itemName("Debug: GOD MODE")
            .tab(RegistryTabs.Category.DEBUGS)
            .model(ItemModels.GENERATED)
            .register();

    public static final RegistryObject<Item> HANDLE = RegistryItems
            .register("handle", BasicItem::new)
            .tab(RegistryTabs.Category.INGREDIENTS)
            .model(ItemModels.GENERATED)
            .register();
    
    public static final RegistryObject<Item> FLAME_CELL = RegistryItems
            .register("flame_cell", BasicItem::new)
            .tab(RegistryTabs.Category.INGREDIENTS)
            .model(ItemModels.GENERATED)
            .register();
    
    public static final RegistryObject<Item> RAW_ADAMANTITE = RegistryItems
            .register("raw_adamantite", BasicItem::new)
            .tab(RegistryTabs.Category.INGREDIENTS)
            .model(ItemModels.GENERATED)
            .register();

    public static final RegistryObject<Item> RAW_COBALT = RegistryItems
            .register("raw_cobalt", BasicItem::new)
            .tab(RegistryTabs.Category.INGREDIENTS)
            .model(ItemModels.GENERATED)
            .register();

    
    public static final RegistryObject<Item> RAW_HELLSTONE = RegistryItems
            .register("raw_hellstone", BasicItem::new)
            .tab(RegistryTabs.Category.INGREDIENTS)
            .model(ItemModels.GENERATED)
            .register();

    public static final RegistryObject<Item> RAW_EMBED_HELLSTONE = RegistryItems
            .register("raw_embed_hellstone", BasicItem::new)
            .tab(RegistryTabs.Category.INGREDIENTS)
            .model(ItemModels.GENERATED)
            .register();

    public static final RegistryObject<Item> RAW_PALLADIUM = RegistryItems
            .register("raw_palladium", BasicItem::new)
            .tab(RegistryTabs.Category.INGREDIENTS)
            .model(ItemModels.GENERATED)
            .register();

    public static final RegistryObject<Item> RAW_SILVER = RegistryItems
            .register("raw_silver", BasicItem::new)
            .tab(RegistryTabs.Category.INGREDIENTS)
            .model(ItemModels.GENERATED)
            .register();

    public static final RegistryObject<Item> RAW_TITANIUM = RegistryItems
            .register("raw_titanium", BasicItem::new)
            .tab(RegistryTabs.Category.INGREDIENTS)
            .model(ItemModels.GENERATED)
            .register();
    public static final RegistryObject<Item> SILVER_INGOT = RegistryItems
            .register("silver_ingot", BasicItem::new)
            .tab(RegistryTabs.Category.INGREDIENTS)
            .model(ItemModels.GENERATED)
            .register();

    public static final RegistryObject<Item> ADAMANTITE_INGOT = RegistryItems
            .register("adamantite_ingot", BasicItem::new)
            .tab(RegistryTabs.Category.INGREDIENTS)
            .model(ItemModels.GENERATED)
            .register();
    public static final RegistryObject<Item> COBALT_INGOT = RegistryItems
            .register("cobalt_ingot", BasicItem::new)
            .tab(RegistryTabs.Category.INGREDIENTS)
            .model(ItemModels.GENERATED)
            .register();

    public static final RegistryObject<Item> HELLSTONE_INGOT = RegistryItems
            .register("hellstone_ingot", BasicItem::new)
            .tab(RegistryTabs.Category.INGREDIENTS)
            .model(ItemModels.GENERATED)
            .register();

    public static final RegistryObject<Item> PALLADIUM_INGOT = RegistryItems
            .register("palladium_ingot", BasicItem::new)
            .tab(RegistryTabs.Category.INGREDIENTS)
            .model(ItemModels.GENERATED)
            .register();
    public static final RegistryObject<Item> TITANIUM_INGOT = RegistryItems
            .register("titanium_ingot", BasicItem::new)
            .tab(RegistryTabs.Category.INGREDIENTS)
            .model(ItemModels.GENERATED)
            .register();
    public static final RegistryObject<Item> IRON_PLATE = RegistryItems
            .register("iron_plate", BasicItem::new)
            .tab(RegistryTabs.Category.INGREDIENTS)
            .model(ItemModels.GENERATED)
            .register();
    public static final RegistryObject<Item> CHAIN_PLATE = RegistryItems
            .register("chain_plate", BasicItem::new)
            .tab(RegistryTabs.Category.INGREDIENTS)
            .model(ItemModels.GENERATED)
            .register();

    public static final RegistryObject<Item> RUBY = basicItem("ruby", RegistryTabs.Category.INGREDIENTS);
    public static final RegistryObject<Item> AMPLIFLYING_TOME = basicItem("ampliflying_tome", RegistryTabs.Category.INGREDIENTS);

    public static final RegistryObject<Item> WHITE_WOOL_FUR = woolFurItem("white_wool_fur", RegistryTabs.Category.INGREDIENTS);
    public static final RegistryObject<Item> ORANGE_WOOL_FUR = woolFurItem("orange_wool_fur", RegistryTabs.Category.INGREDIENTS);
    public static final RegistryObject<Item> MAGENTA_WOOL_FUR = woolFurItem("magenta_wool_fur", RegistryTabs.Category.INGREDIENTS);
    public static final RegistryObject<Item> LIGHT_BLUE_WOOL_FUR = woolFurItem("light_blue_wool_fur", RegistryTabs.Category.INGREDIENTS);
    public static final RegistryObject<Item> YELLOW_WOOL_FUR = woolFurItem("yellow_wool_fur", RegistryTabs.Category.INGREDIENTS);
    public static final RegistryObject<Item> LIME_WOOL_FUR = woolFurItem("lime_wool_fur", RegistryTabs.Category.INGREDIENTS);
    public static final RegistryObject<Item> PINK_WOOL_FUR = woolFurItem("pink_wool_fur", RegistryTabs.Category.INGREDIENTS);
    public static final RegistryObject<Item> GRAY_WOOL_FUR = woolFurItem("gray_wool_fur", RegistryTabs.Category.INGREDIENTS);
    public static final RegistryObject<Item> LIGHT_GRAY_WOOL_FUR = woolFurItem("light_gray_wool_fur", RegistryTabs.Category.INGREDIENTS);
    public static final RegistryObject<Item> CYAN_WOOL_FUR = woolFurItem("cyan_wool_fur", RegistryTabs.Category.INGREDIENTS);
    public static final RegistryObject<Item> PURPLE_WOOL_FUR = woolFurItem("purple_wool_fur", RegistryTabs.Category.INGREDIENTS);
    public static final RegistryObject<Item> BLUE_WOOL_FUR = woolFurItem("blue_wool_fur", RegistryTabs.Category.INGREDIENTS);
    public static final RegistryObject<Item> BROWN_WOOL_FUR = woolFurItem("brown_wool_fur", RegistryTabs.Category.INGREDIENTS);
    public static final RegistryObject<Item> GREEN_WOOL_FUR = woolFurItem("green_wool_fur", RegistryTabs.Category.INGREDIENTS);
    public static final RegistryObject<Item> RED_WOOL_FUR = woolFurItem("red_wool_fur", RegistryTabs.Category.INGREDIENTS);
    public static final RegistryObject<Item> BLACK_WOOL_FUR = woolFurItem("black_wool_fur", RegistryTabs.Category.INGREDIENTS);
    //
    public static final RegistryObject<Item> TOUGH_CLOTH = RegistryItems
            .register("tough_cloth", item -> new BasicAccessoryItem(item, AccessoryBuilder.TOUGH_CLOTH))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register();
    public static final RegistryObject<Item> NULL_MAGIC_RUNE = RegistryItems
            .register("null_magic_rune", item -> new BasicAccessoryItem(item, AccessoryBuilder.NULL_MAGIC_RUNE))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register();
    public static final RegistryObject<Item> FIREFLY_FAYE = RegistryItems
            .register("firefly_faye", item -> new BasicAccessoryItem(item, AccessoryBuilder.FIREFLY_FAYE))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register();
    public static final RegistryObject<Item> SAPPHIRE_CRYSTAL = RegistryItems
            .register("sapphire_crystal", item -> new BasicAccessoryItem(item, AccessoryBuilder.SAPPHIRE_CRYSTAL))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register();
    public static final RegistryObject<Item> CHAIN_VEST = RegistryItems
            .register("chain_vest", item -> new BasicAccessoryItem(item, AccessoryBuilder.CHAIN_VEST))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register();
    public static final RegistryObject<Item> RUMINATIVE_BEADS = RegistryItems
            .register("ruminative_beads", item -> new BasicAccessoryItem(item, AccessoryBuilder.RUMINATIVE_BEADS))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register();

    public static final RegistryObject<Item> SPECTRAL_CLOAK = RegistryItems
            .register("spectral_cloak", item -> new AccessoryItem(item, AccessoryBuilder.SPECTRAL_CLOAK))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register();
    public static final RegistryObject<Item> UNSTABLE_RUNIC_TOME = RegistryItems
            .register("unstable_runic_tome", item -> new AccessoryItem(item, AccessoryBuilder.UNSTABLE_RUNIC_TOME))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register();
    public static final RegistryObject<Item> BROKEN_HERO_SWORD = RegistryItems
            .register("broken_hero_sword", item -> new AccessoryItem(item, AccessoryBuilder.BROKEN_HERO_SWORD))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register();
    public static final RegistryObject<Item> DARK_ESSESNCE_CRYSTAL = RegistryItems
            .register("dark_essence_crystal", item -> new AccessoryItem(item, AccessoryBuilder.MAGICAL_OPS))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register();

    public static final RegistryObject<Item> BOOTS = RegistryItems
            .register("boots", item -> new AccessoryItem(AccessoryItemGroup.BOOTS, item, AccessoryBuilder.BASIC_BOOTS))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register();

    public static final RegistryObject<Item> GOLDEN_BOOTS = RegistryItems
            .register("golden_boots", item -> new AccessoryItem(AccessoryItemGroup.BOOTS, item, AccessoryBuilder.GOLDEN_BOOTS))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register();

    public static final RegistryObject<Item> BERSERKER_BOOTS = RegistryItems
            .register("berserker_boots", item -> new AccessoryItem(AccessoryItemGroup.BOOTS, item, AccessoryBuilder.BERSERKER_BOOTS))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register();

    public static final RegistryObject<Item> IRON_PLATED_BOOTS = RegistryItems
            .register("iron_plated_boots", item -> new AccessoryItem(AccessoryItemGroup.BOOTS, item, AccessoryBuilder.IRON_PLATED_BOOTS))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register();

    public static final RegistryObject<Item> MINER_BOOTS = RegistryItems
            .register("miner_boots", item -> new AccessoryItem(AccessoryItemGroup.BOOTS, item, AccessoryBuilder.MINING_BOOTS))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register();

    public static final RegistryObject<Item> BOOK_OF_KNOWLEDGE = RegistryItems
            .register("book_of_knowledge", item -> new AccessoryItem(AccessoryItemGroup.NONE, item, AccessoryBuilder.BOOK_OF_KNOWLEDGE))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register();

    public static final RegistryObject<Item> ANCIENT_PICKAXE = RegistryItems
            .register("ancient_pickaxe", item -> new AccessoryItem(item, AccessoryBuilder.ANCIENT_PICKAXE))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register();

    public static final RegistryObject<Item> ANCIENT_CHISEL = RegistryItems
            .register("ancient_chisel", item -> new AccessoryItem(item, AccessoryBuilder.ANCIENT_CHISEL))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register();

    public static final RegistryObject<Item> MINER_GLOVES = RegistryItems
            .register("miner_gloves", item -> new AccessoryItem(item, AccessoryBuilder.MINER_GLOVES))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register();

    public static final RegistryObject<Item> DWARF_MINER_RING = RegistryItems
            .register("dwarf_miner_ring", item -> new AccessoryItem(item, AccessoryBuilder.DWARF_MINER_RING))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register();

    public static final RegistryObject<Item> POISON_VILE = RegistryItems
            .register("poison_vile", item -> new AccessoryItem(item, AccessoryBuilder.POISON_VILE))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register();

    public static final RegistryObject<Item> LUCKY_HORSE_SHOE = RegistryItems
            .register("lucky_horse_shoe", item -> new AccessoryItem(item, AccessoryBuilder.NEGATE_FALL_DAMAGE))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register();

    public static final RegistryObject<Item> POWER_CLAW = RegistryItems
            .register("power_claw", item -> new AccessoryItem(item, AccessoryBuilder.POWER_CLAW))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register();

    public static final RegistryObject<Item> BLOOD_OF_BERSERKER = RegistryItems
            .register("blood_of_berserker", item -> new AccessoryItem(item, AccessoryBuilder.BLOOD_OF_BERSERKER))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register();

    public static final RegistryObject<Item> THORNMAIL = RegistryItems
            .register("thornmail", item -> new AccessoryItem(item, AccessoryBuilder.THORNMAIL))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register();

    public static final RegistryObject<Item> SPRING_LOCK = RegistryItems
            .register("spring_lock", item -> new AccessoryItem(item, AccessoryBuilder.SPRING_LOCK))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register();
    public static final RegistryObject<Item> POWER_GLOVES = RegistryItems
            .register("power_gloves", item -> new AccessoryItem(item, AccessoryBuilder.POWER_GLOVES))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register();

    public static final RegistryObject<Item> SWIFT_DAGGER = RegistryItems
            .register("swift_dagger", item -> new AccessoryItem(item, AccessoryBuilder.SWIFT_DAGGER))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register();

    public static final RegistryObject<Item> AETHER_WISP = RegistryItems
            .register("aether_wisp", item -> new AccessoryItem(item, AccessoryBuilder.AETHER_WISP))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register();

    public static final RegistryObject<Item> HEART_OF_THE_TREE = RegistryItems
            .register("heart_of_the_tree", item -> new AccessoryItem(item, AccessoryBuilder.OVERGROWTH))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register();

    public static final RegistryObject<Item> RECTRIX = RegistryItems
            .register("rectrix", item -> new AccessoryItem(item, AccessoryBuilder.RECTRIX))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register();

    public static final RegistryObject<Item> FORTUNE_SHIKIGAMI = RegistryItems
            .register("fortune_shikigami", item -> new AccessoryItem(item, AccessoryBuilder.FORTUNE_SHIKIGAMI))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register();

    public static final RegistryObject<Item> LEATHER_AGILITY = RegistryItems
            .register("leather_agility", item -> new AccessoryItem(item, AccessoryBuilder.LEATHER_AGILITY))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register();

    public static final RegistryObject<Item> AGILE_DAGGER = RegistryItems
            .register("agile_dagger", item -> new AccessoryItem(item, AccessoryBuilder.AGILE_DAGGER))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register();

    public static final RegistryObject<Item> MASK_OF_AGONY = RegistryItems
            .register("mask_of_agony", item -> new AccessoryItem(item, AccessoryBuilder.DESPAIR_AND_DEFY))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register();

    public static final RegistryObject<Item> VITALITY_STONE = RegistryItems
            .register("vitality_stone", item -> new AccessoryItem(item, AccessoryBuilder.VITALITY_STONE))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register();

    public static final RegistryObject<Item> CINDER_STONE = RegistryItems
            .register("cinder_stone", item -> new AccessoryItem(item, AccessoryBuilder.CINDER_STONE))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register();

    public static final RegistryObject<Item> CRYSTALLIZED_PLATE = RegistryItems
            .register("crystallized_plate", item -> new AccessoryItem(item, AccessoryBuilder.CRYSTALLIZED_PLATE))
            .tab(RegistryTabs.Category.ACCESSORY)
            .model(ItemModels.ACCESSORY)
            .tag(BHItemTags.ONLY_ACCESSORY)
            .register();

    //
    public static final RegistryObject<Item> SILVER_SWORD = RegistryItems
            .register("silver_sword", item -> WeaponBuilder.SWORD.create(MeleeWeaponMaterials.SILVER, item))
            .tab(RegistryTabs.Category.COMBAT)
            .model(ItemModels.HANDHELD)
            .register();
    public static final RegistryObject<Item> HELLSTONE_SWORD = RegistryItems
            .register("hellstone_sword", item -> WeaponBuilder.SWORD.create(MeleeWeaponMaterials.HELLSTONE, item))
            .tab(RegistryTabs.Category.COMBAT)
            .model(ItemModels.HANDHELD)
            .register();
    public static final RegistryObject<Item> COBALT_SWORD = RegistryItems
            .register("cobalt_sword", item -> WeaponBuilder.SWORD.create(MeleeWeaponMaterials.COBALT, item))
            .tab(RegistryTabs.Category.COMBAT)
            .model(ItemModels.HANDHELD)
            .register();
    public static final RegistryObject<Item> ADAMANTITE_SWORD = RegistryItems
            .register("adamantite_sword", item -> WeaponBuilder.SWORD.create(MeleeWeaponMaterials.ADAMANTITE, item))
            .tab(RegistryTabs.Category.COMBAT)
            .model(ItemModels.HANDHELD)
            .register();
    public static final RegistryObject<Item> TITANIUM_SWORD = RegistryItems
            .register("titanium_sword", item -> WeaponBuilder.SWORD.create(MeleeWeaponMaterials.TITANIUM, item))
            .tab(RegistryTabs.Category.COMBAT)
            .model(ItemModels.HANDHELD)
            .register();

    public static final RegistryObject<Item> SILVER_AXE = RegistryItems
            .register("silver_axe", item -> WeaponBuilder.AXE.create(MeleeWeaponMaterials.SILVER, item))
            .tab(RegistryTabs.Category.COMBAT, RegistryTabs.Category.AXE, RegistryTabs.Category.TOOLS)
            .model(ItemModels.HANDHELD)
            .register();
    public static final RegistryObject<Item> COBALT_AXE = RegistryItems
            .register("cobalt_axe", item -> WeaponBuilder.AXE.create(MeleeWeaponMaterials.COBALT, item))
            .tab(RegistryTabs.Category.COMBAT, RegistryTabs.Category.AXE, RegistryTabs.Category.TOOLS)
            .model(ItemModels.HANDHELD)
            .register();
    public static final RegistryObject<Item> HELLSTONE_AXE = RegistryItems
            .register("hellstone_axe", item -> WeaponBuilder.AXE.create(MeleeWeaponMaterials.HELLSTONE, item))
            .tab(RegistryTabs.Category.COMBAT, RegistryTabs.Category.AXE, RegistryTabs.Category.TOOLS)
            .model(ItemModels.HANDHELD)
            .register();
    public static final RegistryObject<Item> ADAMANTITE_AXE = RegistryItems
            .register("adamantite_axe", item -> WeaponBuilder.AXE.create(MeleeWeaponMaterials.ADAMANTITE, item))
            .tab(RegistryTabs.Category.COMBAT, RegistryTabs.Category.AXE, RegistryTabs.Category.TOOLS)
            .model(ItemModels.HANDHELD)
            .register();
    public static final RegistryObject<Item> TITANIUM_AXE = RegistryItems
            .register("titanium_axe", item -> WeaponBuilder.AXE.create(MeleeWeaponMaterials.TITANIUM, item))
            .tab(RegistryTabs.Category.COMBAT, RegistryTabs.Category.AXE, RegistryTabs.Category.TOOLS)
            .model(ItemModels.HANDHELD)
            .register();

    public static final RegistryObject<Item> SILVER_PICKAXE = RegistryItems
            .register("silver_pickaxe", item -> WeaponBuilder.PICKAXE.create(MeleeWeaponMaterials.SILVER, item))
            .tab(RegistryTabs.Category.TOOLS)
            .model(ItemModels.HANDHELD)
            .register();
    public static final RegistryObject<Item> COBALT_PICKAXE = RegistryItems
            .register("cobalt_pickaxe", item -> WeaponBuilder.PICKAXE.create(MeleeWeaponMaterials.COBALT, item))
            .tab(RegistryTabs.Category.TOOLS)
            .model(ItemModels.HANDHELD)
            .register();
    public static final RegistryObject<Item> HELLSTONE_PICKAXE = RegistryItems
            .register("hellstone_pickaxe", item -> WeaponBuilder.PICKAXE.create(MeleeWeaponMaterials.HELLSTONE, item))
            .tab(RegistryTabs.Category.TOOLS)
            .model(ItemModels.HANDHELD)
            .register();
    public static final RegistryObject<Item> ADAMANTITE_PICKAXE = RegistryItems
            .register("adamantite_pickaxe", item -> WeaponBuilder.PICKAXE.create(MeleeWeaponMaterials.ADAMANTITE, item))
            .tab(RegistryTabs.Category.TOOLS)
            .model(ItemModels.HANDHELD)
            .register();
    public static final RegistryObject<Item> TITANIUM_PICKAXE = RegistryItems
            .register("titanium_pickaxe", item -> WeaponBuilder.PICKAXE.create(MeleeWeaponMaterials.TITANIUM, item))
            .tab(RegistryTabs.Category.TOOLS)
            .model(ItemModels.HANDHELD)
            .register();
    public static final RegistryObject<Item> SILVER_SHOVEL = RegistryItems
            .register("silver_shovel", item -> WeaponBuilder.SHOVEL.create(MeleeWeaponMaterials.SILVER, item))
            .tab(RegistryTabs.Category.TOOLS)
            .model(ItemModels.HANDHELD)
            .register();
    public static final RegistryObject<Item> COBALT_SHOVEL = RegistryItems
            .register("cobalt_shovel", item -> WeaponBuilder.SHOVEL.create(MeleeWeaponMaterials.COBALT, item))
            .tab(RegistryTabs.Category.TOOLS)
            .model(ItemModels.HANDHELD)
            .register();
    public static final RegistryObject<Item> HELLSTONE_SHOVEL = RegistryItems
            .register("hellstone_shovel", item -> WeaponBuilder.SHOVEL.create(MeleeWeaponMaterials.HELLSTONE, item))
            .tab(RegistryTabs.Category.TOOLS)
            .model(ItemModels.HANDHELD)
            .register();
    public static final RegistryObject<Item> ADAMANTITE_SHOVEL = RegistryItems
            .register("adamantite_shovel", item -> WeaponBuilder.SHOVEL.create(MeleeWeaponMaterials.ADAMANTITE, item))
            .tab(RegistryTabs.Category.TOOLS)
            .model(ItemModels.HANDHELD)
            .register();
    public static final RegistryObject<Item> TITANIUM_SHOVEL = RegistryItems
            .register("titanium_shovel", item -> WeaponBuilder.SHOVEL.create(MeleeWeaponMaterials.TITANIUM, item))
            .tab(RegistryTabs.Category.TOOLS)
            .model(ItemModels.HANDHELD)
            .register();
    public static final RegistryObject<Item> COBALT_HOE = RegistryItems
            .register("cobalt_hoe", item -> WeaponBuilder.HOE.create(MeleeWeaponMaterials.COBALT, item))
            .tab(RegistryTabs.Category.TOOLS)
            .model(ItemModels.HANDHELD)
            .register();
    public static final RegistryObject<Item> HELLSTONE_HOE = RegistryItems
            .register("hellstone_hoe", item -> WeaponBuilder.HOE.create(MeleeWeaponMaterials.HELLSTONE, item))
            .tab(RegistryTabs.Category.TOOLS)
            .model(ItemModels.HANDHELD)
            .register();
    public static final RegistryObject<Item> ADAMANTITE_HOE = RegistryItems
            .register("adamantite_hoe", item -> WeaponBuilder.HOE.create(MeleeWeaponMaterials.ADAMANTITE, item))
            .tab(RegistryTabs.Category.TOOLS)
            .model(ItemModels.HANDHELD)
            .register();
    public static final RegistryObject<Item> TITANIUM_HOE = RegistryItems
            .register("titanium_hoe", item -> WeaponBuilder.HOE.create(MeleeWeaponMaterials.TITANIUM, item))
            .tab(RegistryTabs.Category.TOOLS)
            .model(ItemModels.HANDHELD)
            .register();

    public static final RegistryObject<Item> BLADE_OF_THE_ENDERLORD = RegistryItems
            .register("blade_of_the_enderlord", item -> WeaponBuilder.BLADE_OF_THE_ENDERLORD.create(MeleeWeaponMaterials.TIER_ONE, item))
            .properties(p -> p.rarity(BHRarity.MYTHICAL))
            .tab(RegistryTabs.Category.COMBAT)
            .model(ItemModels.BIG_HANDHELD)
            .register();

    public static final RegistryObject<Item> GIANT_SLAYER_SWORD = RegistryItems
            .register("giant_slayer_sword", item -> WeaponBuilder.GIANT_SLAYER_SWORD.create(MeleeWeaponMaterials.TIER_ONE, item))
            .properties(p -> p.rarity(BHRarity.MYTHICAL))
            .tab(RegistryTabs.Category.COMBAT)
            .model(ItemModels.BIG_HANDHELD)
            .register();
    public static final RegistryObject<Item> ZENITH = RegistryItems
            .register("zenith", item -> WeaponBuilder.ZENITH.create(MeleeWeaponMaterials.TIER_ONE, item))
            .properties(p -> p.rarity(BHRarity.MYTHICAL))
            .tab(RegistryTabs.Category.COMBAT)
            .model(ItemModels.BIG_HANDHELD)
            .register();
    public static final RegistryObject<Item> ELUCIDATOR = RegistryItems
            .register("elucidator", item -> WeaponBuilder.ELUCIDATOR.create(MeleeWeaponMaterials.TIER_ONE, item))
            .properties(p -> p.rarity(BHRarity.MYTHICAL))
            .tab(RegistryTabs.Category.COMBAT)
            .model(ItemModels.BIG_HANDHELD)
            .register();
    public static final RegistryObject<Item> DARK_REPULSER = RegistryItems
            .register("dark_repulser", item -> WeaponBuilder.DARK_REPULSER.create(MeleeWeaponMaterials.TIER_ONE, item))
            .properties(p -> p.rarity(BHRarity.MYTHICAL))
            .tab(RegistryTabs.Category.COMBAT)
            .model(ItemModels.BIG_HANDHELD)
            .register();

    public static final RegistryObject<Item> BLAZING_INFERNO_SPAWN_EGG = spawnEgg("blazing_inferno", BHEntity.BLAZING_INFERNO, ColorUtil.combineRGB(255, 248, 71), ColorUtil.combineRGB(139, 52, 1));


    private static RegistryObject<Item> spawnEgg(String entityName, RegistryObject entityType, int backgroundColor, int highlightColor) {
        String itemName = entityName + "_spawn_egg";
        return RegistryItems.register(itemName,item -> new ForgeSpawnEggItem(entityType, backgroundColor, highlightColor, new Item.Properties()))
                .tab(RegistryTabs.Category.SPAWN_EGG)
                .model(ItemModels.SPAWN_EGG)
                .register();
    }

    public static RegistryObject<Item> basicItem(String name, RegistryTabs.Category category) {
        return RegistryItems.register(name, BasicItem::new).tab(category).model(ItemModels.GENERATED).register();
    }

    public static RegistryObject<Item> woolFurItem(String name, RegistryTabs.Category category) {
        return RegistryItems.register(name, BasicItem::new).tab(category).tag(BHItemTags.WOOL_FUR).model(ItemModels.GENERATED).register();
    }

    public static void register(IEventBus eventBus) {
        RegistryEntries.ITEMS.register(eventBus);
    }
}
