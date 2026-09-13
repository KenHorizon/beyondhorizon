package com.kenhorizon.beyondhorizon.server.init;

import com.kenhorizon.beyondhorizon.client.render.util.Colors;
import net.minecraft.ChatFormatting;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Rarity;

import java.awt.*;

public class BHRarity {
    public static final Rarity LEGENDARY = Rarity.create("beyondhorizon:legendary", style ->
            style.withColor(getPulsingColor(2000, Colors.GOLD, Colors.YELLOW)));

    public static final Rarity MYTHICAL = Rarity.create("beyondhorizon:mythical", style ->
            style.withColor(getPulsingColor(2000, Colors.RED, Colors.ORANGE)));

    public static final Rarity TRANSCENDENT = Rarity.create("beyondhorizon:transcendent", style ->
            style.withColor(Color.HSBtoRGB((System.currentTimeMillis() % 5000) / 5000F, 1F, 1F)));

    private static int getPulsingColor(long cycle, int color1, int color2) {

        float progress = (float)(Math.sin((System.currentTimeMillis() % cycle) / (double)cycle * 2.0 * Math.PI) + 1.0) / 2.0f;


        int R1 = (color1 >> 16) & 0xFF;
        int G1 = (color1 >> 8) & 0xFF;
        int B1 = color1 & 0xFF;
        int R2 = (color2 >> 16) & 0xFF;
        int G2 = (color2 >> 8) & 0xFF;
        int B2 = color2 & 0xFF;


        int r = (int) Mth.lerp(progress, R1, R2);
        int g = (int) Mth.lerp(progress, G1, G2);
        int b = (int) Mth.lerp(progress, B1, B2);

        return (r << 16) | (g << 8) | b;
    }
}
