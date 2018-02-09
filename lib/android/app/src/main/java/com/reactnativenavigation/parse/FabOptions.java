package com.reactnativenavigation.parse;


import org.json.JSONObject;

import static com.reactnativenavigation.parse.Options.BooleanOptions.NoValue;

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
        options.alignHorizontally = TextParser.parse(json, "alignHorizontally");
        options.alignVertically = TextParser.parse(json, "alignVertically");
        options.size = TextParser.parse(json, "size");

        return options;
    }

    public Text id = new NullText();
    public Color backgroundColor = new NullColor();
    public Color clickColor = new NullColor();
    public Color rippleColor = new NullColor();
    public Text icon = new NullText();
    public Options.BooleanOptions hidden = NoValue;
    public Text alignHorizontally = new NullText();
    public Text alignVertically = new NullText();
    public Text size = new NullText();

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
        if (other.hidden != NoValue) {
            hidden = other.hidden;
        }
        if (other.icon.hasValue()) {
            icon = other.icon;
        }
        if (other.alignVertically.hasValue()) {
            alignVertically = other.alignVertically;
        }
        if (other.alignHorizontally.hasValue()) {
            alignHorizontally = other.alignHorizontally;
        }
        if (other.size.hasValue()) {
            size = other.size;
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
        if (hidden == NoValue) {
            hidden = defaultOptions.hidden;
        }
        if (!icon.hasValue()) {
            icon = defaultOptions.icon;
        }
        if (!alignHorizontally.hasValue()) {
            alignHorizontally = defaultOptions.alignHorizontally;
        }
        if (!alignVertically.hasValue()) {
            alignVertically = defaultOptions.alignVertically;
        }
        if (!size.hasValue()) {
            size = defaultOptions.size;
        }
    }
}
