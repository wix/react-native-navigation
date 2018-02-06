package com.reactnativenavigation.views;

import android.content.Context;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.reactnativenavigation.utils.CompatUtils;

public class BottomTabs extends AHBottomNavigation {
    public BottomTabs(Context context) {
        super(context);
        setId(CompatUtils.generateViewId());
    }
}
