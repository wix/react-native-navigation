package com.reactnativenavigation.views;

import android.annotation.SuppressLint;
import android.content.Context;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.reactnativenavigation.parse.BottomTabsOptions;
import com.reactnativenavigation.utils.CompatUtils;

@SuppressLint("ViewConstructor")
public class BottomTabs extends AHBottomNavigation {
    public BottomTabs(Context context, BottomTabsOptions bottomTabsOptions) {
        super(context);
        setId(CompatUtils.generateViewId());
        setTag(bottomTabsOptions.testId.get(""));
    }
}
