package com.kenhorizon.beyondhorizon.server.init;

import com.kenhorizon.beyondhorizon.client.render.util.ColorUtil;
import com.kenhorizon.beyondhorizon.server.api.accessory.AccessoryBuilder;
import com.kenhorizon.beyondhorizon.server.api.accessory.AccessoryItemGroup;
import com.kenhorizon.beyondhorizon.server.item.BasicItem;
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

    public static final RegistryObject<Item> HANDLE = basicItem("handle", RegistryTabs.Category.INGREDIENTS);
    public static final RegistryObject<Item> FLAME_CELL = basicItem("flame_cell", RegistryTabs.Category.INGREDIENTS);
    public static final RegistryObject<Item> WILDFIRE_FRAGMENT = basicItem("wildfire_fragment", RegistryTabs.Category.INGREDIENTS);
    public static final RegistryObject<Item> RAW_LUMINITE = basicItem("raw_luminite", RegistryTabs.Category.INGREDIENTS);
    public static final RegistryObject<Item> RAW_STARITE = basicItem("raw_starite", RegistryTabs.Category.INGREDIENTS);
    public static final RegistryObject<Item> RAW_HELLSTONE = basicItem("raw_hellstone", RegistryTabs.Category.INGREDIENTS);
    public static final RegistryObject<Item> RAW_EMBED_HELLSTONE = basicItem("raw_embed_hellstone", RegistryTabs.Category.INGREDIENTS);
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

    public static final RegistryObject<Item> STARITE_SWORD = swordItem("starite_sword", MeleeWeaponMaterials.STARITE, ItemBuilder.SWORD);
    public static final RegistryObject<Item> STARITE_AXE = toolItem("starite_axe", MeleeWeaponMaterials.STARITE, ItemBuilder.AXE);
    public static final RegistryObject<Item> STARITE_PICKAXE = toolItem("starite_pickaxe", MeleeWeaponMaterials.STARITE, ItemBuilder.PICKAXE);
    public static final RegistryObject<Item> STARITE_SHOVEL = toolItem("starite_shovel", MeleeWeaponMaterials.STARITE, ItemBuilder.SHOVEL);
    public static final RegistryObject<Item> STARITE_HOE = toolItem("starite_hoe", MeleeWeaponMaterials.STARITE, ItemBuilder.HOE);

    //
    public static final RegistryObject<Item> TOUGH_CLOTH = accessoryItem("tough_cloth", AccessoryBuilder.TOUGH_CLOTH);
    public static final RegistryObject<Item> NULL_MAGIC_RUNE = accessoryItem("null_magic_rune", AccessoryBuilder.NULL_MAGIC_RUNE);
    public static final RegistryObject<Item> FIREFLY_FAYE = accessoryItem("firefly_faye", AccessoryBuilder.FIREFLY_FAYE);
    public static final RegistryObject<Item> SAPPHIRE_CRYSTAL = accessoryItem("sapphire_crystal", AccessoryBuilder.SAPPHIRE_CRYSTAL);
    public static final RegistryObject<Item> CHAIN_VEST = accessoryItem("chain_vest", AccessoryBuilder.CHAIN_VEST);
    public static final RegistryObject<Item> RUMINATIVE_BEADS = accessoryItem("ruminative_beads", AccessoryBuilder.RUMINATIVE_BEADS);
    public static final RegistryObject<Item> SPECTRAL_CLOAK = accessoryItem("spectral_cloak", AccessoryBuilder.SPECTRAL_CLOAK);
    public static final RegistryObject<Item> UNSTABLE_RUNIC_TOME = accessoryItem("unstable_runic_tome", AccessoryBuilder.UNSTABLE_RUNIC_TOME);
    public static final RegistryObject<Item> BROKEN_HERO_SWORD = accessoryItem("broken_hero_sword", AccessoryItemGroup.HERO_SWORD, AccessoryBuilder.BROKEN_HERO_SWORD);
    public static final RegistryObject<Item> TRUE_HERO_SWORD = accessoryItem("true_hero_sword", AccessoryItemGroup.HERO_SWORD, AccessoryBuilder.TRUE_HERO_SWORD);
    public static final RegistryObject<Item> INFINITY_SWORD = accessoryItem("infinity_sword", AccessoryItemGroup.HERO_SWORD, AccessoryBuilder.INFINITY_SWORD);
    public static final RegistryObject<Item> NULL_SWORD = accessoryItem("null_sword", AccessoryItemGroup.HERO_SWORD, AccessoryBuilder.NULL_SWORD);
    public static final RegistryObject<Item> ASCENDED_HERO_SWORD = accessoryItem("ascended_hero_sword", AccessoryItemGroup.HERO_SWORD, AccessoryBuilder.ASCENDED_HERO_SWORD);
    public static final RegistryObject<Item> DARK_ESSESNCE_CRYSTAL = accessoryItem("dark_essence_crystal", AccessoryBuilder.MAGICAL_OPS);
    public static final RegistryObject<Item> BOOTS = accessoryItem("boots", AccessoryItemGroup.BOOTS, AccessoryBuilder.GOLDEN_BOOTS);
    public static final RegistryObject<Item> GOLDEN_BOOTS = accessoryItem("golden_boots", AccessoryItemGroup.BOOTS, AccessoryBuilder.BASIC_BOOTS);
    public static final RegistryObject<Item> BERSERKER_BOOTS = accessoryItem("berserker_boots", AccessoryItemGroup.BOOTS, AccessoryBuilder.BERSERKER_BOOTS);
    public static final RegistryObject<Item> IRON_PLATED_BOOTS = accessoryItem("iron_plated_boots", AccessoryItemGroup.BOOTS, AccessoryBuilder.IRON_PLATED_BOOTS);
    public static final RegistryObject<Item> MINER_BOOTS = accessoryItem("miner_boots", AccessoryItemGroup.BOOTS, AccessoryBuilder.MINER_BOOTS);
    public static final RegistryObject<Item> BOOK_OF_KNOWLEDGE = accessoryItem("book_of_knowledge", AccessoryItemGroup.NONE, AccessoryBuilder.BOOK_OF_KNOWLEDGE);
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

    public static final RegistryObject<Item> BROKEN_SHULKER_SHELL = accessoryItem("broken_shulker_shell", AccessoryBuilder.BROKEN_SHULKER_SHELL);
    public static final RegistryObject<Item> ARMOR_PLATE = accessoryItem("armor_plate", AccessoryBuilder.ARMOR_PLATE);
    public static final RegistryObject<Item> VITAMINS = accessoryItem("vitamins", AccessoryBuilder.VITAMINS);
    public static final RegistryObject<Item> MASK_OF_BEWILDERED = accessoryItem("mask_of_bewildered", AccessoryBuilder.MASK_OF_BEWILDERED);
    public static final RegistryObject<Item> CURSED_BLINDFOLD = accessoryItem("cursed_blindfold", AccessoryBuilder.CURSED_BLINDFOLD);
    public static final RegistryObject<Item> CARBONIZED_BONE = accessoryItem("carbonized_bone", AccessoryBuilder.CARBONIZED_BONE);
    public static final RegistryObject<Item> ADHESIVE_BANDAGES = accessoryItem("adhesive_bandage", AccessoryBuilder.ADHESIVE_BANDAGES);

    public static final RegistryObject<Item> POWER_GLOVES = accessoryItem("power_gloves", AccessoryBuilder.POWER_GLOVES);
    public static final RegistryObject<Item> SWIFT_DAGGER = accessoryItem("swift_dagger", AccessoryBuilder.SWIFT_DAGGER);
    public static final RegistryObject<Item> AETHER_WISP = accessoryItem("aether_wisp", AccessoryBuilder.AETHER_WISP);
    public static final RegistryObject<Item> HEART_OF_THE_TREE = accessoryItem("heart_of_the_tree", AccessoryBuilder.HEART_OF_THE_TREE);
    public static final RegistryObject<Item> RECTRIX = accessoryItem("rectrix", AccessoryBuilder.RECTRIX);
    public static final RegistryObject<Item> FORTUNE_SHIKIGAMI = accessoryItem("fortune_shikigami", AccessoryBuilder.FORTUNE_SHIKIGAMI);
    public static final RegistryObject<Item> LEATHER_AGILITY = accessoryItem("leather_agility", AccessoryBuilder.LEATHER_AGILITY);
    public static final RegistryObject<Item> AGILE_DAGGER = accessoryItem("agile_dagger", AccessoryBuilder.AGILE_DAGGER);
    public static final RegistryObject<Item> MASK_OF_AGONY = accessoryItem("mask_of_agony", AccessoryBuilder.DESPAIR_AND_DEFY);
    public static final RegistryObject<Item> VITALITY_STONE = accessoryItem("vitality_stone", AccessoryBuilder.VITALITY_STONE);
    public static final RegistryObject<Item> CINDER_STONE = accessoryItem("cinder_stone", AccessoryBuilder.CINDER_STONE);
    public static final RegistryObject<Item> CRYSTALLIZED_PLATE = accessoryItem("crystallized_plate", AccessoryBuilder.CRYSTALLIZED_PLATE);

    //

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

    public static RegistryObject<Item> swordItem(String name, MeleeWeaponMaterials materials, ItemBuilder.Factory<SwordBaseItem> factory) {
        return swordItem(name, materials, factory, ItemModels.HANDHELD);
    }

    public static RegistryObject<Item> toolItem(String name, MeleeWeaponMaterials materials, ItemBuilder.Factory<DiggerBaseItem> factory) {
        return toolItem(name, materials, factory, ItemModels.HANDHELD);
    }

    public static RegistryObject<Item> swordItem(String name, MeleeWeaponMaterials materials, ItemBuilder.Factory<SwordBaseItem> builder, ItemModels itemModels) {
        return RegistryItems.register(name, properties -> builder.create(materials, properties)).tab(RegistryTabs.Category.COMBAT).model(itemModels).register();
    }
    public static RegistryObject<Item> toolItem(String name, MeleeWeaponMaterials materials, ItemBuilder.Factory<DiggerBaseItem> builder, ItemModels itemModels) {
        return RegistryItems.register(name, properties -> builder.create(materials, properties)).tab(RegistryTabs.Category.TOOLS).model(itemModels).register();
    }
    public static RegistryObject<Item> armorItem(String name, ArmorItem.Type type, ArmorBaseMaterials armorMaterial) {
        return RegistryItems.register(name, properties -> new ArmorBaseItem(armorMaterial, type, properties)).tab(RegistryTabs.Category.COMBAT).model(ItemModels.GENERATED).register();
    }

    private static RegistryObject<Item> accessoryItem(String name, AccessoryItemGroup group, AccessoryBuilder accessoryBuilder) {
        return RegistryItems.register(name, item -> new AccessoryItem(group, item, accessoryBuilder)).tab(RegistryTabs.Category.ACCESSORY).model(ItemModels.ACCESSORY).tag(BHItemTags.ONLY_ACCESSORY).register();
    }

    private static RegistryObject<Item> accessoryItem(String name, AccessoryBuilder accessoryBuilder) {
        return RegistryItems.register(name, item -> new AccessoryItem(item, accessoryBuilder)).tab(RegistryTabs.Category.ACCESSORY).model(ItemModels.ACCESSORY).tag(BHItemTags.ONLY_ACCESSORY).register();
    }

    public static void register(IEventBus eventBus) {
        RegistryEntries.ITEMS.register(eventBus);
    }
}
