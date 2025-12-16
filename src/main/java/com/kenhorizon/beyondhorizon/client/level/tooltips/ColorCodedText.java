package com.kenhorizon.beyondhorizon.client.level.tooltips;

import com.ibm.icu.impl.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorCodedText {
    public static final Map<String, ChatFormatting> TEXT_COLORED = new HashMap<>();
    static {
        TEXT_COLORED.put("hp", ChatFormatting.GREEN);
        TEXT_COLORED.put("current hp", ChatFormatting.GREEN);
        TEXT_COLORED.put("max hp", ChatFormatting.GREEN);
        TEXT_COLORED.put("missing hp", ChatFormatting.GREEN);
        TEXT_COLORED.put("health", ChatFormatting.GREEN);
        TEXT_COLORED.put("max health", ChatFormatting.GREEN);
        TEXT_COLORED.put("current health", ChatFormatting.GREEN);
        TEXT_COLORED.put("missing health", ChatFormatting.GREEN);
        TEXT_COLORED.put("physical", ChatFormatting.GOLD);
        TEXT_COLORED.put("physical damage", ChatFormatting.GOLD);
        TEXT_COLORED.put("magic", ChatFormatting.DARK_AQUA);
        TEXT_COLORED.put("magic damage", ChatFormatting.DARK_AQUA);
        TEXT_COLORED.put("attack damage", ChatFormatting.GOLD);
        TEXT_COLORED.put("true damage", ChatFormatting.GOLD);
        TEXT_COLORED.put("post-mitigation damage", ChatFormatting.GOLD);
        TEXT_COLORED.put("pre-mitigation damage", ChatFormatting.GOLD);
        TEXT_COLORED.put("critical strike", ChatFormatting.GOLD);
        TEXT_COLORED.put("critical damage", ChatFormatting.RED);
        TEXT_COLORED.put("mana", ChatFormatting.DARK_AQUA);
        TEXT_COLORED.put("armor", ChatFormatting.YELLOW);
        TEXT_COLORED.put("magic resistance", ChatFormatting.AQUA);
        TEXT_COLORED.put("magic penetration", ChatFormatting.LIGHT_PURPLE);
        TEXT_COLORED.put("armor penetration", ChatFormatting.RED);
        TEXT_COLORED.put("movement speed", ChatFormatting.GREEN);
    }
    private static final Pattern KEYWORD_PATTERN = buildPattern(TEXT_COLORED.keySet());

    private static Pattern buildPattern(Set<String> words) {
        StringBuilder sb = new StringBuilder();
        sb.append("(?i)\\b(");
        boolean first = true;
        for (String w : words) {
            if (!first) sb.append("|");
            sb.append(Pattern.quote(w));
            first = false;
        }
        sb.append(")\\b");
        return Pattern.compile(sb.toString(), Pattern.UNICODE_CASE);
    }

    public static Component applyFormat(Component input) {
        List<Pair<String, Style>> segments = new ArrayList<>();
        input.visit(((text) -> {
            input.getStyle();
            segments.add(Pair.of(text, input.getStyle()));
            return Optional.empty();
        }));
        MutableComponent output = Component.empty();
        for (Pair<String, Style> pairs : segments) {
            String raw = pairs.first;
            Style style = pairs.second;
            if (style == null) {
                style = Style.EMPTY;
            }
            Matcher matchedPairs = KEYWORD_PATTERN.matcher(raw);
            int last = 0;
            while (matchedPairs.find()) {
                int start = matchedPairs.start();
                int end = matchedPairs.end();

                if (start > last) {
                    output.append(Component.literal(raw.substring(last, start)).withStyle(style));
                }
                String matched = raw.substring(start, end);
                ChatFormatting color = TEXT_COLORED.get(matched.toLowerCase(Locale.ROOT));
                output.append(Component.literal(matched).withStyle(style).withStyle(color));
                last = end;
            }

            // Remaining text
            if (last < raw.length()) {
                output.append(Component.literal(raw.substring(last)).withStyle(style));
            }
        }

        return output;
    }
}
