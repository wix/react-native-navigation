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
                result.top = (int) (Math.abs(toHeight - fromHeight) / 2 * (1 - ratio));
                result.bottom = toHeight- result.top;
            } else {
                result.top = (int) (Math.abs(toHeight - fromHeight) / 2 * ratio);
                result.bottom = fromHeight - result.top;
            }
        }

        if (toWidth == fromWidth) {
            result.right = toWidth;
        } else {
            if (toWidth > fromWidth) {
                result.left = (int) (Math.abs(toWidth - fromWidth) / 2 * (1 - ratio));
                result.right = toWidth - result.left;
            } else {
                result.left = (int) (Math.abs(toWidth - fromWidth) / 2 * ratio);
                result.right = fromWidth - result.left;
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
