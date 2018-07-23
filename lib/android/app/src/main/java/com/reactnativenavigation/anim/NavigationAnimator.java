package com.reactnativenavigation.anim;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.reactnativenavigation.parse.AnimationOptions;
import com.reactnativenavigation.parse.NestedAnimationsOptions;
import com.reactnativenavigation.parse.Transitions;
import com.reactnativenavigation.views.element.Element;
import com.reactnativenavigation.views.element.ElementTransitionManager;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("ResourceType")
public class NavigationAnimator extends BaseAnimator {

    private final ElementTransitionManager transitionManager;

    public NavigationAnimator(Context context, ElementTransitionManager transitionManager) {
        super(context);
        this.transitionManager = transitionManager;
    }

    public void push(ViewGroup view, NestedAnimationsOptions animation, Runnable onAnimationEnd) {
        push(view, animation, new Transitions(), Collections.EMPTY_LIST, Collections.EMPTY_LIST, onAnimationEnd);
    }

    public void push(ViewGroup view, NestedAnimationsOptions animation, Transitions transitions, List<Element> fromElements, List<Element> toElements, Runnable onAnimationEnd) {
        view.setVisibility(View.INVISIBLE);
        AnimatorSet set = animation.content.getAnimation(view, getDefaultPushAnimation(view));
        set.getChildAnimations().addAll(transitionManager.createElementTransitions(transitions, fromElements, toElements));
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                onAnimationEnd.run();
            }
        });
        set.start();
    }

    public void pop(View view, NestedAnimationsOptions pop, Runnable onAnimationEnd) {
        AnimatorSet set = pop.content.getAnimation(view, getDefaultPopAnimation(view));
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                onAnimationEnd.run();
            }
        });
        set.start();
    }

    public void animateStartApp(View view, AnimationOptions startApp, AnimatorListener listener) {
        view.setVisibility(View.INVISIBLE);
        AnimatorSet set = startApp.getAnimation(view);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                listener.onAnimationEnd(animation);
            }
        });
        set.start();
    }
}
