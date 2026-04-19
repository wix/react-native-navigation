package com.reactnativenavigation;

import com.facebook.soloader.SoLoader;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;

@Implements(SoLoader.class)
public class ShadowSoLoader {

    @Implementation
    public static boolean loadLibrary(String shortName) {
        return true;
    }

    @Implementation
    public static boolean loadLibrary(String shortName, int flags) {
        return true;
    }

    @Implementation
    public static void loadLibraryOnNonAndroid(String shortName) {
    }

    @Implementation
    public static void init(android.content.Context context, int flags) {
    }
}
