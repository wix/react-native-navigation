package com.reactnativenavigation.options;

import android.content.Context;
import android.graphics.Color;

import com.reactnativenavigation.options.params.Bool;
import com.reactnativenavigation.options.params.Colour;
import com.reactnativenavigation.options.params.NullBool;
import com.reactnativenavigation.options.params.NullColor;
import com.reactnativenavigation.options.params.RNNColour;
import com.reactnativenavigation.options.params.RNNColourKt;
import com.reactnativenavigation.options.params.RNNNullColor;
import com.reactnativenavigation.options.parsers.BoolParser;
import com.reactnativenavigation.options.parsers.ColorParser;

import org.json.JSONObject;

public class TopBarBackgroundOptions {
    public static TopBarBackgroundOptions parse(Context context, JSONObject json) {
        TopBarBackgroundOptions options = new TopBarBackgroundOptions();
        if (json == null) return options;

        options.color = RNNColourKt.parseRNNColour(context, json.optJSONObject("color"));
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
        color.mergeWith(other.color);
        if (other.waitForRender.hasValue()) waitForRender = other.waitForRender;
        component.mergeWith(other.component);
    }

    void mergeWithDefault(TopBarBackgroundOptions defaultOptions) {
        color.mergeWithDefault(defaultOptions.color);
        if (!waitForRender.hasValue()) waitForRender = defaultOptions.waitForRender;
        component.mergeWithDefault(defaultOptions.component);
    }
}
