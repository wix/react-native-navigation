package com.reactnativenavigation.views.collapsingToolbar;

import android.view.View;

interface CollapsingView {
    float getFinalCollapseValue();

    float getCurrentCollapseValue();

    View asView();
}
