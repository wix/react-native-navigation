package com.reactnativenavigation.utils;

import android.support.annotation.Nullable;

import com.reactnativenavigation.utils.Functions.Func1;
import com.reactnativenavigation.utils.Functions.FuncR1;

public class ObjectUtils {
    public static <T> void perform(@Nullable T obj, Func1<T> action) {
        if (obj != null) action.run(obj);
    }

    public static <T, S> S perform(@Nullable T obj, S defaultValue, FuncR1<T, S> action) {
        return obj == null ? defaultValue : action.run(obj);
    }

    public static boolean notNull(Object o) {
        return o != null;
    }
}
