package com.reactnativenavigation.parse;

import android.graphics.Color;

import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.parse.params.Colour;
import com.reactnativenavigation.parse.params.NullBool;
import com.reactnativenavigation.parse.params.NullColor;
import com.reactnativenavigation.parse.parsers.BoolParser;
import com.reactnativenavigation.parse.parsers.ColorParser;

import org.json.JSONObject;

public class TopBarBackgroundOptions {
    public static TopBarBackgroundOptions parse(JSONObject json) {
        TopBarBackgroundOptions options = new TopBarBackgroundOptions();
        if (json == null) return options;

        options.color = ColorParser.parse(json, "color");
        options.component = Component.parse(json.optJSONObject("component"));
        options.waitForRender = BoolParser.parse(json, "waitForRender");

        if (options.component.hasValue()) {
            options.color = new Colour(Color.TRANSPARENT);
        }

        return options;
    }

    public Colour color = new NullColor();
    public Component component = new Component();
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
