package com.reactnativenavigation.presentation;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v4.content.ContextCompat;

import com.reactnativenavigation.parse.BottomTabOptions;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.utils.ImageLoader;
import com.reactnativenavigation.utils.ImageLoadingListenerAdapter;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.viewcontrollers.bottomtabs.BottomTabFinder;
import com.reactnativenavigation.views.BottomTabs;
import com.reactnativenavigation.views.Component;

import java.util.List;

public class BottomTabPresenter {
    private final Context context;
    private ImageLoader imageLoader;
    private Options defaultOptions;
    private final BottomTabFinder bottomTabFinder;
    private BottomTabs bottomTabs;
    private final int defaultSelectedTextColor;
    private final int defaultTextColor;
    private final List<ViewController> tabs;

    public BottomTabPresenter(Context context, List<ViewController> tabs, ImageLoader imageLoader, Options defaultOptions) {
        this.tabs = tabs;
        this.context = context;
        this.bottomTabFinder = new BottomTabFinder(tabs);
        this.imageLoader = imageLoader;
        this.defaultOptions = defaultOptions;
        defaultSelectedTextColor = defaultOptions.bottomTabOptions.selectedIconColor.get(ContextCompat.getColor(context, com.aurelhubert.ahbottomnavigation.R.color.colorBottomNavigationAccent));
        defaultTextColor = defaultOptions.bottomTabOptions.iconColor.get(ContextCompat.getColor(context, com.aurelhubert.ahbottomnavigation.R.color.colorBottomNavigationInactive));
    }

    public void setDefaultOptions(Options defaultOptions) {
        this.defaultOptions = defaultOptions;
    }

    public void bindView(BottomTabs bottomTabs) {
        this.bottomTabs = bottomTabs;
    }

    public void present() {
        for (int i = 0; i < tabs.size(); i++) {
            BottomTabOptions tab = tabs.get(i).options.copy().withDefaultOptions(defaultOptions).bottomTabOptions;
            bottomTabs.setBadge(i, tab.badge.get(""));
            bottomTabs.setBadgeColor(tab.badgeColor.get(null));
            bottomTabs.setTitleTypeface(i, tab.fontFamily);
            bottomTabs.setIconActiveColor(i, tab.selectedIconColor.get(null));
            bottomTabs.setIconInactiveColor(i, tab.iconColor.get(null));
            bottomTabs.setTitleActiveColor(i, tab.selectedTextColor.get(null));
            bottomTabs.setTitleInactiveColor(i, tab.textColor.get(null));
            bottomTabs.setTitleInactiveTextSizeInSp(i, tab.fontSize.hasValue() ? Float.valueOf(tab.fontSize.get()) : null);
            bottomTabs.setTitleActiveTextSizeInSp(i, tab.selectedFontSize.hasValue() ? Float.valueOf(tab.selectedFontSize.get()) : null);
        }
    }

    public void mergeChildOptions(Options options, Component child) {
        int index = bottomTabFinder.findByComponent(child);
        if (index >= 0) {
            BottomTabOptions bto = options.bottomTabOptions;
            if (bto.badge.hasValue()) bottomTabs.setBadge(index, bto.badge.get());
            if (bto.badgeColor.hasValue()) bottomTabs.setBadgeColor(bto.badgeColor.get());
            if (bto.fontFamily != null) bottomTabs.setTitleTypeface(index, bto.fontFamily);
            if (bto.selectedIconColor.hasValue()) bottomTabs.setIconActiveColor(index, bto.selectedIconColor.get());
            if (bto.iconColor.hasValue()) bottomTabs.setIconInactiveColor(index, bto.iconColor.get());
            if (bto.selectedTextColor.hasValue()) bottomTabs.setTitleActiveColor(index, bto.selectedTextColor.get());
            if (bto.textColor.hasValue()) bottomTabs.setTitleInactiveColor(index, bto.textColor.get());
            if (bto.text.hasValue()) bottomTabs.setText(index, bto.text.get());
            if (bto.icon.hasValue()) imageLoader.loadIcon(context, bto.icon.get(), new ImageLoadingListenerAdapter() {
                @Override
                public void onComplete(@NonNull Drawable drawable) {
                    bottomTabs.setIcon(index, drawable);
                }
            });
        }
    }

    @VisibleForTesting
    public int getDefaultSelectedTextColor() {
        return defaultSelectedTextColor;
    }

    @VisibleForTesting
    public int getDefaultTextColor() {
        return defaultTextColor;
    }
}
