package com.reactnativenavigation.views.collapsingToolbar.behaviours;

public class CollapseTopBarBehaviour implements CollapseBehaviour {
    @Override
    public boolean shouldCollapseOnFling() {
        return true;
    }

    @Override
    public boolean shouldCollapseOnTouchUp() {
        return false;
    }

    @Override
    public boolean canExpend(int scrollY) {
        return scrollY == 0;
    }
}
