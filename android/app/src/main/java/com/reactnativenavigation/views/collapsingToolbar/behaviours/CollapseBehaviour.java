package com.reactnativenavigation.views.collapsingToolbar.behaviours;

public interface CollapseBehaviour {
    boolean shouldCollapseOnFling();

    boolean shouldCollapseOnTouchUp();

    boolean canExpend(int scrollY);
}
