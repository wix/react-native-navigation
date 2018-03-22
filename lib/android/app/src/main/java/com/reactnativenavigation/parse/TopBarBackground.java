package com.reactnativenavigation.parse;

import com.reactnativenavigation.parse.params.Color;
import com.reactnativenavigation.parse.params.NullColor;
import com.reactnativenavigation.parse.parsers.ColorParser;

import org.json.JSONObject;

public class TopBarBackground {
    public static TopBarBackground parse(JSONObject json) {
        TopBarBackground options = new TopBarBackground();
        if (json == null) {
            return options;
        }

        options.color = ColorParser.parse(json, "color");

        return options;
    }

    public Color color = new NullColor();

    void mergeWith(final TopBarBackground other) {
        if (other.color.hasValue()) color = other.color;
    }

    void mergeWithDefault(TopBarBackground defaultOptions) {
        if (!color.hasValue()) color = defaultOptions.color;
    }
}
