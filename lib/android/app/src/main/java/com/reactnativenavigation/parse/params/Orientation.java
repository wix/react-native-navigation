package com.reactnativenavigation.parse.params;

import android.content.pm.ActivityInfo;
import android.support.annotation.Nullable;

public enum Orientation {
    Portrait("portrait", ActivityInfo.SCREEN_ORIENTATION_PORTRAIT),
    Landscape("landscape", ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE),
    Default("default", ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED),
    PortraitLandscape("sensor", ActivityInfo.SCREEN_ORIENTATION_USER);

    public String name;
    public int orientationCode;

    Orientation(String name, int orientationCode) {
        this.name = name;
        this.orientationCode = orientationCode;
    }

    @Nullable
    public static Orientation fromString(String name) {
        for (Orientation orientation : values()) {
            if (orientation.name.equals(name)) {
                return orientation;
            }
        }
        return null;
    }
}
