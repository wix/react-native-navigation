package com.reactnativenavigation.options;

public enum ModalPresentationStyle {
    Unspecified("unspecified"),
    None("none"),
    OverCurrentContext("overCurrentContext"),
    PageSheet("pageSheet");

    public String name;

    ModalPresentationStyle(String name) {
        this.name = name;
    }

    public static ModalPresentationStyle fromString(String name) {
        if (name == null) {
            return Unspecified;
        }
        switch (name) {
            case "none":
                return None;
            case "overCurrentContext":
                return OverCurrentContext;
            case "pageSheet":
            case "formSheet":
                return PageSheet;
            default:
                return Unspecified;
        }
    }
}
