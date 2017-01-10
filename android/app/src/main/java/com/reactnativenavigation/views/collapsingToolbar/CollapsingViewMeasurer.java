package com.reactnativenavigation.views.collapsingToolbar;

import com.reactnativenavigation.params.StyleParams;
import com.reactnativenavigation.screens.Screen;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.views.utils.ViewMeasurer;

public class CollapsingViewMeasurer extends ViewMeasurer {

    int collapsedTopBarHeight;
    private float finalCollapseValue;
    int screenHeight;
    int bottomTabsHeight = 0;
    boolean bottomTabsHidden;

    public CollapsingViewMeasurer(CollapsingTopBar topBar, Screen screen, StyleParams styleParams) {
        this(topBar, screen);
        bottomTabsHidden = styleParams.bottomTabsHidden;
        bottomTabsHeight = (int) ViewUtils.convertDpToPixel(56);
    }

    public CollapsingViewMeasurer(final CollapsingTopBar topBar, final Screen collapsingSingleScreen) {
        ViewUtils.runOnPreDraw(topBar, new Runnable() {
            @Override
            public void run() {
                collapsedTopBarHeight = topBar.getCollapsedHeight();
                finalCollapseValue = topBar.getFinalCollapseValue();
            }
        });

        ViewUtils.runOnPreDraw(collapsingSingleScreen, new Runnable() {
            @Override
            public void run() {
                screenHeight = collapsingSingleScreen.getMeasuredHeight();
            }
        });
    }

    public float getFinalCollapseValue() {
        return finalCollapseValue;
    }

    @Override
    public int getMeasuredHeight(int heightMeasureSpec) {
        return screenHeight - collapsedTopBarHeight + (bottomTabsHidden ? bottomTabsHeight : 0);
    }
}
