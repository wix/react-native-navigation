package com.reactnativenavigation.utils;

import android.support.annotation.Nullable;

public class ObjectUtils {
    public interface Action<T> {
        void performOn(T obj);
    }

    public static <T> void perform(@Nullable T obj, Action<T> action) {
        if (obj != null) action.performOn(obj);
    }
}
