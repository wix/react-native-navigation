package com.reactnativenavigation.parse;

import com.reactnativenavigation.parse.params.Colour;
import com.reactnativenavigation.parse.params.NullColor;
import com.reactnativenavigation.parse.parsers.ColorParser;

import org.json.JSONObject;

public class TopBarBackgroundOptions {
    public static TopBarBackgroundOptions parse(JSONObject json) {
        TopBarBackgroundOptions options = new TopBarBackgroundOptions();
        if (json == null) return options;

        options.color = ColorParser.parse(json, "color");
        options.component = Component.parse(json.optJSONObject("component"));

        if (options.component.hasValue()) {
            options.color = new Colour(android.graphics.Color.TRANSPARENT);
        }

        return options;
    }

    public Colour color = new NullColor();
    public Component component = new Component();

    void mergeWith(final TopBarBackgroundOptions other) {
        if (other.color.hasValue()) color = other.color;
        component.mergeWith(other.component);
    }

    void mergeWithDefault(TopBarBackgroundOptions defaultOptions) {
        if (!color.hasValue()) color = defaultOptions.color;
        component.mergeWithDefault(defaultOptions.component);
    }
}
