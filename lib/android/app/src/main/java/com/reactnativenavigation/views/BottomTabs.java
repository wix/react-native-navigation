package com.reactnativenavigation.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.view.View;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.reactnativenavigation.BuildConfig;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.utils.CompatUtils;

@SuppressLint("ViewConstructor")
public class BottomTabs extends AHBottomNavigation {
    public BottomTabs(Context context) {
        super(context);
        setId(CompatUtils.generateViewId());
        setContentDescription("BottomTabs");
    }

    public void setTabTestId(int index, Text testId) {
        if (!testId.hasValue()) return;
        View view = getViewAtPosition(index);
        view.setTag(testId.get());
        if (BuildConfig.DEBUG) view.setContentDescription(testId.get());
    }

    public void setBadge(int bottomTabIndex, String badge) {
        setNotification(badge, bottomTabIndex);
    }

    public void setBadgeColor(@ColorInt Integer color) {
        if (color == null) return;
        setNotificationBackgroundColor(color);
    }

    @Override
    public void setCurrentItem(@IntRange(from = 0) int position) {
        super.setCurrentItem(position);
    }

    @Override
    public void setTitleState(TitleState titleState) {
        if (getTitleState() != titleState) super.setTitleState(titleState);
    }
}
