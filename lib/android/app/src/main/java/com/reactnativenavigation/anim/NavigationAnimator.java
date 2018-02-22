package com.reactnativenavigation.anim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.reactnativenavigation.parse.AnimationOptions;
import com.reactnativenavigation.parse.AnimationsOptions;
import com.reactnativenavigation.parse.ValueAnimationOptions;
import com.reactnativenavigation.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ResourceType")
public class NavigationAnimator {

    private AnimationsOptions options;

    public interface NavigationAnimationListener {
        void onAnimationEnd();
    }

    private static final int DURATION = 300;
    private static final DecelerateInterpolator DECELERATE_INTERPOLATOR = new DecelerateInterpolator();
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private float translationY;

    public NavigationAnimator(Context context) {
        translationY = UiUtils.getWindowHeight(context);
    }

    public void animatePush(final View view, @Nullable final NavigationAnimationListener animationListener) {
        view.setVisibility(View.INVISIBLE);
        AnimatorSet set;
        if (options != null && !options.push.isEmpty()) {
            set = createAnimation(options.push, view);
        } else {
            set = getDefaultPushAnimation(view);
        }
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (animationListener != null) {
                    animationListener.onAnimationEnd();
                }
            }
        });
        set.start();
    }

    @NonNull
    private AnimatorSet getDefaultPushAnimation(View view) {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, View.ALPHA, 0, 1);
        alpha.setInterpolator(DECELERATE_INTERPOLATOR);

        AnimatorSet set = new AnimatorSet();
        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, this.translationY, 0);
        translationY.setInterpolator(DECELERATE_INTERPOLATOR);
        translationY.setDuration(DURATION);
        alpha.setDuration(DURATION);
        set.playTogether(translationY, alpha);
        return set;
    }

    public void animatePop(View view, @Nullable final NavigationAnimationListener animationListener) {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, View.ALPHA, 1, 0);
        alpha.setInterpolator(ACCELERATE_INTERPOLATOR);

        AnimatorSet set = new AnimatorSet();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (animationListener != null) {
                    animationListener.onAnimationEnd();
                }
            }
        });
        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 0, this.translationY);
        translationY.setInterpolator(ACCELERATE_INTERPOLATOR);
        translationY.setDuration(DURATION);
        alpha.setDuration(DURATION);
        set.playTogether(translationY, alpha);
        set.start();
    }

    public void setOptions(AnimationsOptions options) {
        this.options = options;
    }

    private AnimatorSet createAnimation(AnimationOptions options, View view) {
        AnimatorSet animationSet = new AnimatorSet();
        List<Animator> animators = new ArrayList<>();

        if (!options.alpha.isEmpty()) {
            ObjectAnimator alpha = ObjectAnimator.ofFloat(view, View.ALPHA, options.alpha.from.get(), options.alpha.to.get());
            setUpAnimator(alpha, options.alpha);
            animators.add(alpha);
        }

        if (!options.y.isEmpty()) {
            ObjectAnimator y = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, options.y.from.get(), options.y.to.get());
            setUpAnimator(y, options.y);
            animators.add(y);
        }

        if (!options.x.isEmpty()) {
            ObjectAnimator x = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, options.x.from.get(), options.x.to.get());
            setUpAnimator(x, options.x);
            animators.add(x);
        }

        animationSet.playTogether(animators);
        return animationSet;
    }

    private void setUpAnimator(Animator animator, ValueAnimationOptions options) {
        animator.setInterpolator(options.interpolation.getInterpolator());
        if (options.duration.hasValue())
            animator.setDuration(options.duration.get());
        if (options.startDelay.hasValue())
            animator.setStartDelay(options.startDelay.get());
    }
}
