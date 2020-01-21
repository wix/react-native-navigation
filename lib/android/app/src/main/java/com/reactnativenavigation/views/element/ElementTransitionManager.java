package com.reactnativenavigation.views.element;

import android.animation.AnimatorSet;
import android.view.View;

import com.facebook.react.uimanager.util.ReactFindViewUtil;
import com.reactnativenavigation.parse.AnimationOptions;
import com.reactnativenavigation.parse.ElementTransitionOptions;
import com.reactnativenavigation.parse.ElementTransitions;
import com.reactnativenavigation.parse.NestedAnimationsOptions;
import com.reactnativenavigation.parse.SharedElementTransitionOptions;
import com.reactnativenavigation.parse.SharedElements;
import com.reactnativenavigation.utils.Functions;
import com.reactnativenavigation.viewcontrollers.ViewController;

import static com.facebook.react.uimanager.util.ReactFindViewUtil.findView;
import static com.reactnativenavigation.utils.ObjectUtils.perform;

public class ElementTransitionManager {
    private final TransitionAnimatorCreator animatorCreator;

    public ElementTransitionManager() {
        animatorCreator = new TransitionAnimatorCreator();
    }

    public void createTransitions(NestedAnimationsOptions animation, ViewController fromScreen, ViewController toScreen, Functions.Func1<TransitionSet> onAnimatorsCreated) {
        SharedElements sharedElements = animation.sharedElements;
        ElementTransitions elementTransitions = animation.elementTransitions;
        if (!sharedElements.hasValue() && !elementTransitions.getHasValue()) {
            onAnimatorsCreated.run(new TransitionSet());
            return;
        }
        TransitionSet transitionSet = new TransitionSet();

        for (SharedElementTransitionOptions transitionOptions : sharedElements.get()) {
            SharedElementTransition transition = new SharedElementTransition(transitionOptions);
            transition.viewController = toScreen;
            perform(findView(fromScreen.getView(), transition.getFromId()), transition::setFrom);
            findView(toScreen.getView(), new ReactFindViewUtil.OnViewFoundListener() {
                @Override
                public String getNativeId() {
                    return transition.getToId();
                }

                @Override
                public void onViewFound(View view) {
                    transition.setTo(view);
                    if (transition.isValid()) transitionSet.add(transition);
                    if (transitionSet.size() == (sharedElements.get().size() + elementTransitions.getTransitions().size())) {
                        onAnimatorsCreated.run(transitionSet);
                    }
                }
            });
        }

        for (ElementTransitionOptions transitionOptions : elementTransitions.getTransitions()) {
            ElementTransition transition = new ElementTransition(transitionOptions);
            perform(findView(fromScreen.getView(), transition.getId()), view -> {
                transition.setView(view);
                transition.setViewController(fromScreen);
                transitionSet.add(transition);
            });
            if (transition.isValid()) continue;
            findView(toScreen.getView(), new ReactFindViewUtil.OnViewFoundListener() {
                @Override
                public String getNativeId() {
                    return transition.getId();
                }

                @Override
                public void onViewFound(View view) {
                    transition.setView(view);
                    transition.setViewController(toScreen);
                    transitionSet.add(transition);
                    if (transitionSet.size() == (sharedElements.get().size() + elementTransitions.getTransitions().size())) {
                        onAnimatorsCreated.run(transitionSet);
                    }
                }
            });
        }
    }

    public AnimatorSet createAnimators(AnimationOptions fadeAnimation, TransitionSet transitionSet) {
        return animatorCreator.create(fadeAnimation, transitionSet);
    }
}
