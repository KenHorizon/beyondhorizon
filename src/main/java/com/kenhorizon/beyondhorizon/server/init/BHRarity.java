package com.kenhorizon.beyondhorizon.server.init;

import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Rarity;

import java.awt.*;

public class BHRarity {
    public static final Rarity LEGENDARY = Rarity.create("LEGENDARY", ChatFormatting.GOLD);
    public static final Rarity MYTHICAL = Rarity.create("MYTHICAL", ChatFormatting.RED);
    public static final Rarity TRANSCENDENT = Rarity.create("TRANSCENDENT", style -> style.withColor(Color.HSBtoRGB((System.currentTimeMillis() % 5000) / 5000F, 1f, 1F)));
}
