package com.kenhorizon.beyondhorizon.client.render.misc.tooltips;

import com.kenhorizon.beyondhorizon.client.render.util.Colors;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorCodedText {
    public static final Map<String, Integer> TEXT_COLORED = new HashMap<>();
    static {
        TEXT_COLORED.put("based damage dealt", Colors.GOLD);
        TEXT_COLORED.put("total damage dealt", Colors.GOLD);
        TEXT_COLORED.put("damage dealt", Colors.GOLD);
        TEXT_COLORED.put("bonus ap", Colors.GOLD);
        TEXT_COLORED.put("bonus ad", Colors.GOLD);
        TEXT_COLORED.put("on-hit", Colors.CORAL);
        TEXT_COLORED.put("basic attack", Colors.CORAL);
        TEXT_COLORED.put("ability damage", Colors.CORAL);
        TEXT_COLORED.put("hp", Colors.GREEN);
        TEXT_COLORED.put("current hp", Colors.GREEN);
        TEXT_COLORED.put("of their max hp", Colors.GREEN);
        TEXT_COLORED.put("max hp", Colors.GREEN);
        TEXT_COLORED.put("missing hp", Colors.GREEN);
        TEXT_COLORED.put("based on current hp", Colors.GREEN);
        TEXT_COLORED.put("based on max hp", Colors.GREEN);
        TEXT_COLORED.put("based on missing hp", Colors.GREEN);
        TEXT_COLORED.put("target's max hp", Colors.GREEN);
        TEXT_COLORED.put("target's missing hp", Colors.GREEN);
        TEXT_COLORED.put("target missing hp", Colors.GREEN);
        TEXT_COLORED.put("target's current hp", Colors.GREEN);
        TEXT_COLORED.put("target current hp", Colors.GREEN);
        TEXT_COLORED.put("based on target's max hp", Colors.GREEN);
        TEXT_COLORED.put("based on target's missing hp", Colors.GREEN);
        TEXT_COLORED.put("based on target missing hp", Colors.GREEN);
        TEXT_COLORED.put("based on target's current hp", Colors.GREEN);
        TEXT_COLORED.put("based on target current hp", Colors.GREEN);
        TEXT_COLORED.put("health", Colors.GREEN);
        TEXT_COLORED.put("bonus health", Colors.GREEN);
        TEXT_COLORED.put("bonus hp", Colors.GREEN);
        TEXT_COLORED.put("max health", Colors.GREEN);
        TEXT_COLORED.put("bonus max health", Colors.GREEN);
        TEXT_COLORED.put("current health", Colors.GREEN);
        TEXT_COLORED.put("bonus current health", Colors.GREEN);
        TEXT_COLORED.put("missing health", Colors.GREEN);
        TEXT_COLORED.put("physical damage", Colors.GOLD);
        TEXT_COLORED.put("magic damage", Colors.AZURE);
        TEXT_COLORED.put("bonus magic damage", Colors.AZURE);
        TEXT_COLORED.put("attack damage", Colors.GOLD);
        TEXT_COLORED.put("bonus attack damage", Colors.GOLD);
        TEXT_COLORED.put("true damage", Colors.CORAL);
        TEXT_COLORED.put("bonus true damage", Colors.CORAL);
        TEXT_COLORED.put("ad", Colors.GOLD);
        TEXT_COLORED.put("ap", Colors.AZURE);
        TEXT_COLORED.put("post-mitigation damage", Colors.GOLD);
        TEXT_COLORED.put("pre-mitigation damage", Colors.GOLD);
        TEXT_COLORED.put("critical strike", Colors.GOLD);
        TEXT_COLORED.put("critical damage", Colors.RED);
        TEXT_COLORED.put("bonus critical damage", Colors.RED);
        TEXT_COLORED.put("mana", Colors.AZURE);
        TEXT_COLORED.put("bonus mana", Colors.AZURE);
        TEXT_COLORED.put("max mana", Colors.AZURE);
        TEXT_COLORED.put("armor", Colors.YELLOW);
        TEXT_COLORED.put("total armor", Colors.YELLOW);
        TEXT_COLORED.put("bonus armor", Colors.YELLOW);
        TEXT_COLORED.put("magic resistance", Colors.MAGENTA);
        TEXT_COLORED.put("total magic resistance", Colors.MAGENTA);
        TEXT_COLORED.put("bonus magic resistance", Colors.MAGENTA);
        TEXT_COLORED.put("magic penetration", Colors.VIOLET);
        TEXT_COLORED.put("armor penetration", Colors.RED);
        TEXT_COLORED.put("movement speed", Colors.GREEN);
        TEXT_COLORED.put("increased damage", Colors.CORAL);
        TEXT_COLORED.put("crit", Colors.CORAL);
    }
    private static final Pattern KEYWORD_PATTERN = buildPattern(TEXT_COLORED.keySet());
    private static final Pattern NUMBER_PATTERN = Pattern.compile(
            "[+-]?\\d+(?:\\.\\d+)?(?:\\s*-\\s*[+-]?\\d+(?:\\.\\d+)?)?%?"
    );

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
        return applyFormatLines(List.of(input), null).get(0);
    }

    public static Component applyFormat(Component input, Integer defaultColor) {
        return applyFormatLines(List.of(input), defaultColor).get(0);
    }

    public static List<Component> applyFormatLines(List<Component> lines) {
        return applyFormatLines(lines, null);
    }

    public static List<Component> applyFormatLines(List<Component> lines, Integer defaultColor) {
        int n = lines.size();
        List<List<TextRun>> lineRuns = new ArrayList<>(n);
        List<String> lineTexts = new ArrayList<>(n);

        for (Component line : lines) {
            List<TextRun> runs = visitRuns(line);
            lineRuns.add(runs);
            StringBuilder sb = new StringBuilder();
            for (TextRun r : runs) sb.append(r.text);
            lineTexts.add(sb.toString());
        }
        StringBuilder combinedBuilder = new StringBuilder();
        int[] lineStart = new int[n];
        for (int i = 0; i < n; i++) {
            lineStart[i] = combinedBuilder.length();
            combinedBuilder.append(lineTexts.get(i));
            if (i < n - 1) combinedBuilder.append(' ');
        }
        String combined = combinedBuilder.toString();
        Integer[] colorAt = new Integer[combined.length()];
        Matcher matcher = KEYWORD_PATTERN.matcher(combined);

        List<int[]> keywordSpans = new ArrayList<>();
        while (matcher.find()) {
            String matched = combined.substring(matcher.start(), matcher.end()).toLowerCase(Locale.ROOT);
            Integer color = TEXT_COLORED.get(matched);
            if (color == null) continue;
            for (int i = matcher.start(); i < matcher.end(); i++) {
                colorAt[i] = color;
            }
            keywordSpans.add(new int[]{matcher.start(), matcher.end(), color});
        }

        List<int[]> numberSpans = new ArrayList<>();
        Matcher numMatcher = NUMBER_PATTERN.matcher(combined);
        while (numMatcher.find()) {
            numberSpans.add(new int[]{numMatcher.start(), numMatcher.end()});
        }
        Map<Integer, int[]> spanByEnd = new HashMap<>();
        Map<Integer, int[]> spanByStart = new HashMap<>();
        for (int[] span : numberSpans) {
            spanByEnd.put(span[1], span);
            spanByStart.put(span[0], span);
        }

        for (int[] kw : keywordSpans) {
            int color = kw[2];

            int idx = kw[0] - 1;
            while (idx >= 0 && Character.isWhitespace(combined.charAt(idx))) idx--;
            int[] beforeSpan = spanByEnd.get(idx + 1);
            if (beforeSpan != null) {
                for (int i = beforeSpan[0]; i < beforeSpan[1]; i++) colorAt[i] = color;
            }

            int idx2 = kw[1];
            while (idx2 < combined.length() && Character.isWhitespace(combined.charAt(idx2))) idx2++;
            int[] afterSpan = spanByStart.get(idx2);
            if (afterSpan != null) {
                for (int i = afterSpan[0]; i < afterSpan[1]; i++) colorAt[i] = color;
            }
        }
        List<Component> result = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            result.add(rebuildLine(lineRuns.get(i), lineTexts.get(i), colorAt, lineStart[i], defaultColor));
        }
        return result;
    }

    // Backup
//    public static List<Component> applyFormatLines(List<Component> lines, Integer defaultColor) {
//        int n = lines.size();
//        List<List<TextRun>> lineRuns = new ArrayList<>(n);
//        List<String> lineTexts = new ArrayList<>(n);
//
//        for (Component line : lines) {
//            List<TextRun> runs = visitRuns(line);
//            lineRuns.add(runs);
//            StringBuilder sb = new StringBuilder();
//            for (TextRun r : runs) sb.append(r.text);
//            lineTexts.add(sb.toString());
//        }
//        StringBuilder combinedBuilder = new StringBuilder();
//        int[] lineStart = new int[n];
//        for (int i = 0; i < n; i++) {
//            lineStart[i] = combinedBuilder.length();
//            combinedBuilder.append(lineTexts.get(i));
//            if (i < n - 1) combinedBuilder.append(' ');
//        }
//        String combined = combinedBuilder.toString();
//        Integer[] colorAt = new Integer[combined.length()];
//        Matcher matcher = KEYWORD_PATTERN.matcher(combined);
//        while (matcher.find()) {
//            String matched = combined.substring(matcher.start(), matcher.end()).toLowerCase(Locale.ROOT);
//            Integer color = TEXT_COLORED.get(matched);
//            if (color == null) continue;
//            for (int i = matcher.start(); i < matcher.end(); i++) {
//                colorAt[i] = color;
//            }
//        }
//        List<Component> result = new ArrayList<>(n);
//        for (int i = 0; i < n; i++) {
//            result.add(rebuildLine(lineRuns.get(i), lineTexts.get(i), colorAt, lineStart[i], defaultColor));
//        }
//        return result;
//    }
    private static Component rebuildLine(List<TextRun> runs, String lineText, Integer[] colorAt,
                                         int lineOffset, Integer defaultColor) {
        if (lineText.isEmpty()) return Component.empty();
        Style[] baseStyle = new Style[lineText.length()];
        int cursor = 0;
        for (TextRun run : runs) {
            for (int i = 0; i < run.text.length(); i++) {
                baseStyle[cursor++] = run.style;
            }
        }

        MutableComponent output = Component.empty();
        StringBuilder buffer = new StringBuilder();
        Style currentEffectiveStyle = null;

        for (int i = 0; i < lineText.length(); i++) {
            Integer color = colorAt[lineOffset + i];
            Style effective;
            if (color != null) {
                effective = baseStyle[i].withColor(TextColor.fromRgb(color)); // keyword match -- its own color wins
            } else if (defaultColor != null) {
                effective = baseStyle[i].withColor(TextColor.fromRgb(defaultColor)); // no match -- fall back to default
            } else {
                effective = baseStyle[i]; // no default given -- leave original style untouched
            }

            if (currentEffectiveStyle != null && !Objects.equals(currentEffectiveStyle, effective)) {
                output.append(Component.literal(buffer.toString()).withStyle(currentEffectiveStyle));
                buffer = new StringBuilder();
            }
            currentEffectiveStyle = effective;
            buffer.append(lineText.charAt(i));
        }
        if (buffer.length() > 0) {
            output.append(Component.literal(buffer.toString()).withStyle(currentEffectiveStyle));
        }
        return output;
    }

    private static List<TextRun> visitRuns(Component component) {
        List<TextRun> runs = new ArrayList<>();
        component.visit((style, text) -> {
            if (!text.isEmpty()) runs.add(new TextRun(text, style));
            return Optional.empty();
        }, Style.EMPTY);
        return runs;
    }

    private static class TextRun {
        final String text;
        final Style style;
        TextRun(String text, Style style) {
            this.text = text;
            this.style = style;
        }
    }
}
