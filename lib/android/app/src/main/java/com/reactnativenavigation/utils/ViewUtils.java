package com.reactnativenavigation.utils;

import android.graphics.Point;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.ViewParent;

import com.facebook.react.views.view.ReactViewBackgroundDrawable;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

import static com.reactnativenavigation.utils.ObjectUtils.perform;

public class ViewUtils {
    @Nullable
    public static <T extends View> T findChildByClass(ViewGroup root, Class<T> clazz) {
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

    public static <T> List<T> findChildrenByClassRecursive(ViewGroup root, Class clazz) {
        return findChildrenByClassRecursive(root, clazz, child -> true);
    }

    public static <T> List<T> findChildrenByClassRecursive(ViewGroup root, Class clazz, Matcher<T> matcher) {
        ArrayList<T> ret = new ArrayList<>();
        for (int i = 0; i < root.getChildCount(); i++) {
            View view = root.getChildAt(i);
            if (view instanceof ViewGroup) {
                ret.addAll(findChildrenByClassRecursive((ViewGroup) view, clazz, matcher));
            }
            if (clazz.isAssignableFrom(view.getClass()) && matcher.match((T) view)) {
                ret.add((T) view);
            }
        }
        return ret;
    }

    public static <T> List<T> findChildrenByClass(ViewGroup root, Class<T> clazz) {
        return findChildrenByClass(root, clazz, child -> true);
    }

    public static <T> List<T> findChildrenByClass(ViewGroup root, Class clazz, Matcher<T> matcher) {
        List<T> ret = new ArrayList<>();
        for (int i = 0; i < root.getChildCount(); i++) {
            View child = root.getChildAt(i);
            if (clazz.isAssignableFrom(child.getClass()) && matcher.match((T) child)) {
                ret.add((T) child);
            }
        }
        return ret;
    }

    public interface Matcher<T> {
        boolean match(T child);
    }

    public static boolean isChildOf(ViewGroup parent, View child) {
        if (parent == child) return false;

        for (int i = 0; i < parent.getChildCount(); i++) {
            View view = parent.getChildAt(i);
            if (view == child) {
                return true;
            }

            if (view instanceof ViewGroup) {
                if (isChildOf((ViewGroup) view, child)) return true;
            }
        }
        return false;
    }

    public static int getHeight(View view) {
        if (view.getLayoutParams() == null) return 0;
        return view.getLayoutParams().height < 0 ? view.getHeight() : view.getLayoutParams().height;
    }

    public static <T extends ViewGroup> T findParent(View view, Class<T> clazz) {
        if (view == null) return null;
        @Nullable ViewParent parent = view.getParent();
        if (parent != null) {
            if (parent.getClass().isAssignableFrom(clazz)) return (T) parent;
            return findParent((View) parent, clazz);
        }
        return null;
    }

    public static Point getLocationOnScreen(View view) {
        int[] xy = new int[2];
        view.getLocationOnScreen(xy);
        return new Point(xy[0], xy[1]);
    }

    public static boolean areDimensionsEqual(View a, View b) {
        return a.getWidth() == b.getWidth() && a.getHeight() == b.getHeight();
    }

    public static boolean instanceOf(Class clazz, View... views) {
        for (View view : views) {
            if (!view.getClass().isAssignableFrom(clazz)) return false;
        }
        return true;
    }

    public static int getIndexInParent(View view) {
        ViewParent parent = view.getParent();
        if (parent == null) return -1;
        return ((ViewGroup) parent).indexOfChild(view);
    }

    public static int getBackgroundColor(View view) {
        if (view.getBackground() instanceof ReactViewBackgroundDrawable) {
            return ((ReactViewBackgroundDrawable) view.getBackground()).getColor();
        }
        throw new RuntimeException(view.getBackground().getClass().getSimpleName() + " is not ReactViewBackgroundDrawable");
    }

    public static void removeFromParent(View view) {
        ViewParent parent = view.getParent();
        if (parent != null) {
            ((ViewManager) parent).removeView(view);
        }
    }

    public static boolean isVisible(View view) {
        return perform(view, false, v -> v.getVisibility() == View.VISIBLE);
    }

    public static int topMargin(View view) {
        return ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).topMargin;
    }
}
