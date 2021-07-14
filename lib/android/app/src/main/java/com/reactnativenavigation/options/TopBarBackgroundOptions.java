package com.reactnativenavigation.options;

import android.content.Context;

import com.reactnativenavigation.options.params.Bool;
import com.reactnativenavigation.options.params.NullBool;
import com.reactnativenavigation.options.params.RNNColour;
import com.reactnativenavigation.options.params.RNNColourKt;
import com.reactnativenavigation.options.params.RNNNullColor;
import com.reactnativenavigation.options.parsers.BoolParser;

import org.json.JSONObject;

public class TopBarBackgroundOptions {
    public static TopBarBackgroundOptions parse(Context context, JSONObject json) {
        TopBarBackgroundOptions options = new TopBarBackgroundOptions();
        if (json == null) return options;

        options.color = RNNColourKt.parse(context, json.optJSONObject("color"));
        options.component = ComponentOptions.parse(json.optJSONObject("component"));
        options.waitForRender = BoolParser.parse(json, "waitForRender");

        if (options.component.hasValue()) {
            options.color = RNNColourKt.transparent();
        }

        return options;
    }

    public RNNColour color = new RNNNullColor();
    public ComponentOptions component = new ComponentOptions();
    public Bool waitForRender = new NullBool();

    void mergeWith(final TopBarBackgroundOptions other) {
        if (other.color.hasValue()) color = other.color;
        if (other.waitForRender.hasValue()) waitForRender = other.waitForRender;
        component.mergeWith(other.component);
    }

    void mergeWithDefault(TopBarBackgroundOptions defaultOptions) {
        if (!color.hasValue()) color = defaultOptions.color;
        if (!waitForRender.hasValue()) waitForRender = defaultOptions.waitForRender;
        component.mergeWithDefault(defaultOptions.component);
    }
}
