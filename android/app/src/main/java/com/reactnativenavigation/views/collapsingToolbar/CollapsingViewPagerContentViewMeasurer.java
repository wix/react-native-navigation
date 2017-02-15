package com.reactnativenavigation.views.collapsingToolbar;

import com.reactnativenavigation.params.StyleParams;
import com.reactnativenavigation.screens.Screen;
import com.reactnativenavigation.utils.ViewUtils;

public class CollapsingViewPagerContentViewMeasurer extends CollapsingViewMeasurer {
    private int titleBarHeight;

    public CollapsingViewPagerContentViewMeasurer(final CollapsingTopBar topBar, Screen screen, StyleParams styleParams) {
        super(topBar, screen, styleParams);
        ViewUtils.runOnPreDraw(topBar, new Runnable() {
            @Override
            public void run() {
                titleBarHeight = topBar.getTitleBarHeight();
            }
        });
    }

    @Override
    public int getMeasuredHeight(int heightMeasureSpec) {
        int height = screenHeight - topBar.getCollapsedHeight();
        if (hasBottomTabs() && drawScreenUnderBottomTabs()) {
            height -= bottomTabsHeight;
        }
        if (!styleParams.titleBarHideOnScroll) {
            height -= titleBarHeight;
        }
        return height;
    }

    private boolean drawScreenUnderBottomTabs() {
        return !styleParams.drawScreenAboveBottomTabs;
    }

    private boolean hasBottomTabs() {
        return !styleParams.bottomTabsHidden;
    }
}
