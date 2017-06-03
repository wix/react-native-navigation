package com.reactnativenavigation.views.sharedElementTransition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;

import java.util.ArrayList;
import java.util.List;

public class SharedElementsAnimator {
    private final SharedElements sharedElements;

    public SharedElementsAnimator(SharedElements sharedElements) {
        this.sharedElements = sharedElements;
    }

    public void show(final Runnable onAnimationStart, final Runnable onAnimationEnd) {
        sharedElements.hideToElements();
        sharedElements.performWhenChildViewsAreDrawn(new Runnable()  {
            @Override
            public void run() {
                final AnimatorSet animatorSet = createShowAnimators();
                sharedElements.attachChildViewsToScreen();
                sharedElements.showToElements(new Runnable() {
                    @Override
                    public void run() {
                        sharedElements.hideFromElements();
                        animatorSet.start();
                    }
                });
            }

            private AnimatorSet createShowAnimators() {
                final AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(createTransitionAnimators());
                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        onAnimationStart.run();
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

            private List<Animator> createTransitionAnimators() {
                List<Animator> result = new ArrayList<>();
                for (String key : sharedElements.toElements.keySet()) {
                    SharedElementTransition toElement = sharedElements.getToElement(key);
                    SharedElementTransition fromElement = sharedElements.getFromElement(key);
                    result.addAll(new SharedElementAnimatorCreator(fromElement, toElement).createShow());
                }
                return result;
            }
        });
    }

    public void hide(final Runnable onAnimationStart, final Runnable onAnimationEnd) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(createHideTransitionAnimators());
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                onAnimationStart.run();
            }

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
}
