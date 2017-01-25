package com.reactnativenavigation.views.slidingOverlay;

import com.reactnativenavigation.NavigationApplication;

import java.util.LinkedList;
import java.util.Queue;

public class SlidingOverlaysQueue implements SlidingOverlay.SlidingListener{

    protected Queue<SlidingOverlay> queue = new LinkedList<>();

    public void add(final SlidingOverlay slidingOverlay) {
        NavigationApplication.instance.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                queue.add(slidingOverlay);
                if (queue.size() == 1) {
                    dispatchNextSlidingOverlay();
                }
            }
        });
    }

    @Override
    public void onSlidingOverlayGone() {
        queue.poll();
        dispatchNextSlidingOverlay();
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
}
