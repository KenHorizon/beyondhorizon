package com.kenhorizon.beyondhorizon.server;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Rarity;
import org.lwjgl.glfw.GLFW;

import java.util.Locale;
import java.util.function.Supplier;

public class Utils {

    public static boolean isVanillaRarity(Rarity rarity) {
        return rarity == Rarity.COMMON || rarity == Rarity.UNCOMMON || rarity == Rarity.RARE || rarity == Rarity.EPIC;
    }
    // Copied from ResourceLocation.decompose
    public static String[] decompose(String name, char separator) {
        String[] astring = new String[]{"minecraft", name};
        int i = name.indexOf(separator);
        if (i >= 0) {
            astring[1] = name.substring(i + 1);
            if (i >= 1) {
                astring[0] = name.substring(0, i);
            }
        }

        return astring;
    }

    public static String compactNumbers(long number) {
        if (number < 1000) {
            return Long.toString(number);
        }

        String[] suffixes = {"K", "M", "B", "T", "Qa", "Qi", "Sx", "Sp", "Oc", "No", "Dc"};

        double value = number;
        int index = -1;

        while (value >= 1000 && index < suffixes.length - 1) {
            value /= 1000.0;
            index++;
        }

        if (value >= 100) {
            return String.format("%.0f%s", value, suffixes[index]);
        } else if (value >= 10) {
            return String.format("%.1f%s", value, suffixes[index]);
        } else {
            return String.format("%.2f%s", value, suffixes[index]);
        }
    }

    public static String getObjectDescription(Supplier<?> itemSupplier) {
        return String.format("item.%s.%s.desc", BeyondHorizon.ID, itemSupplier.get());
    }
    public static String translateToLocal(String s) {
        return I18n.get(s);
    }

    public static String capitalize(Component component) {
        return capitalize(component.getString(), (char[]) null);
    }

    public static String capitalize(String str) {
        return capitalize(str, (char[]) null);
    }

    public static String capitalize(String str, char... delimiters) {
        int delimLen = delimiters == null ? -1 : delimiters.length;
        if (!Utils.isEmpty(str) && delimLen != 0) {
            char[] buffer = str.toCharArray();
            boolean capitalizeNext = true;

            for (int i = 0; i < buffer.length; ++i) {
                char ch = buffer[i];
                if (isDelimiter(ch, delimiters)) {
                    capitalizeNext = true;
                } else if (capitalizeNext) {
                    buffer[i] = Character.toTitleCase(ch);
                    capitalizeNext = false;
                }
            }

            return new String(buffer);
        } else {
            return str;
        }
    }

    private static boolean isDelimiter(char ch, char[] delimiters) {
        if (delimiters == null) {
            return Character.isWhitespace(ch);
        } else {
            char[] var2 = delimiters;
            int var3 = delimiters.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                char delimiter = var2[var4];
                if (ch == delimiter) {
                    return true;
                }
            }

            return false;
        }
    }
    public static String builderName(String name) {
        String[] array = name.split("_");
        StringBuilder builderName = new StringBuilder();
        for (int i = 0; i < array.length; ++i) {
            builderName.append(i == 0 ? array[i] : " " + array[i]);
        }
        return Utils.capitalize(builderName.toString());
    }

    public static String formattedWords(String text) {
        return builderName(text.toLowerCase(Locale.ROOT));
    }

    private static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isShiftPressed() {
        long WINDOW = Minecraft.getInstance().getWindow().getWindow();
        return InputConstants.isKeyDown(WINDOW, GLFW.GLFW_KEY_LEFT_SHIFT) || InputConstants.isKeyDown(WINDOW, GLFW.GLFW_KEY_RIGHT_SHIFT);
    }
    public static boolean isSpacePressed() {
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_SPACE);
    }
    public static boolean isCtrlPressed() {
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_CONTROL);
    }
    public static boolean isAltPressed() {
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_ALT);
    }
}
