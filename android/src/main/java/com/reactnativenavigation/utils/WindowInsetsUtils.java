package com.reactnativenavigation.utils;

import androidx.core.view.WindowInsetsCompat;
import android.util.Log;

public class WindowInsetsUtils {
    private static final String TAG = "GUYCA";

    public static void log(WindowInsetsCompat i) {
        Log.i(TAG, "t: " + i.getStableInsetTop() +
                   " sysT: " + i.getSystemWindowInsetTop() +
                   " b: " + i.getStableInsetBottom() +
                   " sysB: " + i.getSystemWindowInsetBottom());
    }
}
