package com.reactnativenavigation.presentation;

import android.content.Context;
import android.support.annotation.VisibleForTesting;
import android.support.v4.content.ContextCompat;

import com.reactnativenavigation.parse.BottomTabOptions;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.viewcontrollers.bottomtabs.BottomTabFinder;
import com.reactnativenavigation.views.BottomTabs;
import com.reactnativenavigation.views.Component;

import java.util.List;

public class BottomTabOptionsPresenter {
    private Options defaultOptions;
    private final BottomTabFinder bottomTabFinder;
    private BottomTabs bottomTabs;
    private final int defaultSelectedTabColor;
    private final int defaultTabColor;
    private final List<ViewController> tabs;

    public BottomTabOptionsPresenter(Context context, List<ViewController> tabs, Options defaultOptions) {
        this.tabs = tabs;
        this.bottomTabFinder = new BottomTabFinder(tabs);
        this.defaultOptions = defaultOptions;
        defaultSelectedTabColor = defaultOptions.bottomTabOptions.selectedIconColor.get(ContextCompat.getColor(context, com.aurelhubert.ahbottomnavigation.R.color.colorBottomNavigationAccent));
        defaultTabColor = defaultOptions.bottomTabOptions.iconColor.get(ContextCompat.getColor(context, com.aurelhubert.ahbottomnavigation.R.color.colorBottomNavigationInactive));
    }

    public void setDefaultOptions(Options defaultOptions) {
        this.defaultOptions = defaultOptions;
    }

    public void bindView(BottomTabs bottomTabs) {
        this.bottomTabs = bottomTabs;
    }

    public void present() {
        for (int i = 0; i < tabs.size(); i++) {
            BottomTabOptions bottomTab = tabs.get(i).options.copy().withDefaultOptions(defaultOptions).bottomTabOptions;
            bottomTabs.setBadge(i, bottomTab.badge.get(""));
            bottomTabs.setTitleTypeface(i, bottomTab.fontFamily);
            if (bottomTab.selectedIconColor.hasValue()) bottomTabs.setIconActiveColor(i, bottomTab.selectedIconColor.get());
            if (bottomTab.iconColor.hasValue()) bottomTabs.setIconInactiveColor(i, bottomTab.iconColor.get());
            if (bottomTab.selectedTextColor.hasValue()) bottomTabs.setTitleActiveColor(i, bottomTab.selectedTextColor.get());
            if (bottomTab.textColor.hasValue()) bottomTabs.setTitleInactiveColor(i, bottomTab.textColor.get());
        }
    }

    public void mergeChildOptions(Options options, Component child) {
        BottomTabOptions withDefaultOptions = options.withDefaultOptions(defaultOptions).bottomTabOptions;
        int index = bottomTabFinder.findByComponent(child);
        if (withDefaultOptions.badge.hasValue()) bottomTabs.setBadge(index, withDefaultOptions.badge.get());
        if (withDefaultOptions.fontFamily != null) bottomTabs.setTitleTypeface(index, withDefaultOptions.fontFamily);
        if (withDefaultOptions.selectedIconColor.hasValue()) bottomTabs.setIconActiveColor(index, withDefaultOptions.selectedIconColor.get());
        if (withDefaultOptions.iconColor.hasValue()) bottomTabs.setIconInactiveColor(index, withDefaultOptions.iconColor.get());
        if (withDefaultOptions.selectedTextColor.hasValue()) bottomTabs.setTitleActiveColor(index, withDefaultOptions.selectedTextColor.get());
        if (withDefaultOptions.textColor.hasValue()) bottomTabs.setTitleInactiveColor(index, withDefaultOptions.textColor.get());
    }

    @VisibleForTesting
    public int getDefaultSelectedTabColor() {
        return defaultSelectedTabColor;
    }

    @VisibleForTesting
    public int getDefaultTabColor() {
        return defaultTabColor;
    }
}
