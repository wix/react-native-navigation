package com.reactnativenavigation.options.params;

import android.content.res.Resources;

import com.reactnativenavigation.utils.UiUtils;

public class PixelDensity extends Param<Integer> {

    public PixelDensity(int value) {
        super((int) UiUtils.dpToPx(Resources.getSystem().getDisplayMetrics(), value));
    }
}
