package com.reactnativenavigation.views.sharedElementTransition;

import android.view.View;

import java.util.HashMap;
import java.util.Map;

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

    void onShowAnimationStart() {
        for (SharedElementTransition toElement : toElements.values()) {
            toElement.attachChildToScreen();
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
        }
    }
}
