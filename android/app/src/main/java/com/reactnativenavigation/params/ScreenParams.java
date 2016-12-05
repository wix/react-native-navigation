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
}
