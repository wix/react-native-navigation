package com.reactnativenavigation.views.pip;

public enum PIPStates {
    NOT_STARTED("NOT_STARTED"),
    MOUNT_START("MOUNT_START"),
    CUSTOM_MOUNTED("CUSTOM_MOUNTED"),
    CUSTOM_EXPANDED("CUSTOM_EXPANDED"),
    CUSTOM_COMPACT("CUSTOM_COMPACT"),
    RESTORE_START("RESTORE_START"),
    UNMOUNT_START("UNMOUNT_START"),
    NATIVE_MOUNT_START("NATIVE_MOUNT_START"),
    NATIVE_MOUNTED("NATIVE_MOUNTED");
    private String value;

    PIPStates(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}