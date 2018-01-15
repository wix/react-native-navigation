package com.reactnativenavigation.parse;

public class NullFraction extends Fraction {
    NullFraction() {
        super(0);
    }

    @Override
    public boolean hasValue() {
        return false;
    }
}
