package com.reactnativenavigation.parse;

public enum  TabsAttachMode {
    TOGETHER,
    AFTER_INITIAL_TAB,
    ON_SWITCH_TO_TAB,
    UNDEFINED;

    public static TabsAttachMode fromString(String mode) {
        switch (mode) {
            case "together":
                return TOGETHER;
            case "afterInitialTab":
                return AFTER_INITIAL_TAB;
            case "onSwitchToTab":
                return ON_SWITCH_TO_TAB;
            default:
                return UNDEFINED;
        }
    }

    public boolean hasValue() {
        return this != UNDEFINED;
    }
}
