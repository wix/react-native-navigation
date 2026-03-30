package com.reactnativenavigation.options.params;

public class NullColor extends Colour {

    public NullColor() {
        super(0);
    }

    @Override
    public boolean hasValue() {
        return false;
    }

    @Override
    public String toString() {
        return "Null Color";
    }
}
