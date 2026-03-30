package com.reactnativenavigation.options;

import android.content.pm.ActivityInfo;
import androidx.annotation.Nullable;

public enum Orientation {
    Default("default", ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED),
    Landscape("landscape", ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE),
    Portrait("portrait", ActivityInfo.SCREEN_ORIENTATION_PORTRAIT),
    PortraitLandscape("sensor", ActivityInfo.SCREEN_ORIENTATION_USER),
    SensorLandscape("sensorLandscape", ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE),
    SensorPortrait("sensorPortrait", ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);

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
