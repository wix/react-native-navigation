package com.reactnativenavigation.params;

import android.graphics.drawable.Drawable;

import java.util.List;

public class ScreenParams extends BaseScreenParams {
    public String tabLabel;
    public Drawable tabIcon;
    public List<PageParams> topTabParams;

    public boolean hasTopTabs() {
        return topTabParams != null && !topTabParams.isEmpty();
    }

    public FabParams getFab() {
        return hasTopTabs() ? topTabParams.get(0).fabParams : null;
    }
}
