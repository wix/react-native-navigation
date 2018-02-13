package com.reactnativenavigation.parse;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.reactnativenavigation.parse.Options.BooleanOptions.NoValue;

public class FabOptions implements DEFAULT_VALUES {

    public static FabOptions parse(JSONObject json) {
        FabOptions options = new FabOptions();
        if (json == null) return options;

        options.id = TextParser.parse(json, "id");
        options.backgroundColor = ColorParser.parse(json, "backgroundColor");
        options.clickColor = ColorParser.parse(json, "clickColor");
        options.rippleColor = ColorParser.parse(json, "rippleColor");
        options.visible = Options.BooleanOptions.parse(json.optString("visible"));
        if (json.has("icon")) {
            options.icon = TextParser.parse(json.optJSONObject("icon"), "uri");
        }
        if (json.has("actions")) {
            JSONArray fabsArray = json.optJSONArray("actions");
            for (int i = 0; i < fabsArray.length(); i++) {
                options.actionsArray.add(FabOptions.parse(fabsArray.optJSONObject(i)));
            }
        }
        options.alignHorizontally = TextParser.parse(json, "alignHorizontally");
        options.alignVertically = TextParser.parse(json, "alignVertically");
        options.hideOnScroll = Options.BooleanOptions.parse(json.optString("hideOnScroll"));
        options.size = TextParser.parse(json, "size");

        return options;
    }

    public Text id = new NullText();
    public Color backgroundColor = new NullColor();
    public Color clickColor = new NullColor();
    public Color rippleColor = new NullColor();
    public Text icon = new NullText();
    public Options.BooleanOptions visible = Options.BooleanOptions.NoValue;
    public ArrayList<FabOptions> actionsArray = new ArrayList<>();
    public Text alignHorizontally = new NullText();
    public Text alignVertically = new NullText();
    public Options.BooleanOptions hideOnScroll = NoValue;
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
        if (other.visible != Options.BooleanOptions.NoValue) {
            visible = other.visible;
        }
        if (other.icon.hasValue()) {
            icon = other.icon;
        }
        if (other.actionsArray.size() > 0) {
            actionsArray = other.actionsArray;
        }
        if (other.alignVertically.hasValue()) {
            alignVertically = other.alignVertically;
        }
        if (other.alignHorizontally.hasValue()) {
            alignHorizontally = other.alignHorizontally;
        }
        if (other.hideOnScroll != NoValue) {
            hideOnScroll = other.hideOnScroll;
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
        if (visible == Options.BooleanOptions.NoValue) {
            visible = defaultOptions.visible;
        }
        if (!icon.hasValue()) {
            icon = defaultOptions.icon;
        }
        if (actionsArray.size() == 0) {
            actionsArray = defaultOptions.actionsArray;
        }
        if (!alignHorizontally.hasValue()) {
            alignHorizontally = defaultOptions.alignHorizontally;
        }
        if (!alignVertically.hasValue()) {
            alignVertically = defaultOptions.alignVertically;
        }
        if (hideOnScroll == NoValue) {
            hideOnScroll = defaultOptions.hideOnScroll;
        }
        if (!size.hasValue()) {
            size = defaultOptions.size;
        }
    }
}
