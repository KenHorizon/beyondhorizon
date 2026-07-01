package com.kenhorizon.beyondhorizon.client.render.misc.tooltips;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.FormattedCharSink;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.*;

@SuppressWarnings({"deprecation", "removal"})
public class Tooltips {
    public static final FormattedCharSequence SPACE = FormattedCharSequence.forward(" ", Style.EMPTY);
    //
    public static final String COMMAND_POINTS_FAILED = String.format("command.%s.role_class.points.failure", BeyondHorizon.ID);
    public static final String COMMAND_POINTS_SUCCESS = String.format("command.%s.role_class.points.succes", BeyondHorizon.ID);
    public static final String COMMAND_LEVEL_SET_SUCCESS = String.format("command.%s.role_class.level.succes", BeyondHorizon.ID);
    public static final String COMMAND_LEVEL_SET_FAILED = String.format("command.%s.role_class.level.failure", BeyondHorizon.ID);
    public static final String COMMAND_RESET_SUCCESS = String.format("command.%s.role_class.reset.succes", BeyondHorizon.ID);
    public static final String COMMAND_RESET_FAILED = String.format("command.%s.role_class.reset.failure", BeyondHorizon.ID);
    //
    public static final String ACCESSORY_LIMITED_TO = String.format("acessory.%s.limited_to", BeyondHorizon.ID);
    public static final String TOOLTIP_KEYBIND = String.format("tooltip.%s.keybind", BeyondHorizon.ID);
    public static final String TOOLTIP_ON_COOLDOWN = String.format("tooltip.%s.on_cooldown", BeyondHorizon.ID);
    public static final String TOOLTIP_COOLDOWN = String.format("tooltip.%s.cooldown", BeyondHorizon.ID);
    public static final String TOOLTIP_MANA_COST_PER_SECOND = String.format("tooltip.%s.mana_cost.per_second", BeyondHorizon.ID);
    public static final String TOOLTIP_MANA_COST_PERCENTAGES = String.format("tooltip.%s.mana_cost.percentage", BeyondHorizon.ID);
    public static final String TOOLTIP_MANA_COST = String.format("tooltip.%s.mana_cost", BeyondHorizon.ID);
    public static final String TOOLTIP_NOT_ENOUGH_MANA = String.format("tooltip.%s.not_enough_mana", BeyondHorizon.ID);

    public static final String BOSS_IS_DEFEATED = String.format("boss.%s.defeated", BeyondHorizon.ID);
    public static final String SKILL_TYPE = String.format("tooltip.%s.skill_type", BeyondHorizon.ID);
    public static final String TOOLTIP_BONUS_ARMOR_SET = String.format("tooltip.%s.bonus_armor_set", BeyondHorizon.ID);
    public static final String TOOLTIP_MINING_SPEED = String.format("tooltip.%s.mining_speed", BeyondHorizon.ID);
    public static final String TOOLTIP_ACCESSORY = String.format("item.%s.accessory", BeyondHorizon.ID);
    public static final String TOOLTIP_ACCESSORY_TYPE = String.format("item.%s.accessory.type", BeyondHorizon.ID);
    public static final String TOOLTIP_ACCESSORY_SKILL_TYPE = String.format("item.%s.accessory.skill_type", BeyondHorizon.ID);
    public static final String TOOLTIP_INVENTORY = String.format("tooltip.%s.inventory", BeyondHorizon.ID);
    public static final String TOOLTIP_HEALTH_RECOVERY_POTION = String.format("tooltip.%s.recovery_potion.health", BeyondHorizon.ID);
    public static final String TOOLTIP_MANA_RECOVERY_POTION = String.format("tooltip.%s.recovery_potion.mana", BeyondHorizon.ID);
    public static final String TOOLTIP_BUILTIN_RESOURCE = String.format("resourcepack.%s.game_art", BeyondHorizon.ID);
    public static final String TOOLTIP_IMMUNE_TO = String.format("tooltip.%s.immune_to", BeyondHorizon.ID);
    public static final String TOOLTIP_WORKBENCH = String.format("block.%s.workbench.name", BeyondHorizon.ID);
    public static final String TOOLTIP_WORKBENCH_FORGE = String.format("block.%s.workbench.forge", BeyondHorizon.ID);
    public static final String TOOLTIP_WORKBENCH_INGREDIENTS = String.format("block.%s.workbench.ingredients", BeyondHorizon.ID);
    public static final String TOOLTIP_WORKBENCH_ITEMS = String.format("block.%s.workbench.items", BeyondHorizon.ID);
    public static final String TOOLTIP_WORKBENCH_HELP_0 = String.format("block.%s.workbench.help.0", BeyondHorizon.ID);
    public static final String TOOLTIP_WORKBENCH_HELP_1 = String.format("block.%s.workbench.help.1", BeyondHorizon.ID);

    public static final ChatFormatting[] ATTRIBUTES = {ChatFormatting.DARK_GREEN, ChatFormatting.RED};
    public static final ChatFormatting[] ENCHANTMENT = {ChatFormatting.GOLD, ChatFormatting.RED};
    public static final ChatFormatting[] TOOLTIP = {ChatFormatting.GRAY, ChatFormatting.DARK_GRAY};


    public static ChatFormatting attributeColorFormat(double amount) {
        return amount > 0.0D ? ATTRIBUTES[0] : ATTRIBUTES[1];
    }

    public static ChatFormatting enchantmentTooltip(boolean isCurseEnchantment) {
        return isCurseEnchantment ? ENCHANTMENT[1] : ENCHANTMENT[0];
    }

    public static String getBossMessage(EntityType<?> entityType) {
        return String.format("boss.%s.defeated.%s", BeyondHorizon.ID, entityType.getDescriptionId());
    }

    public static String getBossMessage(EntityType<?> entityType, int line) {
        return String.format("boss.%s.%s.defeated.%s", BeyondHorizon.ID, entityType.getDescriptionId(), line);
    }
}
