package com.reactnativenavigation.views.collapsingToolbar.behaviours;

public class CollapseTitleBarBehaviour implements CollapseBehaviour {
    @Override
    public boolean shouldCollapseOnFling() {
        return true;
    }

    @Override
    public boolean shouldCollapseOnTouchUp() {
        return true;
    }

    @Override
    public boolean canCollapse(int scrollY, int scaledTouchSlop) {
        return true;
    }

    @Override
    public boolean canExpend(int scrollY, int scaledTouchSlop) {
        return true;
    }
}
