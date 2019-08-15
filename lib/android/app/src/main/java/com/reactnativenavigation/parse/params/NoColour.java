package com.reactnativenavigation.parse.params;

import android.graphics.Color;

import javax.annotation.Nonnull;

public class NoColour extends Colour {
    public NoColour() {
        super(Color.TRANSPARENT);
    }

    @Override
    public Integer get() {
        return null;
    }

    @Override
    public Integer get(Integer defaultValue) {
        return null;
    }

    @Override
    public boolean hasValue() {
        return true;
    }

    @Nonnull
    @Override
    public String toString() {
        return "NoColor";
    }
}
