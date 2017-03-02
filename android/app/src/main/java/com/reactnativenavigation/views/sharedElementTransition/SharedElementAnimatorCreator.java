package com.reactnativenavigation.views.sharedElementTransition;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.view.View;

import com.reactnativenavigation.params.InterpolationParams;
import com.reactnativenavigation.params.InterpolationParams.Easing;
import com.reactnativenavigation.params.PathInterpolationParams;
import com.reactnativenavigation.params.parsers.SharedElementTransitionParams;
import com.reactnativenavigation.views.utils.AnimatorPath;
import com.reactnativenavigation.views.utils.ColorUtils;
import com.reactnativenavigation.views.utils.LabColorEvaluator;
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

    List<Animator> createShow() {
        return create(new AnimatorValuesResolver(from, to, to.showTransitionParams.interpolation), to.showTransitionParams);
    }

    List<Animator> createHide() {
        return create(new ReversedAnimatorValuesResolver(to, from, to.hideTransitionParams.interpolation), to.hideTransitionParams);
    }

    @NonNull
    private List<Animator> create(AnimatorValuesResolver resolver, SharedElementTransitionParams params) {
        List<Animator> result = new ArrayList<>();
        if (shouldAddCurvedMotionAnimator(resolver, params.interpolation)) {
            result.add(createCurvedMotionAnimator(resolver, params.interpolation.easing, params.duration));
        } else {
            if (shouldAddLinearMotionXAnimator(resolver)) {
                result.add(createXAnimator(resolver, params.duration));
            }
            if (shouldAddLinearMotionYAnimator(resolver)) {
                result.add(createYAnimator(resolver, params.duration));
            }
        }
        if (shouldCreateScaleXAnimator(resolver)) {
            result.add(createScaleXAnimator(resolver, params.duration));
        }
        if (shouldCreateScaleYAnimator(resolver)) {
            result.add(createScaleYAnimator(resolver, params.duration));
        }
        if (shouldCreateColorAnimator(resolver)) {
            result.add(createColorAnimator(resolver, params.duration));
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
        return resolver.dx != 0;
    }

    private boolean shouldAddLinearMotionYAnimator(AnimatorValuesResolver resolver) {
        return resolver.dy != 0;
    }

    private boolean shouldAddCurvedMotionAnimator(AnimatorValuesResolver resolver, InterpolationParams interpolation) {
        return interpolation instanceof PathInterpolationParams && (resolver.dx != 0 || resolver.dy != 0);
    }

    private boolean shouldCreateColorAnimator(AnimatorValuesResolver resolver) {
        return resolver.startColor != resolver.endColor;
    }

    private ObjectAnimator createCurvedMotionAnimator(AnimatorValuesResolver resolver, Easing easing, int duration) {
        AnimatorPath path = new AnimatorPath();
        path.moveTo(resolver.startX, resolver.startY);
        path.curveTo(resolver.controlX1, resolver.controlY1, resolver.controlX2, resolver.controlY2, resolver.endX, resolver.endY);
        ObjectAnimator animator = ObjectAnimator.ofObject(
                to,
                "curvedMotion",
                new PathEvaluator(),
                path.getPoints().toArray());
        animator.setInterpolator(easing.getInterpolator());
        animator.setDuration(duration);
        return animator;
    }

    private ObjectAnimator createXAnimator(AnimatorValuesResolver resolver, int duration) {
        return ofFloat(to.getSharedView(), View.TRANSLATION_X, resolver.startX, resolver.endX)
                .setDuration(duration);
    }

    private ObjectAnimator createYAnimator(AnimatorValuesResolver resolver, int duration) {
        return ofFloat(to.getSharedView(), View.TRANSLATION_Y, resolver.startY, resolver.endY)
                .setDuration(duration);
    }

    private ObjectAnimator createScaleXAnimator(AnimatorValuesResolver resolver, int duration) {
        to.getSharedView().setPivotX(0);
        return ObjectAnimator.ofFloat(to.getSharedView(), View.SCALE_X, resolver.startScaleX, resolver.endScaleX)
                .setDuration(duration);
    }

    private ObjectAnimator createScaleYAnimator(AnimatorValuesResolver resolver, int duration) {
        to.getSharedView().setPivotY(0);
        return ObjectAnimator.ofFloat(to.getSharedView(), View.SCALE_Y, resolver.startScaleY, resolver.endScaleY)
                .setDuration(duration);
    }

    private ObjectAnimator createColorAnimator(AnimatorValuesResolver resolver, int duration) {
        return ObjectAnimator.ofObject(
                to,
                "textColor",
                new LabColorEvaluator(),
                ColorUtils.colorToLAB(resolver.startColor),
                ColorUtils.colorToLAB(resolver.endColor))
                .setDuration(duration);
    }
}
