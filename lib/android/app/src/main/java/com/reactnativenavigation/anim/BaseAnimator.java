package com.reactnativenavigation.anim;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import androidx.annotation.NonNull;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.reactnativenavigation.utils.UiUtils;

import static android.view.View.ALPHA;
import static android.view.View.TRANSLATION_Y;

class BaseAnimator {

    private static final int DURATION = 300;
    private static final TimeInterpolator DECELERATE = new DecelerateInterpolator();
    private static final TimeInterpolator ACCELERATE_DECELERATE = new AccelerateDecelerateInterpolator();

    private float translationY;

    BaseAnimator(Context context) {
        translationY = UiUtils.getWindowHeight(context);
    }

    @NonNull
    AnimatorSet getDefaultPushAnimation(View view) {
        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(DECELERATE);
        set.setDuration(DURATION);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, TRANSLATION_Y, this.translationY, 0);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, ALPHA, 0, 1);
        translationY.setDuration(DURATION);
        alpha.setDuration(DURATION);
        set.playTogether(translationY, alpha);
        return set;
    }


    @NonNull
    AnimatorSet getDefaultPopAnimation(View view) {
        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(ACCELERATE_DECELERATE);
        set.setDuration(DURATION);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, TRANSLATION_Y, 0, this.translationY);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, ALPHA, 1, 0);
        set.playTogether(translationY, alpha);
        return set;
    }
}
