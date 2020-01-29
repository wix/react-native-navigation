package com.reactnativenavigation.utils;

import android.view.View;

import androidx.annotation.NonNull;

public class ViewTags {
    public static @NonNull <T> T get(View view, int key, @NonNull T defaultValue) {
        return view.getTag(key) == null ? defaultValue : (T) view.getTag(key);
    }
}
