package com.reactnativenavigation.views.sharedElementTransition;

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
        this.fromElements = fromElements;
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

    public SharedElements() {
        toElements = new HashMap<>();
        this.fromElements = new HashMap<>();
    }

    public void addToElement(SharedElementTransition sharedElement, String key) {
        toElements.put(key, sharedElement);
    }
}
