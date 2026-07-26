package com.kenhorizon.beyondhorizon.client.render.misc.tooltips;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.EntityType;

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
    public static final String KEYBINDS = String.format("tooltip.%s.keybind", BeyondHorizon.ID);
    public static final String ON_COOLDOWN = String.format("tooltip.%s.on_cooldown", BeyondHorizon.ID);
    public static final String COOLDOWN = String.format("tooltip.%s.cooldown", BeyondHorizon.ID);
    public static final String AMMO_COLLECT = String.format("tooltip.%s.ammo_collect", BeyondHorizon.ID);
    public static final String MANA_COST_PER_SECOND = String.format("tooltip.%s.mana_cost.per_second", BeyondHorizon.ID);
    public static final String MANA_COST_PERCENTAGES = String.format("tooltip.%s.mana_cost.percentage", BeyondHorizon.ID);
    public static final String MANA_COST = String.format("tooltip.%s.mana_cost", BeyondHorizon.ID);
    public static final String NOT_ENOUGH_MANA = String.format("tooltip.%s.not_enough_mana", BeyondHorizon.ID);

    public static final String BOSS_IS_DEFEATED = String.format("boss.%s.defeated", BeyondHorizon.ID);
    public static final String SKILL_TYPE = String.format("tooltip.%s.skill_type", BeyondHorizon.ID);
    public static final String PER_PIECE_BONUS_ARMOR_SET = String.format("tooltip.%s.piece_armor_set.per_piece", BeyondHorizon.ID);
    public static final String FULL_BONUS_ARMOR_SET = String.format("tooltip.%s.bonus_armor_set.full_set", BeyondHorizon.ID);
    public static final String MINING_SPEED = String.format("tooltip.%s.mining_speed", BeyondHorizon.ID);
    public static final String ACCESSORY = String.format("item.%s.accessory", BeyondHorizon.ID);
    public static final String ACCESSORY_TYPE = String.format("item.%s.accessory.type", BeyondHorizon.ID);
    public static final String ACCESSORY_SKILL_TYPE = String.format("item.%s.accessory.skill_type", BeyondHorizon.ID);
    public static final String INVENTORY = String.format("tooltip.%s.inventory", BeyondHorizon.ID);
    public static final String HEALTH_RECOVERY_POTION = String.format("tooltip.%s.recovery_potion.health", BeyondHorizon.ID);
    public static final String MANA_RECOVERY_POTION = String.format("tooltip.%s.recovery_potion.mana", BeyondHorizon.ID);
    public static final String BUILTIN_RESOURCE = String.format("resourcepack.%s.game_art", BeyondHorizon.ID);
    public static final String IMMUNE_TO = String.format("tooltip.%s.immune_to", BeyondHorizon.ID);
    public static final String WORKBENCH = String.format("block.%s.workbench.name", BeyondHorizon.ID);
    public static final String WORKBENCH_ITEMS = String.format("block.%s.workbench.items", BeyondHorizon.ID);
    public static final String WORKBENCH_HELP_0 = String.format("block.%s.workbench.help.0", BeyondHorizon.ID);
    public static final String WORKBENCH_HELP_1 = String.format("block.%s.workbench.help.1", BeyondHorizon.ID);
    public static final String COOLDOWN_IN_NAME = String.format("tooltip.%s.value", BeyondHorizon.ID);
    public static final String VALUE_WITH_MAX = String.format("tooltip.%s.value.max", BeyondHorizon.ID);

    public static final ChatFormatting[] ATTRIBUTES = {ChatFormatting.DARK_GREEN, ChatFormatting.RED};
    public static final ChatFormatting[] ENCHANTMENT = {ChatFormatting.GOLD, ChatFormatting.RED};
    public static final ChatFormatting[] TOOLTIP = {ChatFormatting.GRAY, ChatFormatting.DARK_GRAY};



    public static MutableComponent numberMax(int value, int max) {
        return Component.translatable(VALUE_WITH_MAX, value, max);
    }

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
