package com.reactnativenavigation.params;

import java.util.List;

public class ActivityParams {
    public enum Type {
        SingleScreen, TabBased
    }

    public Type type;
    public ScreenParams screenParams;
    public List<ScreenParams> tabParams;
    public SideMenuParams leftSideMenuParams;
    public SideMenuParams rightSideMenuParams;
    public Integer initialTabIndex;
    public boolean animateShow;
}
