package com.reactnativenavigation.options.params;

public class NullPixelDensity extends PixelDensity {
    public NullPixelDensity() {
        super(0);
    }

    @Override
    public boolean hasValue() {
        return false;
    }
}
