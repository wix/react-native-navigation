package com.reactnativenavigation.presentation;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.ViewGroup.MarginLayoutParams;

import com.reactnativenavigation.anim.BottomTabsAnimator;
import com.reactnativenavigation.parse.AnimationsOptions;
import com.reactnativenavigation.parse.BottomTabOptions;
import com.reactnativenavigation.parse.BottomTabsOptions;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.viewcontrollers.bottomtabs.BottomTabFinder;
import com.reactnativenavigation.viewcontrollers.bottomtabs.TabSelector;
import com.reactnativenavigation.views.BottomTabs;
import com.reactnativenavigation.views.Component;

import java.util.List;

public class BottomTabsOptionsPresenter {
    private final BottomTabs bottomTabs;
    private final TabSelector tabSelector;
    private final BottomTabFinder bottomTabFinder;
    private final BottomTabsAnimator animator;
    private final List<ViewController> tabs;
    private final int defaultSelectedTabColor;
    private final int defaultTabColor;

    public BottomTabsOptionsPresenter(Context context, BottomTabs bottomTabs, List<ViewController> tabs, TabSelector tabSelector, BottomTabFinder bottomTabFinder) {
        this.bottomTabs = bottomTabs;
        this.tabs = tabs;
        this.tabSelector = tabSelector;
        this.bottomTabFinder = bottomTabFinder;
        animator = new BottomTabsAnimator(bottomTabs);
        defaultSelectedTabColor = ContextCompat.getColor(context, com.aurelhubert.ahbottomnavigation.R.color.colorBottomNavigationAccent);
        defaultTabColor = ContextCompat.getColor(context, com.aurelhubert.ahbottomnavigation.R.color.colorBottomNavigationInactive);
    }

    public void present(Options options) {
        applyBottomTabsOptions(options.bottomTabsOptions, options.animations);
    }

    public void presentChildOptions(Options options, Component child) {
        applyBottomTabsOptions(options.bottomTabsOptions, options.animations);
        int tabIndex = bottomTabFinder.findByComponent(child);
        applyBottomTabOptions(options.bottomTabOptions, tabIndex);
        applyDrawBehind(options.bottomTabsOptions, tabIndex);
    }

    private void applyBottomTabOptions(BottomTabOptions options, int tabIndex) {
        if (options.badge.hasValue()) {
            bottomTabs.setBadge(tabIndex, options.badge);
        }
        bottomTabs.setAccentColor(tabIndex, options.selectedIconColor.get(defaultSelectedTabColor));
        bottomTabs.setInactiveColor(tabIndex, options.iconColor.get(defaultTabColor));
    }

    private void applyDrawBehind(BottomTabsOptions options, int tabIndex) {
        MarginLayoutParams lp = (MarginLayoutParams) tabs.get(tabIndex).getView().getLayoutParams();
        if (options.drawBehind.isTrue()) {
            lp.bottomMargin = 0;
        }
        if (options.visible.isTrueOrUndefined() && options.drawBehind.isFalseOrUndefined()) {
            lp.bottomMargin = bottomTabs.getHeight();
        }
    }

    private void applyBottomTabsOptions(BottomTabsOptions options, AnimationsOptions animationsOptions) {
        if (options.titleDisplayMode.hasValue()) {
            bottomTabs.setTitleState(options.titleDisplayMode.toState());
        }
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
        for (int i = 0; i < tabs.size(); i++) {
            bottomTabs.setAccentColor(i, tabs.get(i).options.bottomTabOptions.selectedIconColor.get(defaultSelectedTabColor));
            bottomTabs.setInactiveColor(i, tabs.get(i).options.bottomTabOptions.iconColor.get(defaultTabColor));
        }
    }
}
