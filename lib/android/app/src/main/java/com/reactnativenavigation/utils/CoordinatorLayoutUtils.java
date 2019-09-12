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

    public void addGravityParam(CoordinatorLayout.LayoutParams params, int gravityParam) {
        params.gravity = params.gravity | gravityParam;
    }

    public void removeGravityParam(CoordinatorLayout.LayoutParams params, int gravityParam) {
        if ((params.gravity & gravityParam) == gravityParam) {
            params.gravity = params.gravity & ~gravityParam;
        }
    }
}
