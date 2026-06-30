package com.kenhorizon.beyondhorizon.server.util;

import com.kenhorizon.beyondhorizon.server.item.QuiverItem;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class QuiverHelper {
    public interface IQuiverHelper {
        public boolean isQuiver(ItemStack itemStack);
    }

    public static final Predicate<ItemStack> ARROW_QUIVER = (itemStack -> itemStack.is(ItemTags.ARROWS));

    public static ItemStack getQuiverStacks(Player player) {
        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = player.getInventory().getItem(i);
            if (!itemStack.isEmpty() && (itemStack.getItem() instanceof QuiverItem)) return itemStack;
        }
        return ItemStack.EMPTY;
    }

    public static List<ItemStack> findValidQuivers(Player player) {
        List<ItemStack> result = new ArrayList<ItemStack>();
        for (int i = 0; i < 9; i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (!stack.isEmpty() && (stack.getItem() instanceof QuiverItem)) result.add(stack);
        }
        return result;
    }
}
