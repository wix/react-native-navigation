package com.reactnativenavigation.views.sharedElementTransition;

import com.reactnativenavigation.params.InterpolationParams;
import com.reactnativenavigation.params.PathInterpolationParams;
import com.reactnativenavigation.utils.ViewUtils;
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
    float controlX1;
    float controlY1;
    float controlX2;
    float controlY2;

    AnimatorValuesResolver(SharedElementTransition from, SharedElementTransition to, InterpolationParams interpolation) {
        fromXy = ViewUtils.getLocationOnScreen(from);
        toXy = ViewUtils.getLocationOnScreen(to);
        calculate(interpolation);
    }

    protected void calculate(InterpolationParams interpolation) {
        calculateDeltas();
        calculateStartPoint();
        calculateEndPoint();
        if (interpolation instanceof PathInterpolationParams) {
            calculateControlPoints((PathInterpolationParams) interpolation);
        }
    }

    private void calculateDeltas() {
        dx = fromXy.x - toXy.x;
        dy = fromXy.y - toXy.y;
    }

    private void calculateEndPoint() {
        endX = 0;
        endY = 0;
    }

    private void calculateStartPoint() {
        startX = dx;
        startY = dy;
    }

    private void calculateControlPoints(PathInterpolationParams interpolation) {
        controlX1 = dx * interpolation.p1.x;
        controlY1 = dy * interpolation.p1.y;
        controlX2 = dx * interpolation.p2.x;
        controlY2 = dy * interpolation.p2.y;
    }

    float[] withOrder(float... values) {
        return values;
    }
}
