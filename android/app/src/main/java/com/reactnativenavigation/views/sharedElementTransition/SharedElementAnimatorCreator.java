package com.reactnativenavigation.views.sharedElementTransition;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.view.View;

import com.reactnativenavigation.params.InterpolationParams;
import com.reactnativenavigation.params.InterpolationParams.Easing;
import com.reactnativenavigation.params.PathInterpolationParams;
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

    List<Animator> createHide() {
        return create(new ReversedAnimatorValuesResolver(to, from, to.hideInterpolation), to.hideInterpolation);
    }

    public List<Animator> createShow() {
        return create(new AnimatorValuesResolver(from, to, to.showInterpolation), to.showInterpolation);
    }

    @NonNull
    private List<Animator> create(AnimatorValuesResolver resolver, InterpolationParams interpolation) {
        List<Animator> result = new ArrayList<>();

        if (interpolation instanceof PathInterpolationParams) {
            createCurvedMotionAnimator(result, resolver, interpolation.easing);
        } else {
            createXAnimator(result, resolver, interpolation);
            createYAnimator(result, resolver, interpolation);
        }

//        createScaleXAnimator(result);
//        createScaleYAnimator(result);
        return result;
    }

    private void createCurvedMotionAnimator(List<Animator> result, AnimatorValuesResolver resolver, Easing easing) {
        if (resolver.dx != 0 || resolver.dy != 0) {
            AnimatorPath path = new AnimatorPath();
            path.moveTo(resolver.startX, resolver.startY);
            path.curveTo(resolver.controlX1, resolver.controlY1, resolver.controlX2, resolver.controlY2, resolver.endX, resolver.endY);
            ObjectAnimator animator = ObjectAnimator.ofObject(
                    to,
                    "curvedMotion",
                    new PathEvaluator(),
                    path.getPoints().toArray());
            animator.setInterpolator(easing.getInterpolator());
            result.add(animator);
        }
    }

    private void createXAnimator(List<Animator> result, AnimatorValuesResolver resolver, InterpolationParams interpolation) {
        if (resolver.dx != 0) {
            ObjectAnimator a = ObjectAnimator.ofFloat(to, View.TRANSLATION_X, resolver.startX, resolver.endX);
            a.setInterpolator(interpolation.easing.getInterpolator());
            result.add(a);
        }
    }

    private void createYAnimator(List<Animator> result, AnimatorValuesResolver resolver, InterpolationParams interpolation) {
        if (resolver.dy > 0) {
            ObjectAnimator a = ObjectAnimator.ofFloat(to, View.TRANSLATION_Y, resolver.startY, resolver.endY);
            a.setInterpolator(interpolation.easing.getInterpolator());
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
