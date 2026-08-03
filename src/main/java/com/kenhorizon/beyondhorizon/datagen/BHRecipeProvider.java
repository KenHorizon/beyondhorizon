package com.kenhorizon.beyondhorizon.datagen;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.data.RecipeFacory;
import com.kenhorizon.beyondhorizon.datagen.recipes.WorkbenchRecipeProvider;
import com.kenhorizon.beyondhorizon.server.init.BHBlocks;
import com.kenhorizon.beyondhorizon.server.init.BHItems;
import com.kenhorizon.beyondhorizon.server.tags.BHItemTags;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.List;
import java.util.function.Consumer;

public class BHRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public BHRecipeProvider(PackOutput output) {
        super(output);
    }

    private void woolFurToWoolBlock(RecipeFacory recipeFactory, ItemLike builder, ItemLike output, int count) {
        recipeFactory.buildGrid(builder, output, count).save(recipeFactory.getConsumer(), this.getConversionRecipeNameTwoByTwo(output, builder));
    }
    private void woolFurToWoolBlock(RecipeFacory recipeFactory, ItemLike builder, ItemLike output) {
        this.woolFurToWoolBlock(recipeFactory, builder, output, 1);
    }

    protected String getConversionRecipeNameTwoByTwo(ItemLike result, ItemLike ingredient) {
        return String.format("%s_from_%s", getItemName(result), getItemName(ingredient));
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        RecipeFacory recipeFactory = new RecipeFacory(consumer);
        recipeFactory.createGrid(Items.IRON_NUGGET, BHItems.CHAIN_PLATE.get(), 1);

        this.woolFurToWoolBlock(recipeFactory, BHItems.WHITE_WOOL_FUR.get(), Blocks.WHITE_WOOL);
        this.woolFurToWoolBlock(recipeFactory, BHItems.ORANGE_WOOL_FUR.get(), Blocks.ORANGE_WOOL);
        this.woolFurToWoolBlock(recipeFactory, BHItems.MAGENTA_WOOL_FUR.get(), Blocks.MAGENTA_WOOL);
        this.woolFurToWoolBlock(recipeFactory, BHItems.LIGHT_BLUE_WOOL_FUR.get(), Blocks.LIGHT_BLUE_WOOL);
        this.woolFurToWoolBlock(recipeFactory, BHItems.YELLOW_WOOL_FUR.get(), Blocks.YELLOW_WOOL);
        this.woolFurToWoolBlock(recipeFactory, BHItems.LIME_WOOL_FUR.get(), Blocks.LIME_WOOL);
        this.woolFurToWoolBlock(recipeFactory, BHItems.PINK_WOOL_FUR.get(), Blocks.PINK_WOOL);
        this.woolFurToWoolBlock(recipeFactory, BHItems.GRAY_WOOL_FUR.get(), Blocks.GRAY_WOOL);
        this.woolFurToWoolBlock(recipeFactory, BHItems.LIGHT_GRAY_WOOL_FUR.get(), Blocks.LIGHT_GRAY_WOOL);
        this.woolFurToWoolBlock(recipeFactory, BHItems.CYAN_WOOL_FUR.get(), Blocks.CYAN_WOOL);
        this.woolFurToWoolBlock(recipeFactory, BHItems.PURPLE_WOOL_FUR.get(), Blocks.PURPLE_WOOL);
        this.woolFurToWoolBlock(recipeFactory, BHItems.BLUE_WOOL_FUR.get(), Blocks.BLUE_WOOL);
        this.woolFurToWoolBlock(recipeFactory, BHItems.BROWN_WOOL_FUR.get(), Blocks.BROWN_WOOL);
        this.woolFurToWoolBlock(recipeFactory, BHItems.GREEN_WOOL_FUR.get(), Blocks.GREEN_WOOL);
        this.woolFurToWoolBlock(recipeFactory, BHItems.RED_WOOL_FUR.get(), Blocks.RED_WOOL);
        this.woolFurToWoolBlock(recipeFactory, BHItems.BLACK_WOOL_FUR.get(), Blocks.BLACK_WOOL);
        //
        recipeFactory.createSword(BHItems.HELLSTONE_INGOT.get(), Items.STICK, BHItems.HELLSTONE_SWORD.get());
        recipeFactory.createPickaxe(BHItems.HELLSTONE_INGOT.get(), Items.STICK, BHItems.HELLSTONE_PICKAXE.get());
        recipeFactory.createAxe(BHItems.HELLSTONE_INGOT.get(), Items.STICK, BHItems.HELLSTONE_AXE.get());
        recipeFactory.createShovel(BHItems.HELLSTONE_INGOT.get(), Items.STICK, BHItems.HELLSTONE_SHOVEL.get());
        recipeFactory.createHoe(BHItems.HELLSTONE_INGOT.get(), Items.STICK, BHItems.HELLSTONE_HOE.get());

        recipeFactory.createSword(BHItems.STARITE_INGOT.get(), Items.STICK, BHItems.STARITE_SWORD.get());
        recipeFactory.createPickaxe(BHItems.STARITE_INGOT.get(), Items.STICK, BHItems.STARITE_PICKAXE.get());
        recipeFactory.createAxe(BHItems.STARITE_INGOT.get(), Items.STICK, BHItems.STARITE_AXE.get());
        recipeFactory.createShovel(BHItems.STARITE_INGOT.get(), Items.STICK, BHItems.STARITE_SHOVEL.get());
        recipeFactory.createHoe(BHItems.STARITE_INGOT.get(), Items.STICK, BHItems.STARITE_HOE.get());

        recipeFactory.createSword(BHItems.BLACK_IRON_INGOT.get(), Items.STICK, BHItems.BLACK_IRON_SWORD.get());
        recipeFactory.createPickaxe(BHItems.BLACK_IRON_INGOT.get(), Items.STICK, BHItems.BLACK_IRON_PICKAXE.get());
        recipeFactory.createAxe(BHItems.BLACK_IRON_INGOT.get(), Items.STICK, BHItems.BLACK_IRON_AXE.get());
        recipeFactory.createShovel(BHItems.BLACK_IRON_INGOT.get(), Items.STICK, BHItems.BLACK_IRON_SHOVEL.get());
        recipeFactory.createHoe(BHItems.BLACK_IRON_INGOT.get(), Items.STICK, BHItems.BLACK_IRON_HOE.get());
        //
        recipeFactory.createSword(BHItems.HOGLIN_TUSK.get(), Items.STICK, BHItems.HOGLIN_TUSK_SWORD.get());
        recipeFactory.createSword(BHItems.HOGLIN_TUSK.get(), BHItems.HANDLE.get(), BHItems.HOGLIN_TUSK_MACHETE.get());
        recipeFactory.createSpear(BHItems.HOGLIN_TUSK.get(), Items.STICK, BHItems.HOGLIN_TUSK_SPEAR.get());

        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS,BHBlocks.END_GREY_BRICKS.get(), BHBlocks.END_GREY_STONE.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS,BHBlocks.END_GREY_PILLAR.get(), BHBlocks.END_GREY_STONE.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS,BHBlocks.END_GREY_BRICK_SLAB.get(), BHBlocks.END_GREY_STONE.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS,BHBlocks.END_GREY_BRICK_STAIRS.get(), BHBlocks.END_GREY_STONE.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS,BHBlocks.END_GREY_BRICK_STAIRS.get(), BHBlocks.END_GREY_BRICKS.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS,BHBlocks.END_GREY_BRICK_SLAB.get(), BHBlocks.END_GREY_BRICKS.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS,BHBlocks.END_STONE_TILES.get(), Blocks.END_STONE);
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS,BHBlocks.CHISILLED_END_STONE.get(), Blocks.END_STONE);
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, BHBlocks.PLAIN_CHISILLED_END_STONE.get(), Blocks.END_STONE);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.STRING, 4)
                .requires(BHItemTags.WOOL_FUR)
                .unlockedBy("has_wool_fur",
                        inventoryTrigger(ItemPredicate.Builder.item().of(BHItemTags.WOOL_FUR).build()))
                .save(consumer, getItemName(Items.STRING) + "_from_wool_fur");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BHItems.BLACK_IRON_INGOT.get(),9)
                .requires(BHBlocks.BLACK_IRON_BLOCK.get())
                .unlockedBy("has_black_ingot_block",
                        inventoryTrigger(ItemPredicate.Builder.item().of(BHBlocks.BLACK_IRON_BLOCK.get()).build()))
                .save(consumer, getItemName(BHItems.BLACK_IRON_INGOT.get()) + "_from_" + getItemName(BHBlocks.BLACK_IRON_BLOCK.get()));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BHBlocks.TATTERED_BLACK_IRON_LATTICE.get())
                .requires(BHBlocks.BLACK_IRON_LATTICE.get())
                .unlockedBy("has_black_ingot_lattice",
                        inventoryTrigger(ItemPredicate.Builder.item().of(BHBlocks.BLACK_IRON_LATTICE.get()).build()))
                .save(consumer, getItemName(BHBlocks.BLACK_IRON_LATTICE.get()) + "_from_" + getItemName(BHBlocks.TATTERED_BLACK_IRON_LATTICE.get()));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BHBlocks.BLACK_IRON_LATTICE.get())
                .requires(BHBlocks.TATTERED_BLACK_IRON_LATTICE.get())
                .unlockedBy("has_black_ingot_lattice",
                        inventoryTrigger(ItemPredicate.Builder.item().of(BHBlocks.BLACK_IRON_LATTICE.get()).build()))
                .save(consumer, getItemName(BHBlocks.TATTERED_BLACK_IRON_LATTICE.get()) + "_from_" + getItemName(BHBlocks.BLACK_IRON_LATTICE.get()));

        recipeFactory.createBlock(BHItems.BLACK_IRON_INGOT.get(), BHBlocks.BLACK_IRON_BLOCK.get());

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BHItems.FLINT_KNIFE.get())
                .pattern("SF")
                .pattern("# ")
                .define('S', Items.STRING)
                .define('F', Items.FLINT)
                .define('#', Items.STICK)
                .unlockedBy("has_flint_knife_materials",
                        inventoryTrigger(ItemPredicate.Builder.item().of(Items.STICK, Items.FLINT, Items.STRING).build()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BHItems.VOID_BAG.get())
                .pattern("SNS")
                .pattern("BEB")
                .pattern("SAS")
                .define('E', Blocks.ENDER_CHEST)
                .define('B', Items.ECHO_SHARD)
                .define('N', Items.NETHER_STAR)
                .define('A', Items.END_CRYSTAL)
                .define('S', BHItems.STARITE_INGOT.get())
                .unlockedBy("has_void_bag",
                        inventoryTrigger(ItemPredicate.Builder.item().of(BHItems.VOID_BAG.get()).build()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BHItems.PLAYER_TRACKER.get())
                .pattern("NGN")
                .pattern("IRI")
                .pattern("INI")
                .define('G', Blocks.GLASS_PANE)
                .define('N', Items.NETHERITE_INGOT)
                .define('R', Items.REDSTONE)
                .define('I', Items.IRON_INGOT)
                .unlockedBy("has_player_tracker",
                        inventoryTrigger(ItemPredicate.Builder.item().of(BHItems.PLAYER_TRACKER.get()).build()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BHBlocks.BLACK_IRON_LATTICE.get())
                .pattern("N#N")
                .pattern("N#N")
                .pattern("N#N")
                .define('N', BHItems.BLACK_IRON_INGOT.get())
                .define('#', BHItems.BLACK_IRON_NUGGET.get())
                .unlockedBy("has_black_ingot",
                        inventoryTrigger(ItemPredicate.Builder.item().of(BHItems.BLACK_IRON_INGOT.get()).build()))
                .save(consumer);


        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BHBlocks.WORKBENCH.get())
                .pattern("PCP")
                .pattern("IAI")
                .pattern("PPP")
                .define('C', Blocks.CRAFTING_TABLE)
                .define('A', Blocks.ANVIL)
                .define('P', ItemTags.PLANKS)
                .define('I', Items.IRON_INGOT)
                .unlockedBy("has_crafting_table",
                        inventoryTrigger(ItemPredicate.Builder.item().of(Blocks.CRAFTING_TABLE).build()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BHItems.GOLD_RING.get())
                .pattern(" # ")
                .pattern("# #")
                .pattern(" # ")
                .define('#', Items.GOLD_INGOT)
                .unlockedBy("has_gold_ring",
                        inventoryTrigger(ItemPredicate.Builder.item().of(Items.GOLD_INGOT).build()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BHItems.RAW_EMBED_HELLSTONE.get())
                .requires(BHItems.RAW_HELLSTONE.get())
                .requires(Blocks.OBSIDIAN)
                .unlockedBy("has_raw_hellstone",
                        inventoryTrigger(ItemPredicate.Builder.item().of(BHItems.RAW_HELLSTONE.get()).build()))
                .save(consumer);

        WorkbenchRecipeProvider.create(BHItems.DORAN_BLADE.get(), 1)
                .required(Items.IRON_SWORD)
                .required(Items.GOLD_INGOT, 10)
                .required(BHItems.GOLD_RING.get())
                .save(consumer);

        WorkbenchRecipeProvider.create(BHItems.DORAN_BOW.get(), 1)
                .required(Items.BOW)
                .required(Items.GOLD_INGOT, 10)
                .required(BHItems.GOLD_RING.get())
                .save(consumer);

        WorkbenchRecipeProvider.create(BHItems.DORAN_HELM.get(), 1)
                .required(Items.IRON_HELMET)
                .required(Items.GOLD_INGOT, 10)
                .required(BHItems.GOLD_RING.get())
                .save(consumer);

        WorkbenchRecipeProvider.create(BHItems.DORAN_SHIELD.get(), 1)
                .required(Items.SHIELD)
                .required(Items.GOLD_INGOT, 10)
                .required(BHItems.GOLD_RING.get())
                .save(consumer);

        WorkbenchRecipeProvider.create(BHItems.DORAN_RING.get(), 1)
                .required(BHItems.RUBY.get())
                .required(Items.GOLD_INGOT, 10)
                .required(BHItems.GOLD_RING.get())
                .save(consumer);

        WorkbenchRecipeProvider.create(BHItems.QUIVER.get(), 1)
                .required(Items.LEATHER, 20)
                .required(Items.IRON_INGOT, 5)
                .required(Items.STRING, 5)
                .save(consumer);
        WorkbenchRecipeProvider.create(BHItems.NULL_MAGIC_RUNE.get(), 1)
                .required(Items.EMERALD, 20)
                .required(BHItems.RUBY.get(), 20)
                .save(consumer);
        WorkbenchRecipeProvider.create(BHItems.RUMINATIVE_BEADS.get(), 1)
                .required(Items.EMERALD, 20)
                .required(Items.LEAD, 6)
                .save(consumer);
        WorkbenchRecipeProvider.create(BHItems.TOUGH_CLOTH.get(), 1)
                .required(Items.LEATHER, 32)
                .save(consumer);
        WorkbenchRecipeProvider.create(BHItems.CHAIN_VEST.get(), 1)
                .required(BHItems.TOUGH_CLOTH.get())
                .required(BHItems.TOUGH_CLOTH.get())
                .required(BHItems.CHAIN_PLATE.get(), 20)
                .save(consumer);
        WorkbenchRecipeProvider.create(BHItems.VITALITY_STONE.get(), 1)
                .required(BHItems.RUBY.get(), 20)
                .save(consumer);
        WorkbenchRecipeProvider.create(BHItems.BOOTS.get(), 1)
                .required(Items.LEATHER, 5)
                .required(Items.IRON_NUGGET, 3)
                .save(consumer);
        WorkbenchRecipeProvider.create(BHItems.IRON_PLATED_BOOTS.get(), 1)
                .required(BHItems.BOOTS.get())
                .required(BHItems.CHAIN_PLATE.get(), 3)
                .save(consumer);
        WorkbenchRecipeProvider.create(BHItems.BERSERKER_BOOTS.get(), 1)
                .required(BHItems.BOOTS.get())
                .required(BHItems.SWIFT_DAGGER.get())
                .required(BHItems.SWIFT_DAGGER.get())
                .save(consumer);
        WorkbenchRecipeProvider.create(BHItems.AETHER_WISP.get(), 1)
                .required(BHItems.AMPLIFLYING_TOME.get(), 3)
                .save(consumer);
        WorkbenchRecipeProvider.create(BHItems.SPECTRAL_CLOAK.get(), 1)
                .required(BHItems.RUMINATIVE_BEADS.get())
                .required(BHItems.NULL_MAGIC_RUNE.get())
                .save(consumer);
        WorkbenchRecipeProvider.create(BHItems.CINDER_STONE.get(), 1)
                .required(BHItems.VITALITY_STONE.get())
                .required(BHItems.VITALITY_STONE.get())
                .required(BHItems.FIREFLY_FAYE.get())
                .save(consumer);
        WorkbenchRecipeProvider.create(BHItems.INFERNO_HEART_STONE.get(), 1)
                .required(BHItems.CINDER_STONE.get())
                .required(BHItems.ASHES_OF_FLAME.get())
                .save(consumer);

        WorkbenchRecipeProvider.create(BHItems.UNSTABLE_RUNIC_TOME.get(), 1)
                .required(BHItems.AMPLIFLYING_TOME.get())
                .required(BHItems.FIREFLY_FAYE.get())
                .required(BHItems.SAPPHIRE_CRYSTAL.get())
                .save(consumer);
        WorkbenchRecipeProvider.create(BHItems.SHEEN.get(), 1)
                .required(BHItems.BROKEN_HERO_SWORD.get())
                .required(BHItems.SAPPHIRE_CRYSTAL.get())
                .required(BHItems.SAPPHIRE_CRYSTAL.get())
                .save(consumer);
        WorkbenchRecipeProvider.create(BHItems.TWILIGHT_SWORD.get(), 1)
                .required(BHItems.SHEEN.get())
                .required(BHItems.MAGE_WAND.get())
                .required(BHItems.AMPLIFLYING_TOME.get())
                .required(BHItems.AETHER_WISP.get())
                .save(consumer);
        WorkbenchRecipeProvider.create(BHItems.CURSED_SKULL.get(), 1)
                .required(Items.ENDER_EYE, 20)
                .required(Items.GOLD_INGOT, 20)
                .required(BHItems.RUBY.get(), 10)
                .required(Blocks.OBSIDIAN, 10)
                .save(consumer);
        WorkbenchRecipeProvider.create(BHItems.FLAME_OF_TORMENT.get(), 1)
                .required(BHItems.CURSED_SKULL.get())
                .required(BHItems.ASHES_OF_FLAME.get())
                .save(consumer);
        WorkbenchRecipeProvider.create(BHItems.SPEAR_OF_CHAOS.get(), 1)
                .required(BHItems.HARPOON_HEAD.get())
                .required(BHItems.BROKEN_HERO_SWORD.get())
                .save(consumer);
        WorkbenchRecipeProvider.create(BHItems.STATIKK_DAGGER.get(), 1)
                .required(BHItems.SWIFT_DAGGER.get())
                .required(BHItems.LEATHER_AGILITY.get())
                .save(consumer);
        WorkbenchRecipeProvider.create(BHItems.MASK_OF_AGONY.get(), 1)
                .required(BHItems.ASHES_OF_FLAME.get())
                .required(BHItems.AETHER_WISP.get())
                .required(BHItems.CURSED_SKULL.get())
                .save(consumer);
        WorkbenchRecipeProvider.create(BHItems.STEEL_SIGIL.get(), 1)
                .required(BHItems.TOUGH_CLOTH.get())
                .required(BHItems.TOUGH_CLOTH.get())
                .required(Items.IRON_SWORD)
                .save(consumer);
        WorkbenchRecipeProvider.create(BHItems.OBSIDIAN_SIGIL.get(), 1)
                .required(BHItems.OBSIDIAN_PLATE.get())
                .required(BHItems.STEEL_SIGIL.get())
                .save(consumer);
        WorkbenchRecipeProvider.create(BHItems.OBSIDIAN_SHIELD.get(), 1)
                .required(BHItems.OBSIDIAN_PLATE.get())
                .required(BHItems.COBALT_SHIELD.get())
                .save(consumer);
        WorkbenchRecipeProvider.create(BHItems.REFINED_SHULKER_SHELL.get(), 1)
                .required(BHItems.BROKEN_SHULKER_SHELL.get())
                .required(BHItems.ARMOR_PLATE.get())
                .save(consumer);
        WorkbenchRecipeProvider.create(BHItems.CARBONIZED_MASK_OF_BEWILDERED.get(), 1)
                .required(BHItems.CARBONIZED_BONE.get())
                .required(BHItems.MASK_OF_BEWILDERED.get())
                .save(consumer);
        WorkbenchRecipeProvider.create(BHItems.HAUNTING_CURSE_BANDAGES.get(), 1)
                .required(BHItems.ADHESIVE_BANDAGES.get())
                .required(BHItems.CURSED_APPLE.get())
                .save(consumer);
        WorkbenchRecipeProvider.create(BHItems.PROTECTED_SHADES.get(), 1)
                .required(BHItems.SUNGLASSES.get())
                .required(BHItems.CURSED_BLINDFOLD.get())
                .save(consumer);
        WorkbenchRecipeProvider.create(BHItems.BLIGHT_SKULL.get(), 1)
                .required(BHItems.DREAM_CATCHER.get())
                .required(BHItems.CURSE_TORMENT.get())
                .save(consumer);
        WorkbenchRecipeProvider.create(BHItems.ANKH_CHARM.get(), 1)
                .required(BHItems.BLIGHT_SKULL.get())
                .required(BHItems.PROTECTED_SHADES.get())
                .required(BHItems.CARBONIZED_MASK_OF_BEWILDERED.get())
                .required(BHItems.HAUNTING_CURSE_BANDAGES.get())
                .required(BHItems.REFINED_SHULKER_SHELL.get())
                .required(BHItems.VITAMINS.get())
                .required(BHItems.ANCIENT_CLOCK.get())
                .save(consumer);
        WorkbenchRecipeProvider.create(BHItems.ANKH_SHIELD.get(), 1)
                .required(BHItems.ANKH_CHARM.get())
                .required(BHItems.OBSIDIAN_SHIELD.get())
                .save(consumer);
        WorkbenchRecipeProvider.create(BHItems.VOID_STAFF.get(), 1)
                .required(BHItems.CRYSTALLIZED_PLATE.get())
                .required(BHItems.MAGE_WAND.get())
                .save(consumer);
        WorkbenchRecipeProvider.create(BHItems.LESS_SATCTEL_OF_ELIXIR.get(), 1)
                .required(BHItems.RUBY.get(), 5)
                .required(Items.STRING, 5)
                .required(Items.LEATHER, 25)
                .required(Blocks.BREWING_STAND)
                .save(consumer);
        WorkbenchRecipeProvider.create(BHItems.SATCTEL_OF_ELIXIR.get(), 1)
                .required(BHItems.LESS_SATCTEL_OF_ELIXIR.get())
                .required(Items.DIAMOND, 32)
                .required(Items.GOLD_INGOT, 24)
                .save(consumer);
        WorkbenchRecipeProvider.create(BHItems.GREATER_SATCTEL_OF_ELIXIR.get(), 1)
                .required(BHItems.SATCTEL_OF_ELIXIR.get())
                .required(Items.NETHER_STAR, 2)
                .required(Items.NETHERITE_BLOCK)
                .save(consumer);

        WorkbenchRecipeProvider.create(BHItems.OMEGA_SATCTEL_OF_ELIXIR.get(), 1)
                .required(BHItems.GREATER_SATCTEL_OF_ELIXIR.get())
                .required(Items.NETHER_STAR, 2)
                .required(BHItems.STARITE_INGOT.get(), 15)
                .required(BHItems.LUMINITE_INGOT.get(), 15)
                .required(BHItems.SHADOWCRUST.get(), 5)
                .save(consumer);

        WorkbenchRecipeProvider.create(BHItems.FORTUNE_SHIKIGAMI.get(), 1)
                .required(Items.PAPER, 32)
                .required(Items.EMERALD, 32)
                .required(Items.GOLD_INGOT, 24)
                .required(Items.STRING, 22)
                .save(consumer);

        WorkbenchRecipeProvider.create(BHItems.FORTUNE_FAVOR.get(), 1)
                .required(BHItems.FORTUNE_SHIKIGAMI.get())
                .required(Items.EMERALD, 32)
                .save(consumer);

        WorkbenchRecipeProvider.create(BHItems.DAIKICHI.get(), 1)
                .required(BHItems.FORTUNE_FAVOR.get())
                .required(Items.EMERALD, 64)
                .save(consumer);

        oreSmeltings(consumer, List.of(BHItems.RAW_EMBED_HELLSTONE.get()), RecipeCategory.MISC, BHItems.HELLSTONE_INGOT.get(), 0.7F, Maths.sec(10));
        oreSmeltings(consumer, List.of(BHItems.RAW_STARITE.get()), RecipeCategory.MISC, BHItems.STARITE_INGOT.get(), 0.7F, Maths.sec(10));
        oreSmeltings(consumer, List.of(BHItems.RAW_LUMINITE.get()), RecipeCategory.MISC, BHItems.LUMINITE_INGOT.get(), 0.7F, Maths.sec(10));
        oreSmeltings(consumer, List.of(BHItems.RAW_BLACK_IRON.get()), RecipeCategory.MISC, BHItems.BLACK_IRON_INGOT.get(), 0.7F, Maths.sec(10));
        oreSmeltings(consumer, List.of(BHItems.RAW_CRIMSNITE.get()), RecipeCategory.MISC, BHItems.CRIMSNITE_INGOT.get(), 0.7F, Maths.sec(10));
        oreSmeltings(consumer, List.of(BHItems.BLACK_IRON_SWORD.get()), RecipeCategory.MISC, BHItems.BLACK_IRON_NUGGET.get(), 0.1F, Maths.sec(10));
        oreSmeltings(consumer, List.of(BHItems.BLACK_IRON_CLAYMORE.get()), RecipeCategory.MISC, BHItems.BLACK_IRON_NUGGET.get(), 0.1F, Maths.sec(10));
        oreSmeltings(consumer, List.of(BHItems.BLACK_IRON_SHOVEL.get()), RecipeCategory.MISC, BHItems.BLACK_IRON_NUGGET.get(), 0.1F, Maths.sec(10));
        oreSmeltings(consumer, List.of(BHItems.BLACK_IRON_PICKAXE.get()), RecipeCategory.MISC, BHItems.BLACK_IRON_NUGGET.get(), 0.1F, Maths.sec(10));
        oreSmeltings(consumer, List.of(BHItems.BLACK_IRON_AXE.get()), RecipeCategory.MISC, BHItems.BLACK_IRON_NUGGET.get(), 0.1F, Maths.sec(10));
        oreSmeltings(consumer, List.of(BHItems.BLACK_IRON_HOE.get()), RecipeCategory.MISC, BHItems.BLACK_IRON_NUGGET.get(), 0.1F, Maths.sec(10));

    }

    protected static void oreSmeltings(Consumer<FinishedRecipe> consumer, List<ItemLike> ingredients, RecipeCategory category, ItemLike result, float exp, int cookingTime) {
        smelting(consumer, ingredients, category, result, exp, cookingTime, "_from_smelting");
        blasting(consumer, ingredients, category, result, exp, cookingTime / 2, "_from_blasting");

    }

    protected static void woodSmelting(Consumer<FinishedRecipe> consumer, List<ItemLike> ingredients, RecipeCategory category, ItemLike result, float exp, int cookingTime) {
        smelting(consumer, ingredients, category, result, exp, cookingTime, "_from_smelting");
        smoker(consumer, ingredients, category, result, exp, cookingTime / 2, "_from_smoking");

    }

    protected static void smelting(Consumer<FinishedRecipe> consumer, String id, List<ItemLike> ingredients, RecipeCategory category, ItemLike result, float exp, int cookingTime, String group) {
        cooking(consumer, RecipeSerializer.SMELTING_RECIPE, ingredients, category, result, exp, cookingTime, group,  "_" + id + "_from_smelting");
    }

    protected static void smelting(Consumer<FinishedRecipe> consumer, List<ItemLike> ingredients, RecipeCategory category, ItemLike result, float exp, int cookingTime, String group) {
        cooking(consumer, RecipeSerializer.SMELTING_RECIPE, ingredients, category, result, exp, cookingTime, group, "_from_smelting");
    }

    protected static void blasting(Consumer<FinishedRecipe> consumer, List<ItemLike> ingredients, RecipeCategory category, ItemLike result, float exp, int cookingTime, String group) {
        cooking(consumer, RecipeSerializer.BLASTING_RECIPE, ingredients, category, result, exp, cookingTime, group, "_from_blasting");
    }

    protected static void smoker(Consumer<FinishedRecipe> consumer, List<ItemLike> ingredients, RecipeCategory category, ItemLike result, float exp, int cookingTime, String group) {
        cooking(consumer, RecipeSerializer.SMOKING_RECIPE, ingredients, category, result, exp, cookingTime, group, "_from_smoking");
    }

    protected static void campfire(Consumer<FinishedRecipe> consumer, List<ItemLike> ingredients, RecipeCategory category, ItemLike result, float exp, int cookingTime, String group) {
        cooking(consumer, RecipeSerializer.CAMPFIRE_COOKING_RECIPE, ingredients, category, result, exp, cookingTime, group, "_from_campfire");
    }

    protected static void cooking(Consumer<FinishedRecipe> consumer, RecipeSerializer<? extends AbstractCookingRecipe> serializer, List<ItemLike> ingredients, RecipeCategory category, ItemLike result, float exp, int cookingTime, String group, String recipeName) {
        for (ItemLike itemlike : ingredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), category, result, exp, cookingTime, serializer).group(group).unlockedBy(getHasName(itemlike), has(itemlike)).save(consumer, BeyondHorizon.ID + ":" + getItemName(result) + recipeName + "_" + getItemName(itemlike));
        }
    }
}
