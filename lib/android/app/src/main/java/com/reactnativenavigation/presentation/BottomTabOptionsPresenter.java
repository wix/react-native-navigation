package com.reactnativenavigation.presentation;

import android.support.annotation.IntRange;

import com.reactnativenavigation.parse.BottomTabOptions;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.views.BottomTabs;

public class BottomTabOptionsPresenter {
    private BottomTabs bottomTabs;

    public BottomTabOptionsPresenter(BottomTabs bottomTabs) {
        this.bottomTabs = bottomTabs;
    }

    public void present(Options options, @IntRange(from = 0) int bottomTabIndex) {
        applyBottomTabOptions(options.bottomTabOptions, bottomTabIndex);
    }

    private void applyBottomTabOptions(BottomTabOptions options, int bottomTabIndex) {
        if (options.badge.hasValue()) {
            bottomTabs.setBadge(bottomTabIndex, options.badge);
        }
    }
}
