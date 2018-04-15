package com.reactnativenavigation.anim;


import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.reactnativenavigation.interfaces.ScrollEventListener;
import com.reactnativenavigation.views.toptabs.TopTabs;

public class TopTabsCollapseBehavior implements ScrollEventListener.OnScrollListener, ScrollEventListener.OnDragListener {
    private TopTabs topTabs;
    private ScrollEventListener scrollEventListener;
    private TopTabsAnimator animator;
    private View parent;
    private int initialParentHeight;

    public TopTabsCollapseBehavior(TopTabs topTabs, View parent) {
        this.topTabs = topTabs;
        this.animator = new TopTabsAnimator(topTabs);
        this.parent = parent;
    }

    public void enableCollapse(ScrollEventListener scrollEventListener) {
        this.scrollEventListener = scrollEventListener;
        this.scrollEventListener.register(topTabs, this, this);
        initialParentHeight = parent.getMeasuredHeight();
    }

    public void disableCollapse() {
        if (scrollEventListener != null) {
            scrollEventListener.unregister(this, this);
            topTabs.setVisibility(View.VISIBLE);
            topTabs.setTranslationY(0);
        }
    }

    @Override
    public void onScrollUp(float nextTranslation) {
        final int measuredHeight = topTabs.getMeasuredHeight();
        if (nextTranslation < -measuredHeight && topTabs.getVisibility() == View.VISIBLE) {
            topTabs.setVisibility(View.GONE);
            topTabs.setTranslationY(-measuredHeight);
            parent.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            parent.requestLayout();
        } else if (nextTranslation > -measuredHeight && nextTranslation <= 0) {
            topTabs.setTranslationY(nextTranslation);
            parent.getLayoutParams().height = (int) (parent.getMeasuredHeight() + nextTranslation);
            parent.requestLayout();
        }
    }

    @Override
    public void onScrollDown(float nextTranslation) {
        final int measuredHeight = topTabs.getMeasuredHeight();
        if (topTabs.getVisibility() == View.GONE && nextTranslation > -measuredHeight) {
            topTabs.setVisibility(View.VISIBLE);
            topTabs.setTranslationY(nextTranslation);
            parent.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            parent.requestLayout();
        } else if (nextTranslation <= 0 && nextTranslation >= -measuredHeight) {
            topTabs.setTranslationY(nextTranslation);
            int newHeight = (int) (parent.getMeasuredHeight() - nextTranslation);
            if (newHeight <= initialParentHeight) {
                parent.getLayoutParams().height = newHeight;
                parent.requestLayout();
            }
        }
    }

    @Override
    public void onShow() {
        animator.show(topTabs.getTranslationY());
    }

    @Override
    public void onHide() {
        animator.hide(topTabs.getTranslationY());
    }
}
