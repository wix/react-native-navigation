package com.reactnativenavigation.parse.params;

import android.graphics.Color;
import androidx.annotation.ColorInt;

public class Colour extends Param<Integer>{

    public Colour(@ColorInt int color) {
        super(color);
    }

    @SuppressWarnings("MagicNumber")
    @Override
    public String toString() {
        return String.format("#%06X", (0xFFFFFF & get()));
    }

    public boolean hasTransparency() {
        return hasValue() && Color.alpha(value) < 1;
    }
}
