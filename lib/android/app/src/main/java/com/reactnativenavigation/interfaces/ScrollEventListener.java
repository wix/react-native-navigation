package com.reactnativenavigation.interfaces;

import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.EventDispatcherListener;
import com.facebook.react.views.scroll.ScrollEvent;
import com.reactnativenavigation.utils.ReflectionUtils;

public class ScrollEventListener implements EventDispatcherListener {

    private OnVerticalScrollListener verticalScrollListener;
    private int prevScrollY = -1;

    public interface OnVerticalScrollListener {
        void onVerticalScroll(int scrollY);
    }

    public ScrollEventListener(OnVerticalScrollListener verticalScrollListener) {
        this.verticalScrollListener = verticalScrollListener;
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
                if (scrollY != prevScrollY) {
                    //Log.i("ScrollEventListener", "handleScrollEvent: " + " [scrollY: " + scrollY + "]");
                    prevScrollY = scrollY;
                }
                verticalScrollListener.onVerticalScroll(scrollY);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}