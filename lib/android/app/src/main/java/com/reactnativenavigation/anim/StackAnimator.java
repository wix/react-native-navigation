package com.reactnativenavigation.anim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Context;
import android.view.View;

import com.reactnativenavigation.parse.AnimationOptions;
import com.reactnativenavigation.parse.FadeAnimation;
import com.reactnativenavigation.parse.NestedAnimationsOptions;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.views.element.ElementTransitionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.RestrictTo;

import static com.reactnativenavigation.utils.CollectionUtils.*;

@SuppressWarnings("ResourceType")
public class StackAnimator extends BaseAnimator {

    private final ElementTransitionManager transitionManager;
    private Map<View, Animator> runningPushAnimations = new HashMap<>();

    public StackAnimator(Context context, ElementTransitionManager transitionManager) {
        super(context);
        this.transitionManager = transitionManager;
    }

    public void push(ViewController appearing, ViewController disappearing, Options options, Runnable onAnimationEnd) {
        AnimatorSet set = createPushAnimator(appearing, onAnimationEnd);
        runningPushAnimations.put(appearing.getView(), set);
        if (options.animations.push.sharedElements.hasValue()) {
            pushWithElementTransition(appearing, disappearing, options, set);
        } else {
            pushWithoutElementTransitions(appearing, options, set);
        }
    }

    private AnimatorSet createPushAnimator(ViewController appearing, Runnable onAnimationEnd) {
        AnimatorSet set = new AnimatorSet();
        set.addListener(new AnimatorListenerAdapter() {
            private boolean isCancelled;

            @Override
            public void onAnimationCancel(Animator animation) {
                isCancelled = true;
                runningPushAnimations.remove(appearing.getView());
                onAnimationEnd.run();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isCancelled) {
                    runningPushAnimations.remove(appearing.getView());
                    onAnimationEnd.run();
                }
            }
        });
        return set;
    }

    private void pushWithElementTransition(ViewController appearing, ViewController disappearing, Options options, AnimatorSet set) {
        appearing.getView().setAlpha(0);
        transitionManager.createTransitions(
                options.animations.push,
                disappearing,
                appearing,
                transitionSet -> {
                    if (transitionSet.isEmpty()) {
                        set.playTogether(options.animations.push.content.getAnimation(appearing.getView(), getDefaultPushAnimation(appearing.getView())));
                    } else {
                        AnimationOptions fade = options.animations.push.content.isFadeAnimation() ? options.animations.push.content : new FadeAnimation().content;
                        AnimatorSet transitions = transitionManager.createAnimators(fade, transitionSet);
                        ArrayList<Animator.AnimatorListener> listeners = transitions.getListeners();
                        set.playTogether(fade.getAnimation(appearing.getView()), transitions);
                        forEach(listeners, set::addListener);
                        transitions.removeAllListeners();
                    }
                    set.start();
                }
        );
    }

    private void pushWithoutElementTransitions(ViewController appearing, Options options, AnimatorSet set) {
        if (options.animations.push.waitForRender.isTrue()) {
            appearing.getView().setAlpha(0);
            appearing.addOnAppearedListener(() -> {
                appearing.getView().setAlpha(1);
                set.playTogether(options.animations.push.content.getAnimation(appearing.getView(), getDefaultPushAnimation(appearing.getView())));
                set.start();
            });
        } else {
            set.playTogether(options.animations.push.content.getAnimation(appearing.getView(), getDefaultPushAnimation(appearing.getView())));
            set.start();
        }
    }

    public void pop(View view, NestedAnimationsOptions pop, Runnable onAnimationEnd) {
        if (runningPushAnimations.containsKey(view)) {
            runningPushAnimations.get(view).cancel();
            onAnimationEnd.run();
            return;
        }
        AnimatorSet set = pop.content.getAnimation(view, getDefaultPopAnimation(view));
        set.addListener(new AnimatorListenerAdapter() {
            private boolean cancelled;

            @Override
            public void onAnimationCancel(Animator animation) {
                this.cancelled = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!cancelled) onAnimationEnd.run();
            }
        });
        set.start();
    }

    public void setRoot(View root, AnimationOptions setRoot, Runnable onAnimationEnd) {
        root.setVisibility(View.INVISIBLE);
        AnimatorSet set = setRoot.getAnimation(root);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                root.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                onAnimationEnd.run();
            }
        });
        set.start();
    }

    public void cancelPushAnimations() {
        for (View view : runningPushAnimations.keySet()) {
            runningPushAnimations.get(view).cancel();
            runningPushAnimations.remove(view);
        }
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public void endPushAnimation(View view) {
        if (runningPushAnimations.containsKey(view)) {
            runningPushAnimations.get(view).end();
        }
    }
}
