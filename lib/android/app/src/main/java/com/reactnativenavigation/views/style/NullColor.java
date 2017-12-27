package com.reactnativenavigation.views.style;

public class NullColor extends Color {

    public NullColor() {
        super(0);
    }

    public boolean hasColor() {
        return false;
    }

    @SuppressWarnings("MagicNumber")
    @Override
    public String toString() {
        return "Null Color";
    }
}
