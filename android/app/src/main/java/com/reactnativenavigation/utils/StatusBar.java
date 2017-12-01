package com.reactnativenavigation.utils;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.reactnativenavigation.params.StatusBarTextColorScheme;
import com.reactnativenavigation.params.StyleParams;

public class StatusBar {

    public static void setHidden(Window window, boolean statusBarHidden) {
        if (statusBarHidden) {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setColor(Window window, StyleParams.Color statusBarColor) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return;
        if (statusBarColor.hasColor()) {
            window.setStatusBarColor(statusBarColor.getColor());
        } else {
            window.setStatusBarColor(Color.BLACK);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void displayOverScreen(View view, boolean shouldDisplay) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return;

        if(shouldDisplay) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            flags |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            view.setSystemUiVisibility(flags);
        } else {
            int flags = view.getSystemUiVisibility();
            flags &= ~View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            flags &= ~View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            view.setSystemUiVisibility(flags);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void setTextColorScheme(View view, StatusBarTextColorScheme textColorScheme) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return;
        if (StatusBarTextColorScheme.Dark.equals(textColorScheme)) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
        } else {
            clearLightStatusBar(view);
        }
    }

    private static void clearLightStatusBar(View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return;
        int flags = view.getSystemUiVisibility();
        flags &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        view.setSystemUiVisibility(flags);
    }
}
