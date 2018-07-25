package com.reactnativenavigation.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectionUtils {
    public static boolean isNullOrEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    public interface Mapper<K, V> {
        K map(V value);
    }

    public static <K, V> Map<K, V> map(Collection<V> elements, Mapper<K, V> mapper) {
        Map<K, V> map = new HashMap<>();
        for (V value : elements) {
            map.put(mapper.map(value), value);
        }
        return map;
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

    public static <T> List<T> merge(Collection<T> a, Collection<T> b) {
        List<T> result = new ArrayList<>(a);
        result.addAll(b);
        return result;
    }
}
