package com.reactnativenavigation.views.sharedElementTransition;

import android.view.View;

class ReversedAnimatorValuesResolver extends AnimatorValuesResolver {

    ReversedAnimatorValuesResolver(View from, View to) {
        super(from, to);
    }

    @Override
    protected void calculate() {
        dx = toXy.x - fromXy.x;
        dy = toXy.y - fromXy.y;
        startX = 0;
        startY = 0;
        endX = dx;
        endY = dy;
        control0X = dx / 2;
        control0Y = 0;
        control1X = dx;
        control1Y = dy / 2;
    }
}
