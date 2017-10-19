package com.reactnativenavigation.views.slidingOverlay;

import com.reactnativenavigation.NavigationApplication;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

public class SlidingOverlaysQueue implements SlidingOverlay.SlidingListener{

    private static final int SHORT_SUSTAIN_DURATION = 500;

    protected Timer autoDismissTimer = null;
    protected boolean pendingHide;
    protected Queue<SlidingOverlay> queue = new LinkedList<>();

    public void add(final SlidingOverlay slidingOverlay) {
        NavigationApplication.instance.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                queue.add(slidingOverlay);
                if (queue.size() == 1) {
                    dispatchNextSlidingOverlay();
                }
                else {
                    SlidingOverlay currentOverlay = queue.peek();
                    if (currentOverlay != null && currentOverlay.isVisible()) {
                        if (autoDismissTimer != null) {
                            autoDismissTimer.cancel();
                            autoDismissTimer = null;
                        }
                        currentOverlay.hide();
                    }
                }
            }
        });
    }

    public void remove() {
        NavigationApplication.instance.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                SlidingOverlay currentOverlay = queue.peek();

                if(currentOverlay == null) {
                    return;
                }
                
                if (currentOverlay.isShowing()) {
                    pendingHide = true;
                }
                else if (currentOverlay.isVisible()) {
                    cancelTimer();
                    currentOverlay.hide();
                }
            }
        });
    }

    @Override
    public void onSlidingOverlayShown() {
        Integer autoDismissTimerSec = queue.peek() == null ? null : queue.peek().getAutoDismissTimerSec();

        if (autoDismissTimerSec != null || pendingHide || queue.size() > 1) {
            int autoDismissDuration = autoDismissTimerSec != null
                    ? autoDismissTimerSec * 1000
                    : SHORT_SUSTAIN_DURATION;
            pendingHide = false;

            autoDismissTimer = new Timer();
            autoDismissTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    NavigationApplication.instance.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            if(queue.peek() != null) {
                                queue.peek().hide();
                            }
                        }
                    });
                }
            }, autoDismissDuration);
        }
    }

    @Override
    public void onSlidingOverlayGone() {
        queue.poll();
        dispatchNextSlidingOverlay();
    }

    public void destroy() {
        SlidingOverlay currentOverlay = queue.poll();
        while (!queue.isEmpty()) {
            queue.poll();
        }

        if (currentOverlay != null) {
            cancelTimer();
            currentOverlay.setSlidingListener(null);
            currentOverlay.destroy();
        }
    }

    protected void dispatchNextSlidingOverlay() {
        NavigationApplication.instance.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                final SlidingOverlay nextSlidingOverlay = queue.peek();
                if (nextSlidingOverlay != null) {
                    nextSlidingOverlay.setSlidingListener(SlidingOverlaysQueue.this);
                    nextSlidingOverlay.show();
                }
            }
        });
    }

    protected void cancelTimer() {
        if (autoDismissTimer != null) {
            autoDismissTimer.cancel();
            autoDismissTimer = null;
        }
    }
}
