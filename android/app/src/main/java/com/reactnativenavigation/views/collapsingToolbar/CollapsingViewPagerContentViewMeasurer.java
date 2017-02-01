package com.reactnativenavigation.views.collapsingToolbar;

import com.reactnativenavigation.params.StyleParams;
import com.reactnativenavigation.screens.Screen;

public class CollapsingViewPagerContentViewMeasurer extends CollapsingViewMeasurer {

    public CollapsingViewPagerContentViewMeasurer(CollapsingTopBar topBar, Screen screen, StyleParams styleParams) {
        super(topBar, screen, styleParams);
    }

    @Override
    public int getMeasuredHeight(int heightMeasureSpec) {
        int height = screenHeight - collapsedTopBarHeight;
//        if (styleParams.bottomTabsHidden) {
            height -= bottomTabsHeight;
//        }
        return height;
    }
}
