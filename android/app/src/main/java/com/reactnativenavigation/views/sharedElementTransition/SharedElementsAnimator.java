package com.reactnativenavigation.views.sharedElementTransition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

public class SharedElementsAnimator {
    private static final String TAG = "SharedElementsAnimator";
    private final SharedElements sharedElements;

    public SharedElementsAnimator(SharedElements sharedElements) {
        this.sharedElements = sharedElements;
    }

    public void show(final Runnable onAnimationEnd) {
        sharedElements.performWhenChildViewsAreDrawn(new Runnable()  {
            @Override
            public void run() {
                final AnimatorSet animatorSet = createAnimatorSet();
                sharedElements.onShowAnimationWillStart();
                sharedElements.attachChildViewsToScreen();
                animatorSet.start();
            }

            private AnimatorSet createAnimatorSet() {
                final AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.setDuration(500);
                animatorSet.playTogether(createTransitionAnimators());
                animatorSet.setInterpolator(new LinearInterpolator());
                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        sharedElements.onShowAnimationStart();
                        sharedElements.hideFromElements();
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        sharedElements.onShowAnimationEnd();
                        onAnimationEnd.run();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        sharedElements.onShowAnimationEnd();
                    }
                });
                return animatorSet;
            }
        });
    }

    public void hide(final Runnable onAnimationEnd) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(500);
        animatorSet.playTogether(createHideTransitionAnimators());
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                sharedElements.showToElements();
                onAnimationEnd.run();
            }
        });
        sharedElements.onHideAnimationStart();
        animatorSet.start();
    }

    private List<Animator> createHideTransitionAnimators() {
        List<Animator> result = new ArrayList<>();
        for (String key : sharedElements.toElements.keySet()) {
            result.addAll(new SharedElementAnimatorCreator(sharedElements.getToElement(key), sharedElements.getFromElement(key)).createHide());
        }
        return result;
    }

    private List<Animator> createTransitionAnimators() {
        List<Animator> result = new ArrayList<>();
        for (String key : sharedElements.toElements.keySet()) {
            SharedElementTransition toElement = sharedElements.getToElement(key);
            SharedElementTransition fromElement = sharedElements.getFromElement(key);
            result.addAll(new SharedElementAnimatorCreator(fromElement, toElement).createShow());
        }
        return result;
    }
}
