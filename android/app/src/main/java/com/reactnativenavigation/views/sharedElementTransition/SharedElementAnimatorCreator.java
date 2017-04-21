package com.reactnativenavigation.views.sharedElementTransition;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.view.View;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.DraweeView;
import com.reactnativenavigation.params.InterpolationParams;
import com.reactnativenavigation.params.PathInterpolationParams;
import com.reactnativenavigation.params.parsers.SharedElementTransitionParams;
import com.reactnativenavigation.views.utils.AnimatorPath;
import com.reactnativenavigation.views.utils.ClipBoundsEvaluator;
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
        return create(new AnimatorValuesResolver(from, to, to.showTransitionParams), to.showTransitionParams);
    }

    List<Animator> createHide() {
        return create(new ReversedAnimatorValuesResolver(to, from, to.hideTransitionParams), to.hideTransitionParams);
    }

    @NonNull
    private List<Animator> create(AnimatorValuesResolver resolver, SharedElementTransitionParams params) {
        List<Animator> result = new ArrayList<>();
        if (shouldAddCurvedMotionAnimator(resolver, params.interpolation)) {
            result.add(createCurvedMotionAnimator(resolver, params));
        } else {
            if (shouldAddLinearMotionXAnimator(resolver)) {
                result.add(createXAnimator(resolver, params));
            }
            if (shouldAddLinearMotionYAnimator(resolver)) {
                result.add(createYAnimator(resolver, params));
            }
        }
        if (shouldAddScaleXAnimator(resolver, params)) {
            result.add(createScaleXAnimator(resolver, params));
        }
        if (shouldAddScaleYAnimator(resolver, params)) {
            result.add(createScaleYAnimator(resolver, params));
        }
        if (shouldAddColorAnimator(resolver)) {
            result.add(createColorAnimator(resolver, params.duration));
        }
        if (shouldAddImageClipBoundsAnimator(params)) {
            result.add(createImageClipBoundsAnimator(resolver, params));
            result.add(createImageTransformAnimator(resolver, params));
        }
        return result;
    }

    private boolean shouldAddScaleYAnimator(AnimatorValuesResolver resolver, SharedElementTransitionParams params) {
        return resolver.startScaleY != resolver.endScaleY && !params.animateClipBounds;
    }

    private boolean shouldAddScaleXAnimator(AnimatorValuesResolver resolver, SharedElementTransitionParams params) {
        return resolver.startScaleX != resolver.endScaleX && !params.animateClipBounds;
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

    private boolean shouldAddColorAnimator(AnimatorValuesResolver resolver) {
        return resolver.startColor != resolver.endColor;
    }

    private boolean shouldAddImageClipBoundsAnimator(SharedElementTransitionParams params) {
        return params.animateClipBounds;
    }

    private ObjectAnimator createCurvedMotionAnimator(AnimatorValuesResolver resolver, SharedElementTransitionParams params) {
        AnimatorPath path = new AnimatorPath();
        path.moveTo(resolver.startX, resolver.startY);
        path.curveTo(resolver.controlX1, resolver.controlY1, resolver.controlX2, resolver.controlY2, resolver.endX, resolver.endY);
        ObjectAnimator animator = ObjectAnimator.ofObject(
                to,
                "curvedMotion",
                new PathEvaluator(),
                path.getPoints().toArray());
        animator.setInterpolator(params.interpolation.easing.getInterpolator());
        animator.setDuration(params.duration);
        return animator;
    }

    private ObjectAnimator createXAnimator(AnimatorValuesResolver resolver, SharedElementTransitionParams params) {
        ObjectAnimator animator = ofFloat(to.getSharedView(), View.TRANSLATION_X, resolver.startX, resolver.endX)
                .setDuration(params.duration);
        animator.setInterpolator(params.interpolation.easing.getInterpolator());
        return animator;
    }

    private ObjectAnimator createYAnimator(AnimatorValuesResolver resolver, SharedElementTransitionParams params) {
        ObjectAnimator animator = ofFloat(to.getSharedView(), View.TRANSLATION_Y, resolver.startY, resolver.endY)
                .setDuration(params.duration);
        animator.setInterpolator(params.interpolation.easing.getInterpolator());
        return animator;
    }

    private ObjectAnimator createScaleXAnimator(AnimatorValuesResolver resolver, SharedElementTransitionParams params) {
        to.getSharedView().setPivotX(0);
        ObjectAnimator animator =
                ofFloat(to.getSharedView(), View.SCALE_X, resolver.startScaleX, resolver.endScaleX)
                        .setDuration(params.duration);
        animator.setInterpolator(params.interpolation.easing.getInterpolator());
        return animator;
    }

    private ObjectAnimator createScaleYAnimator(AnimatorValuesResolver resolver, SharedElementTransitionParams params) {
        to.getSharedView().setPivotY(0);
        ObjectAnimator animator =
                ofFloat(to.getSharedView(), View.SCALE_Y, resolver.startScaleY, resolver.endScaleY)
                        .setDuration(params.duration);
        animator.setInterpolator(params.interpolation.easing.getInterpolator());
        return animator;
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

    private ObjectAnimator createImageClipBoundsAnimator(AnimatorValuesResolver resolver, SharedElementTransitionParams params) {
        ObjectAnimator animator = ObjectAnimator.ofObject(
                to,
                "clipBounds",
                new ClipBoundsEvaluator(),
                resolver.startDrawingRect,
                resolver.endDrawingRect)
                .setDuration(params.duration);
        animator.setInterpolator(params.interpolation.easing.getInterpolator());
        return animator;
    }
    private Animator createImageTransformAnimator(AnimatorValuesResolver resolver, SharedElementTransitionParams params) {
        ScalingUtils.InterpolatingScaleType ist = new ScalingUtils.InterpolatingScaleType(
                resolver.fromScaleType,
                resolver.toScaleType,
                resolver.fromBounds,
                resolver.toBounds
        );
        ((DraweeView<GenericDraweeHierarchy>) to.getSharedView()).getHierarchy().setActualImageScaleType(ist);
        ObjectAnimator animator = ObjectAnimator.ofFloat(to, "matrixTransform", 0, 1).setDuration(params.duration);
        animator.setInterpolator(params.interpolation.easing.getInterpolator());
        return animator;
    }
}
