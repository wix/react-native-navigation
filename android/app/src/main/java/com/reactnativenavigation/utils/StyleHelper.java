package com.reactnativenavigation.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Window;

import com.reactnativenavigation.activities.BaseReactActivity;
import com.reactnativenavigation.core.objects.Screen;
import com.reactnativenavigation.views.RnnToolBar;

/**
 * Created by guyc on 07/05/16.
 */
public class StyleHelper {

    public static void updateStyles(RnnToolBar toolBar, Screen screen) {
        try {
            toolBar.updateAndSetButtons(screen);
            setWindowStyle(screen);
            toolBar.setupToolbarButtonsAsync(screen);
        } catch (Exception e) {
            Log.w("RNNavigation", "Tried to update styles with no screen!");
        }
    }

    private static void setWindowStyle(Screen screen) {
        BaseReactActivity context = ContextProvider.getActivityContext();
        if (context != null) {
            StyleHelper.setWindowStyle(context.getWindow(), context, screen);
        }
    }

    public static void setWindowStyle(Window window, Context context, Screen screen) {
        if (SdkSupports.lollipop()) {
            final int black = ContextCompat.getColor(context, android.R.color.black);
            if (screen.statusBarColor != null) {
                window.setStatusBarColor(screen.statusBarColor);
            } else {
                window.setStatusBarColor(black);
            }

            if (screen.navigationBarColor != null) {
                window.setNavigationBarColor(screen.navigationBarColor);
            } else {
                window.setNavigationBarColor(black);
            }
        }

    }
}
