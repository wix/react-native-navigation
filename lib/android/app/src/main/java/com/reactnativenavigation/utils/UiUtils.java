package com.reactnativenavigation.utils;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class UiUtils {
    private static final int DEFAULT_TOOLBAR_HEIGHT = 56;
    private static final int DEFAULT_BOTTOM_TABS_HEIGHT = 56;

    private static int topBarHeight = -1;
    private static int bottomTabsHeight = -1;

    public static <T extends View> void runOnPreDrawOnce(@Nullable final T view, final Functions.Func1<T> task) {
        if (view == null) return;
        runOnPreDrawOnce(view, () -> task.run(view));
    }

    public static void runOnPreDrawOnce(@Nullable final View view, final Runnable task) {
        if (view == null) return;
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                view.getViewTreeObserver().removeOnPreDrawListener(this);
                task.run();
                return true;
            }
        });
    }

    public static void doOnLayout(@Nullable final View view, final Runnable task) {
        if (view == null) return;
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                task.run();
            }
        });
    }

    public static void runOnMeasured(View view, Runnable task) {
        if (view.getHeight() > 0 && view.getWidth() > 0) {
            task.run();
        } else {
            ViewTreeObserver.OnGlobalLayoutListener listener = new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (view.getHeight() > 0 && view.getWidth() > 0) {
                        view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        task.run();
                    }
                }
            };
            runOnDetach(view, () -> view.getViewTreeObserver().removeOnGlobalLayoutListener(listener));
            view.getViewTreeObserver().addOnGlobalLayoutListener(listener);
        }
    }

    public static void runOnDetach(View view, Runnable task) {
        view.getViewTreeObserver().addOnWindowAttachListener(new ViewTreeObserver.OnWindowAttachListener() {
            @Override
            public void onWindowAttached() {

            }

            @Override
            public void onWindowDetached() {
                view.getViewTreeObserver().removeOnWindowAttachListener(this);
                task.run();
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

    public static int getTopBarHeightDp(Context context) {
        return (int) UiUtils.pxToDp(context, getTopBarHeight(context));
    }

    public static int getTopBarHeight(Context context) {
        if (topBarHeight > 0) {
            return topBarHeight;
        }
        final Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier("action_bar_size", "dimen", "android");
        topBarHeight = resourceId > 0 ?
                resources.getDimensionPixelSize(resourceId) :
                dpToPx(context, DEFAULT_TOOLBAR_HEIGHT);
        return topBarHeight;
    }

    public static int getBottomTabsHeight(Context context) {
        if (bottomTabsHeight > 0) {
            return bottomTabsHeight;
        }
        final Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier("bottom_navigation_height", "dimen", context.getPackageName());
        bottomTabsHeight = resourceId > 0 ?
                resources.getDimensionPixelSize(resourceId) :
                dpToPx(context, DEFAULT_BOTTOM_TABS_HEIGHT);
        return bottomTabsHeight;
    }

    public static float dpToPx(Context context, float dp) {
        Resources resources = context.getResources();
        return dpToPx(resources.getDisplayMetrics(), dp);
    }

    public static float dpToPx(DisplayMetrics metrics, float dp) {
        return dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static int dpToPx(Context context, int dp) {
        if (dp <= 0) return dp;
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return (int) (dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static float pxToDp(Context context, float px) {
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static float dpToSp(Context context, float dp) {
        return dpToPx(context, dp) / context.getResources().getDisplayMetrics().density;
    }
}
