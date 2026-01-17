package com.kenhorizon.beyondhorizon.server.api.bonus_set;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.init.BHItems;
import com.kenhorizon.beyondhorizon.server.util.Constant;
import net.minecraft.world.item.ItemStack;

public class ArmorBonusSets {
    private static final String WILDFIRE_ARMOR_SET_NAME = "wildfire_armor_set";
    public static final ArmorBonusSet WILDFIRE_ARMOR_SET = new ArmorBonusSetMagnitude(DefaultArmorBonus.WILDFIRE_INCREASED_DAMAGE, DefaultArmorBonus.WILDFIRE_SHOCKWAVE_DAMAGE, WILDFIRE_ARMOR_SET_NAME, 2, new ItemStack(BHItems.WILDFIRE_HELMET.get()), new ItemStack(BHItems.WILDFIRE_CHESTPLATE.get()), new ItemStack(BHItems.WILDFIRE_LEGGINGS.get()), new ItemStack(BHItems.WILDFIRE_BOOTS.get()));

    public static void register() {
        ArmorSetRegistry.register(WILDFIRE_ARMOR_SET);
    }
}
