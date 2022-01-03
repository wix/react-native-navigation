package com.reactnativenavigation.utils;

import android.graphics.Color;

import java.util.Random;

public final class ColorUtils {
    private final static Random rnd = new Random();

    public static int randomColor(float alpha){
        return Color.argb(Math.round(255.0f*alpha), rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    public static double[] colorToLAB(int color) {
        final double[] result = new double[3];
        androidx.core.graphics.ColorUtils.colorToLAB(color, result);
        return result;
    }

    public static int labToColor(double[] lab) {
        return androidx.core.graphics.ColorUtils.LABToColor(lab[0], lab[1], lab[2]);
    }
}
