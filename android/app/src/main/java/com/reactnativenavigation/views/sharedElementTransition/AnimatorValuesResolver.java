package com.reactnativenavigation.views.sharedElementTransition;

import android.view.View;

import com.reactnativenavigation.views.utils.Point;

class AnimatorValuesResolver {

    final Point fromXy;
    final Point toXy;
    int dx;
    int dy;
    int startX;
    int startY;
    int endX;
    int endY;
    float control0X;
    float control0Y;
    float control1X;
    float control1Y;

    AnimatorValuesResolver(View from, View to) {
        fromXy = getLocationOnScreen(from);
        toXy = getLocationOnScreen(to);
        calculate();
    }

    protected void calculate() {
        dx = fromXy.x - toXy.x;
        dy = fromXy.y - toXy.y;
        startX = dx;
        startY = dy;
        endX = 0;
        endY = 0;
        control0X = dx / 2;
        control0Y = dy;
        control1X = 0;
        control1Y = dy / 2;
    }

    float[] withOrder(float... values) {
        return values;
    }

    private Point getLocationOnScreen(View view) {
        int[] xy = new int[2];
        view.getLocationOnScreen(xy);
        return new Point(xy[0], xy[1]);
    }
}
