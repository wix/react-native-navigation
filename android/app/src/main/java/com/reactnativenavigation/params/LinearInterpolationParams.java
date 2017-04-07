package com.reactnativenavigation.params;

import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

public class LinearInterpolationParams extends InterpolationParams {
    @Override
    public Interpolator get() {
        return new LinearInterpolator();
    }
}
