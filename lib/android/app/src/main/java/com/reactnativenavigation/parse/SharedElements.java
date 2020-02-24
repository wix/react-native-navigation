package com.reactnativenavigation.parse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RestrictTo;

public class SharedElements {
    private List<SharedElementTransitionOptions> transitions = new ArrayList<>();

    public List<SharedElementTransitionOptions> get() {
        return transitions;
    }

    public SharedElements() {

    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public SharedElements(List<SharedElementTransitionOptions> transitions) {
        this.transitions = transitions;
    }

    public static SharedElements parse(JSONObject json) {
        SharedElements result = new SharedElements();
        try {
            JSONArray sharedElements = json.getJSONArray("sharedElementTransitions");
            if (sharedElements == null || sharedElements.length() == 0) return result;
            for (int i = 0; i < sharedElements.length(); i++) {
                result.add(SharedElementTransitionOptions.parse(sharedElements.getJSONObject(i)));
            }
            return result;
        } catch (JSONException e) {
            return result;
        }
    }

    public boolean hasValue() {
        return !transitions.isEmpty();
    }

    void mergeWith(final SharedElements other) {
        if (other.hasValue()) transitions = other.transitions;
    }

    void mergeWithDefault(SharedElements defaultOptions) {
        if (!hasValue()) transitions = defaultOptions.transitions;
    }

    private void add(SharedElementTransitionOptions transition) {
        transitions.add(transition);
    }
}
