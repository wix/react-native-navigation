package com.reactnativenavigation.views.element;

import android.animation.Animator;
import android.view.View;

import com.reactnativenavigation.parse.Transition;
import com.reactnativenavigation.views.element.animators.PropertyAnimatorCreator;
import com.reactnativenavigation.views.element.animators.XAnimator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TransitionAnimatorCreator {

    public Collection<? extends Animator> create(List<Transition> transitions, Map<String, Element> from, Map<String, Element> to) {
        if (transitions.isEmpty()) return Collections.EMPTY_LIST;
        List<Animator> animators = new ArrayList<>();
        for (Transition transition : transitions) {
            animators.addAll(create(transition, from.get(transition.fromId.get()), to.get(transition.toId.get())));
        }
        return animators;
    }

    protected Collection<? extends Animator> create(Transition transition, Element from, Element to) {
        Collection<Animator> animators = new ArrayList<>();
        for (PropertyAnimatorCreator creator : getAnimators(from.getChild(), to.getChild())) {
            if (creator.shouldAnimateProperty()) animators.add(creator.create(transition));
        }
        return animators;
    }

    protected List<PropertyAnimatorCreator> getAnimators(View from, View to) {
        return Arrays.asList(
            new XAnimator(from, to)
        );
    }
}
