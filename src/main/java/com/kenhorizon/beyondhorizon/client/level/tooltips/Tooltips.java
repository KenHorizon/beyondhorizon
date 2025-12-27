package com.kenhorizon.beyondhorizon.client.level.tooltips;

import com.ibm.icu.impl.Pair;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings({"deprecation", "removal"})
public class Tooltips {

    public static final FormattedCharSequence SPACE = FormattedCharSequence.forward(" ", Style.EMPTY);
    //
    public static final String DAMAGE = "damage";
    public static final String HEALTH = "health";

    //
    public static final String COMMAND_ROLE_SET_SUCCESS = String.format("command.%s.role_class.roles.succes", BeyondHorizon.ID);
    public static final String COMMAND_ROLE_SET_FAILED = String.format("command.%s.role_class.roles.failure", BeyondHorizon.ID);
    public static final String COMMAND_LEVEL_SET_SUCCESS = String.format("command.%s.role_class.level.succes", BeyondHorizon.ID);
    public static final String COMMAND_LEVEL_SET_FAILED = String.format("command.%s.role_class.level.failure", BeyondHorizon.ID);
    public static final String COMMAND_RESET_SUCCESS = String.format("command.%s.role_class.reset.succes", BeyondHorizon.ID);
    public static final String COMMAND_RESET_FAILED = String.format("command.%s.role_class.reset.failure", BeyondHorizon.ID);
    public static final String COMMAND_POINTS_SUCCESS = String.format("command.%s.role_class.points.succes", BeyondHorizon.ID);
    public static final String COMMAND_POINTS_FAILED = String.format("command.%s.role_class.points.failure", BeyondHorizon.ID);
    //
    public static final String BOSS_IS_DEFEATED = String.format("boss.%s.defeated", BeyondHorizon.ID);
    public static final String SKILL_TYPE = String.format("tooltip.%s.skill_type", BeyondHorizon.ID);
    public static final String TOOLTIP_MINING_SPEED = String.format("tooltip.%s.mining_speed", BeyondHorizon.ID);
    public static final String TOOLTIP_ACCESSORY = String.format("item.%s.accessory", BeyondHorizon.ID);
    public static final String TOOLTIP_ACCESSORY_TYPE = String.format("item.%s.accessory.type", BeyondHorizon.ID);
    public static final String TOOLTIP_INVENTORY = String.format("tooltip.%s.inventory", BeyondHorizon.ID);
    public static final String TOOLTIP_HEALTH_RECOVERY_POTION = String.format("tooltip.%s.recovery_potion.health", BeyondHorizon.ID);
    public static final String TOOLTIP_MANA_RECOVERY_POTION = String.format("tooltip.%s.recovery_potion.mana", BeyondHorizon.ID);
    public static final String TOOLTIP_BUILTIN_RESOURCE = String.format("resourcePack.%s.game_art", BeyondHorizon.ID);

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

    public static String getBossMessage(EntityType<?> entityType) {
        return String.format("boss.%s.defeated.%s", BeyondHorizon.ID, entityType.getDescriptionId());
    }

    public static String getBossMessage(EntityType<?> entityType, int line) {
        return String.format("boss.%s.%s.defeated.%s", BeyondHorizon.ID, entityType.getDescriptionId(), line);
    }

    public static class TitleBreakComponent implements TooltipComponent, ClientTooltipComponent {
        @Override
        public int getHeight() {
            return 0;
        }

        @Override
        public int getWidth(Font font) {
            return 0;
        }

        public static void registerFactory() {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(TitleBreakComponent::onRegisterTooltipEvent);
        }

        private static void onRegisterTooltipEvent(RegisterClientTooltipComponentFactoriesEvent event) {
            event.register(TitleBreakComponent.class, x -> x);
        }
    }

    public static List<FormattedText> recompose(List<ClientTooltipComponent> components) {
        List<FormattedText> recomposedLines = new ArrayList<>();
        for (ClientTooltipComponent component : components) {
            if (component instanceof ClientTextTooltip) {
                RecomposerSink recomposer = new RecomposerSink();
                ((ClientTextTooltip) component).text.accept(recomposer);
                recomposedLines.add(recomposer.getFormattedText());
            }
        }
        return recomposedLines;
    }

    public static int calculateTitleLines(List<ClientTooltipComponent> components) {
        if (components == null || components.isEmpty()) {
            return 0;
        }
        int titleLines = 0;
        boolean foundTitleBreak = false;
        for (ClientTooltipComponent component : components) {
            if (component instanceof ClientTextTooltip) {
                titleLines++;
            } else if (component instanceof TitleBreakComponent) {
                foundTitleBreak = true;
                break;
            }
        }
        if (!foundTitleBreak) {
            titleLines = 1;
        }

        return titleLines;
    }

    public static List<ClientTooltipComponent> centerTitle(List<ClientTooltipComponent> components, Font font, int width, int titleLines) {
        List<ClientTooltipComponent> result = new ArrayList<>(components);
        if (components.isEmpty() || titleLines <= 0 || titleLines >= components.size()) {
            return result;
        }
        int titleIndex = 0;
        for (ClientTooltipComponent clientTooltipComponent : components) {
            if (clientTooltipComponent instanceof ClientTextTooltip) {
                break;
            }
            titleIndex++;
        }
        for (int i = 0; i < titleLines; i++) {
            ClientTooltipComponent titleComponent = components.get(titleIndex + i);
            if (titleComponent != null) {
                List<FormattedText> formattedTexts = recompose(List.of(titleComponent));
                if (formattedTexts.isEmpty()) {
                    return result;
                }
                FormattedCharSequence title = Language.getInstance().getVisualOrder(formattedTexts.get(0));

                while (ClientTooltipComponent.create(title).getWidth(font) < width) {
                    title = FormattedCharSequence.fromList(List.of(SPACE, title, SPACE));
                    if (title == null) {
                        break;
                    }
                }
                result.set(titleIndex + i, ClientTooltipComponent.create(title));
            }
        }
        return result;
    }

    private static class RecomposerSink implements FormattedCharSink {
        private final StringBuilder builder = new StringBuilder();
        private final MutableComponent text = Component.literal("").withStyle(Style.EMPTY);

        @Override
        public boolean accept(int index, Style style, int charCode) {
            builder.append(Character.toChars(charCode));
            if (!style.equals(text.getStyle())) {
                text.append(Component.literal(builder.toString()).withStyle(style));
                builder.setLength(0);
            }
            return true;
        }

        public FormattedText getFormattedText() {
            text.append(Component.literal(builder.toString()).withStyle(text.getStyle()));
            return text;
        }
    }
}
