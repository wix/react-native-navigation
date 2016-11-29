package com.reactnativenavigation.params;

import java.util.List;

public class ActivityParams {
    public enum Type {
        SingleScreen, TabBased
    }

    public Type type;
    public ScreenParams screenParams;
    public List<ScreenParams> tabParams;
    public SideMenuParams sideMenuParams;
    public boolean animateShow;
    public int softInputMode;
}
