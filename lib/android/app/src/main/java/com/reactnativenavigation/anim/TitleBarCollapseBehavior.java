package com.reactnativenavigation.anim;


import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.reactnativenavigation.interfaces.ScrollEventListener;
import com.reactnativenavigation.views.titlebar.TitleBar;

public class TitleBarCollapseBehavior implements ScrollEventListener.OnScrollListener, ScrollEventListener.OnDragListener {
    private TitleBar titleBar;
    private ScrollEventListener scrollEventListener;
    private TitleBarAnimator animator;
    private View parent;
    private int initialParentHeight;

    public TitleBarCollapseBehavior(TitleBar titleBar, View parent) {
        this.titleBar = titleBar;
        this.animator = new TitleBarAnimator(titleBar);
        this.parent = parent;
    }

    public void enableCollapse(ScrollEventListener scrollEventListener) {
        this.scrollEventListener = scrollEventListener;
        this.scrollEventListener.register(titleBar, this, this);
        initialParentHeight = parent.getMeasuredHeight();
    }

    public void disableCollapse() {
        if (scrollEventListener != null) {
            scrollEventListener.unregister(this, this);
            titleBar.setVisibility(View.VISIBLE);
            titleBar.setTranslationY(0);
        }
    }

    @Override
    public void onScrollUp(float nextTranslation) {
        final int measuredHeight = titleBar.getMeasuredHeight();
        if (nextTranslation < -measuredHeight && titleBar.getVisibility() == View.VISIBLE) {
            titleBar.setVisibility(View.GONE);
            titleBar.setTranslationY(-measuredHeight);
            parent.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            parent.requestLayout();
        } else if (nextTranslation > -measuredHeight && nextTranslation <= 0) {
            titleBar.setTranslationY(nextTranslation);
            int newHeight = (int) (parent.getMeasuredHeight() + nextTranslation);
            if (newHeight <= initialParentHeight) {
                parent.getLayoutParams().height = (int) (parent.getMeasuredHeight() + nextTranslation);
                parent.requestLayout();
            }
        }
    }

    @Override
    public void onScrollDown(float nextTranslation) {
        final int measuredHeight = titleBar.getMeasuredHeight();
        if (titleBar.getVisibility() == View.GONE && nextTranslation > -measuredHeight) {
            titleBar.setVisibility(View.VISIBLE);
            titleBar.setTranslationY(nextTranslation);
            parent.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            parent.requestLayout();
        } else if (nextTranslation <= 0 && nextTranslation >= -measuredHeight) {
            titleBar.setTranslationY(nextTranslation);
            int newHeight = (int) (parent.getMeasuredHeight() - nextTranslation);
            if (newHeight <= initialParentHeight) {
                parent.getLayoutParams().height = newHeight;
                parent.requestLayout();
            }
        }
    }

    @Override
    public void onShow() {
        animator.show(titleBar.getTranslationY());
    }

    @Override
    public void onHide() {
        animator.hide(titleBar.getTranslationY());
    }
}
