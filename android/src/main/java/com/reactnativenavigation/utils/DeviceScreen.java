package com.reactnativenavigation.utils;

import android.content.res.Resources;

public class DeviceScreen {
    public static int width(Resources resources) {
        return resources.getDisplayMetrics().widthPixels;
    }

    public static int height(Resources resources) {
        return resources.getDisplayMetrics().heightPixels;
    }
}
