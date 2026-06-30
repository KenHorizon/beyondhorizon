package com.kenhorizon.beyondhorizon.server.api.accessory;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.kenhorizon.beyondhorizon.server.capability.Capabilities;
import com.kenhorizon.beyondhorizon.server.init.BHCapabilties;
import com.kenhorizon.beyondhorizon.server.item.base.AccessoryItem;
import com.kenhorizon.beyondhorizon.server.registry.BHRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.*;

public final class AccessoryHelper {
    private static final Map<String, UUID> UUIDS = new HashMap<>();

    public static LazyOptional<IAccessoryStackHandler> getInventory(Player player) {
        if (player != null) {
            return player.getCapability(BHCapabilties.ACCESSORY);
        } else {
            return LazyOptional.empty();
        }
    }
    public static LazyOptional<IAccessory> getAccessory(ItemStack stack) {
        return stack.getCapability(BHCapabilties.ACCESSORY_ITEM);
    }

    public static boolean getAccessory(Player player, Accessory accessory) {
        return getAllAccessory(player).contains(accessory);
    }

    private static List<Accessory> getAllAccessory(Player player) {
        List<Accessory> result = new ArrayList<>();
        if (!player.isAlive()) return result;
        AccessoryHelper.getInventory(player).ifPresent(handler -> {
            var stacks = handler.getStacks();
            for (int i = 0; i < handler.getStacks().getSlots(); i++) {
                ItemStack itemStack = stacks.getStackInSlot(i);
                if (!itemStack.isEmpty() && itemStack.getItem() instanceof IAccessoryItem caller) {
                    result.addAll(caller.getAccessories());
                }
            }
        });
        return result;
    }

    public static boolean checkAccessorySlot(Player player, Item item) {
        List<ItemStack> itemStacks = new ArrayList<>();
        ItemStack itemStack = item.getDefaultInstance();
        AccessoryHelper.getInventory(player).ifPresent(handler -> {
            var stacks = handler.getStacks();
            for (int i = 0; i < stacks.getSlots(); ++i) {
                ItemStack accessory = stacks.getStackInSlot(i);
                if (!accessory.isEmpty()) {
                    itemStacks.add(accessory);
                }
            }
        });
        return itemStacks.contains(itemStack);
    }

    public static List<Accessory> getAccessories(ItemStack itemStack) {
        if (!itemStack.isEmpty() && itemStack.getItem() instanceof IAccessoryItem container) {
            return container.getAccessories();
        }
        return List.of();
    }

    public static boolean checkAccessoryIsPresentInSlot(ItemStack outsideStack, IAccessoryStackHandler handler) {
        List<ItemStack> list = AccessoryHelper.getAccessoryItems(handler);
        boolean isValid = list.isEmpty();
        if (!list.isEmpty()) {
            for (ItemStack inSlotItemStack : list) {
                if (!inSlotItemStack.isEmpty() && inSlotItemStack.getItem() instanceof IAccessoryItem inSlotContainer) {

                    if (((AccessoryItem) inSlotContainer).noGroupItem()) {
                        isValid = true;
                    } else {
                        if (!(!ItemStack.isSameItem(inSlotItemStack, outsideStack) && inSlotContainer.isCompatible(inSlotItemStack, outsideStack))) {
                            return false;
                        }
                        if (ItemStack.isSameItem(inSlotItemStack, outsideStack)) {
                            isValid = false;
                            break;
                        }
                        isValid = !ItemStack.isSameItem(inSlotItemStack, outsideStack) && inSlotContainer.isCompatible(inSlotItemStack, outsideStack);
                    }
                }
            }
        }
        return isValid;
    }

    private static String getItemName(ItemStack itemStack) {
        return itemStack.getItem().getDescription().getString();
    }

    public static List<ItemStack> getAccessoryItems(IAccessoryStackHandler handler) {
        List<ItemStack> map = new ArrayList<>();
        var stacks = handler.getStacks();
        for (int i = 0; i < stacks.getSlots(); i++) {
            ItemStack stackInSlot = stacks.getStackInSlot(i);
            if (!stackInSlot.isEmpty() && (stackInSlot.getItem() instanceof IAccessoryItem)) {
                map.add(stackInSlot);
            }
        }
        return map;
    }

    public static CompoundTag storeAccessory(@Nullable ResourceLocation id) {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString("id",  String.valueOf((Object) id));
        return compoundTag;
    }

    @Nullable
    public static ResourceLocation getAccessoryId(Accessory accessory) {
        return BHRegistries.ACCESSORY_KEY.get().getKey(accessory);
    }

    @Nullable
    public static ResourceLocation getAccessoryId(CompoundTag tag) {
        return ResourceLocation.tryParse(tag.getString("id"));
    }

    public static UUID getSlotUuid(AccessorySlotContext context) {
        String key = context.identifier() + context.index();
        return UUIDS.computeIfAbsent(key, (k) -> UUID.nameUUIDFromBytes(k.getBytes()));
    }

    public static Multimap<Attribute, AttributeModifier> getAttributeModifiers(UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> multimap = HashMultimap.create();
        CompoundTag nbt = stack.getOrCreateTag();
        if ((!stack.isEmpty() && !nbt.isEmpty()) && nbt.contains(Accessory.ACCESSORY_ATTRIBUTES_TAGS, 9)) {
            ListTag nbtList = nbt.getList(Accessory.ACCESSORY_ATTRIBUTES_TAGS, 10);
            for (int i = 0; i < nbtList.size(); ++i) {
                CompoundTag tags = nbtList.getCompound(i);
                Optional<Attribute> optional = Optional.ofNullable(ForgeRegistries.ATTRIBUTES.getValue(ResourceLocation.tryParse(tags.getString("attribute_name"))));
                if (optional.isPresent()) {
                    AttributeModifier attributeModifier = AttributeModifier.load(tags);
                    if (attributeModifier != null && attributeModifier.getId().getLeastSignificantBits() != 0L && attributeModifier.getId().getMostSignificantBits() != 0L) {
                        multimap.put(optional.get(), attributeModifier);
                    }
                }
            }
        } else {
            multimap = AccessoryHelper.getAccessory(stack).map(accessory -> accessory.getAttributeModifiers(uuid, stack)).orElse(multimap);
        }
        return multimap;
    }
}
