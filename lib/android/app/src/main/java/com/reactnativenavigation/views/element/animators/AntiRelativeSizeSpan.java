package com.reactnativenavigation.views.element.animators;

import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import android.util.Log;

public class AntiRelativeSizeSpan extends MetricAffectingSpan {
    private final float size;

    public AntiRelativeSizeSpan(float size) {
        this.size = size;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        updateAnyState(ds);
    }

    @Override
    public void updateMeasureState(TextPaint ds) {
        updateAnyState(ds);
    }

    private void updateAnyState(TextPaint ds) {
        Log.i("AntiRelativeSizeSpan", "updateAnyState: " + size + "|" + ds.density);
        ds.setTextSize(size);
    }
}

