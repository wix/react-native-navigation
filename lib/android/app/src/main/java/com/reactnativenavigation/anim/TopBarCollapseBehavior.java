package com.reactnativenavigation.anim;


import android.view.View;

import com.facebook.react.uimanager.events.EventDispatcher;
import com.reactnativenavigation.interfaces.ScrollEventListener;
import com.reactnativenavigation.utils.UiThread;
import com.reactnativenavigation.views.TopBar;

public class TopBarCollapseBehavior {
    private TopBar topBar;

    private EventDispatcher eventDispatcher;
    private ScrollEventListener scrollEventListener;
    private boolean dragStarted;
    private TopBarAnimator animator;

    public TopBarCollapseBehavior(EventDispatcher eventDispatcher, TopBar topBar) {
        this.eventDispatcher = eventDispatcher;
        this.topBar = topBar;
        this.animator = new TopBarAnimator(topBar, null);
    }

    public void enableCollapse() {
        scrollEventListener = (new ScrollEventListener(new ScrollEventListener.OnVerticalScrollListener() {
            @Override
            public void onVerticalScroll(int scrollY, int oldScrollY) {
                if (scrollY < 0) return;
                if (!dragStarted) return;

                final int scrollDiff = calcScrollDiff(scrollY, oldScrollY, topBar.getMeasuredHeight());
                final float nextTranslation = topBar.getTranslationY() - scrollDiff;
                if (scrollDiff < 0) {
                    down(topBar.getMeasuredHeight(), nextTranslation);
                } else {
                    up(topBar.getMeasuredHeight(), nextTranslation);
                }
            }

            @Override
            public void onDrag(boolean started, double velocity) {
                dragStarted = started;
                UiThread.post(() -> {
                    if (!dragStarted) {
                        if (velocity > 0) {
                            animator.show(topBar.getTranslationY(), null, 100);
                        } else {
                            animator.hide(topBar.getTranslationY(), null, 100);
                        }
                    }
                });
            }
        }));
        if (eventDispatcher != null) {
            eventDispatcher.addListener(scrollEventListener);
        }
    }

    private void up(int measuredHeight, float nextTranslation) {
        if (nextTranslation < -measuredHeight && topBar.getVisibility() == View.VISIBLE) {
            topBar.setVisibility(View.GONE);
            topBar.setTranslationY(-measuredHeight);
        } else if (nextTranslation > -measuredHeight && nextTranslation <= 0) {
            topBar.setTranslationY(nextTranslation);
        }
    }

    private void down(int measuredHeight, float nextTranslation) {
        if (topBar.getVisibility() == View.GONE && nextTranslation > -measuredHeight) {
            topBar.setVisibility(View.VISIBLE);
            topBar.setTranslationY(nextTranslation);
        } else if (nextTranslation <= 0 && nextTranslation >= -measuredHeight) {
            topBar.setTranslationY(nextTranslation);
        }
    }

    private int calcScrollDiff(int scrollY, int oldScrollY, int measuredHeight) {
        int diff = scrollY - oldScrollY;
        if (Math.abs(diff) > measuredHeight) {
            diff = (Math.abs(diff) / diff) * measuredHeight;
        }
        return diff;
    }

    public void disableCollapse() {
        if (eventDispatcher != null) {
            eventDispatcher.removeListener(scrollEventListener);
        }
        topBar.setVisibility(View.VISIBLE);
        topBar.setTranslationY(0);
    }
}
