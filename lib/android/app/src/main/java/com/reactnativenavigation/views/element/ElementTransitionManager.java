package com.reactnativenavigation.views.element;

import android.animation.Animator;
import android.support.annotation.RestrictTo;
import android.util.Log;

import com.reactnativenavigation.parse.Transition;
import com.reactnativenavigation.parse.Transitions;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.reactnativenavigation.utils.CollectionUtils.filter;
import static com.reactnativenavigation.utils.CollectionUtils.map;

public class ElementTransitionManager {

    private final TransitionValidator validator;
    private final TransitionAnimatorCreator animatorCreator;

    @RestrictTo(RestrictTo.Scope.TESTS)
    ElementTransitionManager(TransitionValidator validator, TransitionAnimatorCreator animatorCreator) {
        this.validator = validator;
        this.animatorCreator = animatorCreator;
    }

    public ElementTransitionManager() {
        validator = new TransitionValidator();
        animatorCreator = new TransitionAnimatorCreator();
    }

    public Collection<Animator> createTransitions(Transitions transitions, List<Element> fromElements, List<Element> toElements) {
        if (!transitions.hasValue() || fromElements.isEmpty() || toElements.isEmpty()) return Collections.EMPTY_LIST;
        Map<String, Element> from = map(fromElements, Element::getElementId);
        Map<String, Element> to = map(toElements, Element::getElementId);
        List<Transition> validTransitions = filter(transitions.get(), (t) -> validator.validate(t, from, to));

        return animatorCreator.create(validTransitions, from, to);
    }
}
