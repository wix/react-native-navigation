package com.reactnativenavigation.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;

public final class LaunchArgsParser {

    private static final String LAUNCH_ARGS = "launchArgs";

    /**
     * Parses launch args passed to activity intent to WritableMap
     * @param activity to fetch the extra launch args
     * @return parsed writable map if it exist, otherwise empty map will be returned
     */
    public static WritableMap parse(Activity activity) {
        if (activity != null) {
            Intent intent = activity.getIntent();
            if (intent != null) {
                Bundle launchArgs = intent.getBundleExtra(LAUNCH_ARGS);
                if (launchArgs != null) {
                    return Arguments.fromBundle(launchArgs);
                }
            }
        }
        return Arguments.createMap();
    }
}
