package com.reactnativenavigation.views.element;

import android.animation.Animator;

import com.reactnativenavigation.parse.Transition;
import com.reactnativenavigation.views.element.animators.BackgroundColorAnimator;
import com.reactnativenavigation.views.element.animators.MatrixAnimator;
import com.reactnativenavigation.views.element.animators.PropertyAnimatorCreator;
import com.reactnativenavigation.views.element.animators.ScaleXAnimator;
import com.reactnativenavigation.views.element.animators.ScaleYAnimator;
import com.reactnativenavigation.views.element.animators.TextChangeAnimator;
import com.reactnativenavigation.views.element.animators.XAnimator;
import com.reactnativenavigation.views.element.animators.YAnimator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TransitionAnimatorCreator {

    public Collection<Animator> create(List<Transition> transitions, Map<String, Element> from, Map<String, Element> to) {
        if (transitions.isEmpty()) return Collections.EMPTY_LIST;
        List<Animator> animators = new ArrayList<>();
        for (Transition transition : transitions) {
            animators.addAll(create(transition, from.get(transition.fromId.get()), to.get(transition.toId.get())));
        }
        return animators;
    }

    protected Collection<? extends Animator> create(Transition transition, Element from, Element to) {
        Collection<Animator> animators = new ArrayList<>();
        for (PropertyAnimatorCreator creator : getAnimators(from, to)) {
            if (creator.shouldAnimateProperty()) animators.add(creator.create(transition));
        }
        return animators;
    }

    protected List<PropertyAnimatorCreator> getAnimators(Element from, Element to) {
        return Arrays.asList(
                new XAnimator(from, to),
                new YAnimator(from, to),
                new MatrixAnimator(from, to),
                new ScaleXAnimator(from, to),
                new ScaleYAnimator(from, to),
                new BackgroundColorAnimator(from, to),
                new TextChangeAnimator(from, to)
        );
    }
}
