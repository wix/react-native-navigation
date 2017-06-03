package com.reactnativenavigation.views.sharedElementTransition;

import android.graphics.Rect;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.DraweeView;
import com.facebook.react.views.image.ReactImageView;
import com.reactnativenavigation.params.InterpolationParams;
import com.reactnativenavigation.params.PathInterpolationParams;
import com.reactnativenavigation.params.parsers.SharedElementTransitionParams;
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
    int startColor;
    int endColor;
    Rect startDrawingRect = new Rect();
    Rect endDrawingRect = new Rect();
    final Rect fromBounds;
    final Rect toBounds;
    final ScalingUtils.ScaleType fromScaleType;
    final ScalingUtils.ScaleType toScaleType;

    AnimatorValuesResolver(SharedElementTransition from, SharedElementTransition to, SharedElementTransitionParams params) {
        fromXy = ViewUtils.getLocationOnScreen(from.getSharedView());
        toXy = ViewUtils.getLocationOnScreen(to.getSharedView());
        startScaleX = calculateStartScaleX(from, to);
        endScaleX = calculateEndScaleX(from, to);
        startScaleY = calculateStartScaleY(from, to);
        endScaleY = calculateEndScaleY(from, to);
        calculateColor(from, to);
        calculate(params.interpolation);
        fromBounds = calculateBounds(from);
        toBounds = calculateBounds(to);
        fromScaleType = getScaleType(from);
        toScaleType = getScaleType(to);
        calculateDrawingReacts(from, to);
    }

    private ScalingUtils.ScaleType getScaleType(SharedElementTransition view) {
        if (view.getSharedView() instanceof ReactImageView) {
            return ((DraweeView<GenericDraweeHierarchy>) view.getSharedView()).getHierarchy().getActualImageScaleType();
        }
        return null;
    }

    private Rect calculateBounds(SharedElementTransition view) {
        if (view.getSharedView() instanceof ReactImageView) {
            return new Rect(0, 0, view.getSharedView().getWidth(), view.getSharedView().getHeight());
        }
        return null;
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

    private void calculateColor(SharedElementTransition from, SharedElementTransition to) {
        if (from.getSharedView() instanceof TextView && to.getSharedView() instanceof TextView) {
            ForegroundColorSpan[] startColorForegroundColorSpans = ViewUtils.getForegroundColorSpans((TextView) from.getSharedView());
            if (startColorForegroundColorSpans.length > 0) {
                startColor = startColorForegroundColorSpans[0].getForegroundColor();
            }
            ForegroundColorSpan[] endColorForegroundColorSpans = ViewUtils.getForegroundColorSpans((TextView) to.getSharedView());
            if (endColorForegroundColorSpans.length > 0) {
                endColor = endColorForegroundColorSpans[0].getForegroundColor();
            }
        }
    }

    private void calculateDrawingReacts(SharedElementTransition from, SharedElementTransition to) {
        from.getDrawingRect(startDrawingRect);
        to.getDrawingRect(endDrawingRect);
    }
}
