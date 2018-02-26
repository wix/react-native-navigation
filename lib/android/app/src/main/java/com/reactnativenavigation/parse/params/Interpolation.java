package com.reactnativenavigation.parse.params;


import android.animation.TimeInterpolator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

public enum Interpolation {
    ACCELERATING,
    DECELERATING,
    DEFAULT,
    NO_VALUE;

    public TimeInterpolator getInterpolator() {
        switch (this) {
            case ACCELERATING:
                return new AccelerateInterpolator();
            case DECELERATING:
                return new DecelerateInterpolator();
            case DEFAULT:
                return new AccelerateDecelerateInterpolator();
        }
        return null;
    }
}
