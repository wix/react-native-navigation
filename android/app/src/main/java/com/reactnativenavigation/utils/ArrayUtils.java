package com.reactnativenavigation.utils;

import java.util.Arrays;

public class ArrayUtils {
    public static float[] reverse(float[] array) {
        float[] result = Arrays.copyOf(array, array.length);
        for(int i = 0; i < result.length / 2; i++) {
            float temp = result[i];
            result[i] = result[result.length - i - 1];
            result[result.length - i - 1] = temp;
        }
        return result;
    }
}
