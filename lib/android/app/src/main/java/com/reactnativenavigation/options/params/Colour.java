package com.reactnativenavigation.options.params;

import android.graphics.Color;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

public class Colour extends Param<Integer>{

    public Colour(@ColorInt int color) {
        super(color);
    }

    @NonNull
    @SuppressWarnings("MagicNumber")
    @Override
    public String toString() {
        return String.format("#%06X", (0xFFFFFF & get()));
    }

    public boolean hasTransparency() {
        return hasValue() && Color.alpha(value) < 1;
    }
}
