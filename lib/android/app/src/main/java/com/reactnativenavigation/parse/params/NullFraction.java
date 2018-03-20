package com.reactnativenavigation.parse.params;

public class NullFraction extends Fraction {
    public NullFraction() {
        super(0);
    }

    @Override
    public boolean hasValue() {
        return false;
    }
}
