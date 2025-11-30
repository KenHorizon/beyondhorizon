package com.kenhorizon.beyondhorizon.server.merchant;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.trading.MerchantOffer;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MerchantTrades {
    public static class BaseTradeItems implements ItemListing {
        private final ItemStack tradeItem1;
        private final ItemStack tradeItem2;
        private final ItemStack itemTrade;
        private final int maxUses;
        private final int villagerXp;
        private final float priceMultiplier;

        public BaseTradeItems(ItemStack tradeItem1, ItemStack tradeItem2, ItemStack offeredItemStack, int maxTrade, int villagerXp, float priceMultiplier) {
            this.tradeItem1 = tradeItem1;
            this.tradeItem2 = tradeItem2;
            this.itemTrade = offeredItemStack;
            this.maxUses = maxTrade;
            this.villagerXp = villagerXp;
            this.priceMultiplier = priceMultiplier;
        }

        public ItemStack getTradeItem1() {
            return this.tradeItem1;
        }

        public ItemStack getTradeItem2() {
            return this.tradeItem2;
        }

        public ItemStack getItemTrade() {
            return this.itemTrade;
        }

        public int getMaxUses() {
            return this.maxUses;
        }

        public int getVillagerXp() {
            return this.villagerXp;
        }

        public float getPriceMultiplier() {
            return this.priceMultiplier;
        }

        @Override
        public MerchantOffer getOffer(Entity trader, RandomSource randomSource) {
            return new MerchantOffer(getTradeItem1(), getTradeItem2(), getItemTrade(), getMaxUses(), getVillagerXp(), getPriceMultiplier());
        }
    }

    public static class EmeraldsForItems extends BaseTradeItems {
        public EmeraldsForItems(int emeraldCost, ItemStack offeredStack, int maxTrade, int villagerXp, float priceMultiplier) {
            super(new ItemStack(Items.EMERALD, emeraldCost), ItemStack.EMPTY, offeredStack, maxTrade, villagerXp, priceMultiplier);
        }
    }

    public static class EnchantedItemsForEmeralds extends EmeraldsForItems {

        public EnchantedItemsForEmeralds(int emeraldCost, ItemStack offeredStack, int maxTrade, int villagerXp, float priceMultiplier) {
            super(emeraldCost, offeredStack, maxTrade, villagerXp, priceMultiplier);
        }

        @Nullable
        @Override
        public MerchantOffer getOffer(Entity trader, RandomSource randomSource) {
            int randomizeCount = 5 + randomSource.nextInt(15);
            ItemStack itemstack = EnchantmentHelper.enchantItem(randomSource, new ItemStack(this.getItemTrade().getItem()), randomizeCount, false);
            int emeraldCost = Math.min(getTradeItem1().getCount() + randomizeCount, 64);
            return new MerchantOffer(new ItemStack(Items.EMERALD, emeraldCost), ItemStack.EMPTY, itemstack, getMaxUses(), getVillagerXp(), getPriceMultiplier());
        }
    }

    public static class RandomizedTradeItem {
        private final ItemStack wantItem1;
        private final ItemStack wantItem2;
        private final ItemStack tradeItemStack;
        private final int cost;
        private final int maxTrade;

        public RandomizedTradeItem(Item item1, Item item2, Item tradedItems, int cost, int maxTrade) {
            this.wantItem1 = new ItemStack(item1);
            this.wantItem2 = new ItemStack(item2);
            this.tradeItemStack = new ItemStack(tradedItems);
            this.cost = cost;
            this.maxTrade = maxTrade;
        }

        public RandomizedTradeItem(Item item1, Item item2, Item tradedItems) {
            this(item1, item2, tradedItems, 0, 0);
        }

        public RandomizedTradeItem(Item item1, Item item2, Item tradedItems, int maxTrade) {
            this(item1, item2, tradedItems, 0, maxTrade);
        }

        public RandomizedTradeItem(Item item, int cost, int maxTrade) {
            this(item, new ItemStack(Items.AIR).getItem(), new ItemStack(Items.AIR).getItem(), cost, maxTrade);
        }

        public RandomizedTradeItem(Item item, int cost) {
            this(item, new ItemStack(Items.AIR).getItem(), new ItemStack(Items.AIR).getItem(), cost, 0);
        }

        public ItemStack getWantItem1() {
            return this.wantItem1;
        }

        public ItemStack getWantItem2() {
            return this.wantItem2;
        }

        public ItemStack getTradeItemStack() {
            return this.tradeItemStack;
        }

        public int getCost() {
            return this.cost;
        }

        public int getMaxTrade() {
            return this.maxTrade;
        }
    }

    public static class RandomizedEmeraldsForItems implements ItemListing {
        protected List<RandomizedTradeItem> offeredItems;
        protected int maxUses;
        protected int xpGiven;    // Not to be confused with player XP
        protected float priceMultiplier;

        public RandomizedEmeraldsForItems(List<RandomizedTradeItem> items, int maxUses, int xpGiven, float priceMultiplier) {
            this.offeredItems = items;
            this.maxUses = maxUses;
            this.xpGiven = xpGiven;
            this.priceMultiplier = priceMultiplier;
        }

        @Override
        public MerchantOffer getOffer(Entity trader, RandomSource random) {
            RandomizedTradeItem offeredItem = offeredItems.get(offeredItems.size() == 1 ? 0 : random.nextInt(offeredItems.size() - 1));
            return new MerchantOffer(new ItemStack(Items.EMERALD, offeredItem.getCost()), offeredItem.getWantItem1(), offeredItem.getMaxTrade() > 0 ? offeredItem.getMaxTrade() : maxUses, xpGiven, priceMultiplier);
        }
    }

    public static class RandomizedItems implements ItemListing {
        protected List<RandomizedTradeItem> offeredItems;
        protected int maxUses;
        protected int xpGiven;    // Not to be confused with player XP
        protected float priceMultiplier;

        public RandomizedItems(List<RandomizedTradeItem> items, int maxUses, int xpGiven, float priceMultiplier) {
            this.offeredItems = items;
            this.maxUses = maxUses;
            this.xpGiven = xpGiven;
            this.priceMultiplier = priceMultiplier;
        }

        @Override
        public MerchantOffer getOffer(Entity trader, RandomSource random) {
            RandomizedTradeItem offeredItem = offeredItems.get(offeredItems.size() == 1 ? 0 : random.nextInt(offeredItems.size() - 1));
            return new MerchantOffer(offeredItem.getWantItem1(), offeredItem.getWantItem2(), offeredItem.getTradeItemStack(), offeredItem.getMaxTrade() > 0 ? offeredItem.getMaxTrade() : maxUses, xpGiven, priceMultiplier);
        }
    }

    public static class RandomizedEnchantedItemsForEmeralds extends RandomizedEmeraldsForItems {
        public RandomizedEnchantedItemsForEmeralds(List<RandomizedTradeItem> items, int maxUses, int xpGiven, float priceMultiplier) {
            super(items, maxUses, xpGiven, priceMultiplier);
        }

        @Override
        public MerchantOffer getOffer(Entity trader, RandomSource random) {
            RandomizedTradeItem offeredItem = offeredItems.get(random.nextInt(offeredItems.size() - 1));
            int level = 5 + random.nextInt(15);
            ItemStack enchantedStack = EnchantmentHelper.enchantItem(random, new ItemStack(offeredItem.getWantItem1().getItem()), level, false);
            return new MerchantOffer(new ItemStack(Items.EMERALD, offeredItem.getCost()), enchantedStack, maxUses, xpGiven, priceMultiplier);
        }
    }
}
