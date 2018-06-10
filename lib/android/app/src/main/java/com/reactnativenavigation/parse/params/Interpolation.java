package com.reactnativenavigation.parse.params;


import android.animation.TimeInterpolator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

public enum Interpolation {
    ACCELERATE,
    DECELERATE,
    ACCELERATE_DECELERATE,
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
            case DEFAULT:
                return new LinearInterpolator();
        }
        return null;
    }
}
