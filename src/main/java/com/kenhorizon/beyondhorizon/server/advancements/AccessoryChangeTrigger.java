package com.kenhorizon.beyondhorizon.server.advancements;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.inventory.AccessoryStackHandler;
import com.kenhorizon.beyondhorizon.server.inventory.DynamicStackHandler;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.ItemLike;

import java.util.List;

public class AccessoryChangeTrigger extends SimpleCriterionTrigger<AccessoryChangeTrigger.TriggerInstance> {
    static final ResourceLocation ID = BeyondHorizon.resource("accessory_slot_changed");

    public ResourceLocation getId() {
        return ID;
    }

    public AccessoryChangeTrigger.TriggerInstance createInstance(JsonObject json,
                                                                 ContextAwarePredicate predicate,
                                                                 DeserializationContext context) {

        JsonObject slots = GsonHelper.getAsJsonObject(json, "slots", new JsonObject());
        MinMaxBounds.Ints occupied = MinMaxBounds.Ints.fromJson(slots.get("occupied"));
        MinMaxBounds.Ints full = MinMaxBounds.Ints.fromJson(slots.get("full"));
        MinMaxBounds.Ints empty = MinMaxBounds.Ints.fromJson(slots.get("empty"));
        ItemPredicate[] items = ItemPredicate.fromJsonArray(json.get("items"));
        return new AccessoryChangeTrigger.TriggerInstance(predicate, occupied, full, empty, items);
    }

    public void trigger(ServerPlayer player, AccessoryStackHandler inventory, ItemStack itemStack) {
        int empty = 0;
        int occupied = 0;

        for (int i = 0; i < inventory.getStacks().getSlots(); ++i) {
            ItemStack itemstack = inventory.getStacks().getStackInSlot(i);
            if (itemstack.isEmpty()) {
                ++empty;
            } else {
                ++occupied;
            }
        }

        this.trigger(player, inventory, itemStack, empty, occupied);
    }

    private void trigger(ServerPlayer player, AccessoryStackHandler inventory, ItemStack itemStack, int empty, int occupied) {
        this.trigger(player, (instance) -> {
            return instance.matches(inventory, itemStack, empty, occupied);
        });
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final MinMaxBounds.Ints slotsOccupied;
        private final MinMaxBounds.Ints slotsFull;
        private final MinMaxBounds.Ints slotsEmpty;
        private final ItemPredicate[] predicates;

        public TriggerInstance(ContextAwarePredicate player, MinMaxBounds.Ints slotsOccupied, MinMaxBounds.Ints slotsFull, MinMaxBounds.Ints slotsEmpty, ItemPredicate[] predicates) {
            super(AccessoryChangeTrigger.ID, player);
            this.slotsOccupied = slotsOccupied;
            this.slotsFull = slotsFull;
            this.slotsEmpty = slotsEmpty;
            this.predicates = predicates;
        }

        public static AccessoryChangeTrigger.TriggerInstance hasItems(ItemPredicate... pItems) {
            return new AccessoryChangeTrigger.TriggerInstance(ContextAwarePredicate.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, pItems);
        }

        public static AccessoryChangeTrigger.TriggerInstance hasItems(ItemLike... pItems) {
            ItemPredicate[] aitempredicate = new ItemPredicate[pItems.length];

            for(int i = 0; i < pItems.length; ++i) {
                aitempredicate[i] = new ItemPredicate((TagKey<Item>)null, ImmutableSet.of(pItems[i].asItem()), MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, EnchantmentPredicate.NONE, EnchantmentPredicate.NONE, (Potion)null, NbtPredicate.ANY);
            }

            return hasItems(aitempredicate);
        }

        public JsonObject serializeToJson(SerializationContext context) {
            JsonObject jsonobject = super.serializeToJson(context);
            if (!this.slotsOccupied.isAny() || !this.slotsFull.isAny() || !this.slotsEmpty.isAny()) {
                JsonObject jsonobject1 = new JsonObject();
                jsonobject1.add("occupied", this.slotsOccupied.serializeToJson());
                jsonobject1.add("full", this.slotsFull.serializeToJson());
                jsonobject1.add("empty", this.slotsEmpty.serializeToJson());
                jsonobject.add("slots", jsonobject1);
            }

            if (this.predicates.length > 0) {
                JsonArray jsonarray = new JsonArray();

                for(ItemPredicate itempredicate : this.predicates) {
                    jsonarray.add(itempredicate.serializeToJson());
                }

                jsonobject.add("items", jsonarray);
            }

            return jsonobject;
        }

        public boolean matches(AccessoryStackHandler inventory, ItemStack stack, int empty, int occupied) {
            if (!this.slotsEmpty.matches(empty)) {
                return false;
            } else if (!this.slotsOccupied.matches(occupied)) {
                return false;
            } else {
                int i = this.predicates.length;
                if (i == 0) {
                    return true;
                } else if (i != 1) {
                    List<ItemPredicate> list = new ObjectArrayList<>(this.predicates);
                    int containerSize = inventory.getStacks().getSlots();

                    for (int k = 0; k < containerSize; ++k) {
                        if (list.isEmpty()) {
                            return true;
                        }

                        ItemStack itemstack = inventory.getStacks().getStackInSlot(k);;
                        if (!itemstack.isEmpty()) {
                            list.removeIf((predicate) -> {
                                return predicate.matches(itemstack);
                            });
                        }
                    }

                    return list.isEmpty();
                } else {
                    return !stack.isEmpty() && this.predicates[0].matches(stack);
                }
            }
        }
    }
}