package com.reactnativenavigation.views.collapsingToolbar;

import com.reactnativenavigation.screens.Screen;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.views.utils.ViewMeasurer;

public class CollapsingViewMeasurer extends ViewMeasurer {
    private int collapsedTopBarHeight;
    private float getFinalCollapseValue;
    private int screenHeight;

    public CollapsingViewMeasurer(final CollapsingTopBar topBar, final Screen collapsingSingleScreen) {
        ViewUtils.runOnPreDraw(topBar, new Runnable() {
            @Override
            public void run() {
                collapsedTopBarHeight = topBar.getCollapsedHeight();
                getFinalCollapseValue = topBar.getFinalCollapseValue();
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
        return getFinalCollapseValue;
    }

    @Override
    public int getMeasuredHeight(int heightMeasureSpec) {
        return screenHeight - collapsedTopBarHeight;
    }
}
