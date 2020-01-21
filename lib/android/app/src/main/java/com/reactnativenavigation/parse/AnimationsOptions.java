package com.reactnativenavigation.parse;


import org.json.JSONObject;

public class AnimationsOptions {

    public static AnimationsOptions parse(JSONObject json) {
        AnimationsOptions options = new AnimationsOptions();
        if (json == null) return options;

        options.push = NestedAnimationsOptions.parse(json.optJSONObject("push"));
        options.pop = NestedAnimationsOptions.parse(json.optJSONObject("pop"));
        options.setStackRoot = NestedAnimationsOptions.parse(json.optJSONObject("setStackRoot"));
        options.setRoot = new AnimationOptions(json.optJSONObject("setRoot"));
        options.showModal = new AnimationOptions(json.optJSONObject("showModal"));
        options.dismissModal = new AnimationOptions(json.optJSONObject("dismissModal"));

        return options;
    }

    public NestedAnimationsOptions push = new NestedAnimationsOptions();
    public NestedAnimationsOptions pop = new NestedAnimationsOptions();
    public NestedAnimationsOptions setStackRoot = new NestedAnimationsOptions();
    public AnimationOptions setRoot = new AnimationOptions();
    public AnimationOptions showModal = new AnimationOptions();
    public AnimationOptions dismissModal = new AnimationOptions();

    public void mergeWith(AnimationsOptions other) {
        push.mergeWith(other.push);
        pop.mergeWith(other.pop);
        setRoot.mergeWith(other.setRoot);
        setStackRoot.mergeWith(other.setStackRoot);
        showModal.mergeWith(other.showModal);
        dismissModal.mergeWith(other.dismissModal);
    }

    void mergeWithDefault(AnimationsOptions defaultOptions) {
        push.mergeWithDefault(defaultOptions.push);
        pop.mergeWithDefault(defaultOptions.pop);
        setStackRoot.mergeWithDefault(defaultOptions.setStackRoot);
        setRoot.mergeWithDefault(defaultOptions.setRoot);
        showModal.mergeWithDefault(defaultOptions.showModal);
        dismissModal.mergeWithDefault(defaultOptions.dismissModal);
    }
}
