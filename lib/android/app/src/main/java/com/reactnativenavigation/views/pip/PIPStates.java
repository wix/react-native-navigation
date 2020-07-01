package com.reactnativenavigation.views.pip;

public enum PIPStates {
    NOT_STARTED("NOT_STARTED"),
    CUSTOM_MOUNT_START("CUSTOM_MOUNT_START"),
    CUSTOM_MOUNTED("CUSTOM_MOUNTED"),
    CUSTOM_EXPANDED("CUSTOM_EXPANDED"),
    CUSTOM_COMPACT("CUSTOM_COMPACT"),
    CUSTOM_UNMOUNT_START("CUSTOM_UNMOUNT_START"),
    CUSTOM_UNMOUNTED("CUSTOM_UNMOUNTED"),
    NATIVE_MOUNTED("NATIVE_MOUNTED");
    private String value;

    PIPStates(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
