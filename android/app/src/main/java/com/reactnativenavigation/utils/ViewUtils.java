package com.reactnativenavigation.utils;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import com.reactnativenavigation.NavigationApplication;
import com.reactnativenavigation.params.AppStyle;
import com.reactnativenavigation.screens.Screen;

import java.util.concurrent.atomic.AtomicInteger;

public class ViewUtils {
    private static final AtomicInteger viewId = new AtomicInteger(1);

    public static void runOnPreDraw(final View view, final Runnable task) {
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (!view.getViewTreeObserver().isAlive()) {
                    return true;
                }
                view.getViewTreeObserver().removeOnPreDrawListener(this);
                task.run();
                return true;
            }
        });
    }

    public static void tintDrawable(Drawable drawable, int tint, boolean enabled) {
        drawable.setColorFilter(new PorterDuffColorFilter(enabled ? tint :
                AppStyle.appStyle.titleBarDisabledButtonColor.getColor(),
                PorterDuff.Mode.SRC_IN));
    }

    public static float convertDpToPixel(float dp) {
        float scale = NavigationApplication.instance.getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public static float convertPixelToSp(float pixels) {
        float scaledDensity = NavigationApplication.instance.getResources().getDisplayMetrics().scaledDensity;
        return pixels/scaledDensity;
    }

    public static float convertSpToPixel(float pixels) {
        float scaledDensity = NavigationApplication.instance.getResources().getDisplayMetrics().scaledDensity;
        return pixels * scaledDensity;
    }

    public static int generateViewId() {
        if (Build.VERSION.SDK_INT >= 17) {
            return View.generateViewId();
        } else {
            return compatGenerateViewId();
        }
    }

    public static float getScreenHeight() {
        WindowManager wm = (WindowManager) NavigationApplication.instance.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }

    private static int compatGenerateViewId() {
        for (; ; ) {
            final int result = viewId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (viewId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

    /**
     * Returns the first instance of clazz in root
     */
    @Nullable public static <T> T findChildByClass(ViewGroup root, Class clazz) {
        for (int i = 0; i < root.getChildCount(); i++) {
            View view = root.getChildAt(i);
            if (clazz.isAssignableFrom(view.getClass())) {
                return (T) view;
            }

            if (view instanceof ViewGroup) {
                view = findChildByClass((ViewGroup) view, clazz);
                if (view != null && clazz.isAssignableFrom(view.getClass())) {
                    return (T) view;
                }
            }
        }
        return null;
    }

    public static void performOnChildren(ViewGroup root, PerformOnViewTask task) {
        for (int i = 0; i < root.getChildCount(); i++) {
            View child = root.getChildAt(i);
            if (child instanceof ViewGroup) {
                performOnChildren((ViewGroup) child, task);
            }
            task.runOnView(child);
        }
    }

    public interface PerformOnViewTask {
        void runOnView(View view);
    }

    public static void performOnParentScreen(View child, Task<Screen> task) {
        Screen parentScreen = findParentScreen(child.getParent());
        if (parentScreen != null) {
            task.run(parentScreen);
        }
    }

    private static Screen findParentScreen(ViewParent parent) {
        if (parent == null) {
            return null;
        }
        if (parent instanceof Screen) {
            return (Screen) parent;
        }
        return findParentScreen(parent.getParent());
    }
}

