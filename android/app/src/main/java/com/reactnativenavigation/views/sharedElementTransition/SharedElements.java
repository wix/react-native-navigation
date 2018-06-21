package com.reactnativenavigation.views.sharedElementTransition;

import android.view.View;

import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.utils.ViewVisibilityChecker;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class SharedElements {
    // These need to be weak references or better yet - clear them in `onViewRemoved`
    Map<String, SharedElementTransition> toElements;
    private Map<String, SharedElementTransition> fromElements;

    public void setFromElements(Map<String, SharedElementTransition> fromElements) {
        this.fromElements.clear();
        for (String fromElementKey : fromElements.keySet()) {
            if (toElements.containsKey(fromElementKey)) {
                this.fromElements.put(fromElementKey, fromElements.get(fromElementKey));
            }
        }
    }

    public void setToElements(Map<String, SharedElementTransition> toElements) {
        this.toElements.clear();
        for (String toElementKey : toElements.keySet()) {
            if (fromElements.containsKey(toElementKey)) {
                this.toElements.put(toElementKey, toElements.get(toElementKey));
            }
        }
    }

    public Map<String, SharedElementTransition> getToElements() {
        return toElements;
    }

    SharedElementTransition getFromElement(String key) {
        return fromElements.get(key);
    }

    SharedElementTransition getToElement(String key) {
        return toElements.get(key);
    }

    public void addToElement(SharedElementTransition sharedElement, String key) {
        toElements.put(key, sharedElement);
    }

    public SharedElements() {
        toElements = new HashMap<>();
        this.fromElements = new HashMap<>();
    }

    void performWhenChildViewsAreDrawn(final Runnable onReady) {
        final AtomicInteger latch = new AtomicInteger(toElements.size());
        for (SharedElementTransition toElement : toElements.values()) {
            ViewUtils.runOnPreDraw(toElement.getSharedView(), new Runnable() {
                @Override
                public void run() {
                    if (latch.decrementAndGet() == 0) {
                        onReady.run();
                    }
                }
            });
        }
    }

    void attachChildViewsToScreen() {
        for (SharedElementTransition toElement : toElements.values()) {
            toElement.attachChildToScreen();
        }
    }

    void hideToElements() {
        for (SharedElementTransition toElement : toElements.values()) {
            toElement.hide();
        }
    }

    void showToElements(final Runnable onReady) {
        final AtomicInteger latch = new AtomicInteger(toElements.size());
        for (final SharedElementTransition toElement : toElements.values()) {
            toElement.show();
            ViewUtils.runOnPreDraw(toElement, new Runnable() {
                @Override
                public void run() {
                    if (latch.decrementAndGet() == 0) {
                        onReady.run();
                    }
                }
            });
        }
    }

    void onShowAnimationEnd() {
        for (SharedElementTransition toElement : toElements.values()) {
            toElement.attachChildToSelf();
        }
    }

    void hideFromElements() {
        for (final SharedElementTransition fromElement : fromElements.values()) {
            fromElement.post(new Runnable() {
                @Override
                public void run() {
                    fromElement.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    void showToElements() {
        for (SharedElementTransition toElement : toElements.values()) {
            toElement.setVisibility(View.VISIBLE);
        }
    }

    void onHideAnimationStart() {
        for (SharedElementTransition fromElement : fromElements.values()) {
            fromElement.attachChildToScreen();
            fromElement.getSharedView().setAlpha(1);
        }
    }

    public void destroy() {
        toElements.clear();
        fromElements.clear();
    }

    public void removeHiddenElements() {
        Iterator<String> iterator = toElements.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (!ViewVisibilityChecker.check(toElements.get(key)) && fromElements.get(key) != null) {
                iterator.remove();
                fromElements.get(key).show();
            }
        }
    }
}
