package com.reactnativenavigation.views.collapsingToolbar.behaviours;

public class TitleBarHideOnScrollBehaviour implements CollapseBehaviour {
    @Override
    public boolean shouldCollapseOnFling() {
        return true;
    }

    @Override
    public boolean shouldCollapseOnTouchUp() {
        return true;
    }

    @Override
    public boolean canExpend(int scrollY) {
        return true;
    }
}
