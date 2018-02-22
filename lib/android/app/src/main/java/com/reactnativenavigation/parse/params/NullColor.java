package com.reactnativenavigation.parse.params;

public class NullColor extends Color {

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
