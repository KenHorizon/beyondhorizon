package com.kenhorizon.beyondhorizon.server.merchant;

import com.kenhorizon.beyondhorizon.server.init.BHItems;
import net.minecraft.world.entity.npc.VillagerTrades;

import java.util.ArrayList;
import java.util.List;

public class ClericTradesList {
    public static List<MerchantTrades.RandomizedTradeItem> LVL1_ITEMS = new ArrayList<>();
    public static List<MerchantTrades.RandomizedTradeItem> LVL2_ITEMS = new ArrayList<>();
    public static List<MerchantTrades.RandomizedTradeItem> LVL3_ITEMS = new ArrayList<>();
    public static List<MerchantTrades.RandomizedTradeItem> LVL4_ITEMS = new ArrayList<>();
    public static List<MerchantTrades.RandomizedTradeItem> LVL5_ITEMS = new ArrayList<>();
    public static VillagerTrades.ItemListing LVL1_TRADE = new MerchantTrades.RandomizedEmeraldsForItems(LVL1_ITEMS, 16, 2, 0.2F);
    public static VillagerTrades.ItemListing LVL2_TRADE = new MerchantTrades.RandomizedEmeraldsForItems(LVL2_ITEMS, 12, 2, 0.2F);
    public static VillagerTrades.ItemListing LVL3_TRADE = new MerchantTrades.RandomizedEmeraldsForItems(LVL3_ITEMS, 12, 2, 0.2F);
    public static VillagerTrades.ItemListing LVL4_TRADE = new MerchantTrades.RandomizedEmeraldsForItems(LVL4_ITEMS, 12, 2, 0.2F);
    public static VillagerTrades.ItemListing LVL5_TRADE = new MerchantTrades.RandomizedEmeraldsForItems(LVL5_ITEMS, 12, 2, 0.2F);

    static {
        LVL3_ITEMS = new ArrayList<MerchantTrades.RandomizedTradeItem>();
        addToListConditional(LVL3_ITEMS, new MerchantTrades.RandomizedTradeItem(BHItems.AMPLIFLYING_TOME.get(), 20, 12));
        LVL3_TRADE = new MerchantTrades.RandomizedEmeraldsForItems(LVL3_ITEMS, 12, 20, 0.2F);
        LVL3_ITEMS = new ArrayList<MerchantTrades.RandomizedTradeItem>();
        addToListConditional(LVL4_ITEMS, new MerchantTrades.RandomizedTradeItem(BHItems.PLAYER_TRACKER.get(), 20, 12));
        LVL4_TRADE = new MerchantTrades.RandomizedEmeraldsForItems(LVL4_ITEMS, 3, 20, 0.22F);
    }

    public static void addToListConditional(List<MerchantTrades.RandomizedTradeItem> list, MerchantTrades.RandomizedTradeItem item) {
        list.add(item);
    }
}
