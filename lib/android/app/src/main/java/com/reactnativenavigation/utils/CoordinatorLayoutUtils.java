package com.reactnativenavigation.utils;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class CoordinatorLayoutUtils {
    public static CoordinatorLayout.LayoutParams matchParentLP() {
        return new CoordinatorLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
    }

    public static CoordinatorLayout.LayoutParams matchParentWithBehaviour(CoordinatorLayout.Behavior behavior) {
        CoordinatorLayout.LayoutParams lp = new CoordinatorLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        lp.setBehavior(behavior);
        return lp;
    }
}
