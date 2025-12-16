package com.kenhorizon.beyondhorizon.server.util;

import net.minecraft.Util;
import org.joml.Quaternionf;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Maths {
    private static final DecimalFormat FORMAT = Util.make(new DecimalFormat("#.##"), (decimalFormat) -> {
        decimalFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ROOT));
    });

    public static String format0(float value) {
        return FORMAT.format(value * 100.0F);
    }
    public static String format0(double value) {
        return FORMAT.format(value * 100.0F);
    }
    public static String format1(float value) {
        return FORMAT.format(value);
    }
    public static String format1(double value) {
        return FORMAT.format(value);
    }
    public static float toPercent(float a) {
        return a / 100.0F;
    }

    public static float toPercent(float a, float b) {
        return a / b;
    }

    public static double toPercent(double a) {
        return a / 100.0D;
    }

    public static double toPercent(double a, double b) {
        return a / b;
    }

    public static float percentIntoDecimal(float a) {
        return a * 100.0F;
    }

    public static double percentIntoDecimal(double a) {
        return a * 100.0F;
    }

    public static int tickToSeconds(int seconds) {
        int time = 0;
        time += seconds * 20;
        if (time % 1200 == 0) {
            time += seconds * 1200;
        } else if (time % 72000 == 0) {
            time += seconds * 72000;
        }
        return time;
    }

    public static int sec(float seconds) {
        return (int) (20 * seconds);
    }

    public static int mins(float minutes) {
        return (int) (1200 * minutes);
    }

    public static int mins(float minutes, int seconds) {
        return (int) ((1200 * minutes) + sec(seconds));
    }

    public static int hours(float hours) {
        return (int) (72000 * hours);
    }

    public static int hours(float hours, float minutes) {
        return (int) ((72000 * hours) + mins(minutes));
    }


    public static int hours(float hours, float minutes, float seconds) {
        return (int) ((72000 * hours) + mins(minutes) + sec(seconds));
    }

    public static float circle(int index, float radian) {
        float radians = 0.0F;
        double angle = (360.0F / index) * radian;
        radians = (float) Math.toRadians(angle - 90.0F);
        return radians;
    }


    public static Quaternionf quatFromRotationXYZ(float x, float y, float z, boolean degrees) {
        if (degrees) {
            x *= ((float)Math.PI / 180F);
            y *= ((float)Math.PI / 180F);
            z *= ((float)Math.PI / 180F);
        }
        return (new Quaternionf()).rotationXYZ(x, y, z);
    }

    public static double perValue(double number, double perValue, double increment) {
        return (number / perValue) * increment;
    }
}
