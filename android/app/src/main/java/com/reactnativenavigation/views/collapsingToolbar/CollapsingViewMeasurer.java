package com.reactnativenavigation.views.collapsingToolbar;

import com.reactnativenavigation.params.StyleParams;
import com.reactnativenavigation.screens.Screen;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.views.utils.ViewMeasurer;

public class CollapsingViewMeasurer extends ViewMeasurer {
    int screenHeight;
    int bottomTabsHeight = 0;
    CollapsingTopBar topBar;
    protected StyleParams styleParams;

    public CollapsingViewMeasurer(final CollapsingTopBar topBar, final Screen collapsingSingleScreen, StyleParams styleParams) {
        this.topBar = topBar;
        this.styleParams = styleParams;
        bottomTabsHeight = (int) ViewUtils.convertDpToPixel(56);
        ViewUtils.runOnPreDraw(collapsingSingleScreen, new Runnable() {
            @Override
            public void run() {
                screenHeight = collapsingSingleScreen.getHeight();
            }
        });
    }

    public float getFinalCollapseValue() {
        return topBar.getFinalCollapseValue();
    }

    @Override
    public int getMeasuredHeight(int heightMeasureSpec) {
        int height = screenHeight - topBar.getCollapsedHeight();
        if (styleParams.bottomTabsHidden) {
            height += bottomTabsHeight;
        }
        return height;
    }
}
