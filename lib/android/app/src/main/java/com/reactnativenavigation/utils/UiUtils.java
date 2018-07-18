package com.reactnativenavigation.utils;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

public class UiUtils {
    private static final int STATUS_BAR_HEIGHT_M = 24;
    private static final int STATUS_BAR_HEIGHT_L = 25;
    private static final int DEFAULT_TOOLBAR_HEIGHT = 56;

    private static int statusBarHeight = -1;
    private static int toolBarHeight = -1;

    public static void runOnPreDrawOnce(final View view, final Runnable task) {
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                view.getViewTreeObserver().removeOnPreDrawListener(this);
                task.run();
                return true;
            }
        });
    }

	public static void runOnMainThread(Runnable runnable) {
		new Handler(Looper.getMainLooper()).post(runnable);
	}

	public static float getWindowHeight(Context context) {
        return getDisplayMetrics(context).heightPixels;
	}

    public static float getWindowWidth(Context context) {
        return getDisplayMetrics(context).widthPixels;
    }

    @NonNull
    private static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(metrics);
        }
        return metrics;
    }

    public static int getStatusBarHeight(Context context) {
        if (statusBarHeight > 0) {
            return statusBarHeight;
        }
        final Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        statusBarHeight = resourceId > 0 ?
                resources.getDimensionPixelSize(resourceId) :
                (int) dpToPx(context, Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? STATUS_BAR_HEIGHT_M : STATUS_BAR_HEIGHT_L);
        return statusBarHeight;
    }

    public static int getToolBarHeight(Context context) {
        if (toolBarHeight > 0) {
            return toolBarHeight;
        }
        final Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier("action_bar_size", "dimen", "android");
        toolBarHeight = resourceId > 0 ?
                resources.getDimensionPixelSize(resourceId) :
                (int) dpToPx(context, DEFAULT_TOOLBAR_HEIGHT);
        return toolBarHeight;
    }

    public static float dpToPx(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public static float dpToSp(Context context, float dp) {
        return dpToPx(context, dp) / context.getResources().getDisplayMetrics().density;
    }
}
