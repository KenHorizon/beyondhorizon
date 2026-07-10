package com.kenhorizon.beyondhorizon.server.init;

import com.kenhorizon.beyondhorizon.client.render.util.Colors;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Rarity;

import java.awt.*;

public class BHRarity {

    public static final Rarity RARITY_SWEET = Rarity.create("alexscaves:sweet", style -> style.withColor(0XFF8ACD));
    public static final Rarity LEGENDARY = Rarity.create("beyondhorizon:legendary", ChatFormatting.GOLD);
    public static final Rarity MYTHICAL = Rarity.create("beyondhorizon:mythical", style -> style.withColor(Colors.combineRGB(255, 81, 81)));
    public static final Rarity TRANSCENDENT = Rarity.create("beyondhorizon:transcendent", style -> style.withColor(Color.HSBtoRGB((System.currentTimeMillis() % 5000) / 5000F, 1f, 1F)));
}
