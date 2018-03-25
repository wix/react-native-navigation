package com.reactnativenavigation.parse;


import org.json.JSONObject;

public class AnimationsOptions {

    public static AnimationsOptions parse(JSONObject json) {
        AnimationsOptions options = new AnimationsOptions();
        if (json == null) return options;

        options.push = NestedAnimationsOptions.parse(json.optJSONObject("push"));
        options.pop = NestedAnimationsOptions.parse(json.optJSONObject("pop"));
        options.startApp = AnimationOptions.parse(json.optJSONObject("startApp"));
        options.showModal = AnimationOptions.parse(json.optJSONObject("showModal"));
        options.dismissModal = AnimationOptions.parse(json.optJSONObject("dismissModal"));

        return options;
    }

    public NestedAnimationsOptions push = new NestedAnimationsOptions();
    public NestedAnimationsOptions pop = new NestedAnimationsOptions();
    public AnimationOptions startApp = new AnimationOptions();
    public AnimationOptions showModal = new AnimationOptions();
    public AnimationOptions dismissModal = new AnimationOptions();

    public void mergeWith(AnimationsOptions other) {
        push.mergeWith(other.push);
        pop.mergeWith(other.pop);
        startApp.mergeWith(other.startApp);
        showModal.mergeWith(other.showModal);
        dismissModal.mergeWith(other.dismissModal);
    }

    void mergeWithDefault(AnimationsOptions defaultOptions) {
        push.mergeWithDefault(defaultOptions.push);
        pop.mergeWithDefault(defaultOptions.pop);
        startApp.mergeWithDefault(defaultOptions.startApp);
        showModal.mergeWithDefault(defaultOptions.showModal);
        dismissModal.mergeWithDefault(defaultOptions.dismissModal);
    }
}
