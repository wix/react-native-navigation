package com.reactnativenavigation.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.reactnativenavigation.NavigationApplication;
import com.reactnativenavigation.params.AppStyle;
import com.reactnativenavigation.screens.Screen;
import com.reactnativenavigation.views.utils.Point;

import java.util.concurrent.atomic.AtomicInteger;

public class ViewUtils {
    private static final AtomicInteger viewId = new AtomicInteger(1);
    private static int statusBarHeight = -1;

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
        return pixels / scaledDensity;
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

    public static float getScreenWidth() {
        WindowManager wm = (WindowManager) NavigationApplication.instance.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
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

    public interface Matcher<T> {
        boolean match(T child);
    }

    /**
     * Returns the first instance of clazz in root
     */
    @Nullable
    public static <T> T findChildByClass(ViewGroup root, Class clazz) {
        return findChildByClass(root, clazz, null);
    }

    @Nullable
    public static <T> T findChildByClass(ViewGroup root, Class clazz, Matcher<T> matcher) {
        for (int i = 0; i < root.getChildCount(); i++) {
            View view = root.getChildAt(i);
            if (clazz.isAssignableFrom(view.getClass())) {
                return (T) view;
            }

            if (view instanceof ViewGroup) {
                view = (View) findChildByClass((ViewGroup) view, clazz, matcher);
                if (view != null && clazz.isAssignableFrom(view.getClass())) {
                    if (matcher == null) {
                        return (T) view;
                    }
                    if (matcher.match((T) view)) {
                        return (T) view;
                    }
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

    public static Point getLocationOnScreen(View view) {
        int[] xy = new int[2];
        view.getLocationOnScreen(xy);
        xy[1] -= getStatusBarHeight();
        return new Point(xy[0], xy[1]);
    }

    public static int getStatusBarHeight() {
        if (statusBarHeight > 0) {
            return statusBarHeight;
        }
        final Resources resources = NavigationApplication.instance.getResources();
        final int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        statusBarHeight = resourceId > 0 ?
                resources.getDimensionPixelSize(resourceId) :
                (int) convertDpToPixel(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? 24 : 25);
        return statusBarHeight;
    }

    public static ForegroundColorSpan[] getForegroundColorSpans(TextView view) {
        SpannedString text = new SpannedString(view.getText());
        return text.getSpans(0, text.length(), ForegroundColorSpan.class);
    }

    public static void setSpanColor(SpannableString span, int color) {
        span.setSpan(new ForegroundColorSpan(color), 0, span.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public static void removeRuleCompat(RelativeLayout.LayoutParams layoutParams, int rule) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            layoutParams.removeRule(rule);
        } else {
            layoutParams.addRule(rule, 0);
        }
    }
}
