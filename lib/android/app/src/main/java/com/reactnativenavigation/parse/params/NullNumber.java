package com.reactnativenavigation.parse.params;

public class NullNumber extends Number {
    public NullNumber() {
        super(0);
    }

    @Override
    public boolean hasValue() {
        return false;
    }
}
