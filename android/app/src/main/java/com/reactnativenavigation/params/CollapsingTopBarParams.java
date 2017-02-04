package com.reactnativenavigation.params;

import android.support.annotation.Nullable;

import com.reactnativenavigation.views.collapsingToolbar.behaviours.CollapseBehaviour;

public class CollapsingTopBarParams {
    public @Nullable String imageUri;
    public @Nullable String reactViewId;
    public int reactViewHeight;
    public StyleParams.Color scrimColor;
    public CollapseBehaviour collapseBehaviour;
    public boolean expendOnTopTabChange;
    public boolean showTitleWhenCollapsed;

    public boolean hasBackgroundImage() {
        return imageUri != null;
    }

    public boolean hasReactView() {
        return reactViewId != null;
    }
}
