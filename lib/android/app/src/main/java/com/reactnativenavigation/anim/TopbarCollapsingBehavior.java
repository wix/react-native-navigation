package com.reactnativenavigation.anim;


import android.view.View;

import com.facebook.react.uimanager.events.EventDispatcher;
import com.reactnativenavigation.interfaces.ScrollEventListener;
import com.reactnativenavigation.utils.UiThread;
import com.reactnativenavigation.views.TopBar;

public class TopbarCollapsingBehavior {
    private TopBar topBar;

    private EventDispatcher eventDispatcher;
    private ScrollEventListener scrollEventListener;
    private boolean dragStarted;
    private TopBarAnimator animator;

    public TopbarCollapsingBehavior(EventDispatcher eventDispatcher, TopBar topBar) {
        this.eventDispatcher = eventDispatcher;
        this.topBar = topBar;
    }

    public void enableCollapsing() {
        scrollEventListener = (new ScrollEventListener(new ScrollEventListener.OnVerticalScrollListener() {
            @Override
            public void onVerticalScroll(int scrollY, int oldScrollY) {
                if (scrollY < 0) return;
                if (!dragStarted) return;

                int measuredHeight = topBar.getMeasuredHeight();
                int diff = scrollY - oldScrollY;
                if (Math.abs(diff) > measuredHeight) {
                    diff = (Math.abs(diff) / diff) * measuredHeight;
                }
                float nextTranslation = topBar.getTranslationY() - diff;
                if (diff < 0) {
                    if (topBar.getVisibility() == View.GONE && nextTranslation > -measuredHeight) {
                        topBar.setVisibility(View.VISIBLE);
                        topBar.setTranslationY(nextTranslation);
                    } else if (nextTranslation <= 0 && nextTranslation >= -measuredHeight) {
                        topBar.setTranslationY(nextTranslation);
                    }
                } else {
                    if (nextTranslation < -measuredHeight && topBar.getVisibility() == View.VISIBLE) {
                        topBar.setVisibility(View.GONE);
                        topBar.setTranslationY(-measuredHeight);
                    } else if (nextTranslation > -measuredHeight && nextTranslation <= 0) {
                        topBar.setTranslationY(nextTranslation);
                    }
                }
            }

            @Override
            public void onDrag(boolean started, double velocity) {
                dragStarted = started;
                UiThread.post(() -> {
                    if (!dragStarted) {
                        if (velocity > 0) {
                            animator.animateShowTopBar(topBar, null, topBar.getTranslationY(), null, 100);
                        } else {
                            animator.animateHideTopBar(topBar, null, topBar.getTranslationY(), null, 100);
                        }
                    }
                });
            }
        }));
        if (eventDispatcher != null) {
            eventDispatcher.addListener(scrollEventListener);
        }
    }

    public void disableCollapsing() {
        if (eventDispatcher != null) {
            eventDispatcher.removeListener(scrollEventListener);
        }
        topBar.setVisibility(View.VISIBLE);
        topBar.setTranslationY(0);
    }
}
