package com.kenhorizon.beyondhorizon.server.init;

import com.kenhorizon.beyondhorizon.client.render.util.Colors;
import com.kenhorizon.beyondhorizon.server.api.accessory.AccessoryBuilder;
import com.kenhorizon.beyondhorizon.server.api.accessory.AccessoryItemGroup;
import com.kenhorizon.beyondhorizon.server.item.*;
import com.kenhorizon.beyondhorizon.server.item.base.*;
import com.kenhorizon.beyondhorizon.server.item.base.armor.ArmorBaseItem;
import com.kenhorizon.beyondhorizon.server.item.base.tools.DiggerBaseItem;
import com.kenhorizon.beyondhorizon.server.item.base.weapons.SwordBaseItem;
import com.kenhorizon.beyondhorizon.server.item.debug_items.*;
import com.kenhorizon.beyondhorizon.server.item.materials.ArmorBaseMaterials;
import com.kenhorizon.beyondhorizon.server.item.materials.MeleeWeaponMaterials;
import com.kenhorizon.beyondhorizon.server.tags.BHItemTags;
import com.kenhorizon.libs.client.model.item.ItemModels;
import com.kenhorizon.libs.registry.RegistryEntries;
import com.kenhorizon.libs.registry.RegistryItems;
import com.kenhorizon.libs.registry.RegistryTabs;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
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
            .register("debug3", DebugLevelSystemResetItems::new)
            .itemName("Debug: Level System Reset")
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
    public static final RegistryObject<Item> DEBUG6 = RegistryItems
            .register("debug6", DebugGodModeItems::new)
            .itemName("Debug: Mob set to cant despawn")
            .tab(RegistryTabs.Category.DEBUGS)
            .model(ItemModels.HANDHELD)
            .register();

    public static final RegistryObject<Item> GUIDE_BOOK = RegistryItems
            .register("guide_book", GuideBookItem::new)
            .tab(RegistryTabs.Category.MISC)
            .model(ItemModels.GENERATED)
            .register();

    public static final RegistryObject<Item> VOID_BAG = RegistryItems
            .register("void_bag", VoidBagItem::new)
            .properties(p -> p.rarity(Rarity.EPIC).stacksTo(1).fireResistant())
            .tab(RegistryTabs.Category.MISC)
            .model(ItemModels.GENERATED)
            .register();

    public static final RegistryObject<Item> QUIVER = RegistryItems
            .register("quiver", QuiverItem::new)
            .tab(RegistryTabs.Category.COMBAT, RegistryTabs.Category.MISC)
            .model(ItemModels.GENERATED)
            .register();

    public static final RegistryObject<Item> GOLD_RING = basicItem("gold_ring", RegistryTabs.Category.INGREDIENTS);
    public static final RegistryObject<Item> HANDLE = basicItem("handle", RegistryTabs.Category.INGREDIENTS);
    public static final RegistryObject<Item> STINGER = basicItem("stinger", RegistryTabs.Category.INGREDIENTS);
    public static final RegistryObject<Item> HOGLIN_TUSK = basicItem("hoglin_tusk", RegistryTabs.Category.INGREDIENTS);
    public static final RegistryObject<Item> DUSK_LEATHER = basicItem("dusk_leather", RegistryTabs.Category.INGREDIENTS);
    public static final RegistryObject<Item> FLAME_CELL = basicItem("flame_cell", RegistryTabs.Category.INGREDIENTS);
    public static final RegistryObject<Item> WILDFIRE_FRAGMENT = basicItem("wildfire_fragment", RegistryTabs.Category.INGREDIENTS);
    public static final RegistryObject<Item> RAW_BLACK_IRON = basicItem("raw_black_iron", RegistryTabs.Category.INGREDIENTS);
    public static final RegistryObject<Item> RAW_LUMINITE = basicItem("raw_luminite", RegistryTabs.Category.INGREDIENTS);
    public static final RegistryObject<Item> RAW_STARITE = basicItem("raw_starite", RegistryTabs.Category.INGREDIENTS);
    public static final RegistryObject<Item> RAW_HELLSTONE = basicItem("raw_hellstone", RegistryTabs.Category.INGREDIENTS);
    public static final RegistryObject<Item> RAW_EMBED_HELLSTONE = basicItem("raw_embed_hellstone", RegistryTabs.Category.INGREDIENTS);
    public static final RegistryObject<Item> BLACK_IRON_INGOT = basicItem("black_iron_ingot", RegistryTabs.Category.INGREDIENTS);
    public static final RegistryObject<Item> BLACK_IRON_NUGGET = basicItem("black_iron_nugget", RegistryTabs.Category.INGREDIENTS);
    public static final RegistryObject<Item> HELLSTONE_INGOT = basicItem("hellstone_ingot", RegistryTabs.Category.INGREDIENTS);
    public static final RegistryObject<Item> STARITE_INGOT = basicItem("starite_ingot", RegistryTabs.Category.INGREDIENTS);
    public static final RegistryObject<Item> LUMINITE_INGOT = basicItem("luminite_ingot", RegistryTabs.Category.INGREDIENTS);
    public static final RegistryObject<Item> DARK_CRYSTAL = basicItem("dark_crystal", RegistryTabs.Category.INGREDIENTS);
    public static final RegistryObject<Item> SHADOWCRUST = basicItem("shadowcrust", RegistryTabs.Category.INGREDIENTS);

    public static final RegistryObject<Item> CHAIN_PLATE = basicItem("chain_plate", RegistryTabs.Category.INGREDIENTS);
    public static final RegistryObject<Item> RUBY = basicItem("ruby", RegistryTabs.Category.INGREDIENTS);
    public static final RegistryObject<Item> AMPLIFLYING_TOME = basicItem("ampliflying_tome", RegistryTabs.Category.INGREDIENTS);

    public static final RegistryObject<Item> WILDFIRE_HELMET = armorItem("wildfire_helmet", ArmorItem.Type.HELMET, ArmorBaseMaterials.WILDFIRE);
    public static final RegistryObject<Item> WILDFIRE_CHESTPLATE = armorItem("wildfire_chestplate", ArmorItem.Type.CHESTPLATE, ArmorBaseMaterials.WILDFIRE);
    public static final RegistryObject<Item> WILDFIRE_LEGGINGS = armorItem("wildfire_leggings", ArmorItem.Type.LEGGINGS, ArmorBaseMaterials.WILDFIRE);
    public static final RegistryObject<Item> WILDFIRE_BOOTS = armorItem("wildfire_boots", ArmorItem.Type.BOOTS, ArmorBaseMaterials.WILDFIRE);

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
    public static final RegistryObject<Item> HELLSTONE_SWORD = swordItem("hellstone_sword", MeleeWeaponMaterials.HELLSTONE, ItemBuilder.SWORD);
    public static final RegistryObject<Item> HELLSTONE_AXE = toolItem("hellstone_axe", MeleeWeaponMaterials.HELLSTONE, ItemBuilder.AXE);
    public static final RegistryObject<Item> HELLSTONE_PICKAXE = toolItem("hellstone_pickaxe", MeleeWeaponMaterials.HELLSTONE, ItemBuilder.PICKAXE);
    public static final RegistryObject<Item> HELLSTONE_SHOVEL = toolItem("hellstone_shovel", MeleeWeaponMaterials.HELLSTONE, ItemBuilder.SHOVEL);
    public static final RegistryObject<Item> HELLSTONE_HOE = toolItem("hellstone_hoe", MeleeWeaponMaterials.HELLSTONE, ItemBuilder.HOE);

    public static final RegistryObject<Item> STARITE_CLAYMORE = swordItem("starite_claymore", MeleeWeaponMaterials.STARITE, ItemBuilder.CLAYMORE, ItemModels.BIG32_HANDHELD);
    public static final RegistryObject<Item> STARITE_SWORD = swordItem("starite_sword", MeleeWeaponMaterials.STARITE, ItemBuilder.SWORD);
    public static final RegistryObject<Item> STARITE_AXE = toolItem("starite_axe", MeleeWeaponMaterials.STARITE, ItemBuilder.AXE);
    public static final RegistryObject<Item> STARITE_PICKAXE = toolItem("starite_pickaxe", MeleeWeaponMaterials.STARITE, ItemBuilder.PICKAXE);
    public static final RegistryObject<Item> STARITE_SHOVEL = toolItem("starite_shovel", MeleeWeaponMaterials.STARITE, ItemBuilder.SHOVEL);
    public static final RegistryObject<Item> STARITE_HOE = toolItem("starite_hoe", MeleeWeaponMaterials.STARITE, ItemBuilder.HOE);

    public static final RegistryObject<Item> BLACK_IRON_CLAYMORE = swordItem("black_iron_claymore", MeleeWeaponMaterials.BLACK_IRON, ItemBuilder.CLAYMORE, ItemModels.BIG32_HANDHELD);
    public static final RegistryObject<Item> BLACK_IRON_SWORD = swordItem("black_iron_sword", MeleeWeaponMaterials.BLACK_IRON, ItemBuilder.SWORD);
    public static final RegistryObject<Item> BLACK_IRON_AXE = toolItem("black_iron_axe", MeleeWeaponMaterials.BLACK_IRON, ItemBuilder.AXE);
    public static final RegistryObject<Item> BLACK_IRON_PICKAXE = toolItem("black_iron_pickaxe", MeleeWeaponMaterials.BLACK_IRON, ItemBuilder.PICKAXE);
    public static final RegistryObject<Item> BLACK_IRON_SHOVEL = toolItem("black_iron_shovel", MeleeWeaponMaterials.BLACK_IRON, ItemBuilder.SHOVEL);
    public static final RegistryObject<Item> BLACK_IRON_HOE = toolItem("black_iron_hoe", MeleeWeaponMaterials.BLACK_IRON, ItemBuilder.HOE);
    //
    public static final RegistryObject<Item> BROKEN_HERO_SWORD = accessoryHandheldItem("broken_hero_sword", AccessoryItemGroup.HERO_SWORD, AccessoryBuilder.BROKEN_HERO_SWORD);
    public static final RegistryObject<Item> SHEEN = accessoryHandheldItemX32("sheen", AccessoryItemGroup.HERO_SWORD, AccessoryBuilder.SHEEN);
    public static final RegistryObject<Item> TWILIGHT_SWORD = accessoryHandheldItemX32("twilight_sword", AccessoryItemGroup.HERO_SWORD, AccessoryBuilder.TWILIGHT_SWORD);

    public static final RegistryObject<Item> SPEAR_OF_CHAOS = accessoryHandheldItemX32("spear_of_chaos", AccessoryItemGroup.FATALITY, AccessoryBuilder.SPEAR_OF_CHAOS);
    public static final RegistryObject<Item> HARPOON_HEAD = accessoryItem("harpoon_head", AccessoryItemGroup.FATALITY, AccessoryBuilder.HARPOON_HEAD);

    public static final RegistryObject<Item> VOID_STAFF = accessoryHandheldItem("void_staff", AccessoryItemGroup.BLIGHT, AccessoryBuilder.VOID_STAFF);
    //

    public static final RegistryObject<Item> TOUGH_CLOTH = accessoryBasicItem("tough_cloth", AccessoryBuilder.TOUGH_CLOTH);
    public static final RegistryObject<Item> NULL_MAGIC_RUNE = accessoryBasicItem("null_magic_rune", AccessoryBuilder.NULL_MAGIC_RUNE);
    public static final RegistryObject<Item> FIREFLY_FAYE = accessoryBasicItem("firefly_faye", AccessoryBuilder.FIREFLY_FAYE);
    public static final RegistryObject<Item> SAPPHIRE_CRYSTAL = accessoryBasicItem("sapphire_crystal", AccessoryBuilder.SAPPHIRE_CRYSTAL);
    public static final RegistryObject<Item> CHAIN_VEST = accessoryBasicItem("chain_vest", AccessoryBuilder.CHAIN_VEST);
    public static final RegistryObject<Item> RUMINATIVE_BEADS = accessoryBasicItem("ruminative_beads", AccessoryBuilder.RUMINATIVE_BEADS);
    public static final RegistryObject<Item> SPECTRAL_CLOAK = accessoryBasicItem("spectral_cloak", AccessoryBuilder.SPECTRAL_CLOAK);
     //
     public static final RegistryObject<Item> DORAN_BLADE = accessoryItem("doran_blade", AccessoryBuilder.DORAN_BLADE);
    public static final RegistryObject<Item> DORAN_BOW = accessoryItem("doran_bow", AccessoryBuilder.DORAN_BOW);
    public static final RegistryObject<Item> DORAN_HELM = accessoryItem("doran_helm", AccessoryBuilder.DORAN_HELM);
    public static final RegistryObject<Item> DORAN_RING = accessoryItem("doran_ring", AccessoryBuilder.DORAN_RING);
    public static final RegistryObject<Item> DORAN_SHIELD = accessoryItem("doran_shield", AccessoryBuilder.DORAN_SHIELD);
    public static final RegistryObject<Item> LIGHT_STRING_BOW = accessoryItem("light_string_bow", AccessoryBuilder.LIGHT_STRING_BOW);
    public static final RegistryObject<Item> HEAVY_STRING_BOW = accessoryItem("heavy_string_bow", AccessoryBuilder.HEAVY_STRING_BOW);
    public static final RegistryObject<Item> KRAKEN_SLAYER = accessoryItem("kraken_slayer", AccessoryBuilder.KRAKEN_SLAYER);
    public static final RegistryObject<Item> TITAN_GLOVES = accessoryItem("titan_gloves", AccessoryBuilder.TITAN_GLOVES);
    public static final RegistryObject<Item> GLOVE_OF_AFTERSHOCK = accessoryItem("glove_of_aftershock", AccessoryBuilder.GLOVE_OF_AFTERSHOCK);
    public static final RegistryObject<Item> FLAME_OF_TORMENT = accessoryItem("flame_of_torment", AccessoryBuilder.FLAME_OF_TORMENT);
    public static final RegistryObject<Item> CURSED_SKULL = accessoryItem("cursed_skull", AccessoryBuilder.CURSED_SKULL);
    public static final RegistryObject<Item> RECURVE_ARROW = accessoryItem("recurve_arrow", AccessoryBuilder.RECURVE_ARROW);
    public static final RegistryObject<Item> ASHES_OF_FLAME = accessoryItem("ashes_of_flame", AccessoryBuilder.ASHES_OF_FLAME);
    public static final RegistryObject<Item> ABYSSAL_TOOTH = accessoryItem("abyssal_tooth", AccessoryBuilder.ABYSSAL_TOOTH);
    public static final RegistryObject<Item> TALISMAN_OF_ASCENSION = accessoryItem("talisman_of_ascension", AccessoryBuilder.TALISMAN_OF_ASCENSION);
    public static final RegistryObject<Item> WARDEN_MAIL = accessoryItem("warden_mail", AccessoryBuilder.WARDEN_MAIL);
    public static final RegistryObject<Item> WARD_CLEANSE = accessoryItem("ward_cleanse", AccessoryBuilder.WARD_CLEANSE);
    public static final RegistryObject<Item> SOUL_SIPHON = accessoryItem("soul_siphon", AccessoryBuilder.SOUL_SIPHON);
    public static final RegistryObject<Item> DEATH_CONTRACT = accessoryItem("death_contract", AccessoryBuilder.DEATH_CONTRACT);
    public static final RegistryObject<Item> UNSTABLE_RUNIC_TOME = accessoryItem("unstable_runic_tome", AccessoryBuilder.UNSTABLE_RUNIC_TOME);
    public static final RegistryObject<Item> TRUE_HERO_GEM = accessoryItem("wraith_gem", AccessoryItemGroup.POWER_GEM, AccessoryBuilder.TRUE_HERO_SWORD);
    public static final RegistryObject<Item> INFINITY_GEM = accessoryItem("infinity_gem", AccessoryItemGroup.POWER_GEM, AccessoryBuilder.INFINITY_SWORD);
    public static final RegistryObject<Item> NULL_GEM = accessoryItem("null_gem", AccessoryItemGroup.POWER_GEM, AccessoryBuilder.NULL_SWORD);
    public static final RegistryObject<Item> CATALYST_GEM = accessoryItem("catalyst_gem", AccessoryItemGroup.POWER_GEM, AccessoryBuilder.ASCENDED_HERO_SWORD);
    public static final RegistryObject<Item> DARK_ESSESNCE_CRYSTAL = accessoryItem("dark_essence_crystal", AccessoryBuilder.MAGICAL_OPS);

    public static final RegistryObject<Item> BOOTS = accessoryItem("boots", AccessoryItemGroup.BOOTS, AccessoryBuilder.BASIC_BOOTS);
    public static final RegistryObject<Item> GOLDEN_BOOTS = accessoryItem("golden_boots", AccessoryItemGroup.BOOTS, AccessoryBuilder.GOLDEN_BOOTS);
    public static final RegistryObject<Item> BERSERKER_BOOTS = accessoryItem("berserker_boots", AccessoryItemGroup.BOOTS, AccessoryBuilder.BERSERKER_BOOTS);
    public static final RegistryObject<Item> IRON_PLATED_BOOTS = accessoryItem("iron_plated_boots", AccessoryItemGroup.BOOTS, AccessoryBuilder.IRON_PLATED_BOOTS);
    public static final RegistryObject<Item> MINER_BOOTS = accessoryItem("miner_boots", AccessoryItemGroup.BOOTS, AccessoryBuilder.MINER_BOOTS);
    public static final RegistryObject<Item> BOOTS_OF_SWIFTNESS = accessoryItemModel("boots_of_swiftness", AccessoryBuilder.BOOTS_OF_SWITFNESS);

    public static final RegistryObject<Item> BOOK_OF_KNOWLEDGE = accessoryItem("book_of_knowledge", AccessoryItemGroup.NONE, AccessoryBuilder.BOOK_OF_KNOWLEDGE);
    public static final RegistryObject<Item> ULTIMA = accessoryItem("ultima", AccessoryBuilder.ULTIMA);
    public static final RegistryObject<Item> ANCIENT_PICKAXE = accessoryItem("ancient_pickaxe", AccessoryBuilder.ANCIENT_PICKAXE);
    public static final RegistryObject<Item> ANCIENT_CHISEL = accessoryItem("ancient_chisel", AccessoryBuilder.ANCIENT_CHISEL);
    public static final RegistryObject<Item> MINER_GLOVES = accessoryItem("miner_gloves", AccessoryBuilder.MINER_GLOVES);
    public static final RegistryObject<Item> DWARF_MINER_RING = accessoryItem("dwarf_miner_ring", AccessoryBuilder.DWARF_MINER_RING);
    public static final RegistryObject<Item> POISON_VILE = accessoryItem("poison_vile", AccessoryBuilder.POISON_VILE);
    public static final RegistryObject<Item> LUCKY_HORSE_SHOE = accessoryItem("lucky_horse_shoe", AccessoryBuilder.NEGATE_FALL_DAMAGE);
    public static final RegistryObject<Item> POWER_CLAW = accessoryItem("power_claw", AccessoryBuilder.POWER_CLAW);
    public static final RegistryObject<Item> BLOOD_OF_BERSERKER = accessoryItem("blood_of_berserker", AccessoryBuilder.BLOOD_OF_BERSERKER);
    public static final RegistryObject<Item> THORNMAIL = accessoryItem("thornmail", AccessoryBuilder.THORNMAIL);
    public static final RegistryObject<Item> SPRING_LOCK = accessoryItem("spring_lock", AccessoryBuilder.SPRING_LOCK);
    public static final RegistryObject<Item> ANKH_ETERNITY = accessoryItem("ankh_eternity", AccessoryBuilder.ETERNAL_LIFE);
    public static final RegistryObject<Item> TWILIGHT_EDGE = accessoryItem("twilight_edge", AccessoryBuilder.TWILIGHT_EDGE);
    public static final RegistryObject<Item> DUSKBLADE = accessoryHandheldItem("duskblade", AccessoryItemGroup.UNIQUE, AccessoryBuilder.DUSKBLADE);
    public static final RegistryObject<Item> GHOUL_HEART = accessoryItem("ghoul_heart", AccessoryBuilder.GHOUL_HEART);
    public static final RegistryObject<Item> FORBIDDEN_EYE = accessoryItem("forbidden_eye", AccessoryBuilder.FORBIDDEN_EYE);

    public static final RegistryObject<Item> ANKH_CHARM = accessoryItem("ankh_charm", AccessoryBuilder.ANKH_CHARM); //
    public static final RegistryObject<Item> ANKH_SHIELD = accessoryItem("ankh_shield", AccessoryBuilder.ANKH_SHIELD); //

    public static final RegistryObject<Item> BROKEN_SHULKER_SHELL = accessoryItem("broken_shulker_shell", AccessoryBuilder.BROKEN_SHULKER_SHELL); //
    public static final RegistryObject<Item> ARMOR_PLATE = accessoryItem("armor_plate", AccessoryBuilder.ARMOR_PLATE); //
    public static final RegistryObject<Item> MASK_OF_BEWILDERED = accessoryItem("mask_of_bewildered", AccessoryBuilder.MASK_OF_BEWILDERED); //
    public static final RegistryObject<Item> CARBONIZED_BONE = accessoryItem("carbonized_bone", AccessoryBuilder.CARBONIZED_BONE); //
    public static final RegistryObject<Item> ADHESIVE_BANDAGES = accessoryItem("adhesive_bandage", AccessoryBuilder.ADHESIVE_BANDAGES);
    public static final RegistryObject<Item> CURSED_APPLE = accessoryItem("cursed_apple", AccessoryBuilder.CURSED_APPLE);
    public static final RegistryObject<Item> CURSED_BLINDFOLD = accessoryItem("cursed_blindfold", AccessoryBuilder.CURSED_BLINDFOLD);
    public static final RegistryObject<Item> SUNGLASSES = accessoryItem("sunglasses", AccessoryBuilder.SUNGLASSES);
    public static final RegistryObject<Item> VITAMINS = accessoryItem("vitamins", AccessoryBuilder.VITAMINS);
    public static final RegistryObject<Item> DREAM_CATCHER = accessoryItem("dream_catcher", AccessoryBuilder.DREAM_CATCHER);
    public static final RegistryObject<Item> CURSE_TORMENT = accessoryItem("cursed_torment", AccessoryBuilder.CURSE_TORMENT);
    public static final RegistryObject<Item> ANCIENT_CLOCK = accessoryItem("ancient_clock", AccessoryBuilder.ANCIENT_CLOCK);
    //
    public static final RegistryObject<Item> BLIGHT_SKULL = accessoryItem("blight_skull", AccessoryBuilder.BLIGHT_SKULL);
    public static final RegistryObject<Item> PROTECTED_SHADES = accessoryItem("protected_shades", AccessoryBuilder.PROTECTED_SHADES);
    public static final RegistryObject<Item> CARBONIZED_MASK_OF_BEWILDERED = accessoryItem("carbonized_mask_of_bewildered", AccessoryBuilder.CARBONIZED_MASK_OF_BEWILDERED);
    public static final RegistryObject<Item> HAUNTING_CURSE_BANDAGES = accessoryItem("haunting_curse_bandage", AccessoryBuilder.HAUNTING_CURSE_BANDAGES);
    public static final RegistryObject<Item> REFINED_SHULKER_SHELL = accessoryItem("refined_shulker_shell", AccessoryBuilder.REFINED_SHULKER_SHELL);

    public static final RegistryObject<Item> COBALT_SHIELD = accessoryItem("cobalt_shield", AccessoryBuilder.COBALT_SHIELD);
    public static final RegistryObject<Item> STEEL_SIGIL = accessoryItem("steel_sigil", AccessoryBuilder.STEEL_SIGIL);
    public static final RegistryObject<Item> OBSIDIAN_PLATE = accessoryItem("obsidian_plate", AccessoryBuilder.OBSIDIAN_PLATE);
    public static final RegistryObject<Item> OBSIDIAN_SHIELD = accessoryItem("obsidian_shield", AccessoryBuilder.OBSIDIAN_SHIELD);
    public static final RegistryObject<Item> OBSIDIAN_SIGIL = accessoryItem("obsidian_sigil", AccessoryBuilder.OBSIDIAN_SIGIL);
    public static final RegistryObject<Item> LIGHTNING_STONE = accessoryItem("lightning_stone", AccessoryBuilder.LIGHTNING_STONE);
    public static final RegistryObject<Item> POWER_GLOVES = accessoryItem("power_gloves", AccessoryBuilder.POWER_GLOVES);
    public static final RegistryObject<Item> AETHER_WISP = accessoryItem("aether_wisp", AccessoryBuilder.AETHER_WISP);
    public static final RegistryObject<Item> HEART_OF_THE_TREE = accessoryItem("heart_of_the_tree", AccessoryBuilder.HEART_OF_THE_TREE);
    public static final RegistryObject<Item> RECTRIX = accessoryItem("rectrix", AccessoryBuilder.RECTRIX);
    public static final RegistryObject<Item> FORTUNE_SHIKIGAMI = accessoryItem("fortune_shikigami", AccessoryBuilder.FORTUNE_SHIKIGAMI);
    public static final RegistryObject<Item> LEATHER_AGILITY = accessoryItem("leather_agility", AccessoryBuilder.LEATHER_AGILITY);
    public static final RegistryObject<Item> SWIFT_DAGGER = accessoryItem("swift_dagger", AccessoryBuilder.SWIFT_DAGGER);
    public static final RegistryObject<Item> STATIKK_DAGGER = accessoryItem("statikk_dagger", AccessoryBuilder.STATIKK_DAGGER);
    public static final RegistryObject<Item> MASK_OF_AGONY = accessoryItem("mask_of_agony", AccessoryBuilder.DESPAIR_AND_DEFY);
    public static final RegistryObject<Item> VITALITY_STONE = accessoryItem("vitality_stone", AccessoryBuilder.VITALITY_STONE);
    public static final RegistryObject<Item> CINDER_STONE = accessoryItem("cinder_stone", AccessoryItemGroup.IMMOLATE, AccessoryBuilder.CINDER_STONE);
    public static final RegistryObject<Item> INFERNO_HEART_STONE = accessoryItem("inferno_heart_stone", AccessoryItemGroup.IMMOLATE, AccessoryBuilder.INFERNO_HEART_STONE);
    public static final RegistryObject<Item> CRYSTALLIZED_PLATE = accessoryItem("crystallized_plate", AccessoryBuilder.CRYSTALLIZED_PLATE);
    public static final RegistryObject<Item> STEALTH_CLOAK = accessoryItem("stealth_cloak", AccessoryBuilder.STEALTH_CLOAK);
    public static final RegistryObject<Item> SAINT_DEMON_CROWN = accessoryItem("saint_demon_crown", Rarity.EPIC, AccessoryBuilder.SAINT_DEMON_CROWN);
    public static final RegistryObject<Item> ROYAL_CROWN = accessoryItem("royal_crown", Rarity.EPIC, AccessoryBuilder.ROYAL_CROWN);


    //

    public static final RegistryObject<Item> PLAYER_TRACKER = RegistryItems
            .register("player_tracker", PlayerTrackerItem::new)
            .properties(p -> p.rarity(Rarity.RARE))
            .tab(RegistryTabs.Category.MISC)
            .model(ItemModels.GENERATED)
            .register();

    public static final RegistryObject<Item> BLADE_OF_THE_ENDERLORD = RegistryItems
            .register("blade_of_the_enderlord", item -> ItemBuilder.BLADE_OF_THE_ENDERLORD.create(MeleeWeaponMaterials.TIER_ONE, item))
            .properties(p -> p.rarity(BHRarity.MYTHICAL))
            .tab(RegistryTabs.Category.COMBAT)
            .model(ItemModels.BIG_HANDHELD)
            .register();

    public static final RegistryObject<Item> GIANT_SLAYER_SWORD = RegistryItems
            .register("giant_slayer_sword", item -> ItemBuilder.GIANT_SLAYER_SWORD.create(MeleeWeaponMaterials.TIER_ONE, item))
            .properties(p -> p.rarity(BHRarity.MYTHICAL))
            .tab(RegistryTabs.Category.COMBAT)
            .model(ItemModels.BIG_HANDHELD)
            .register();

    public static final RegistryObject<Item> ZENITH = RegistryItems
            .register("zenith", item -> ItemBuilder.ZENITH.create(MeleeWeaponMaterials.TIER_ONE, item))
            .properties(p -> p.rarity(BHRarity.MYTHICAL))
            .tab(RegistryTabs.Category.COMBAT)
            .model(ItemModels.BIG_HANDHELD)
            .register();

    public static final RegistryObject<Item> ELUCIDATOR = RegistryItems
            .register("elucidator", item -> ItemBuilder.ELUCIDATOR.create(MeleeWeaponMaterials.TIER_ONE, item))
            .properties(p -> p.rarity(BHRarity.MYTHICAL))
            .tab(RegistryTabs.Category.COMBAT)
            .model(ItemModels.BIG_HANDHELD)
            .register();

    public static final RegistryObject<Item> DARK_REPULSER = RegistryItems
            .register("dark_repulser", item -> ItemBuilder.DARK_REPULSER.create(MeleeWeaponMaterials.TIER_ONE, item))
            .properties(p -> p.rarity(BHRarity.MYTHICAL))
            .tab(RegistryTabs.Category.COMBAT)
            .model(ItemModels.BIG_HANDHELD)
            .register();

    public static final RegistryObject<Item> GUARDIAN_SWORD = RegistryItems
            .register("guardian_sword", item -> ItemBuilder.GUARDIAN_SWORD.create(MeleeWeaponMaterials.TIER_ONE, item))
            .properties(p -> p.rarity(BHRarity.MYTHICAL))
            .tab(RegistryTabs.Category.COMBAT)
            .model(ItemModels.BIG32_HANDHELD)
            .register();

    public static final RegistryObject<Item> RADIANT = RegistryItems
            .register("radiant", item -> ItemBuilder.RADIANT.create(MeleeWeaponMaterials.TIER_ONE, item))
            .properties(p -> p.rarity(BHRarity.MYTHICAL))
            .tab(RegistryTabs.Category.COMBAT)
            .model(ItemModels.BIG32_HANDHELD)
            .register();

    public static final RegistryObject<Item> HARVESTER = RegistryItems
            .register("harvester", item -> ItemBuilder.HARVESTER.create(MeleeWeaponMaterials.TIER_ONE, item))
            .properties(p -> p.rarity(BHRarity.MYTHICAL))
            .tab(RegistryTabs.Category.COMBAT)
            .model(ItemModels.BIG32_HANDHELD)
            .register();

    public static final RegistryObject<Item> SOLARFLARE = RegistryItems
            .register("solarflare", item -> ItemBuilder.SOLARFLARE.create(MeleeWeaponMaterials.TIER_ONE, item))
            .properties(p -> p.rarity(BHRarity.MYTHICAL))
            .tab(RegistryTabs.Category.COMBAT)
            .model(ItemModels.BIG32_HANDHELD)
            .register();

    public static final RegistryObject<Item> STELLAR_AXE = RegistryItems
            .register("stellar_axe", item -> ItemBuilder.STELLAR_AXE.create(MeleeWeaponMaterials.TIER_ONE, item))
            .properties(p -> p.rarity(BHRarity.MYTHICAL))
            .tab(RegistryTabs.Category.COMBAT)
            .model(ItemModels.BIG32_INHAND)
            .register();

    public static final RegistryObject<Item> BLAZING_INFERNO_SPAWN_EGG = spawnEgg("blazing_inferno", BHEntity.BLAZING_INFERNO, Colors.combineRGB(255, 248, 71), Colors.combineRGB(139, 52, 1));
    public static final RegistryObject<Item> FAYE_FLARES_SPAWN_EGG = spawnEgg("faye_flares", BHEntity.FAYE_FLARES, Colors.combineRGB(255, 248, 71), Colors.combineRGB(182, 31, 0));
    public static final RegistryObject<Item> FAYE_WILDFIRE_SPAWN_EGG = spawnEgg("faye_wildfire", BHEntity.FAYE_WILDFIRE, Colors.combineRGB(255, 248, 71), Colors.combineRGB(31, 31, 31));
    public static final RegistryObject<Item> PYROLLIGER_SPAWN_EGG = spawnEgg("pyrolliger", BHEntity.PYROLLIGER, Colors.RED, Colors.YELLOW);
    public static final RegistryObject<Item> DRAGON_HORNET_SPAWN_EGG = spawnEgg("dragon_hornet", BHEntity.DRAGON_HORNET, Colors.YELLOW, Colors.ORANGE);


    private static RegistryObject<Item> spawnEgg(String entityName, RegistryObject entityType, int backgroundColor, int highlightColor) {
        String itemName = entityName + "_spawn_egg";
        return RegistryItems.register(itemName,item -> new ForgeSpawnEggItem(entityType, backgroundColor, highlightColor, new Item.Properties()))
                .tab(RegistryTabs.Category.SPAWN_EGG)
                .model(ItemModels.SPAWN_EGG)
                .register();
    }

    private static RegistryObject<Item> basicItem(String name, RegistryTabs.Category category) {
        return RegistryItems.register(name, BasicItem::new).tab(category).model(ItemModels.GENERATED).register();
    }

    private static RegistryObject<Item> woolFurItem(String name, RegistryTabs.Category category) {
        return RegistryItems.register(name, BasicItem::new).tab(category).tag(BHItemTags.WOOL_FUR).model(ItemModels.GENERATED).register();
    }

    private static RegistryObject<Item> swordItem(String name, MeleeWeaponMaterials materials, ItemBuilder.Factory<SwordBaseItem> factory) {
        return swordItem(name, materials, factory, ItemModels.HANDHELD);
    }

    private static RegistryObject<Item> swordItem(String name, MeleeWeaponMaterials materials, ItemBuilder.Factory<SwordBaseItem> factory, ItemModels itemModels) {
        return RegistryItems.register(name, properties -> factory.create(materials, properties)).tab(RegistryTabs.Category.COMBAT).model(itemModels).register();
    }

    private static RegistryObject<Item> toolItem(String name, MeleeWeaponMaterials materials, ItemBuilder.Factory<DiggerBaseItem> factory) {
        return toolItem(name, materials, factory, ItemModels.HANDHELD);
    }
    private static RegistryObject<Item> toolItem(String name, MeleeWeaponMaterials materials, ItemBuilder.Factory<DiggerBaseItem> builder, ItemModels itemModels) {
        return RegistryItems.register(name, properties -> builder.create(materials, properties)).tab(RegistryTabs.Category.TOOLS).model(itemModels).register();
    }
    private static RegistryObject<Item> armorItem(String name, ArmorItem.Type type, ArmorBaseMaterials armorMaterial) {
        return RegistryItems.register(name, properties -> new ArmorBaseItem(armorMaterial, type, properties)).tab(RegistryTabs.Category.COMBAT).model(ItemModels.GENERATED).register();
    }

    private static RegistryObject<Item> accessoryItemModel(String name, AccessoryBuilder accessoryBuilder) {
        return RegistryItems.register(name, item -> new AccessoryItem(AccessoryItemGroup.UNIQUE, item, accessoryBuilder))
                .tab(RegistryTabs.Category.ACCESSORY).tag(BHItemTags.ONLY_ACCESSORY).register();
    }

    private static RegistryObject<Item> accessoryItemModel(String name, AccessoryItemGroup group, AccessoryBuilder accessoryBuilder) {
        return RegistryItems.register(name, item -> new AccessoryItem(group,item, accessoryBuilder))
                .tab(RegistryTabs.Category.ACCESSORY).tag(BHItemTags.ONLY_ACCESSORY).register();
    }


    private static RegistryObject<Item> accessoryItem(String name, AccessoryItemGroup group, AccessoryBuilder accessoryBuilder) {
        return RegistryItems.register(name, item -> new AccessoryItem(group, item, accessoryBuilder))
                .tab(RegistryTabs.Category.ACCESSORY).model(ItemModels.ACCESSORY).tag(BHItemTags.ONLY_ACCESSORY).register();
    }

    private static RegistryObject<Item> accessoryBasicItem(String name, AccessoryBuilder accessoryBuilder) {
        return RegistryItems.register(name, item -> new AccessoryItem(AccessoryItemGroup.NONE, item, accessoryBuilder))
                .tab(RegistryTabs.Category.ACCESSORY).model(ItemModels.ACCESSORY).tag(BHItemTags.ONLY_ACCESSORY).register();
    }
    private static RegistryObject<Item> accessoryItem(String name, AccessoryBuilder accessoryBuilder) {
        return RegistryItems.register(name, item -> new AccessoryItem(AccessoryItemGroup.UNIQUE, item, accessoryBuilder))
                .tab(RegistryTabs.Category.ACCESSORY).model(ItemModels.ACCESSORY).tag(BHItemTags.ONLY_ACCESSORY).register();
    }

    private static RegistryObject<Item> accessoryItem(String name, AccessoryItemGroup group, Rarity rarity, AccessoryBuilder accessoryBuilder) {
        return RegistryItems.register(name, item -> new AccessoryItem(group, item, accessoryBuilder))
                .properties(properties -> properties.rarity(rarity))
                .tab(RegistryTabs.Category.ACCESSORY).model(ItemModels.ACCESSORY).tag(BHItemTags.ONLY_ACCESSORY).register();
    }

    private static RegistryObject<Item> accessoryItem(String name, Rarity rarity, AccessoryBuilder accessoryBuilder) {
        return RegistryItems.register(name, item -> new AccessoryItem(AccessoryItemGroup.UNIQUE, item, accessoryBuilder))
                .properties(properties -> properties.rarity(rarity))
                .tab(RegistryTabs.Category.ACCESSORY).model(ItemModels.ACCESSORY).tag(BHItemTags.ONLY_ACCESSORY).register();
    }

    private static RegistryObject<Item> accessoryHandheldItem(String name, AccessoryItemGroup group, AccessoryBuilder accessoryBuilder) {
        return RegistryItems.register(name, item -> new AccessoryItem(group, item, accessoryBuilder))
                .tab(RegistryTabs.Category.ACCESSORY).model(ItemModels.HANDHELD_ACCESSORY).tag(BHItemTags.ONLY_ACCESSORY).register();
    }

    private static RegistryObject<Item> accessoryHandheldItemX32(String name, AccessoryItemGroup group, AccessoryBuilder accessoryBuilder) {
        return RegistryItems.register(name, item -> new AccessoryItem(group, item, accessoryBuilder))
                .tab(RegistryTabs.Category.ACCESSORY).model(ItemModels.BIG_HANDHELD_ACCESSORY).tag(BHItemTags.ONLY_ACCESSORY).register();
    }

    private static RegistryObject<Item> accessoryHandheldItemX64(String name, AccessoryItemGroup group, AccessoryBuilder accessoryBuilder) {
        return RegistryItems.register(name, item -> new AccessoryItem(group, item, accessoryBuilder))
                .tab(RegistryTabs.Category.ACCESSORY).model(ItemModels.BIG32_HANDHELD_ACCESSORY).tag(BHItemTags.ONLY_ACCESSORY).register();
    }

    public static void register(IEventBus eventBus) {
        RegistryEntries.ITEMS.register(eventBus);
    }
}
