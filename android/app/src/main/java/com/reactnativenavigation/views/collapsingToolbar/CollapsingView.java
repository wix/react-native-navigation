package com.reactnativenavigation.views.collapsingToolbar;

import android.view.View;

public interface CollapsingView {
    float getFinalCollapseValue();

    float getCurrentCollapseValue();

    View asView();
}
