package com.reactnativenavigation.parse;

import org.json.JSONObject;

public class BottomTabOptions implements DEFAULT_VALUES {

    public static BottomTabOptions parse(JSONObject json) {
        BottomTabOptions options = new BottomTabOptions();
        if (json == null) return options;

        options.title = TextParser.parse(json, "title");
        if (!json.has("icon")) {
            throw new RuntimeException("BottomTab must have an icon");
        }
        options.icon = TextParser.parse(json.optJSONObject("icon"), "uri");
        options.testId = TextParser.parse(json, "testID");
        return options;
    }

    public Text title = new NullText();
    public Text icon = new NullText();
    public Text testId = new NullText();

    void mergeWith(final BottomTabOptions other) {
        if (other.title.hasValue()) {
            title = other.title;
        }
        if (other.icon.hasValue()) {
            icon = other.icon;
        }
    }

    void mergeWithDefault(final BottomTabOptions defaultOptions) {
        if (!title.hasValue()) {
            title = defaultOptions.title;
        }
        if (!icon.hasValue()) {
            icon = defaultOptions.icon;
        }
    }
}
