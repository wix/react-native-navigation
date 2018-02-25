package com.reactnativenavigation.presentation;

import com.reactnativenavigation.parse.BottomTabOptions;
import com.reactnativenavigation.parse.BottomTabsOptions;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.views.BottomTabs;

public class BottomTabsOptionsPresenter {
    private BottomTabs bottomTabs;

    public BottomTabsOptionsPresenter(BottomTabs bottomTabs) {
        this.bottomTabs = bottomTabs;
    }

    public void present(Options options) {
        applyBottomTabsOptions(options.bottomTabsOptions);
    }

    public void present(Options options, int tabIndex) {
        applyBottomTabOptions(options.bottomTabOptions, tabIndex);
    }

    private void applyBottomTabOptions(BottomTabOptions options, int tabIndex) {
        if (options.badge.hasValue()) {
            bottomTabs.setBadge(tabIndex, options.badge);
        }
    }

    private void applyBottomTabsOptions(BottomTabsOptions options) {
        if (options.backgroundColor.hasValue()) {
            bottomTabs.setBackgroundColor(options.backgroundColor.get());
        }
        if (options.currentTabIndex.hasValue()) {
            bottomTabs.setCurrentItem(options.currentTabIndex.get());
        }
        if (options.testId.hasValue()) {
            bottomTabs.setTag(options.testId.get());
        }
        if (options.selectedTabColor.hasValue()) {
            bottomTabs.setAccentColor(options.selectedTabColor.get());
        }
        if (options.tabColor.hasValue()) {
            bottomTabs.setInactiveColor(options.tabColor.get());
        }
    }
}
