package com.reactnativenavigation.utils;

import android.content.res.Configuration;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.reactnativenavigation.NavigationApplication;
import com.reactnativenavigation.controllers.NavigationActivity;

public class Orientation {
    public static String get(NavigationActivity currentActivity) {
        return getOrientation(currentActivity.getResources().getConfiguration().orientation);
    }

    public static void onConfigurationChanged(Configuration newConfig) {
        WritableMap params = Arguments.createMap();
        params.putString("orientation", getOrientation(newConfig.orientation));
        NavigationApplication.instance.getEventEmitter().sendNavigatorEvent("orientationChanged", params);
    }

    private static String getOrientation(int orientation) {
        switch (orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                return "PORTRAIT";
            case Configuration.ORIENTATION_LANDSCAPE:
                return "LANDSCAPE";
            default:
                return "UNDEFINED";
        }
    }
}
