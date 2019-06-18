package com.reactnativenavigation.presentation;

import android.graphics.Color;
import android.support.annotation.IntRange;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation.TitleState;
import com.reactnativenavigation.anim.BottomTabsAnimator;
import com.reactnativenavigation.parse.AnimationsOptions;
import com.reactnativenavigation.parse.BottomTabsOptions;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.viewcontrollers.bottomtabs.BottomTabFinder;
import com.reactnativenavigation.viewcontrollers.bottomtabs.TabSelector;
import com.reactnativenavigation.views.BottomTabs;

import java.util.List;

public class BottomTabsPresenter {
    private final BottomTabFinder bottomTabFinder;
    private final List<ViewController> tabs;
    private Options defaultOptions;
    private BottomTabs bottomTabs;
    private BottomTabsAnimator animator;
    private TabSelector tabSelector;

    public BottomTabsPresenter(List<ViewController> tabs, Options defaultOptions) {
        this.tabs = tabs;
        this.defaultOptions = defaultOptions;
        this.bottomTabFinder = new BottomTabFinder(tabs);
    }

    public void setDefaultOptions(Options defaultOptions) {
        this.defaultOptions = defaultOptions;
    }

    public void bindView(BottomTabs bottomTabs, TabSelector tabSelector) {
        this.bottomTabs = bottomTabs;
        this.tabSelector = tabSelector;
        animator = new BottomTabsAnimator(bottomTabs);
    }

    public void mergeOptions(Options options) {
        mergeBottomTabsOptions(options.bottomTabsOptions, options.animations);
    }

    public void applyOptions(Options options) {
        Options withDefaultOptions = options.copy().withDefaultOptions(defaultOptions);
        applyBottomTabsOptions(withDefaultOptions.bottomTabsOptions, withDefaultOptions.animations);
    }

    public void applyChildOptions(Options options, ViewController child) {
        int tabIndex = bottomTabFinder.findByControllerId(child.getId());
        if (tabIndex >= 0) {
            Options withDefaultOptions = options.copy().withDefaultOptions(defaultOptions);
            applyBottomTabsOptions(withDefaultOptions.bottomTabsOptions, withDefaultOptions.animations);
            applyDrawBehind(tabIndex);
        }
    }

    public void mergeChildOptions(Options options, ViewController child) {
        mergeBottomTabsOptions(options.bottomTabsOptions, options.animations);
        int tabIndex = bottomTabFinder.findByControllerId(child.getId());
        if (tabIndex >= 0) mergeDrawBehind(tabIndex);
    }

    private void mergeBottomTabsOptions(BottomTabsOptions options, AnimationsOptions animations) {
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
        if (options.visible.isTrue()) {
            if (options.animate.isTrueOrUndefined()) {
                animator.show(animations);
            } else {
                bottomTabs.restoreBottomNavigation(false);
            }
        }
        if (options.visible.isFalse()) {
            if (options.animate.isTrueOrUndefined()) {
                animator.hide(animations);
            } else {
                bottomTabs.hideBottomNavigation(false);
            }
        }
    }

    private void applyDrawBehind(@IntRange(from = 0) int tabIndex) {
        tabs.get(tabIndex).applyBottomInset();
    }

    private void mergeDrawBehind(int tabIndex) {
        tabs.get(tabIndex).applyBottomInset();
    }

    private void applyBottomTabsOptions(BottomTabsOptions options, AnimationsOptions animationsOptions) {
        bottomTabs.setTitleState(options.titleDisplayMode.get(TitleState.SHOW_WHEN_ACTIVE));
        bottomTabs.setBackgroundColor(options.backgroundColor.get(Color.WHITE));
        if (options.currentTabIndex.hasValue()) {
            int tabIndex = options.currentTabIndex.get();
            if (tabIndex >= 0) tabSelector.selectTab(tabIndex);
        }
        if (options.testId.hasValue()) bottomTabs.setTag(options.testId.get());
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
        if (options.elevation.hasValue()) {
            bottomTabs.setUseElevation(true, options.elevation.get().floatValue());
        }
    }
}
