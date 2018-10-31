package com.reactnativenavigation.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectionUtils {
    public interface Apply<T> {
        void on(T t);
    }

    public static boolean isNullOrEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    public interface KeyBy<K, V> {
        K by(V value);
    }

    public static <K, V> Map<K, V> keyBy(Collection<V> elements, KeyBy<K, V> key) {
        Map<K, V> map = new HashMap<>();
        for (V value : elements) {
            map.put(key.by(value), value);
        }
        return map;
    }

    public interface Mapper<T, S> {
        S map(T value);
    }

    public static @Nullable <T, S> List<S> map(@Nullable Collection<T> items, Mapper<T, S> map) {
        if (items == null) return null;
        List<S> result = new ArrayList<>();
        for (T item : items) {
            result.add(map.map(item));
        }
        return result;
    }

    public interface Filter<T> {
        boolean filter(T value);
    }

    public static <T> List<T> filter(Collection<T> list, Filter<T> filter) {
        List<T> result = new ArrayList<>();
        for (T t : list) {
            if (filter.filter(t)) result.add(t);
        }
        return result;
    }

    public static <T> List<T> merge(@Nullable Collection<T> a, @Nullable Collection<T> b, @NonNull List<T> defaultValue) {
        List<T> result = merge(a, b);
        return result == null ? defaultValue : result;
    }

    public static <T> List<T> merge(@Nullable Collection<T> a, @Nullable Collection<T> b) {
        if (a == null && b == null) return null;
        List<T> result = new ArrayList<>(get(a));
        result.addAll(get(b));
        return result;
    }

    public static <T> void forEach(@Nullable Collection<T> items, Apply<T> apply) {
        if (items == null) return;
        for (T item : items) {
            apply.on(item);
        }
    }

    public static @Nullable <T> T first(@Nullable Collection<T> items, Filter<T> by) {
        if (isNullOrEmpty(items)) return null;
        for (T item : items) {
            if (by.filter(item)) return item;
        }
        return null;
    }

    private static @NonNull <T> Collection<T> get(@Nullable Collection<T> t) {
        return t == null ? Collections.EMPTY_LIST : t;
    }
}
