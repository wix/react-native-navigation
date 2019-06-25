package com.reactnativenavigation.presentation;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.aurelhubert.ahbottomnavigation.notification.AHNotification;
import com.reactnativenavigation.parse.BottomTabOptions;
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
            applyBadge(i, tab);
        }
    }

    public void mergeChildOptions(Options options, Component child) {
        int index = bottomTabFinder.findByComponent(child);
        if (index >= 0) {
            BottomTabOptions bto = options.bottomTabOptions;
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
            if (bto.testId.hasValue()) bottomTabs.setTag(index, bto.testId.get());
            mergeBadge(index, bto);
        }
    }

    private void applyBadge(int tabIndex, BottomTabOptions tab) {
        AHNotification.Builder builder = new AHNotification.Builder();
        builder.setText(tab.badge.get(""));
        builder.setBackgroundColor(tab.badgeColor.get(null));
        builder.setSize(dpToPx(bottomTabs.getContext(), tab.badgeSize.get(AHNotification.NOTIFICATION_SIZE_DEFAULT)));
        bottomTabs.setNotification(builder.build(), tabIndex);
    }

    private void mergeBadge(int index, BottomTabOptions bto) {
        AHNotification.Builder builder = new AHNotification.Builder();
        if (bto.badge.hasValue()) builder.setText(bto.badge.get(""));
        if (bto.badgeColor.hasValue()) builder.setBackgroundColor(bto.badgeColor.get());
        if (bto.badgeSize.hasValue()) builder.setSize(dpToPx(bottomTabs.getContext(), bto.badgeSize.get()));
        AHNotification notification = builder.build();
        if (notification.hasValue()) bottomTabs.setNotification(notification, index);
    }
}
