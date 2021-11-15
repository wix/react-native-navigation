package com.reactnativenavigation.utils;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Size;
import androidx.core.util.Pair;

@SuppressWarnings("WeakerAccess")
public class CollectionUtils {

    @SafeVarargs
    public static <T> List<T> asList(T... args) {
        ArrayList<T> res = new ArrayList<>();
        forEach(args, a -> {
            if (a != null) res.add(a);
        });
        return res;
    }

    public interface Apply<T> {
        void on(T t);
    }

    public interface Comparator<T> {
        boolean compare(T a, T b);
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

    public static @Nullable <T, S> ArrayList<S> map(@Nullable Collection<T> items, Mapper<T, S> map) {
        if (items == null) return null;
        ArrayList<S> result = new ArrayList<>();
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

    public static <K, V> V getOrDefault(@Nullable Map<K, V> map, K key, Functions.FuncR<V> defaultValueCreator) {
        if (map == null) return defaultValueCreator.run();
        return map.containsKey(key) ? map.get(key) : defaultValueCreator.run();
    }

    public static <T> List<T> merge(@Nullable Collection<T> a, @Nullable Collection<T> b, @NonNull List<T> defaultValue) {
        List<T> result = merge(a, b);
        return result == null ? defaultValue : result;
    }

    public static <T> ArrayList<T> merge(@Nullable Collection<T> a, @Nullable Collection<T> b) {
        if (a == null && b == null) return null;
        ArrayList<T> result = new ArrayList<>(get(a));
        result.addAll(get(b));
        return result;
    }

    /**
     * @return Items in a, that are not in b
     */
    public static <T> List<T> difference(@NonNull Collection<T> a, @Nullable Collection<T> b, Comparator<T> comparator) {
        if (b == null) return new ArrayList<>(a);
        ArrayList<T> results = new ArrayList<>();
        forEach(a, btn -> {
            if (!contains(b, btn, comparator)) results.add(btn);
        });
        return results;
    }

    private static <T> boolean contains(@NonNull Collection<T> items, T item, Comparator<T> comparator) {
        for (T t : items) {
            if (comparator.compare(t, item)) return true;
        }
        return false;
    }

    public static <T> void forEach(@Nullable Collection<T> items, Apply<T> apply) {
        if (items != null) forEach(new ArrayList<>(items), 0, apply);
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

    public static <T> void forEachIndexed(@Nullable List<T> items, Functions.Func2<T, Integer> apply) {
        if (items == null) return;
        for (int i = 0; i < items.size(); i++) {
            apply.run(items.get(i), i);
        }
    }

    public static @Nullable <T> T first(@Nullable Collection<T> items, Filter<T> by) {
        if (isNullOrEmpty(items)) return null;
        for (T item : items) {
            if (by.filter(item)) return item;
        }
        return null;
    }

    public static @Nullable <T> T first(@Nullable Collection<T> items, Filter<T> by, Functions.Func1<T> apply) {
        if (isNullOrEmpty(items)) return null;
        for (T item : items) {
            if (by.filter(item)) {
                apply.run(item);
                return item;
            }
        }
        return null;
    }

    public static <T> T last(@Nullable List<T> items) {
        return CollectionUtils.isNullOrEmpty(items) ? null : items.get(items.size() - 1);
    }

    public static <T> T requireLast(@Size(min = 1) List<T> items) {
        return items.get(items.size() - 1);
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
        return t == null ? Collections.emptyList() : t;
    }

    public static @NonNull <T> Collection<T> get(@Nullable Map<?, T> t) {
        return t == null ? Collections.emptyList() : t.values();
    }

    public static <T> boolean equals(@Nullable Collection<T> a, @Nullable Collection<T> b) {
        if (size(a) != size(b)) return false;
        return reduce(zip(a, b), true, (p, currentValue) -> currentValue && Objects.equals(p.first, p.second));
    }

    public static int size(@Nullable Collection items) {
        return items == null ? 0 : items.size();
    }

    public static <T> Collection<Pair<T, T>> zip(@Nullable Collection<T> a, @Nullable Collection<T> b) {
        if (a == null || b == null) return new ArrayList<>();
        Iterator iter1 = a.iterator();
        Iterator iter2 = b.iterator();
        ArrayList<Pair<T,T>> result = new ArrayList<>();
        while (iter1.hasNext() && iter2.hasNext()) {
            result.add(new Pair(iter1.next(), iter2.next()));
        }
        return result;
    }

    public static @Nullable<T> T safeGet(List<T> items, int index) {
        return index >= 0 && index < items.size() ? items.get(index) : null;
    }
}
