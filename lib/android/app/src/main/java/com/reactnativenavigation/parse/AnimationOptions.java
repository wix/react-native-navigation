package com.reactnativenavigation.parse;


import org.json.JSONObject;

public class AnimationOptions {

    public static AnimationOptions parse(JSONObject json) {
        AnimationOptions options = new AnimationOptions();
        if (json == null) return options;

        options.empty = false;

        options.y = ValueAnimationOptions.parse(json.optJSONObject("y"));
        options.x = ValueAnimationOptions.parse(json.optJSONObject("x"));
        options.rotationX = ValueAnimationOptions.parse(json.optJSONObject("rotationX"));
        options.rotationY = ValueAnimationOptions.parse(json.optJSONObject("rotationY"));
        options.rotation = ValueAnimationOptions.parse(json.optJSONObject("rotation"));
        options.alpha = ValueAnimationOptions.parse(json.optJSONObject("alpha"));
        options.scaleX = ValueAnimationOptions.parse(json.optJSONObject("scaleX"));
        options.scaleY = ValueAnimationOptions.parse(json.optJSONObject("scaleY"));

        return options;
    }

    private boolean empty = true;

    public ValueAnimationOptions y = new ValueAnimationOptions();
    public ValueAnimationOptions x = new ValueAnimationOptions();
    public ValueAnimationOptions rotationX = new ValueAnimationOptions();
    public ValueAnimationOptions rotationY = new ValueAnimationOptions();
    public ValueAnimationOptions rotation = new ValueAnimationOptions();
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
        rotationX.mergeWith(other.rotationX);
        rotationY.mergeWith(other.rotationY);
        rotation.mergeWith(other.rotation);
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
        rotationX.mergeWithDefault(defaultOptions.rotationX);
        rotationY.mergeWithDefault(defaultOptions.rotationY);
        rotation.mergeWithDefault(defaultOptions.rotation);
    }

    public boolean isEmpty() {
        return empty;
    }
}
