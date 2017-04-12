package com.reactnativenavigation.views.utils;

public class ColorUtils {
    public static double[] colorToLAB(int color) {
        final double[] result = new double[3];
        android.support.v4.graphics.ColorUtils.colorToLAB(color, result);
        return result;
    }

    public static int labToColor(double[] lab) {
        return android.support.v4.graphics.ColorUtils.LABToColor(lab[0], lab[1], lab[2]);
    }
}
