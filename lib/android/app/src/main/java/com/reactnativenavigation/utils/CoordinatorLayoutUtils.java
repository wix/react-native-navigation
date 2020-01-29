package com.reactnativenavigation.utils;

import android.view.View;

import com.reactnativenavigation.R;

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

    public static void updateBottomMargin(View view, int additionalMargin) {
        ((CoordinatorLayout.LayoutParams) view.getLayoutParams()).bottomMargin = additionalMargin + ViewTags.get(view, R.id.fab_bottom_margin, 0);
    }
}
