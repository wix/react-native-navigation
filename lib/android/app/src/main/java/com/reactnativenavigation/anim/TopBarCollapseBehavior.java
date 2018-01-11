package com.reactnativenavigation.anim;


import android.view.View;

import com.reactnativenavigation.interfaces.ScrollEventListener;
import com.reactnativenavigation.views.TopBar;

public class TopBarCollapseBehavior implements ScrollEventListener.OnScrollListener, ScrollEventListener.OnDragListener {
    private TopBar topBar;
    private ScrollEventListener scrollEventListener;
    private TopBarAnimator animator;

    public TopBarCollapseBehavior(TopBar topBar, ScrollEventListener scrollEventListener) {
        this.topBar = topBar;
        this.animator = new TopBarAnimator(topBar);
        this.scrollEventListener = scrollEventListener;
    }

    public void enableCollapse() {
        scrollEventListener.register(topBar, this, this);
    }

    public void disableCollapse() {
        scrollEventListener.unregister();
        topBar.setVisibility(View.VISIBLE);
        topBar.setTranslationY(0);
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
        animator.show(topBar.getTranslationY(), null, 100);
    }

    @Override
    public void onHide() {
        animator.hide(topBar.getTranslationY(), null, 100);
    }
}
