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
        List<T> result = new ArrayList(get(a));
        result.addAll(get(b));
        return result;
    }

    public static <T> void forEach(@Nullable Collection<T> items, Apply<T> apply) {
        if (items != null) forEach(new ArrayList(items), 0, apply);
    }

    public static <T> void forEach(@Nullable T[] items, Apply<T> apply) {
        if (items == null) return;
        for (T item : items) {
            apply.on(item);
        }
    }

    public static <T> void forEach(@Nullable List<T> items, Apply<T> apply) {
        forEach(items, 0, apply);
    }

    public static <T> void forEach(@Nullable List<T> items, int startIndex, Apply<T> apply) {
        if (items == null) return;
        for (int i = startIndex; i < items.size(); i++) {
            apply.on(items.get(i));
        }
    }

    public static @Nullable <T> T first(@Nullable Collection<T> items, Filter<T> by) {
        if (isNullOrEmpty(items)) return null;
        for (T item : items) {
            if (by.filter(item)) return item;
        }
        return null;
    }

    public static <T> T last(@Nullable List<T> items) {
        return CollectionUtils.isNullOrEmpty(items) ? null : items.get(items.size() - 1);
    }

    public static <T> T removeLast(@NonNull List<T> items) {
        return items.remove(items.size() - 1);
    }

    public interface Reducer<S, T> {
        S reduce(T item, S currentValue);
    }

    public static <S, T> S reduce(Collection<T> items, S initialValue, Reducer<S, T> reducer) {
        S currentValue = initialValue;
        for (T item : items) {
            currentValue = reducer.reduce(item, currentValue);
        }
        return currentValue;
    }

    public static <T> Boolean reduce(@Nullable Collection<T> items, boolean initialValue, Functions.FuncR1<T, Boolean> reducer) {
        boolean currentValue = initialValue;
        if (CollectionUtils.isNullOrEmpty(items)) return currentValue;
        for (T item : items) {
            currentValue &= reducer.run(item);
            if (!currentValue) return false;
        }
        return currentValue;
    }

    public static @NonNull <T> Collection<T> get(@Nullable Collection<T> t) {
        return t == null ? Collections.EMPTY_LIST : t;
    }

    public static @NonNull <T> Collection<T> get(@Nullable Map<?, T> t) {
        return t == null ? Collections.EMPTY_LIST : t.values();
    }

}
