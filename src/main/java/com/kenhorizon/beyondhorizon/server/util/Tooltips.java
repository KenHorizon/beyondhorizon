package com.kenhorizon.beyondhorizon.server.util;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import net.minecraft.ChatFormatting;

public class Tooltips {
    //
    public static final String SKILL_TYPE = String.format("tooltip.%s.skill_type", BeyondHorizon.ID);

    public static final String TOOLTIP_PREFIX = String.format("tooltip.%s.", BeyondHorizon.ID);
    public static final ChatFormatting[] ATTRIBUTES = {ChatFormatting.DARK_GREEN, ChatFormatting.RED};
    public static final ChatFormatting[] ENCHANTMENT = {ChatFormatting.GOLD, ChatFormatting.RED};
    public static final ChatFormatting[] TOOLTIP = {ChatFormatting.GRAY, ChatFormatting.DARK_GRAY};

    public static ChatFormatting attributeColorFormat(double amount) {
        return amount > 0.0D ? ATTRIBUTES[0] : ATTRIBUTES[1];
    }

    public static ChatFormatting enchantmentTooltip(boolean isCurseEnchantment) {
        return isCurseEnchantment ? ENCHANTMENT[1] : ENCHANTMENT[0];
    }

}
