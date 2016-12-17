package com.reactnativenavigation.params;

import android.graphics.drawable.Drawable;

import com.reactnativenavigation.params.parsers.SharedElementTransitionParams;

import java.util.List;
import java.util.Map;

public class ScreenParams extends BaseScreenParams {
    public String tabLabel;
    public List<PageParams> topTabParams;
    public Map<String, SharedElementTransitionParams> sharedElementsTransitions;

    public boolean hasTopTabs() {
        return topTabParams != null && !topTabParams.isEmpty();
    }

    public FabParams getFab() {
        return hasTopTabs() ? topTabParams.get(0).fabParams : fabParams;
    }
}
