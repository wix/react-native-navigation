package com.reactnativenavigation.views.utils;

import android.animation.TypeEvaluator;
import android.graphics.Rect;

public class ClipBoundsEvaluator implements TypeEvaluator<Rect> {
    private int fromWidth;
    private int fromHeight;
    private int toWidth;
    private int toHeight;
    private final Rect result = new Rect();

    @Override
    public Rect evaluate(float ratio, Rect from, Rect to) {
        sync(from, to);

        if (toHeight == fromHeight ) {
            result.bottom = toHeight;
        } else {
            if (toHeight > fromHeight) {
                result.bottom = (int) (toHeight - (toHeight - fromHeight) * (1 - ratio));
            } else {
                result.bottom = (int) (toHeight + (fromHeight - toHeight) * (1 - ratio));
            }
        }

        if (toWidth == fromWidth) {
            result.right = toWidth;
        } else {
            if (toWidth > fromWidth) {
                result.right = (int) (toWidth - (toWidth - fromWidth) * (1 - ratio));
            } else {
                result.right = (int) (toWidth + (fromWidth - toWidth) * (1 - ratio));
            }
        }
        return result;
    }

    private void sync(Rect from, Rect to) {
        fromWidth = from.right;
        fromHeight = from.bottom;
        toWidth = to.right;
        toHeight = to.bottom;
    }
}
