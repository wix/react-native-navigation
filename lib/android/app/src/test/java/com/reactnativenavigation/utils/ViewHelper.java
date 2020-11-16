package com.reactnativenavigation.utils;

import android.view.View;

public class ViewHelper {
    public static boolean isVisible(View view) {
        return view != null && view.getParent() != null && view.getVisibility() == View.VISIBLE;
    }
}
