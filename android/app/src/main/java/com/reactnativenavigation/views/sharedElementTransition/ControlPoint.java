package com.reactnativenavigation.views.sharedElementTransition;

import android.support.annotation.Nullable;

public class ControlPoint {
    public final float x;
    public final float y;

    public ControlPoint(@Nullable Float x, float defaultX, Float y, float defaultY) {
        this.x = x == null ? defaultX : x;
        this.y = y == null ? defaultY : y;
    }
}
