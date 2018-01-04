package com.reactnativenavigation.parse;

import android.support.annotation.ColorInt;

public class Color extends Param<Integer>{

    public Color(@ColorInt int color) {
        super(color);
    }

    @SuppressWarnings("MagicNumber")
    @Override
    public String toString() {
        return String.format("#%06X", (0xFFFFFF & get()));
    }
}
