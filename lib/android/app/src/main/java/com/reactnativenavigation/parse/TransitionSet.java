package com.reactnativenavigation.parse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TransitionSet {
    public List<Transition> transitions = new ArrayList<>();

    public static TransitionSet parse(JSONObject json) {
        TransitionSet result = new TransitionSet();
        if (json != null && json.has("animations")) {
            JSONArray animations = json.optJSONArray("animations");
            for (int i = 0; i < animations.length(); i++) {
                result.transitions.add(Transition.parse(animations.optJSONObject(i)));
            }
        }
        return result;
    }

    public boolean hasValue() {
        return !transitions.isEmpty();
    }

    void mergeWith(final TransitionSet other) {
        if (other.hasValue()) transitions = other.transitions;
    }

    void mergeWithDefault(TransitionSet defaultOptions) {
        if (!hasValue()) transitions = defaultOptions.transitions;
    }
}
