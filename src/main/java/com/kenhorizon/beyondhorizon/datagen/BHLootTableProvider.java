package com.kenhorizon.beyondhorizon.datagen;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.libs.registry.RegistryBlocks;
import com.kenhorizon.libs.registry.RegistryEntries;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class BHLootTableProvider {
    public static LootTableProvider create(PackOutput output) {
        return new LootTableProvider(output, Set.of(),
                List.of(
                        new LootTableProvider.SubProviderEntry(Blocks::new, LootContextParamSets.BLOCK)
                ));
    }

    public static class Entity extends EntityLootSubProvider {

        public Entity() {
            super(FeatureFlags.REGISTRY.allFlags());
        }

        @Override
        public void generate() {

        }

        private LootItemEntityPropertyCondition.Builder hasProperties(LootContext.EntityTarget entityTarget, EntityPredicate.Builder predicateBuilder) {
            return LootItemEntityPropertyCondition.hasProperties(entityTarget, predicateBuilder);
        }
        private LootItemCondition.Builder dropRate(float chances) {
            return LootItemRandomChanceCondition.randomChance(chances);
        }
        private LootItemCondition.Builder randomChanceAndLootingBoost(float chances, float mulitplier) {
            return LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(chances, mulitplier);
        }

        private LootPool.Builder rolls(float rolls) {
            return LootPool.lootPool().setRolls(ConstantValue.exactly(rolls));
        }

        private LootItemCondition.Builder killedByPlayer() {
            return LootItemKilledByPlayerCondition.killedByPlayer();
        }

        private LootItemFunction.Builder lootingMultiplier() {
            return lootingMultiplier(0.0F, 1.0F);
        }

        private LootItemFunction.Builder lootingMultiplier(float min, float max) {
            return LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(min, max));
        }

        private SetItemCountFunction.Builder<?> setCount(float count) {
            return SetItemCountFunction.setCount(UniformGenerator.between(count, count));
        }

        private SetItemCountFunction.Builder<?> setCount(float min, float max) {
            return SetItemCountFunction.setCount(UniformGenerator.between(min, max));
        }

        private LootItem.Builder<?> item(ItemLike items) {
            return LootItem.lootTableItem(items);
        }

        @Override
        protected @NotNull Stream<EntityType<?>> getKnownEntityTypes() {
            return RegistryEntries.ENTITY_TYPES.getEntries().stream().map(RegistryObject::get);
        }
    }

    public static class Blocks extends BlockLootSubProvider {

        public static List<Block> ADD_DROPS = new ArrayList<>();
        public static List<Supplier<? extends Block>> ADD_DROP = new ArrayList<>();
        public static List<Supplier<? extends Block>> ADD_DROP_SELF = new ArrayList<>();
        public static List<Supplier<? extends Block>> ADD_DROP_DOOR = new ArrayList<>();
        public static List<Supplier<? extends Block>> ADD_DROP_SLAB = new ArrayList<>();
        public static List<Supplier<? extends Block>> ADD_LEAVES = new ArrayList<>();
        public static List<Supplier<? extends Block>> ADD_OAK_LEAVES = new ArrayList<>();
        public static Map<RegistryBlocks.OreDrops, RegistryBlocks.MinMax> ADD_ORE_DROP = new HashMap<>();
        public static Map<RegistryObject<? extends Block>, RegistryBlocks.MinMax> ORE_DROP_VALUE = new HashMap<>();

        protected static final LootItemCondition.Builder HAS_SILK_TOUCH = MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1))));
        protected static final LootItemCondition.Builder HAS_NO_SILK_TOUCH = HAS_SILK_TOUCH.invert();
        protected static final LootItemCondition.Builder HAS_SHEARS = MatchTool.toolMatches(ItemPredicate.Builder.item().of(Items.SHEARS));

        private static final LootItemCondition.Builder HAS_SHEARS_OR_SILK_TOUCH = HAS_SHEARS.or(HAS_SILK_TOUCH);
        private static final LootItemCondition.Builder HAS_NO_SHEARS_OR_SILK_TOUCH = HAS_SHEARS_OR_SILK_TOUCH.invert();

        protected static final float[] NORMAL_LEAVES_SAPLING_CHANCES = new float[]{0.05F, 0.0625F, 0.083333336F, 0.1F};
        private static final float[] NORMAL_LEAVES_STICK_CHANCES = new float[]{0.02F, 0.022222223F, 0.025F, 0.033333335F, 0.1F};

        protected Blocks() {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags());
        }

        @Override
        protected void generate() {
            BeyondHorizon.LOGGER.debug("{}", ADD_DROP_SELF);
            ADD_DROPS.forEach(this::dropSelf);
            ADD_DROP.forEach(add -> this.dropSelf(add.get()));
            ADD_DROP_SELF.forEach(add -> this.dropSelf(add.get()));
            ADD_ORE_DROP.forEach((provider, values) -> {
                this.add(provider.blocks().get(), properties -> createOreDrops(provider.blocks().get(), provider.items().get(), values.min(), values.max()));
            });
        }
        private LootTable.Builder createOreDrops(Block block, Item itemDrop, float min, float max) {
            return createSilkTouchDispatchTable(block, this.applyExplosionDecay(block, LootItem.lootTableItem(itemDrop)
                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max)))
                    .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
        }

        private LootTable.Builder createOreDrops(Block block, Item itemDrop) {
            return createSilkTouchDispatchTable(block, this.applyExplosionDecay(block, LootItem.lootTableItem(itemDrop)
                    .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
        }

        @Override
        protected @NotNull Iterable<Block> getKnownBlocks() {
            return RegistryEntries.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
        }
    }
}
