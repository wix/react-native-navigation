package com.reactnativenavigation.params.parsers;

import com.reactnativenavigation.params.InterpolationParams;
import com.reactnativenavigation.views.sharedElementTransition.SharedElementTransition;

public class SharedElementTransitionParams {
    public int fromViewTag;
    public InterpolationParams interpolation;
    public SharedElementTransition view;
    public String key;

    public SharedElementTransitionParams(int fromViewTag, String key) {
        this.fromViewTag = fromViewTag;
        this.key = key;
    }

    public SharedElementTransitionParams(SharedElementTransition toView, String key) {
        this.view = toView;
        this.key = key;
    }
}
