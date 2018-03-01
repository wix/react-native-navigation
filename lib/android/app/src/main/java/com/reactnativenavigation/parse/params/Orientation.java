package com.reactnativenavigation.parse.params;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.annotation.Nullable;

public enum Orientation {
    Portrait("portrait", Configuration.ORIENTATION_PORTRAIT, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT),
    Landscape("landscape", Configuration.ORIENTATION_LANDSCAPE, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE),
    Default("default", Configuration.ORIENTATION_UNDEFINED, ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

    public String name;
    public int configurationCode;
    public int orientationCode;

    Orientation(String name, int configurationCode, int orientationCode) {
        this.name = name;
        this.configurationCode = configurationCode;
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

    public static String fromConfigurationCode(int configurationCode) {
        for (Orientation orientation : values()) {
            if (orientation.configurationCode == configurationCode) {
                return orientation.name;
            }
        }
        throw new RuntimeException();
    }
}
