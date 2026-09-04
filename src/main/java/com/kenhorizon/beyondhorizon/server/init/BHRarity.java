package com.kenhorizon.beyondhorizon.server.init;

import com.kenhorizon.beyondhorizon.client.render.util.Colors;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Rarity;

import java.awt.*;

public class BHRarity {
    public static final Rarity LEGENDARY = Rarity.create("beyondhorizon:legendary", style -> style.withColor(0xFFDC16).withBold(true));
    public static final Rarity MYTHICAL = Rarity.create("beyondhorizon:mythical", style -> style.withColor(0xFF3636).withBold(true));
    public static final Rarity TRANSCENDENT = Rarity.create("beyondhorizon:transcendent", style -> style.withColor(Color.HSBtoRGB((System.currentTimeMillis() % 5000) / 5000F, 1F, 1F)).withBold(true));
}
