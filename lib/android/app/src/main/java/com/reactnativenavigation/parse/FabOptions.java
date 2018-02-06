package com.reactnativenavigation.parse;


import org.json.JSONObject;

public class FabOptions implements DEFAULT_VALUES {

    public static FabOptions parse(JSONObject json) {
        FabOptions options = new FabOptions();
        if (json == null) return options;

        options.id = TextParser.parse(json, "id");
        options.backgroundColor = ColorParser.parse(json, "backgroundColor");
        options.clickColor = ColorParser.parse(json, "clickColor");
        options.rippleColor = ColorParser.parse(json, "rippleColor");
        options.hidden = Options.BooleanOptions.parse(json.optString("hidden"));
        if (json.has("icon")) {
            options.icon = TextParser.parse(json.optJSONObject("icon"), "uri");
        }

        return options;
    }

    public Text id = new NullText();
    public Color backgroundColor = new NullColor();
    public Color clickColor = new NullColor();
    public Color rippleColor = new NullColor();
    public Text icon = new NullText();
    public Options.BooleanOptions hidden = Options.BooleanOptions.False;

    void mergeWith(final FabOptions other) {
        if (other.id.hasValue()) {
            id = other.id;
        }
        if (other.backgroundColor.hasValue()) {
            backgroundColor = other.backgroundColor;
        }
        if (other.clickColor.hasValue()) {
            clickColor = other.clickColor;
        }
        if (other.rippleColor.hasValue()) {
            rippleColor = other.rippleColor;
        }
        if (other.hidden != Options.BooleanOptions.NoValue) {
            hidden = other.hidden;
        }
        if (other.icon.hasValue()) {
            icon = other.icon;
        }
    }

    void mergeWithDefault(FabOptions defaultOptions) {
        if (!id.hasValue()) {
            id = defaultOptions.id;
        }
        if (!backgroundColor.hasValue()) {
            backgroundColor = defaultOptions.backgroundColor;
        }
        if (!clickColor.hasValue()) {
            clickColor = defaultOptions.clickColor;
        }
        if (!rippleColor.hasValue()) {
            rippleColor = defaultOptions.rippleColor;
        }
        if (hidden == Options.BooleanOptions.NoValue) {
            hidden = defaultOptions.hidden;
        }
        if (!icon.hasValue()) {
            icon = defaultOptions.icon;
        }
    }
}
