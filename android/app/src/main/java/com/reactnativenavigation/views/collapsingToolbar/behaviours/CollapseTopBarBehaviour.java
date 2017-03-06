package com.reactnativenavigation.views.collapsingToolbar.behaviours;

public class CollapseTopBarBehaviour implements CollapseBehaviour {
    @Override
    public boolean shouldCollapseOnFling() {
        return false;
    }

    @Override
    public boolean shouldCollapseOnTouchUp() {
        return false;
    }

    @Override
    public boolean canCollapse(int scrollY, int scaledTouchSlop) {
        return scrollY <= scaledTouchSlop;
    }

    @Override
    public boolean canExpend(int scrollY, int scaledTouchSlop) {
        return scrollY <= scaledTouchSlop;
    }
}
