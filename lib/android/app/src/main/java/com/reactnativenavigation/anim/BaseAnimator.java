package com.reactnativenavigation.anim;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.reactnativenavigation.parse.AnimationsOptions;
import com.reactnativenavigation.utils.UiUtils;

class BaseAnimator {

    AnimationsOptions options = new AnimationsOptions();

    private static final int DURATION = 300;
    private static final DecelerateInterpolator DECELERATE_INTERPOLATOR = new DecelerateInterpolator();
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();

    private float translationY;

    BaseAnimator(Context context) {
        translationY = UiUtils.getWindowHeight(context);
    }

    @NonNull
    AnimatorSet getDefaultPushAnimation(View view) {
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


    @NonNull
    AnimatorSet getDefaultPopAnimation(View view) {
        AnimatorSet set = new AnimatorSet();

        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, View.ALPHA, 1, 0);
        alpha.setInterpolator(ACCELERATE_INTERPOLATOR);

        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 0, this.translationY);
        translationY.setInterpolator(ACCELERATE_INTERPOLATOR);
        translationY.setDuration(DURATION);
        alpha.setDuration(DURATION);
        set.playTogether(translationY, alpha);
        return set;
    }

    public void setOptions(AnimationsOptions options) {
        this.options = options;
    }
}
