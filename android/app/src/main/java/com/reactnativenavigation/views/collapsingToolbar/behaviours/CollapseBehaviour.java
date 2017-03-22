package com.reactnativenavigation.views.collapsingToolbar.behaviours;

public interface CollapseBehaviour {
    boolean shouldCollapseOnFling();

    boolean shouldCollapseOnTouchUp();

    boolean canCollapse(int scrollY, int scaledTouchSlop);

    boolean canExpend(int scrollY, int scaledTouchSlop);
}
