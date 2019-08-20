package com.reactnativenavigation.parse.params;

import android.graphics.Color;

import javax.annotation.Nonnull;

public class DontApplyColour extends Colour {
    public DontApplyColour() {
        super(Color.TRANSPARENT);
    }

    @Override
    public boolean hasValue() {
        return true;
    }

    @Override
    public boolean canApplyValue() {
        return false;
    }

    @Nonnull
    @Override
    public String toString() {
        return "NoColor";
    }
}
