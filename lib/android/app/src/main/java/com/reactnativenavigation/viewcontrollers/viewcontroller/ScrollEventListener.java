package com.reactnativenavigation.viewcontrollers.viewcontroller;

import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.EventDispatcher;
import com.facebook.react.uimanager.events.EventDispatcherListener;
import com.facebook.react.views.scroll.ScrollEvent;
import com.reactnativenavigation.utils.ReflectionUtils;
import com.reactnativenavigation.utils.UiThread;

public class ScrollEventListener implements EventDispatcherListener {

    public interface OnScrollListener {
        void onScrollUp(float nextTranslation);

        void onScrollDown(float nextTranslation);
    }

    public interface OnDragListener {
        void onShow();

        void onHide();
    }

    public interface ScrollAwareView {
        int getMeasuredHeight();

        float getTranslationY();
    }

    private ScrollAwareView view;
    private OnScrollListener onScrollListener;
    private OnDragListener dragListener;
    private EventDispatcher eventDispatcher;
    private int prevScrollY = -1;
    private boolean dragStarted;

    public ScrollEventListener(EventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    public void register(ScrollAwareView scrollAwareView, OnScrollListener scrollListener, OnDragListener dragListener) {
        this.view = scrollAwareView;
        this.onScrollListener = scrollListener;
        this.dragListener = dragListener;
        eventDispatcher.addListener(this);
    }

    public void unregister() {
        eventDispatcher.removeListener(this);
    }

    @Override
    public void onEventDispatch(Event event) {
        if (event instanceof ScrollEvent) {
            handleScrollEvent((ScrollEvent) event);
        }
    }

    private void handleScrollEvent(ScrollEvent event) {
        try {
            if ("topScroll".equals(event.getEventName())) {
                int scrollY = (int) ReflectionUtils.getDeclaredField(event, "mScrollY");
                onVerticalScroll(scrollY, prevScrollY);
                if (scrollY != prevScrollY) {
                    prevScrollY = scrollY;
                }
            } else if ("topScrollBeginDrag".equals(event.getEventName())) {
                double velocity = (double) ReflectionUtils.getDeclaredField(event, "mYVelocity");
                onDrag(true, velocity);
            } else if ("topScrollEndDrag".equals(event.getEventName())) {
                double velocity = (double) ReflectionUtils.getDeclaredField(event, "mYVelocity");
                onDrag(false, velocity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onVerticalScroll(int scrollY, int oldScrollY) {
        if (scrollY < 0) return;
        if (!dragStarted) return;
        if (view == null) return;

        final int scrollDiff = calcScrollDiff(scrollY, oldScrollY, view.getMeasuredHeight());
        final float translationY = view.getTranslationY() - scrollDiff;
        if (scrollDiff < 0) {
            onScrollListener.onScrollDown(translationY);
        } else {
            onScrollListener.onScrollUp(translationY);
        }
    }

    private int calcScrollDiff(int scrollY, int oldScrollY, int measuredHeight) {
        int diff = scrollY - oldScrollY;
        if (Math.abs(diff) > measuredHeight) {
            diff = (Math.abs(diff) / diff) * measuredHeight;
        }
        return diff;
    }

    private void onDrag(boolean started, double velocity) {
        dragStarted = started;
        UiThread.post(() -> {
            if (!dragStarted) {
                if (velocity > 0) {
                    dragListener.onShow();
                } else {
                    dragListener.onHide();
                }
            }
        });
    }
}