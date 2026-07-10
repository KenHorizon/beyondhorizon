package com.kenhorizon.beyondhorizon.server.init;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.render.util.Colors;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Rarity;

import java.awt.*;

public class BHChatformatting {
    public static final Style PHYSICAL_DAMAGE = Style.EMPTY.withColor(Colors.combineRGB(255, 143, 52));
    public static final Style RAW_DAMAGE = Style.EMPTY.withColor(Colors.RED);
    public static final Style TRUE_DAMAGE = Style.EMPTY.withColor(Colors.WHITE);
    public static final Style CRITICAL_DAMAGE = Style.EMPTY.withColor(Colors.GOLD).withBold(true);
    public static final Style MAGIC_DAMAGE = Style.EMPTY.withColor(Colors.combineRGB(0, 179, 244));
    public static final Style HEAL = Style.EMPTY.withColor(Colors.GREEN);

    public static final Style MANA = Style.EMPTY.withColor(Colors.combineRGB(0, 179, 244));
    public static final Style COOLDOWN = Style.EMPTY.withColor(Colors.GRAY);
}
