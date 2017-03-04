package com.reactnativenavigation.params;

import java.util.List;

public class ScreenParams extends BaseScreenParams {
    public String tabLabel;
    public List<PageParams> topTabParams;
    public List<String> sharedElementsTransitions;

    public boolean hasTopTabs() {
        return topTabParams != null && !topTabParams.isEmpty();
    }

    public FabParams getFab() {
        return hasTopTabs() ? topTabParams.get(0).fabParams : fabParams;
    }
}
