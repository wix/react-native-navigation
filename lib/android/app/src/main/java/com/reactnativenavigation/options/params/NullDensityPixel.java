package com.reactnativenavigation.options.params;

public class NullDensityPixel extends DensityPixel {
    public NullDensityPixel() {
        super(0);
    }

    @Override
    public boolean hasValue() {
        return false;
    }
}
