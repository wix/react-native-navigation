package com.reactnativenavigation.params;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;

public enum Orientation {
    Portrait("portrait", Configuration.ORIENTATION_PORTRAIT, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT),
    Landscape("landscape", Configuration.ORIENTATION_LANDSCAPE, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE),
    auto("auto", Configuration.ORIENTATION_UNDEFINED, ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

    public String name;
    public int configurationCode;
    public int orientationCode;

    Orientation(String name, int configurationCode, int orientationCode) {
        this.name = name;
        this.configurationCode = configurationCode;
        this.orientationCode = orientationCode;
    }

    public static Orientation fromString(String name) {
        for (Orientation orientation : values()) {
            if (orientation.name.equals(name)) {
                return orientation;
            }
        }
        throw new RuntimeException();
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
