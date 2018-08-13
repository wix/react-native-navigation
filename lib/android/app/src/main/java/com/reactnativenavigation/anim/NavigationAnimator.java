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

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.reactnativenavigation.utils.CollectionUtils.merge;

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
        view.setAlpha(0);
        AnimatorSet push = animation.content.getAnimation(view, getDefaultPushAnimation(view));
        AnimatorSet set = new AnimatorSet();
        Collection<Animator> elementTransitions = transitionManager.createTransitions(transitions, fromElements, toElements);
        set.playTogether(merge(push.getChildAnimations(), elementTransitions));
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setAlpha(1);
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
