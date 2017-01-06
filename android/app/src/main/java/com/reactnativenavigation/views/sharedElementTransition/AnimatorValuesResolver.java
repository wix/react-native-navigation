package com.reactnativenavigation.views.sharedElementTransition;

import com.reactnativenavigation.params.InterpolationParams;
import com.reactnativenavigation.params.PathInterpolationParams;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.views.utils.Point;

class AnimatorValuesResolver {

    final Point fromXy;
    final Point toXy;
    final float startScaleX;
    final float endScaleX;
    final float startScaleY;
    final float endScaleY;
    int dx;
    int dy;
    int startX;
    int startY;
    int endX;
    int endY;
    float controlX1;
    float controlY1;
    float controlX2;
    float controlY2;

    AnimatorValuesResolver(SharedElementTransition from, SharedElementTransition to, InterpolationParams interpolation) {
        fromXy = ViewUtils.getLocationOnScreen(from.getSharedView());
        toXy = ViewUtils.getLocationOnScreen(to.getSharedView());
        startScaleX = calculateStartScaleX(from, to);
        endScaleX = calculateEndScaleX(from, to);
        startScaleY = calculateStartScaleY(from, to);
        endScaleY = calculateEndScaleY(from, to);
        calculate(interpolation);
    }

    protected float calculateEndScaleY(SharedElementTransition from, SharedElementTransition to) {
        return 1;
    }

    protected float calculateStartScaleY(SharedElementTransition from, SharedElementTransition to) {
        return ((float) from.getHeight()) / to.getHeight();
    }

    protected float calculateEndScaleX(SharedElementTransition from, SharedElementTransition to) {
        return 1;
    }

    protected float calculateStartScaleX(SharedElementTransition from, SharedElementTransition to) {
        return ((float) from.getWidth()) / to.getWidth();
    }

    private void calculate(InterpolationParams interpolation) {
        calculateDeltas();
        calculateStartPoint();
        calculateEndPoint();
        if (interpolation instanceof PathInterpolationParams) {
            calculateControlPoints((PathInterpolationParams) interpolation);
        }
    }

    protected void calculateDeltas() {
        dx = fromXy.x - toXy.x;
        dy = fromXy.y - toXy.y;
    }

    protected void calculateEndPoint() {
        endX = 0;
        endY = 0;
    }

    protected void calculateStartPoint() {
        startX = dx;
        startY = dy;
    }

    protected void calculateControlPoints(PathInterpolationParams interpolation) {
        controlX1 = dx * interpolation.p1.x;
        controlY1 = dy * interpolation.p1.y;
        controlX2 = dx * interpolation.p2.x;
        controlY2 = dy * interpolation.p2.y;
    }
}
