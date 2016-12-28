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

import static android.animation.ObjectAnimator.ofFloat;

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

    List<Animator> createShow() {
        return create(new AnimatorValuesResolver(from, to, to.showInterpolation), to.showInterpolation);
    }

    @NonNull
    private List<Animator> create(AnimatorValuesResolver resolver, InterpolationParams interpolation) {
        List<Animator> result = new ArrayList<>();
        if (shouldAddCurvedMotionAnimator(resolver, interpolation)) {
            result.add(createCurvedMotionAnimator(resolver, interpolation.easing));
        } else {
            if (shouldAddLinearMotionXAnimator(resolver)) {
                result.add(createXAnimator(resolver, interpolation));
            }
            if (shouldAddLinearMotionYAnimator(resolver)) {
                result.add(createYAnimator(resolver, interpolation));
            }
        }
        if (shouldCreateScaleXAnimator(resolver)) {
            result.add(createScaleXAnimator(resolver));
        }
        if (shouldCreateScaleYAnimator(resolver)) {
            result.add(createScaleYAnimator(resolver));
        }
        return result;
    }

    private boolean shouldCreateScaleYAnimator(AnimatorValuesResolver resolver) {
        return resolver.startScaleY != resolver.endScaleY;
    }

    private boolean shouldCreateScaleXAnimator(AnimatorValuesResolver resolver) {
        return resolver.startScaleX != resolver.endScaleX;
    }

    private boolean shouldAddLinearMotionXAnimator(AnimatorValuesResolver resolver) {
        return resolver.dy != 0;
    }

    private boolean shouldAddLinearMotionYAnimator(AnimatorValuesResolver resolver) {
        return resolver.dy != 0;
    }

    private boolean shouldAddCurvedMotionAnimator(AnimatorValuesResolver resolver, InterpolationParams interpolation) {
        return interpolation instanceof PathInterpolationParams && (resolver.dx != 0 || resolver.dy != 0);
    }

    private ObjectAnimator createCurvedMotionAnimator(AnimatorValuesResolver resolver, Easing easing) {
        AnimatorPath path = new AnimatorPath();
        path.moveTo(resolver.startX, resolver.startY);
        path.curveTo(resolver.controlX1, resolver.controlY1, resolver.controlX2, resolver.controlY2, resolver.endX, resolver.endY);
        ObjectAnimator animator = ObjectAnimator.ofObject(
                to,
                "curvedMotion",
                new PathEvaluator(),
                path.getPoints().toArray());
        animator.setInterpolator(easing.getInterpolator());
        return animator;
    }

    private ObjectAnimator createXAnimator(AnimatorValuesResolver resolver, InterpolationParams interpolation) {
        ObjectAnimator animator = ofFloat(to, View.TRANSLATION_X, resolver.startX, resolver.endX);
        animator.setInterpolator(interpolation.easing.getInterpolator());
        return animator;
    }

    private ObjectAnimator createYAnimator(AnimatorValuesResolver resolver, InterpolationParams interpolation) {
        ObjectAnimator animator = ofFloat(to, View.TRANSLATION_Y, resolver.startY, resolver.endY);
        animator.setInterpolator(interpolation.easing.getInterpolator());
        return animator;
    }

    private ObjectAnimator createScaleXAnimator(AnimatorValuesResolver resolver) {
        to.setPivotX(0);
        return ObjectAnimator.ofFloat(to, View.SCALE_X, resolver.startScaleX, resolver.endScaleX);
    }

    private ObjectAnimator createScaleYAnimator(AnimatorValuesResolver resolver) {
        to.setPivotY(0);
        return ObjectAnimator.ofFloat(to, View.SCALE_Y, resolver.startScaleY, resolver.endScaleY);
    }
}
