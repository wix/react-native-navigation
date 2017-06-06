package com.reactnativenavigation.utils;

import android.app.Activity;
import android.content.res.Configuration;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.reactnativenavigation.NavigationApplication;
import com.reactnativenavigation.controllers.NavigationActivity;
import com.reactnativenavigation.params.Orientation;

public class OrientationHelper {
    public static String getOrientation(NavigationActivity currentActivity) {
        return Orientation.fromConfigurationCode(currentActivity.getResources().getConfiguration().orientation);
    }

    public static void setOrientation(Activity activity, Orientation orientation) {
        activity.setRequestedOrientation(orientation.orientationCode);
    }

    public static void onConfigurationChanged(Configuration newConfig) {
        WritableMap params = Arguments.createMap();
        params.putString("orientation", Orientation.fromConfigurationCode(newConfig.orientation));
        NavigationApplication.instance.getEventEmitter().sendNavigatorEvent("orientationChanged", params);
    }
}
