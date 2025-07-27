package com.reactnativenavigation.utils;

import android.graphics.Color;

public class ColorUtils {
    public static double[] colorToLAB(int color) {
        final double[] result = new double[3];
        androidx.core.graphics.ColorUtils.colorToLAB(color, result);
        return result;
    }

    public static int labToColor(double[] lab) {
        return androidx.core.graphics.ColorUtils.LABToColor(lab[0], lab[1], lab[2]);
    }

    public static boolean isColorLight(int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return darkness < 0.5;
    }

    public static int setAlpha(int color, int alpha) {
        return (color & 0x00FFFFFF) | (alpha << 24);
    }

    public static boolean isOpaque(int color) {
        return (color & 0xFF000000) == 0xFF000000;
    }
}
