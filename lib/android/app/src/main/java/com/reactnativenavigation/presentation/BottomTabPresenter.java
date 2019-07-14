package com.reactnativenavigation.presentation;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.aurelhubert.ahbottomnavigation.notification.AHNotification;
import com.reactnativenavigation.parse.BottomTabOptions;
import com.reactnativenavigation.parse.DotIndicatorOptions;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.utils.ImageLoader;
import com.reactnativenavigation.utils.ImageLoadingListenerAdapter;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.viewcontrollers.bottomtabs.BottomTabFinder;
import com.reactnativenavigation.views.BottomTabs;
import com.reactnativenavigation.views.Component;

import java.util.List;

import static com.reactnativenavigation.utils.UiUtils.dpToPx;

public class BottomTabPresenter {
    private final Context context;
    private ImageLoader imageLoader;
    private Options defaultOptions;
    private final BottomTabFinder bottomTabFinder;
    private BottomTabs bottomTabs;
    private final int defaultSelectedTextColor;
    private final int defaultTextColor;
    private final List<ViewController> tabs;
    private final int defaultDotIndicatorSize;

    public BottomTabPresenter(Context context, List<ViewController> tabs, ImageLoader imageLoader, Options defaultOptions) {
        this.tabs = tabs;
        this.context = context;
        this.bottomTabFinder = new BottomTabFinder(tabs);
        this.imageLoader = imageLoader;
        this.defaultOptions = defaultOptions;
        defaultSelectedTextColor = defaultOptions.bottomTabOptions.selectedIconColor.get(ContextCompat.getColor(context, com.aurelhubert.ahbottomnavigation.R.color.colorBottomNavigationAccent));
        defaultTextColor = defaultOptions.bottomTabOptions.iconColor.get(ContextCompat.getColor(context, com.aurelhubert.ahbottomnavigation.R.color.colorBottomNavigationInactive));
        defaultDotIndicatorSize = dpToPx(context, 6);
    }

    public void setDefaultOptions(Options defaultOptions) {
        this.defaultOptions = defaultOptions;
    }

    public void bindView(BottomTabs bottomTabs) {
        this.bottomTabs = bottomTabs;
    }

    public void applyOptions() {
        for (int i = 0; i < tabs.size(); i++) {
            BottomTabOptions tab = tabs.get(i).resolveCurrentOptions(defaultOptions).bottomTabOptions;
            bottomTabs.setTitleTypeface(i, tab.fontFamily);
            bottomTabs.setIconActiveColor(i, tab.selectedIconColor.get(null));
            bottomTabs.setIconInactiveColor(i, tab.iconColor.get(null));
            bottomTabs.setTitleActiveColor(i, tab.selectedTextColor.get(null));
            bottomTabs.setTitleInactiveColor(i, tab.textColor.get(null));
            bottomTabs.setTitleInactiveTextSizeInSp(i, tab.fontSize.hasValue() ? Float.valueOf(tab.fontSize.get()) : null);
            bottomTabs.setTitleActiveTextSizeInSp(i, tab.selectedFontSize.hasValue() ? Float.valueOf(tab.selectedFontSize.get()) : null);
            if (tab.testId.hasValue()) bottomTabs.setTag(i, tab.testId.get());
            if (shouldApplyDot(tab)) applyDotIndicator(i, tab.dotIndicator); else applyBadge(i, tab);
        }
    }

    public void mergeChildOptions(Options options, Component child) {
        int index = bottomTabFinder.findByComponent(child);
        if (index >= 0) {
            BottomTabOptions tab = options.bottomTabOptions;
            if (tab.fontFamily != null) bottomTabs.setTitleTypeface(index, tab.fontFamily);
            if (tab.selectedIconColor.hasValue()) bottomTabs.setIconActiveColor(index, tab.selectedIconColor.get());
            if (tab.iconColor.hasValue()) bottomTabs.setIconInactiveColor(index, tab.iconColor.get());
            if (tab.selectedTextColor.hasValue()) bottomTabs.setTitleActiveColor(index, tab.selectedTextColor.get());
            if (tab.textColor.hasValue()) bottomTabs.setTitleInactiveColor(index, tab.textColor.get());
            if (tab.text.hasValue()) bottomTabs.setText(index, tab.text.get());
            if (tab.icon.hasValue()) imageLoader.loadIcon(context, tab.icon.get(), new ImageLoadingListenerAdapter() {
                @Override
                public void onComplete(@NonNull Drawable drawable) {
                    bottomTabs.setIcon(index, drawable);
                }
            });
            if (tab.testId.hasValue()) bottomTabs.setTag(index, tab.testId.get());
            if (shouldApplyDot(tab)) mergeDotIndicator(index, tab.dotIndicator); else mergeBadge(index, tab);
        }
    }

    private void applyDotIndicator(int tabIndex, DotIndicatorOptions dotIndicator) {
        AHNotification.Builder builder = new AHNotification.Builder()
                .setText("")
                .setBackgroundColor(dotIndicator.color.get(null))
                .setSize(dotIndicator.size.get(defaultDotIndicatorSize));
        bottomTabs.setNotification(builder.build(), tabIndex);
    }

    private void applyBadge(int tabIndex, BottomTabOptions tab) {
        AHNotification.Builder builder = new AHNotification.Builder()
                .setText(tab.badge.get(""))
                .setBackgroundColor(tab.badgeColor.get(null));
        bottomTabs.setNotification(builder.build(), tabIndex);
    }

    private void mergeBadge(int index, BottomTabOptions tab) {
        if (!tab.badge.hasValue()) return;
        AHNotification.Builder builder = new AHNotification.Builder();
        if (tab.badge.hasValue()) builder.setText(tab.badge.get());
        if (tab.badgeColor.hasValue()) builder.setBackgroundColor(tab.badgeColor.get());
        bottomTabs.setNotification(builder.build(), index);
    }

    private void mergeDotIndicator(int index, DotIndicatorOptions dotIndicator) {
        AHNotification.Builder builder = new AHNotification.Builder();
        if (dotIndicator.color.hasValue()) builder.setBackgroundColor(dotIndicator.color.get());
        builder.setSize(dotIndicator.visible.isFalse() ? 0 : dotIndicator.size.get(defaultDotIndicatorSize));
        AHNotification notification = builder.build();
        if (notification.hasValue()) bottomTabs.setNotification(notification, index);
    }

    private boolean shouldApplyDot(BottomTabOptions tab) {
        return tab.dotIndicator.visible.hasValue() && !tab.badge.hasValue();
    }
}
