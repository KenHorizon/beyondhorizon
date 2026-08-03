package com.kenhorizon.beyondhorizon.server.item.util;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.init.BHItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class IconUtils {
    public static ItemStack create(String location) {
        ItemStack stack = new ItemStack(BHItems.ICON_ITEMS.get());
        CompoundTag nbts = stack.getOrCreateTag();
        nbts.putString("IconLocation", String.format("%s:%s", BeyondHorizon.ID, location));
        return stack;
    }
}
