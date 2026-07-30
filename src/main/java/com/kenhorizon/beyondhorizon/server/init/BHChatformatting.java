package com.kenhorizon.beyondhorizon.server.init;

import com.kenhorizon.beyondhorizon.client.render.util.Colors;
import net.minecraft.network.chat.Style;

public class BHChatformatting {
    public static final Style PHYSICAL_DAMAGE = Style.EMPTY.withColor(Colors.GOLD);
    public static final Style RAW_DAMAGE = Style.EMPTY.withColor(Colors.RED);
    public static final Style TRUE_DAMAGE = Style.EMPTY.withColor(Colors.WHITE);
    public static final Style CRITICAL_DAMAGE = Style.EMPTY.withColor(Colors.GOLD).withBold(true);
    public static final Style MAGIC_DAMAGE = Style.EMPTY.withColor(Colors.NAVY_BLUE);
    public static final Style HEAL = Style.EMPTY.withColor(Colors.GREEN);
    public static final Style EFFECTS = Style.EMPTY.withColor(Colors.CORAL);

    public static final Style MANA = Style.EMPTY.withColor(Colors.CYAN);
    public static final Style COOLDOWN = Style.EMPTY.withColor(Colors.LIGHT_GRAY);
}
