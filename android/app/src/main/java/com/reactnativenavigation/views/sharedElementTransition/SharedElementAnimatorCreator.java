package com.reactnativenavigation.views.sharedElementTransition;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.view.View;

import com.reactnativenavigation.views.utils.AnimatorPath;
import com.reactnativenavigation.views.utils.PathEvaluator;

import java.util.ArrayList;
import java.util.List;

class SharedElementAnimatorCreator {
    private final SharedElementTransition from;
    private final SharedElementTransition to;

    SharedElementAnimatorCreator(SharedElementTransition from, SharedElementTransition to) {
        this.from = from;
        this.to = to;
    }

    List<Animator> createReverse() {
        return create(new ReversedAnimatorValuesResolver(to, from));
    }

    public List<Animator> create() {
        return create(new AnimatorValuesResolver(from, to));
    }

    @NonNull
    private List<Animator> create(AnimatorValuesResolver resolver) {
        List<Animator> result = new ArrayList<>();
//        createXAnimator(result, orderedAnimationValues);
//        createYAnimator(result, orderedAnimationValues);

        createCurvedMotionAnimator(result, resolver);

//        createScaleXAnimator(result);
//        createScaleYAnimator(result);
        return result;
    }

    private void createCurvedMotionAnimator(List<Animator> result, AnimatorValuesResolver resolver) {
        if (resolver.dx != 0 || resolver.dy != 0) {
            AnimatorPath path = new AnimatorPath();
            path.moveTo(resolver.startX, resolver.startY);
            path.curveTo(resolver.control0X, resolver.control0Y, resolver.control1X, resolver.control1Y, resolver.endX, resolver.endY);
            ObjectAnimator a = ObjectAnimator.ofObject(
                    to,
                    "curvedMotion",
                    new PathEvaluator(),
                    path.getPoints().toArray());
//            a.setInterpolator(to.interpolation.get());
            result.add(a);
        }
    }

    private void createXAnimator(List<Animator> result, AnimatorValuesResolver values) {
        int[] fromXY = new int[2];
        int[] toXY = new int[2];
        from.getLocationOnScreen(fromXY);
        to.getLocationOnScreen(toXY);

        final float fromX = fromXY[0];
        final float toX = toXY[0];
        if (fromX != toX) {
            ObjectAnimator a = ObjectAnimator.ofFloat(to, View.TRANSLATION_X, values.withOrder(fromX - toX, 0));
            a.setInterpolator(to.interpolation.get());
            result.add(a);
        }
    }

    private void createYAnimator(List<Animator> result, AnimatorValuesResolver values) {
        int[] fromXY = new int[2];
        int[] toXY = new int[2];
        from.getLocationOnScreen(fromXY);
        to.getLocationOnScreen(toXY);
        final int fromY = fromXY[1];
        final int toY = toXY[1];
        if (fromY != toY) {
            ObjectAnimator a = ObjectAnimator.ofFloat(to, View.TRANSLATION_Y, values.withOrder(fromY - toY, 0));
            a.setInterpolator(to.interpolation.get());
            result.add(a);
        }
    }

    private void createScaleXAnimator(List<Animator> result) {
        final int fromWidth = from.getWidth();
        final int toWidth = to.getWidth();
        if (fromWidth != toWidth) {
//            to.setPivotX(from.getX());
            result.add(ObjectAnimator.ofFloat(to, View.SCALE_X, (float) (fromWidth / toWidth), 1));
        }
    }

    private void createScaleYAnimator(List<Animator> result) {
        final int fromHeight = from.getHeight();
        final int toHeight = to.getHeight();
        if (fromHeight != toHeight) {
//            to.setPivotY(from.getY());
            result.add(ObjectAnimator.ofFloat(to, View.SCALE_Y, (float) (fromHeight / toHeight), 1));
        }
    }
}
