package com.reactnativenavigation.views.bottomtabs;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;

import com.reactnativenavigation.R;

public class BottomTabsLayout extends CoordinatorLayout {
    public BottomTabsLayout(Context context) {
        super(context);
    }

    public int getBottomTabsHeight() {
        return findViewById(R.id.bottomTabs).getHeight();
    }
}
