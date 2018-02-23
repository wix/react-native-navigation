package com.reactnativenavigation.parse;


import org.json.JSONObject;

public class AnimationOptions {

    public static AnimationOptions parse(JSONObject json) {
        AnimationOptions options = new AnimationOptions();
        if (json == null) return options;

        options.empty = false;

        options.y = ValueAnimationOptions.parse(json.optJSONObject("y"));
        options.x = ValueAnimationOptions.parse(json.optJSONObject("x"));
        options.alpha = ValueAnimationOptions.parse(json.optJSONObject("alpha"));
        options.scaleX = ValueAnimationOptions.parse(json.optJSONObject("scaleX"));
        options.scaleY = ValueAnimationOptions.parse(json.optJSONObject("scaleX"));

        return options;
    }

    private boolean empty = true;

    public ValueAnimationOptions y = new ValueAnimationOptions();
    public ValueAnimationOptions x = new ValueAnimationOptions();
    public ValueAnimationOptions alpha = new ValueAnimationOptions();
    public ValueAnimationOptions scaleY = new ValueAnimationOptions();
    public ValueAnimationOptions scaleX = new ValueAnimationOptions();

    void mergeWith(AnimationOptions other) {
        if (!other.isEmpty()) {
            empty = false;
        }
        y.mergeWith(other.y);
        x.mergeWith(other.x);
        alpha.mergeWith(other.alpha);
        scaleY.mergeWith(other.scaleY);
        scaleX.mergeWith(other.scaleX);

    }

    void mergeWithDefault(AnimationOptions defaultOptions) {
        if (!defaultOptions.isEmpty()) {
            empty = false;
        }
        y.mergeWithDefault(defaultOptions.y);
        x.mergeWithDefault(defaultOptions.x);
        alpha.mergeWithDefault(defaultOptions.alpha);
        scaleX.mergeWithDefault(defaultOptions.scaleX);
        scaleY.mergeWithDefault(defaultOptions.scaleY);
    }

    public boolean isEmpty() {
        return empty;
    }
}
