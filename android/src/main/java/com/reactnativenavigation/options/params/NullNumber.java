package com.reactnativenavigation.options.params;

public class NullNumber extends Number {
    public NullNumber() {
        super(0);
    }

    @Override
    public boolean hasValue() {
        return false;
    }
}
