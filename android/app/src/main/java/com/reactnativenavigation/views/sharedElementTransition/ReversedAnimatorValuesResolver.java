package com.reactnativenavigation.views.sharedElementTransition;

import com.reactnativenavigation.params.PathInterpolationParams;
import com.reactnativenavigation.params.parsers.SharedElementTransitionParams;

class ReversedAnimatorValuesResolver extends AnimatorValuesResolver {

    ReversedAnimatorValuesResolver(SharedElementTransition from, SharedElementTransition to, SharedElementTransitionParams params) {
        super(from, to, params);
    }

    @Override
    protected void calculateControlPoints(PathInterpolationParams interpolation) {
        controlX1 = dx * interpolation.p1.x;
        controlY1 = dy * interpolation.p1.y;
        controlX2 = dx * interpolation.p2.x;
        controlY2 = dy * interpolation.p2.y;
    }

    @Override
    protected float calculateEndScaleY(SharedElementTransition from, SharedElementTransition to) {
        return ((float) to.getHeight()) / from.getHeight();
    }

    @Override
    protected float calculateStartScaleY(SharedElementTransition from, SharedElementTransition to) {
        return 1;
    }

    @Override
    protected float calculateEndScaleX(SharedElementTransition from, SharedElementTransition to) {
        return ((float) to.getWidth()) / from.getWidth();
    }

    @Override
    protected float calculateStartScaleX(SharedElementTransition from, SharedElementTransition to) {
        return 1;
    }

    @Override
    protected void calculateEndPoint() {
        endX = dx;
        endY = dy;
    }

    @Override
    protected void calculateStartPoint() {
        startX = 0;
        startY = 0;
    }

    @Override
    protected void calculateDeltas() {
        dx = toXy.x - fromXy.x;
        dy = toXy.y - fromXy.y;
    }
}
