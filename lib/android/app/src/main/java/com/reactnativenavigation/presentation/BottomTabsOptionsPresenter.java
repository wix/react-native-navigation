package com.reactnativenavigation.presentation;

import com.reactnativenavigation.anim.BottomTabsAnimator;
import com.reactnativenavigation.parse.AnimationsOptions;
import com.reactnativenavigation.parse.BottomTabOptions;
import com.reactnativenavigation.parse.BottomTabsOptions;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.viewcontrollers.bottomtabs.BottomTabFinder;
import com.reactnativenavigation.viewcontrollers.bottomtabs.TabSelector;
import com.reactnativenavigation.views.BottomTabs;

public class BottomTabsOptionsPresenter {
    private BottomTabs bottomTabs;
    private TabSelector tabSelector;
    private BottomTabFinder bottomTabFinder;
    private BottomTabsAnimator animator;

    public BottomTabsOptionsPresenter(BottomTabs bottomTabs, TabSelector tabSelector, BottomTabFinder bottomTabFinder) {
        this.bottomTabs = bottomTabs;
        this.tabSelector = tabSelector;
        this.bottomTabFinder = bottomTabFinder;
        animator = new BottomTabsAnimator(bottomTabs);
    }

    public void present(Options options) {
        applyBottomTabsOptions(options.bottomTabsOptions, options.animations);
    }

    public void present(Options options, int tabIndex) {
        applyBottomTabsOptions(options.bottomTabsOptions, options.animations);
        applyBottomTabOptions(options.bottomTabOptions, tabIndex);
    }

    private void applyBottomTabOptions(BottomTabOptions options, int tabIndex) {
        if (options.badge.hasValue()) {
            bottomTabs.setBadge(tabIndex, options.badge);
        }
    }

    private void applyBottomTabsOptions(BottomTabsOptions options, AnimationsOptions animationsOptions) {
        if (options.backgroundColor.hasValue()) {
            bottomTabs.setBackgroundColor(options.backgroundColor.get());
        }
        if (options.currentTabIndex.hasValue()) {
            int tabIndex = options.currentTabIndex.get();
            if (tabIndex >= 0) tabSelector.selectTab(tabIndex);
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
        if (options.currentTabId.hasValue()) {
            int tabIndex = bottomTabFinder.findByControllerId(options.currentTabId.get());
            if (tabIndex >= 0) tabSelector.selectTab(tabIndex);
        }
        if (options.visible.isTrueOrUndefined()) {
            if (options.animate.isTrueOrUndefined()) {
                animator.show(animationsOptions);
            } else {
                bottomTabs.restoreBottomNavigation(false);
            }
        }
        if (options.visible.isFalse()) {
            if (options.animate.isTrueOrUndefined()) {
                animator.hide(animationsOptions);
            } else {
                bottomTabs.hideBottomNavigation(false);
            }
        }
    }

}
