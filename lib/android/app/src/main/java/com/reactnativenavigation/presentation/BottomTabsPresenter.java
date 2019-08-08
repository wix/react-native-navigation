package com.reactnativenavigation.presentation;

import android.graphics.Color;
import android.support.annotation.IntRange;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation.TitleState;
import com.reactnativenavigation.anim.BottomTabsAnimator;
import com.reactnativenavigation.parse.AnimationsOptions;
import com.reactnativenavigation.parse.BottomTabsOptions;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.viewcontrollers.bottomtabs.BottomTabFinder;
import com.reactnativenavigation.viewcontrollers.bottomtabs.TabSelector;
import com.reactnativenavigation.views.BottomTabs;
import com.reactnativenavigation.views.Component;

import java.util.List;

import static com.reactnativenavigation.utils.ViewUtils.getHeight;

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

    public void applyLayoutParamsOptions(Options options, int tabIndex) {
        Options withDefaultOptions = options.copy().withDefaultOptions(defaultOptions);
        applyDrawBehind(withDefaultOptions.bottomTabsOptions, tabIndex);
    }

    public void mergeOptions(Options options) {
        mergeBottomTabsOptions(options);
    }

    public void applyOptions(Options options) {
        applyBottomTabsOptions(options.copy().withDefaultOptions(defaultOptions));
    }

    public void applyChildOptions(Options options, Component child) {
        int tabIndex = bottomTabFinder.findByComponent(child);
        if (tabIndex >= 0) {
            Options withDefaultOptions = options.copy().withDefaultOptions(defaultOptions);
            applyBottomTabsOptions(withDefaultOptions);
            applyDrawBehind(withDefaultOptions.bottomTabsOptions, tabIndex);
        }
    }

    public void mergeChildOptions(Options options, Component child) {
        mergeBottomTabsOptions(options);
        int tabIndex = bottomTabFinder.findByComponent(child);
        if (tabIndex >= 0) mergeDrawBehind(options.bottomTabsOptions, tabIndex);
    }

    private void mergeBottomTabsOptions(Options options) {
        BottomTabsOptions bottomTabsOptions = options.bottomTabsOptions;
        AnimationsOptions animations = options.animations;

        if (options.layout.direction.hasValue()) bottomTabs.setLayoutDirection(options.layout.direction);
        if (bottomTabsOptions.titleDisplayMode.hasValue()) {
            bottomTabs.setTitleState(bottomTabsOptions.titleDisplayMode.toState());
        }
        if (bottomTabsOptions.backgroundColor.hasValue()) {
            bottomTabs.setBackgroundColor(bottomTabsOptions.backgroundColor.get());
        }
        if (bottomTabsOptions.currentTabIndex.hasValue()) {
            int tabIndex = bottomTabsOptions.currentTabIndex.get();
            if (tabIndex >= 0) tabSelector.selectTab(tabIndex);
        }
        if (bottomTabsOptions.testId.hasValue()) {
            bottomTabs.setTag(bottomTabsOptions.testId.get());
        }
        if (bottomTabsOptions.currentTabId.hasValue()) {
            int tabIndex = bottomTabFinder.findByControllerId(bottomTabsOptions.currentTabId.get());
            if (tabIndex >= 0) tabSelector.selectTab(tabIndex);
        }
        if (bottomTabsOptions.visible.isTrue()) {
            if (bottomTabsOptions.animate.isTrueOrUndefined()) {
                animator.show(animations);
            } else {
                bottomTabs.restoreBottomNavigation(false);
            }
        }
        if (bottomTabsOptions.visible.isFalse()) {
            if (bottomTabsOptions.animate.isTrueOrUndefined()) {
                animator.hide(animations);
            } else {
                bottomTabs.hideBottomNavigation(false);
            }
        }
    }

    private void applyDrawBehind(BottomTabsOptions options, @IntRange(from = 0) int tabIndex) {
        ViewGroup tab = tabs.get(tabIndex).getView();
        MarginLayoutParams lp = (MarginLayoutParams) tab.getLayoutParams();
        if (options.drawBehind.isTrue()) {
            lp.bottomMargin = 0;
        } else if (options.visible.isTrueOrUndefined()) {
            lp.bottomMargin = getHeight(bottomTabs);
        }
    }

    private void mergeDrawBehind(BottomTabsOptions options, int tabIndex) {
        ViewGroup tab = tabs.get(tabIndex).getView();
        MarginLayoutParams lp = (MarginLayoutParams) tab.getLayoutParams();
        if (options.drawBehind.isTrue()) {
            lp.bottomMargin = 0;
        } else if (options.visible.isTrue() && options.drawBehind.isFalse()) {
            lp.bottomMargin = getHeight(bottomTabs);
        }
    }

    private void applyBottomTabsOptions(Options options) {
        BottomTabsOptions bottomTabsOptions = options.bottomTabsOptions;
        AnimationsOptions animationsOptions = options.animations;

        bottomTabs.setLayoutDirection(options.layout.direction);
        bottomTabs.setTitleState(bottomTabsOptions.titleDisplayMode.get(TitleState.SHOW_WHEN_ACTIVE));
        bottomTabs.setBackgroundColor(bottomTabsOptions.backgroundColor.get(Color.WHITE));
        if (bottomTabsOptions.currentTabIndex.hasValue()) {
            int tabIndex = bottomTabsOptions.currentTabIndex.get();
            if (tabIndex >= 0) tabSelector.selectTab(tabIndex);
        }
        if (bottomTabsOptions.testId.hasValue()) bottomTabs.setTag(bottomTabsOptions.testId.get());
        if (bottomTabsOptions.currentTabId.hasValue()) {
            int tabIndex = bottomTabFinder.findByControllerId(bottomTabsOptions.currentTabId.get());
            if (tabIndex >= 0) tabSelector.selectTab(tabIndex);
        }
        if (bottomTabsOptions.visible.isTrueOrUndefined()) {
            if (bottomTabsOptions.animate.isTrueOrUndefined()) {
                animator.show(animationsOptions);
            } else {
                bottomTabs.restoreBottomNavigation(false);
            }
        }
        if (bottomTabsOptions.visible.isFalse()) {
            if (bottomTabsOptions.animate.isTrueOrUndefined()) {
                animator.hide(animationsOptions);
            } else {
                bottomTabs.hideBottomNavigation(false);
            }
        }
        if (bottomTabsOptions.elevation.hasValue()) {
            bottomTabs.setUseElevation(true, bottomTabsOptions.elevation.get().floatValue());
        }
    }
}
