package com.reactnativenavigation.utils;

import com.reactnativenavigation.utils.Functions.Func1;
import com.reactnativenavigation.utils.Functions.FuncR1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ObjectUtils {
    public static <T> void perform(@Nullable T obj, Func1<T> action) {
        if (obj != null) action.run(obj);
    }

    public static <T, S> S perform(@Nullable T obj, S defaultValue, FuncR1<T, S> action) {
        return obj == null ? defaultValue : action.run(obj);
    }

    public static <T> T take(@Nullable T obj, @NonNull T defaultValue) {
        return obj == null ? defaultValue : obj;
    }

    public static <T> T getOrCreate(@Nullable T obj, @NonNull Functions.FuncR<T> creator) {
        return obj == null ? creator.run() : obj;
    }

    public static boolean notNull(Object o) {
        return o != null;
    }

    public static <T> boolean equalsNotNull(@Nullable T a, @Nullable T b) {
        return a != null && a.equals(b);
    }
}
