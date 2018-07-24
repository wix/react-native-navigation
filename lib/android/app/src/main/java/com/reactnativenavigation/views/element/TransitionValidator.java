package com.reactnativenavigation.views.element;

import com.reactnativenavigation.parse.Transition;

import java.util.Map;

public class TransitionValidator {
    protected boolean validate(Transition transition, Map<String, Element> fromMap, Map<String, Element> toMap) {
        return fromMap.containsKey(transition.fromId.get()) &&
               toMap.containsKey(transition.toId.get()) &&
               transition.duration.hasValue();
    }
}
