package com.reactnativenavigation.views.sharedElementTransition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

public class SharedElementsAnimator {
    private final SharedElements sharedElements;

    public SharedElementsAnimator(SharedElements sharedElements) {
        this.sharedElements = sharedElements;
    }

    private List<Animator> createTransitionAnimators() {
        List<Animator> result = new ArrayList<>();
        for (String key : sharedElements.toElements.keySet()) {
            result.addAll(new SharedElementAnimatorCreator(sharedElements.getFromElement(key), sharedElements.getToElement(key)).createShow());
        }
        return result;
    }

    private List<Animator> createHideTransitionAnimators() {
        List<Animator> result = new ArrayList<>();
        for (String key : sharedElements.toElements.keySet()) {
            result.addAll(new SharedElementAnimatorCreator(sharedElements.getToElement(key), sharedElements.getFromElement(key)).createHide());
        }
        return result;
    }

    public void show(final Runnable onAnimationEnd) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(300);
        animatorSet.playTogether(createTransitionAnimators());
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                sharedElements.hideFromElements();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                onAnimationEnd.run();
            }
        });
        animatorSet.start();
    }

    public void hide(final Runnable onAnimationEnd) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(300);
        animatorSet.playTogether(createHideTransitionAnimators());
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                sharedElements.showToElements();
                onAnimationEnd.run();
            }
        });
        animatorSet.start();
    }
}
