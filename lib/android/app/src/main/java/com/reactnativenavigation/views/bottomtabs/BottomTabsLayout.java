package com.reactnativenavigation.views.bottomtabs;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class BottomTabsLayout extends CoordinatorLayout {

    private BottomTabsContainer bottomTabsContainer;

    public BottomTabsLayout(Context context) {
        super(context);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (bottomTabsContainer != null && child != bottomTabsContainer) {
            super.addView(child, getChildCount() - 1, params);
        } else {
            super.addView(child, 0, params);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (android.os.Build.VERSION.SDK_INT > 34) {
            if (bottomTabsContainer != null) {
                int systemBarInsets = getRootWindowInsets().getSystemWindowInsetBottom();
                bottomTabsContainer.setPadding(0, 0, 0, systemBarInsets);
            }
        }
    }

    public void addBottomTabsContainer(BottomTabsContainer bottomTabsContainer) {
        CoordinatorLayout.LayoutParams lp = new CoordinatorLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        lp.gravity = Gravity.BOTTOM;
        addView(bottomTabsContainer, lp);
        this.bottomTabsContainer = bottomTabsContainer;
    }
}
