package com.reactnativenavigation.options.params;

import android.content.res.Resources;

import com.reactnativenavigation.utils.UiUtils;

public class DensityPixel extends Param<Integer> {

    public DensityPixel(int value) {
        super((int) UiUtils.dpToPx(Resources.getSystem().getDisplayMetrics(), value));
    }
}
