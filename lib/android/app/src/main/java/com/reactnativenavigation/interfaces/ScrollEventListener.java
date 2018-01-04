package com.reactnativenavigation.interfaces;

import android.util.Log;

import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.EventDispatcherListener;
import com.facebook.react.views.scroll.ScrollEvent;
import com.reactnativenavigation.utils.ReflectionUtils;

import java.util.Timer;
import java.util.TimerTask;

public class ScrollEventListener implements EventDispatcherListener {

    private OnVerticalScrollListener verticalScrollListener;
    private int prevScrollY = -1;

    private boolean endDrag;

    private static final long SCHEDULE_TIME = 300;
    private Timer timer;
    private DragEndTimerTask timerTask;

    private class DragEndTimerTask extends TimerTask {
        @Override
        public void run() {
            if (endDrag) {
                verticalScrollListener.onDragEnd();
            }
        }
    }

    public interface OnVerticalScrollListener {
        void onVerticalScroll(int scrollY, int oldScrollY);

        void onDragEnd();
    }

    public ScrollEventListener(OnVerticalScrollListener verticalScrollListener) {
        this.verticalScrollListener = verticalScrollListener;
        timer = new Timer();
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
                verticalScrollListener.onVerticalScroll(scrollY, prevScrollY);
                if (scrollY != prevScrollY) {
                    prevScrollY = scrollY;
                }

                timer.cancel();
                timer = new Timer();
                timerTask = new DragEndTimerTask();
                timer.schedule(timerTask, SCHEDULE_TIME);
            } else if ("topScrollBeginDrag".equals(event.getEventName())) {
                endDrag = false;
            } else if ("topScrollEndDrag".equals(event.getEventName())) {
                endDrag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}