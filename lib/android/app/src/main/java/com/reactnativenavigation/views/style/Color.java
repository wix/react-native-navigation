package com.reactnativenavigation.views.style;

import android.support.annotation.ColorInt;

public class Color {

    private Integer color;

    public Color(@ColorInt int color) {
        this.color = color;
    }

    public boolean hasColor() {
        return color != null;
    }

    @ColorInt
    public int get() {
        if (hasColor()) {
            return color;
        }
        throw new RuntimeException("Tried to get null color!");
    }

    @SuppressWarnings("MagicNumber")
    @Override
    public String toString() {
        return String.format("#%06X", (0xFFFFFF & get()));
    }
}
