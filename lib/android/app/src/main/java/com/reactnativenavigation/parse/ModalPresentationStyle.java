package com.reactnativenavigation.parse;

public enum ModalPresentationStyle {
    Unspecified("unspecified"),
    None("none"),
    OverCurrentContext("overCurrentContext");

    public String name;

    ModalPresentationStyle(String name) {
        this.name = name;
    }

    public static ModalPresentationStyle fromString(String name) {
        switch (name) {
            case "none":
                return None;
            case "overCurrentContext":
                return OverCurrentContext;
            default:
                return Unspecified;
        }
    }
}
