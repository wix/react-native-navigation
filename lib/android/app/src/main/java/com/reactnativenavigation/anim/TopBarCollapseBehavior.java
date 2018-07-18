package com.reactnativenavigation.anim;


import android.view.View;

import com.reactnativenavigation.interfaces.ScrollEventListener;
import com.reactnativenavigation.views.topbar.TopBar;

public class TopBarCollapseBehavior implements ScrollEventListener.OnScrollListener, ScrollEventListener.OnDragListener {
    private TopBar topBar;
    private ScrollEventListener scrollEventListener;
    private TopBarAnimator animator;

    public TopBarCollapseBehavior(TopBar topBar) {
        this.topBar = topBar;
        this.animator = new TopBarAnimator(topBar);
    }

    public void enableCollapse(ScrollEventListener scrollEventListener) {
        this.scrollEventListener = scrollEventListener;
        this.scrollEventListener.register(topBar, this, this);
    }

    public void disableCollapse() {
        if (scrollEventListener != null) {
            scrollEventListener.unregister();
            topBar.setVisibility(View.VISIBLE);
            topBar.setTranslationY(0);
        }
    }

    @Override
    public void onScrollUp(float nextTranslation) {
        final int measuredHeight = topBar.getMeasuredHeight();
        if (nextTranslation < -measuredHeight && topBar.getVisibility() == View.VISIBLE) {
            topBar.setVisibility(View.GONE);
            topBar.setTranslationY(-measuredHeight);
        } else if (nextTranslation > -measuredHeight && nextTranslation <= 0) {
            topBar.setTranslationY(nextTranslation);
        }
    }

    @Override
    public void onScrollDown(float nextTranslation) {
        final int measuredHeight = topBar.getMeasuredHeight();
        if (topBar.getVisibility() == View.GONE && nextTranslation > -measuredHeight) {
            topBar.setVisibility(View.VISIBLE);
            topBar.setTranslationY(nextTranslation);
        } else if (nextTranslation <= 0 && nextTranslation >= -measuredHeight) {
            topBar.setTranslationY(nextTranslation);
        }
    }

    @Override
    public void onShow() {
        animator.show(topBar.getTranslationY());
    }

    @Override
    public void onHide() {
        animator.hide(topBar.getTranslationY());
    }
}
