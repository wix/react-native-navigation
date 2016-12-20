package com.reactnativenavigation.params;

import android.os.Bundle;

import java.util.List;

public class BaseScreenParams {
    public String screenId;
    public String title;
    public String subtitle;
    public NavigationParams navigationParams;
    public List<TitleBarButtonParams> rightButtons;
    public TitleBarLeftButtonParams leftButton;
    public FabParams fabParams;

    public boolean overrideBackPressInJs;
    public StyleParams styleParams;
    public String fragmentCreatorClassName;
    public Bundle fragmentCreatorPassProps;
    public boolean animateScreenTransitions;

    public boolean isFragmentScreen() {
        return fragmentCreatorClassName != null;
    }

    public String getScreenInstanceId() {
        return navigationParams.screenInstanceId;
    }

    public String getNavigatorId() {
        return navigationParams.navigatorId;
    }

    public String getNavigatorEventId() {
        return navigationParams.navigatorEventId;
    }

    public boolean hasCollapsingTopBar() {
        return styleParams.collapsingTopBarParams != null;
    }
}
