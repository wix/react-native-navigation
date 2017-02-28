package com.reactnativenavigation.views.utils;

import android.animation.TypeEvaluator;
import android.support.v4.graphics.ColorUtils;

public class LabColorEvaluator implements TypeEvaluator<double[]> {
    private double[] outputParam = new double[3];

    @Override
    public double[] evaluate(float ratio, double[] from, double[] to) {
        ColorUtils.blendLAB(from, to, ratio, outputParam);
        return outputParam;
    }
}
