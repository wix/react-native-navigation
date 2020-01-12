package com.reactnativenavigation.utils;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import static android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
import static com.reactnativenavigation.utils.UiUtils.dpToPx;

public class StatusBarUtils {
    private static final int STATUS_BAR_HEIGHT_M = 24;
    private static final int STATUS_BAR_HEIGHT_L = 25;
    private static int statusBarHeight = -1;

    public static void saveStatusBarHeight(int height) {
        statusBarHeight = height;
    }

    public static int getStatusBarHeight(Context context) {
        if (statusBarHeight > 0) {
            return statusBarHeight;
        }
        final Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        statusBarHeight = resourceId > 0 ?
                resources.getDimensionPixelSize(resourceId) :
                dpToPx(context, Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? STATUS_BAR_HEIGHT_M : STATUS_BAR_HEIGHT_L);
        return statusBarHeight;
    }

    public static boolean isTranslucent(Window window) {
        WindowManager.LayoutParams lp = window.getAttributes();
        return lp != null && (lp.flags & FLAG_TRANSLUCENT_STATUS) == FLAG_TRANSLUCENT_STATUS;
    }
}
