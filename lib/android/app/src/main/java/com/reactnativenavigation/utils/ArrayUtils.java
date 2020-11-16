package com.reactnativenavigation.utils;

public class ArrayUtils {
    public static boolean contains(Object[] array, Object item) {
        if (isNullOrEmpty(array)) return false;
        for (Object o : array) {
            if (o == item) return true;
        }
        return false;
    }

    private static boolean isNullOrEmpty(Object[] array) {
        return array == null || array.length == 0;
    }
}
