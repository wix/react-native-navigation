package com.reactnativenavigation.views.sharedElementTransition;

import android.view.View;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SharedElements {
    Map<String, SharedElementTransition> toElements;
    private Map<String, SharedElementTransition> fromElements;

    Collection<SharedElementTransition> getFromElements() {
        return fromElements.values();
    }

    public void setFromElements(Map<String, SharedElementTransition> fromElements) {
        for (String fromElementKey : fromElements.keySet()) {
            if (toElements.containsKey(fromElementKey)) {
                this.fromElements.put(fromElementKey, fromElements.get(fromElementKey));
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

    void hideFromElements() {
        for (final SharedElementTransition fromElement : getFromElements()) {
            fromElement.post(new Runnable() {
                @Override
                public void run() {
                    fromElement.setVisibility(View.INVISIBLE);
                }
            });
        }
    }
}
