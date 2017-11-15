package com.reactnativenavigation.utils;

import android.app.Activity;
import android.content.Intent;

public class CompatUtils {
    public static boolean isSplashOpenedOverNavigationActivity(final Activity act, final Intent intent) {
        return intent != null && intent.getAction() != null
               && intent.getAction().equals(Intent.ACTION_MAIN)
               && !act.isTaskRoot()
               && intent.hasCategory(Intent.CATEGORY_LAUNCHER);
    }
}
