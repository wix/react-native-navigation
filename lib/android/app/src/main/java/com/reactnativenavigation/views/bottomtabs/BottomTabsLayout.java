package com.reactnativenavigation.views.bottomtabs;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import static com.reactnativenavigation.utils.UiUtils.dpToPx;

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

    public void addBottomTabsContainer(BottomTabsContainer bottomTabsContainer) {
        CoordinatorLayout.LayoutParams lp = new CoordinatorLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lp.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        lp.bottomMargin = dpToPx(getContext(), 16); // TODO prop or use ShadowLayout's props (shadow distance?)
        addView(bottomTabsContainer, -1, lp);
        this.bottomTabsContainer = bottomTabsContainer;
  }
}
