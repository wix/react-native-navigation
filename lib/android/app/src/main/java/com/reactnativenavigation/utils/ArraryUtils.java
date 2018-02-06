package com.reactnativenavigation.utils;

public class ArraryUtils {
    public static boolean isNullOrEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    public static boolean containes(Object[] array, Object item) {
        if (isNullOrEmpty(array)) return false;
        for (Object o : array) {
            if (o == item) return true;
        }
        return false;
    }
}
