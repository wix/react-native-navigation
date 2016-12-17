package com.reactnativenavigation.views.sharedElementTransition;

import android.support.annotation.NonNull;
import android.view.View;

import com.reactnativenavigation.params.parsers.SharedElementTransitionParams;
import com.reactnativenavigation.utils.Task;
import com.reactnativenavigation.utils.ViewUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SharedElements {
    Map<String, SharedElementTransition> toElements;
    Map<String, SharedElementTransitionParams> fromElements;

    public SharedElementTransition getFromElement(String key) {
        return fromElements.get(key).view;
    }

    public SharedElementTransition getToElement(String key) {
        return toElements.get(key);
    }

    public SharedElements(Map<String, SharedElementTransitionParams> fromElements) {
        toElements = new HashMap<>();
        this.fromElements = fromElements;
    }

    public void addToElement(SharedElementTransition sharedElement, String key) {
        toElements.put(key, sharedElement);
    }

    public void resolveSharedElements(Runnable onComplete) {
        resolveFromViews(onComplete);
    }

    private void resolveFromViews(final Runnable onComplete) {
        List<Integer> tags = getFromElementsViewTags();
        ViewUtils.resolveViewsByTag(tags, new Task<View[]>() {
            @Override
            public void run(View[] fromViews) {
                setFromElementsViews(fromViews);
                onComplete.run();
            }
        });
    }

    @NonNull
    private List<Integer> getFromElementsViewTags() {
        List<Integer> tags = new ArrayList<>();
        for (String key : fromElements.keySet()) {
            tags.add(fromElements.get(key).fromViewTag);
        }
        return tags;
    }

    private void setFromElementsViews(View[] views) {
        int i = 0;
        for (String key : fromElements.keySet()) {
            fromElements.get(key).view = (SharedElementTransition) views[i++];
        }
    }
}
