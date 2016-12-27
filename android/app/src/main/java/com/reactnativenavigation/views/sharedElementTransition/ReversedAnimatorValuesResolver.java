package com.reactnativenavigation.views.sharedElementTransition;

import com.reactnativenavigation.params.InterpolationParams;
import com.reactnativenavigation.params.PathInterpolationParams;

class ReversedAnimatorValuesResolver extends AnimatorValuesResolver {

    ReversedAnimatorValuesResolver(SharedElementTransition from, SharedElementTransition to, InterpolationParams interpolation) {
        super(from, to, interpolation);
    }

    @Override
    protected void calculate(InterpolationParams interpolation) {
        calculateDelta();
        calculateStartPoint();
        calculateEndPoint();
        if (interpolation instanceof PathInterpolationParams) {
            calculateControlPoints((PathInterpolationParams) interpolation);
        }
    }

    private void calculateControlPoints(PathInterpolationParams interpolation) {
        controlX1 = dx * interpolation.p1.x;
        controlY1 = dy * interpolation.p1.y;
        controlX2 = dx * interpolation.p2.x;
        controlY2 = dy * interpolation.p2.y;
    }

    private void calculateEndPoint() {
        endX = dx;
        endY = dy;
    }

    private void calculateStartPoint() {
        startX = 0;
        startY = 0;
    }

    private void calculateDelta() {
        dx = toXy.x - fromXy.x;
        dy = toXy.y - fromXy.y;
    }
}
