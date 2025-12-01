package com.kenhorizon.beyondhorizon.server.skills.accessory;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.capability.CapabilityCaller;
import com.kenhorizon.beyondhorizon.server.init.BHCapabilties;
import com.kenhorizon.beyondhorizon.server.skills.ISkillItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class AccessoryHelper {
    public static boolean getAccessory(Player player, Accessory accessory) {
        return getAllAccessory(player).contains(accessory);
    }

    private static List<Accessory> getAllAccessory(Player player) {
        List<Accessory> result = new ArrayList<>();
        if (!player.isAlive()) return result;
        IAccessoryItemHandler handler = CapabilityCaller.accessory(player);
        if (handler == null) return result;
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack itemStack = handler.getStackInSlot(i);
            if (!itemStack.isEmpty() && itemStack.getItem() instanceof IAccessoryItems<?> caller) {
                result.addAll(caller.getAllAccessory());
            }
        }
        return result;
    }

    public static boolean checkAccessorySlot(Player player, Item item) {
        List<ItemStack> itemStacks = new ArrayList<>();
        ItemStack itemStack = item.getDefaultInstance();
        IAccessoryItemHandler handler = CapabilityCaller.accessory(player);
        for (int i = 0; i < handler.getSlots(); ++i) {
            ItemStack accessory = handler.getStackInSlot(i);
            if (!accessory.isEmpty()) {
                itemStacks.add(accessory);
            }
        }
        return itemStacks.contains(itemStack);
    }

    public static List<Accessory> getAccessories(ItemStack itemStack) {
        if (!itemStack.isEmpty() && itemStack.getItem() instanceof IAccessoryItems<?> container) {
            return container.getAllAccessory();
        }
        return List.of();
    }

    public static boolean checkAccessoryIsPresentInSlot(ItemStack outsideStack, IAccessoryItemHandler handler) {
        List<ItemStack> list = AccessoryHelper.getAccessoryItems(handler);
        boolean isValid = list.isEmpty();
        if (!list.isEmpty()) {
            for (int index = 0; index < list.size(); index++) {
                ItemStack inSlotItemStack = list.get(index);
                if (!inSlotItemStack.isEmpty() && inSlotItemStack.getItem() instanceof IAccessoryItems<?> inSlotContainer) {
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
        return isValid;
    }

    private static String getItemName(ItemStack itemStack) {
        return itemStack.getItem().getDescription().getString();
    }

    public static List<ItemStack> getAccessoryItems(IAccessoryItemHandler handler) {
        List<ItemStack> map = new ArrayList<>();
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack stackInSlot = handler.getStackInSlot(i);
            if (!stackInSlot.isEmpty() && (stackInSlot.getItem() instanceof IAccessoryItems<?>)) {
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
        return Accessories.SUPPLIER_KEY.get().getKey(accessory);
    }

    @Nullable
    public static ResourceLocation getAccessoryId(CompoundTag tag) {
        return ResourceLocation.tryParse(tag.getString("id"));
    }
}
