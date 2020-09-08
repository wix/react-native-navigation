package com.reactnativenavigation.options.params;


import android.animation.TimeInterpolator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

public enum Interpolation {
    ACCELERATE,
    DECELERATE,
    ACCELERATE_DECELERATE,
    SPRING,
    DEFAULT,
    NO_VALUE;

    public TimeInterpolator getInterpolator() {
        switch (this) {
            case ACCELERATE:
                return new AccelerateInterpolator();
            case DECELERATE:
                return new DecelerateInterpolator();
            case ACCELERATE_DECELERATE:
                return new AccelerateDecelerateInterpolator();
            case SPRING:
                // TODO: Expose tension property to JS-API and make uniform with iOS implementation (has springiness and mass properties)
                return new OvershootInterpolator(1f);
            case DEFAULT:
                return new LinearInterpolator();
        }
        return null;
    }
}
