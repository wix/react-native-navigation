package com.reactnativenavigation.utils;

import android.support.annotation.Nullable;
import android.view.View;

import com.reactnativenavigation.views.ContentView;

public class ViewVisibilityChecker {

    public static boolean check(@Nullable View view) {
        if (view == null) {
            return false;
        }
        final int top = getTopRelativeToContentView(view);
        final int scrollYInScreen = getScrollYInScreen(view);
        return top + view.getHeight() > scrollYInScreen;
    }

    private static int getTopRelativeToContentView(View view) {
        return view instanceof ContentView ? 0 : view.getTop() + getTopRelativeToContentView((View) view.getParent());
    }

    private static int getScrollYInScreen(View view) {
        return view instanceof ContentView ? 0 : view.getScrollY() + getScrollYInScreen((View) view.getParent());
    }
}
