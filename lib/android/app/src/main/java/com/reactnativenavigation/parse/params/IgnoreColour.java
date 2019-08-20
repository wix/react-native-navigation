package com.reactnativenavigation.parse.params;

import android.graphics.Color;

import javax.annotation.Nonnull;

public class IgnoreColour extends Colour {
    public IgnoreColour() {
        super(Color.TRANSPARENT);
    }

    @Override
    public boolean hasValue() {
        return true;
    }

    @Override
    public boolean ignore() {
        return true;
    }

    @Nonnull
    @Override
    public String toString() {
        return "NoColor";
    }
}
