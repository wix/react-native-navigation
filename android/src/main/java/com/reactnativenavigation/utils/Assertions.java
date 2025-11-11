package com.reactnativenavigation.utils;

import javax.annotation.Nullable;

public class Assertions {
    public static void assertNotNull(@Nullable Object object) {
        if (object == null) throw new AssertionError();
    }
}
