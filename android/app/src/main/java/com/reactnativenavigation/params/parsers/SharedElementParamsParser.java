package com.reactnativenavigation.params.parsers;

import android.os.Bundle;

import com.facebook.react.bridge.ReadableMap;
import com.reactnativenavigation.bridge.BundleConverter;

public class SharedElementParamsParser {
    private static final int DEFAULT_DURATION = 300;

    private int showDuration = DEFAULT_DURATION;
    private int hideDuration = DEFAULT_DURATION;
    private Bundle showInterpolation = Bundle.EMPTY;
    private Bundle hideInterpolation = Bundle.EMPTY;
    public boolean animateClipBounds;

    public void setDuration(int duration) {
        showDuration = duration;
        hideDuration = duration;
    }

    public void setShowDuration(int duration) {
        showDuration = duration;
    }

    public void setHideDuration(int duration) {
        hideDuration = duration;
    }

    public void setShowInterpolation(ReadableMap showInterpolation) {
        this.showInterpolation = BundleConverter.toBundle(showInterpolation);
    }

    public void setHideInterpolation(ReadableMap hideInterpolation) {
        this.hideInterpolation = BundleConverter.toBundle(hideInterpolation);
    }

    public SharedElementTransitionParams parseShowTransitionParams() {
        SharedElementTransitionParams result = new SharedElementTransitionParams();
        result.duration = showDuration;
        result.interpolation = new InterpolationParser(showInterpolation).parseShowInterpolation();
        result.animateClipBounds = animateClipBounds;
        return result;
    }

    public SharedElementTransitionParams parseHideTransitionParams() {
        SharedElementTransitionParams result = new SharedElementTransitionParams();
        result.duration = hideDuration;
        result.interpolation = new InterpolationParser(hideInterpolation).parseHideInterpolation();
        result.animateClipBounds = animateClipBounds;
        return result;
    }
}
