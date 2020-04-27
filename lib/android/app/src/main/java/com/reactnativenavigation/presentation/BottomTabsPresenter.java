package com.reactnativenavigation.presentation;

import android.graphics.Color;
import android.view.ViewGroup;

import com.reactnativenavigation.parse.AnimationsOptions;
import com.reactnativenavigation.parse.BottomTabsOptions;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.viewcontrollers.bottomtabs.BottomTabFinder;
import com.reactnativenavigation.viewcontrollers.bottomtabs.TabSelector;
import com.reactnativenavigation.views.BottomTabs;

import java.util.List;

import androidx.annotation.IntRange;

import org.jetbrains.annotations.NotNull;

public class BottomTabsPresenter {
    private final BottomTabFinder bottomTabFinder;
    private final List<ViewController> tabs;
    private Options defaultOptions;
    private BottomTabs bottomTabs;
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

        if (options.layout.direction.hasValue()) bottomTabs.setLayoutDirection(options.layout.direction);
       if (bottomTabsOptions.titleDisplayMode.hasValue()) {
            bottomTabs.setTitleState(bottomTabsOptions.titleDisplayMode);
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
        if (view.isViewShown()) {
            if (bottomTabsOptions.visible.isTrue()) {
                if (bottomTabsOptions.animate.isTrueOrUndefined()) {
                    bottomTabs.show();
                }
            }
            if (bottomTabsOptions.visible.isFalse()) {
                if (bottomTabsOptions.animate.isTrueOrUndefined()) {
                    bottomTabs.hide();
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
        bottomTabs.setTitleState(new Text(bottomTabsOptions.titleDisplayMode.get(getDefaultTitleState())));
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
                bottomTabs.show();
        }
        if (bottomTabsOptions.visible.isFalse()) {
                bottomTabs.hide();
        }
        if (bottomTabsOptions.elevation.hasValue()) {
            bottomTabs.setElevation(bottomTabsOptions.elevation.get().floatValue());
        }
    }

    @NotNull
    private String getDefaultTitleState() {
        for (int i = 0; i < bottomTabs.getMenu().size(); i++) {
            if (bottomTabs.getMenu().getItem(i).getIcon() != null) return "SHOW_WHEN_ACTIVE";
        }
        return "ALWAYS_SHOW";
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
