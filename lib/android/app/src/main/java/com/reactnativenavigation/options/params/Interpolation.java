package com.reactnativenavigation.options.params;


import android.animation.TimeInterpolator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.reactnativenavigation.utils.SpringInterpolator;

public enum Interpolation {
    ACCELERATE,
    DECELERATE,
    ACCELERATE_DECELERATE,
    OVERSHOOT,
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
            case OVERSHOOT:
                // TODO: Expose tension property to JS-API
                return new OvershootInterpolator(1f);
            case SPRING:
                // TODO: Expose mass, damping and stiffness property to JS-API
                return new SpringInterpolator(3, 500, 1000);
            case DEFAULT:
                return new LinearInterpolator();
        }
        return null;
    }
}
