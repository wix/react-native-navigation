package com.reactnativenavigation.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.IntRange;
import android.view.View;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.utils.CompatUtils;

@SuppressLint("ViewConstructor")
public class BottomTabs extends AHBottomNavigation {
    public BottomTabs(Context context) {
        super(context);
        setId(CompatUtils.generateViewId());
        setContentDescription("BottomTabs");
    }

    public void setTabTag(int index, Text testId) {
        if (!testId.hasValue()) return;
        View viewAtPosition = getViewAtPosition(index);
        if(viewAtPosition!= null) {
            String tag = testId.get();
            viewAtPosition.setTag(tag);
            viewAtPosition.setContentDescription(tag);
        }
    }

    public void setBadge(int bottomTabIndex, Text badge) {
        setNotification(badge.get(), bottomTabIndex);
    }

    @Override
    public void setCurrentItem(@IntRange(from = 0) int position) {
        super.setCurrentItem(position);
    }
}
