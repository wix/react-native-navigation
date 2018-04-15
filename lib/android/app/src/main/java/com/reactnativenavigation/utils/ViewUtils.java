package com.reactnativenavigation.utils;

import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ViewUtils {
    @Nullable
    public static <T> T findChildByClass(ViewGroup root, Class clazz) {
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
        ArrayList<T> ret = new ArrayList<>();
        for (int i = 0; i < root.getChildCount(); i++) {
            View view = root.getChildAt(i);
            if (view instanceof ViewGroup) {
                ret.addAll(findChildrenByClassRecursive((ViewGroup) view, clazz));
            }
            if (clazz.isAssignableFrom(view.getClass())) {
                ret.add((T) view);
            }
        }
        return ret;
    }

    public static <T> List<T> findChildrenByClass(ViewGroup root, Class clazz) {
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
        if (parent == child) return true;

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
}
