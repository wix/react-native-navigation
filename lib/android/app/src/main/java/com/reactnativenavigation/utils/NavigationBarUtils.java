package com.reactnativenavigation.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

public class NavigationBarUtils {
    private static int navigationBarHeight = -1;
    public static void saveNavigationBarHeight(int height) {
        navigationBarHeight = height;
    }

    public static int getNavigationBarHeight(Context context) {
        if (navigationBarHeight > 0) {
            return navigationBarHeight;
        }
        final Resources resources = context.getResources();
        final int orientation = resources.getConfiguration().orientation;
        final int resourceId = resources.getIdentifier(orientation == Configuration.ORIENTATION_PORTRAIT ? "navigation_bar_height" : "navigation_bar_height_landscape", "dimen", "android");
        if(resourceId > 0) {
            navigationBarHeight = resources.getDimensionPixelSize(resourceId);
        }

        return navigationBarHeight;
    }

}
