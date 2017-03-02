package com.reactnativenavigation.params.parsers;

import android.os.Bundle;

import com.facebook.react.bridge.ReadableMap;
import com.reactnativenavigation.bridge.BundleConverter;

public class SharedElementParamsParser {
    private static final int DEFAULT_DURATION = 300;

    private int showDuration = DEFAULT_DURATION;
    private int hideDuration = DEFAULT_DURATION;
    private Bundle interpolation = Bundle.EMPTY;

    public void setInterpolation(ReadableMap interpolation) {
        this.interpolation = BundleConverter.toBundle(interpolation);
    }

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

    public SharedElementTransitionParams parseShowTransitionParams() {
        SharedElementTransitionParams result = new SharedElementTransitionParams();
        result.duration = showDuration;
        result.interpolation = new InterpolationParser(interpolation).parseShowInterpolation();
        return result;
    }

    public SharedElementTransitionParams parseHideTransitionParams() {
        SharedElementTransitionParams result = new SharedElementTransitionParams();
        result.duration = hideDuration;
        result.interpolation = new InterpolationParser(interpolation).parseHideInterpolation();
        return result;
    }
}
