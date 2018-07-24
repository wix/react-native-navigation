package com.reactnativenavigation.views.element;

import android.animation.Animator;
import android.support.annotation.RestrictTo;

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

    @RestrictTo(RestrictTo.Scope.TESTS)
    public ElementTransitionManager(TransitionValidator validator) {
        this.validator = validator;
    }

    public ElementTransitionManager() {
        validator = new TransitionValidator();
    }

    public Collection<? extends Animator> createTransitions(Transitions transitions, List<Element> fromElements, List<Element> toElements) {
        if (!transitions.hasValue() || fromElements.isEmpty() || toElements.isEmpty()) return Collections.EMPTY_LIST;

        Map<String, Element> from = map(fromElements, Element::getElementId);
        Map<String, Element> to = map(toElements, Element::getElementId);
        List<Transition> validTransitions = filter(transitions.get(), (t) -> validator.validate(t, from, to));
        if (validTransitions.isEmpty()) return Collections.EMPTY_LIST;

        return null;
    }
}
