package com.reactnativenavigation.views.bottomtabs;

import static android.view.Gravity.CENTER_HORIZONTAL;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.reactnativenavigation.RNNFeatureToggles;
import com.reactnativenavigation.RNNToggles;

import eightbitlab.com.blurview.BlurTarget;

/**
 * Implementation note: The space-view and its containing linear-layout is a trick meant to force
 * a bottom margin layout which isn't based on a `MarginLayoutParams.bottomMargin`. That's because
 * it is not always honored by the CoordinatorLayout. This appears to be an ok work-around (which
 * BTW actually would have been an idiomatic solution in ComposeUI).
 */
public class BottomTabsLayout extends CoordinatorLayout {

    private BottomTabsContainer bottomTabsContainer;
    private View spaceView;
    private BlurTarget blurSurface;

    public BottomTabsLayout(Context context) {
        super(context);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (bottomTabsContainer != null && child != bottomTabsContainer) {
            if (RNNFeatureToggles.isEnabled(RNNToggles.TAB_BAR_TRANSLUCENCE)) {
                // As loosely explained in BlurView's README, the blur *view* and blur *target* must
                // reside in different sub-sections of the view-hierarchy
                lazyInitBlurSurface();
                blurSurface.addView(child, -1, params);
            } else {
                super.addView(child, getChildCount() - 1, params);
            }
        } else {
            super.addView(child, 0, params);
        }

        if (bottomTabsContainer != null && blurSurface != null) {
            bottomTabsContainer.setBlurSurface(blurSurface);
        }
    }

    public void addBottomTabsContainer(BottomTabsContainer bottomTabsContainer) {
        View spaceView = new View(getContext());
        spaceView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, 0));

        LinearLayout surface = new LinearLayout(getContext());
        surface.setOrientation(LinearLayout.VERTICAL);
        surface.setGravity(CENTER_HORIZONTAL);
        surface.setBackgroundColor(Color.TRANSPARENT);
        surface.addView(bottomTabsContainer, new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        surface.addView(spaceView);

        // Note: Width should always be WRAP_CONTENT so we could delegate the width-related decision
        // making to the bottom-tabs view hierarchy itself (i.e. based on user properties).
        CoordinatorLayout.LayoutParams lp = new CoordinatorLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lp.gravity = Gravity.BOTTOM | CENTER_HORIZONTAL;
        addView(surface, -1, lp);

        this.bottomTabsContainer = bottomTabsContainer;
        this.spaceView = spaceView;
    }

    public void setBottomMargin(int bottomMargin) {
        ((MarginLayoutParams) spaceView.getLayoutParams()).bottomMargin = bottomMargin;
    }

    private void lazyInitBlurSurface() {
        if (blurSurface == null) {
            blurSurface = new BlurTarget(getContext());
            super.addView(blurSurface, super.getChildCount() - 1, new CoordinatorLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        }
    }
}
