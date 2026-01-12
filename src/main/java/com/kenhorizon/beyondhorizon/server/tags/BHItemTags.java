package com.kenhorizon.beyondhorizon.server.tags;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class BHItemTags {
    public static final TagKey<Item> IGNORE_PIGLIN_HOSTILITY = create("ignore_piglin_hostility");
    public static final TagKey<Item> NOT_ALLOWED_IN_VOID_BAG = create("only_allowed_in_void_bag");
    public static final TagKey<Item> ONLY_ACCESSORY = create("only_accessory");
    public static final TagKey<Item> ONLY_HAMMER = create("only_hammer");
    public static final TagKey<Item> USING_RANGED_WEAPONS = create("using_ranged_weapons");
    public static final TagKey<Item> HEAVY_CROSSBOW = create("heavy_crossbow");
    public static final TagKey<Item> CROSSBOW = create("crossbow");
    public static final TagKey<Item> CAN_FLY = create("can_fly");
    public static final TagKey<Item> HEALING_ITEM = create("healing_item");
    public static final TagKey<Item> THROWABLE = create("throwable");
    public static final TagKey<Item> BOWS = create("bows");
    public static final TagKey<Item> OPS_TAGS = create("ops_tags");
    public static final TagKey<Item> CAN_LIT_BASIN = create("can_lit_basin");
    public static final TagKey<Item> DAGGER = create("dagger");
    public static final TagKey<Item> WOOL_FUR = create("wool_fur");

    public static TagKey<Item> create(String name) {
        return ItemTags.create(BeyondHorizon.resource(name));
    }
}