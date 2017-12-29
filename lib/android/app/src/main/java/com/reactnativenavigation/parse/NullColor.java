package com.reactnativenavigation.parse;

public class NullColor extends Color {

    NullColor() {
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
