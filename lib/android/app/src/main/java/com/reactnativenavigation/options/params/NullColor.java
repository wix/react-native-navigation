package com.reactnativenavigation.options.params;

import androidx.annotation.NonNull;

public class NullColor extends Colour {

    public NullColor() {
        super(0);
    }

    @Override
    public boolean hasValue() {
        return false;
    }

    @NonNull
    @Override
    public String toString() {
        return "Null Color";
    }
}
