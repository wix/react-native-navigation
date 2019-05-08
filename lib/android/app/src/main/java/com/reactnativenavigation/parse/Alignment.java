package com.reactnativenavigation.parse;

public enum Alignment {
    Center, Fill, Default;

    public static Alignment fromString(String alignment) {
        switch (alignment) {
            case "center":
                return Center;
            case "fill":
                return Fill;
            default:
                return Default;
        }
    }
}
