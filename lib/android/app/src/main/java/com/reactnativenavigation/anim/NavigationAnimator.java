package com.reactnativenavigation.anim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;

import com.reactnativenavigation.parse.AnimationsOptions;

@SuppressWarnings("ResourceType")
public class NavigationAnimator extends BaseAnimator {

    public NavigationAnimator(Context context) {
        super(context);
    }

    public void animatePush(final View view, @Nullable final AnimationListener animationListener) {
        view.setVisibility(View.INVISIBLE);
        AnimatorSet set;
        if (options.push.content.hasValue()) {
            set = options.push.content.getAnimation(view);
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

    public void animatePop(View view, @Nullable final AnimationListener animationListener) {
        AnimatorSet set;
        if (options.pop.content.hasValue()) {
            set = options.pop.content.getAnimation(view);
        } else {
            set = getDefaultPopAnimation(view);
        }
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (animationListener != null) {
                    animationListener.onAnimationEnd();
                }
            }
        });
        set.start();
    }

    public void setOptions(AnimationsOptions options) {
        this.options = options;
    }

    public void mergeOptions(AnimationsOptions options) {
        this.options.mergeWith(options);
    }
}
