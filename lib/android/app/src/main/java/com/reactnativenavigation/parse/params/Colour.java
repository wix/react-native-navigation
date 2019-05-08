package com.reactnativenavigation.parse.params;

import android.support.annotation.ColorInt;

public class Colour extends Param<Integer>{

    public Colour(@ColorInt int color) {
        super(color);
    }

    @SuppressWarnings("MagicNumber")
    @Override
    public String toString() {
        return String.format("#%06X", (0xFFFFFF & get()));
    }
}
