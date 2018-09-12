package com.reactnativenavigation.parse;

import android.support.annotation.RestrictTo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Transitions {
    private List<Transition> transitions = new ArrayList<>();

    public List<Transition> get() {
        return transitions;
    }

    public Transitions() {

    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public Transitions(List<Transition> transitions) {
        this.transitions = transitions;
    }

    public static Transitions parse(JSONObject json) {
        Transitions result = new Transitions();
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

    void mergeWith(final Transitions other) {
        if (other.hasValue()) transitions = other.transitions;
    }

    void mergeWithDefault(Transitions defaultOptions) {
        if (!hasValue()) transitions = defaultOptions.transitions;
    }
}
