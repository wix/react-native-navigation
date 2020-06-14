package com.reactnativenavigation.presentation;

import android.graphics.Color;
import android.view.ViewGroup;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation.TitleState;
import com.reactnativenavigation.anim.BottomTabsAnimator;
import com.reactnativenavigation.parse.AnimationsOptions;
import com.reactnativenavigation.parse.BottomTabsOptions;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.viewcontrollers.bottomtabs.BottomTabFinder;
import com.reactnativenavigation.viewcontrollers.bottomtabs.TabSelector;
import com.reactnativenavigation.views.BottomTabs;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.IntRange;

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

    public void bindView(BottomTabs bottomTabs, TabSelector tabSelector, BottomTabsAnimator animator) {
        this.bottomTabs = bottomTabs;
        this.tabSelector = tabSelector;
        this.animator = animator;
    }

    public void mergeOptions(Options options, ViewController view) {
        mergeBottomTabsOptions(options, view);
    }

    public void applyOptions(Options options) {
        applyBottomTabsOptions(options.copy().withDefaultOptions(defaultOptions));
    }

    public void applyChildOptions(Options options, ViewController child) {
        int tabIndex = bottomTabFinder.findByControllerId(child.getId());
        if (tabIndex >= 0) {
            applyBottomTabsOptions(options.copy().withDefaultOptions(defaultOptions));
            applyDrawBehind(tabIndex);
        }
    }

    public void mergeChildOptions(Options options, ViewController child) {
        mergeBottomTabsOptions(options, child);
        int tabIndex = bottomTabFinder.findByControllerId(child.getId());
        if (tabIndex >= 0) mergeDrawBehind(tabIndex);
    }

    private void mergeBottomTabsOptions(Options options, ViewController view) {
        BottomTabsOptions bottomTabsOptions = options.bottomTabsOptions;
        AnimationsOptions animations = options.animations;

        if (options.layout.direction.hasValue()) bottomTabs.setLayoutDirection(options.layout.direction);
        if (bottomTabsOptions.preferLargeIcons.hasValue()) bottomTabs.setPreferLargeIcons(bottomTabsOptions.preferLargeIcons.get());
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
        if (bottomTabsOptions.hideOnScroll.hasValue()) {
            bottomTabs.setBehaviorTranslationEnabled(bottomTabsOptions.hideOnScroll.get());
        }
        if (view.isViewShown()) {
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
    }

    private void applyDrawBehind(@IntRange(from = 0) int tabIndex) {
        tabs.get(tabIndex).applyBottomInset();
    }

    private void mergeDrawBehind(int tabIndex) {
        tabs.get(tabIndex).applyBottomInset();
    }

    private void applyBottomTabsOptions(Options options) {
        BottomTabsOptions bottomTabsOptions = options.bottomTabsOptions;
        AnimationsOptions animationsOptions = options.animations;

        bottomTabs.setLayoutDirection(options.layout.direction);
        bottomTabs.setPreferLargeIcons(options.bottomTabsOptions.preferLargeIcons.get(false));
        bottomTabs.setTitleState(bottomTabsOptions.titleDisplayMode.get(getDefaultTitleState()));
        bottomTabs.setBackgroundColor(bottomTabsOptions.backgroundColor.get(Color.WHITE));
        if (bottomTabsOptions.currentTabIndex.hasValue()) {
            int tabIndex = bottomTabsOptions.currentTabIndex.get();
            if (tabIndex >= 0) {
                bottomTabsOptions.currentTabIndex.consume();
                tabSelector.selectTab(tabIndex);
            }
        }
        if (bottomTabsOptions.testId.hasValue()) bottomTabs.setTag(bottomTabsOptions.testId.get());
        if (bottomTabsOptions.currentTabId.hasValue()) {
            int tabIndex = bottomTabFinder.findByControllerId(bottomTabsOptions.currentTabId.get());
            if (tabIndex >= 0) {
                bottomTabsOptions.currentTabId.consume();
                tabSelector.selectTab(tabIndex);
            }
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
        bottomTabs.setBehaviorTranslationEnabled(bottomTabsOptions.hideOnScroll.get(false));
    }

    @NotNull
    private TitleState getDefaultTitleState() {
        for (int i = 0; i < bottomTabs.getItemsCount(); i++) {
            if (bottomTabs.getItem(i).hasIcon()) return TitleState.SHOW_WHEN_ACTIVE;
        }
        return TitleState.ALWAYS_SHOW;
    }

    public void applyBottomInset(int bottomInset) {
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) bottomTabs.getLayoutParams();
        lp.bottomMargin = bottomInset;
        bottomTabs.requestLayout();
    }

    public int getBottomInset(Options resolvedOptions) {
        return resolvedOptions.withDefaultOptions(defaultOptions).bottomTabsOptions.isHiddenOrDrawBehind() ? 0 : bottomTabs.getHeight();
    }
}
